package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

public class World {
    private TETile[][] tiles;
    private ArrayList<int[]> startPointArray;


    private int width;
    private int height;
    private Random random;

    private PriorityQueue<int[]> startPointQueue;

    private ArrayList<int[]> storeFloorCorr;
    private double currDistance = 0.0;

    private HashMap<int[], int[]> startEndMap;

    public World(int gridWidth, int gridHeight, String seed) {
        tiles = new TETile[gridWidth][gridHeight];
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
        //Generates a new random object given a seed
        random = new Random(seed.hashCode());
        width = gridWidth;
        height = gridHeight;

        //Creates min Heap for storing rooms
        startPointQueue = new PriorityQueue<>(new CoordinateComparator());

        //Creates an array for storing starting points
        startPointArray = new ArrayList<>();

        //Maps the startPoint of the race to the endPoint for every Avatar
        startEndMap = new HashMap<>();

        //Creates a randomly number of generated rooms
        int numberOfRooms = random.nextInt(20);
        if (numberOfRooms < 15) {
            numberOfRooms = 15;
        }
        for (int i = 0; i < numberOfRooms; i++) {
            this.makeRoom();
        }

        //Creates numberOfRooms - 1 hallways to connect the rooms
        for (int i = 0; i < numberOfRooms - 1; i++) {
            this.createHallways();
        }
        this.surroundWithWalls();
        this.fillWithRandom();
        this.closeBoundaries();
    }

    public TETile[][] accessGrid() {
        return tiles;
    }

    private void makeRoom() {
        // Sets min and max bounds for how big/small the rooms can be
        double upper = 0.30;
        double lower = 0.10;

        //Width and height starting point for the room
        int widthStartingPoint = random.nextInt(width);
        int heightStartingPoint = random.nextInt(height);

        if (widthStartingPoint == 0) {
            widthStartingPoint = 1;
        }
        else if (widthStartingPoint== width - 1) {
            widthStartingPoint = width - 2;
        }


        if (heightStartingPoint < 4) {
            heightStartingPoint = 5;
        }
        else if (heightStartingPoint > height - 4) {
            heightStartingPoint = height - 9;
        }


        int upperWidth = (int) (upper * width);
        int lowerWidth = (int) (lower * width);

        int upperHeight = (int) (upper * height);
        int lowerHeight = (int) (lower * height);

        // Creates room width and height
        int roomWidth = random.nextInt(upperWidth);
        int roomHeight = random.nextInt(upperHeight);

        if (roomWidth < lowerWidth) {
            roomWidth = lowerWidth;
        }
        if (roomHeight < lowerHeight) {
            roomHeight = lowerHeight;
        }

        //Put coordinates into an array
        int[] corrArray = {widthStartingPoint, heightStartingPoint};
        startPointQueue.add(corrArray);
        startPointArray.add(corrArray);

        //second connections
        //int[] corrSecondArray = {widthStartingPoint, heightStartingPoint + roomHeight};
        //secondConnectionQueue.add(corrSecondArray);

        //Generate floor plan
        for (int x = widthStartingPoint; x < limitWidth(widthStartingPoint + roomWidth); x++) {
            for (int y = heightStartingPoint; y < limitHeight(heightStartingPoint + roomHeight); y++) {
                tiles[x][y] = Tileset.FLOOR;
            }
        }
    }

    private int limitWidth(int roomWidth) {
        if (roomWidth > width) {
            roomWidth = width - 1;
        }
        return roomWidth;
    }

    private int limitHeight(int roomHeight) {
        if (roomHeight > height) {
            roomHeight = height - 1;
        }
        return roomHeight;
    }

    private void createHallways() {
        //Get the starting point width/height
        int[] startingCorr = startPointQueue.peek();
        int startingWidth = startingCorr[0];
        int startingHeight = startingCorr[1];
        startPointQueue.remove();

        //Get the ending point width/height
        int[] endingCorr = startPointQueue.peek();
        int endingWidth = endingCorr[0];
        int endingHeight = endingCorr[1];

        if (startingHeight <= endingHeight && startingWidth <= endingWidth) {
            //connect width wise
            for (int x = startingWidth; x < endingWidth; x++) {
                tiles[x][startingHeight] = Tileset.FLOOR;
            }
            //connect height wise
            for (int y = startingHeight; y < endingHeight; y++) {
                tiles[endingWidth][y] = Tileset.FLOOR;
            }
        } else if (startingHeight >= endingHeight && startingWidth <= endingWidth) {
            for (int x = startingWidth; x < endingWidth; x++) {
                tiles[x][startingHeight] = Tileset.FLOOR;
            }
            //connect height wise
            for (int y = startingHeight; y > endingHeight; y--) {
                tiles[endingWidth][y] = Tileset.FLOOR;
            }

        } else if (startingHeight <= endingHeight && startingWidth >= endingWidth) {
            for (int x = startingWidth; x > endingWidth; x--) {
                tiles[x][startingHeight] = Tileset.FLOOR;
            }
            for (int y = startingHeight; y < endingHeight; y++) {
                tiles[endingWidth][y] = Tileset.FLOOR;
            }

        } else {
            for (int x = startingWidth; x > endingWidth; x--) {
                tiles[x][startingHeight] = Tileset.FLOOR;
            }
            //connect height wise
            for (int y = startingHeight; y > endingHeight; y--) {
                tiles[endingWidth][y] = Tileset.FLOOR;
            }
        }
    }

    private boolean validateIndex(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private void surroundWithWalls() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (validateIndex(x, y + 1) && tiles[x][y + 1] == Tileset.FLOOR) {
                    if (tiles[x][y] != Tileset.FLOOR) {
                        tiles[x][y] = Tileset.WALL;
                    }
                } else if (validateIndex(x, y - 1) && tiles[x][y - 1] == Tileset.FLOOR) {
                    if (tiles[x][y] != Tileset.FLOOR) {
                        tiles[x][y] = Tileset.WALL;
                    }
                } else if (validateIndex(x + 1, y) && tiles[x + 1][y] == Tileset.FLOOR) {
                    if (tiles[x][y] != Tileset.FLOOR) {
                        tiles[x][y] = Tileset.WALL;
                    }
                } else if (validateIndex(x - 1, y) && tiles[x - 1][y] == Tileset.FLOOR) {
                    if (tiles[x][y] != Tileset.FLOOR) {
                        tiles[x][y] = Tileset.WALL;
                    }
                }
            }
        }
    }


    public int[] getRandomAvatarPosition() {
        // random room
        // non-hallway: [x+1, y], [x-1, y], [x, y+1], [x, y-1]
        int randomIndex = random.nextInt(startPointArray.size() - 1);

        // access the corresponding element in the array
        int[] randomPoint = startPointArray.get(randomIndex);

        // use the random point in your code as needed
        int randomX = randomPoint[0];
        int randomY = randomPoint[1];

        int[] avatarPos = {randomX, randomY};



        return avatarPos;


    }

    private double findDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

    public void storeFloor(int x1, int y1) {
        ArrayList<int[]> distanceCoorArray = new ArrayList<>();
        storeFloorCorr = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y] == Tileset.FLOOR) {
                    //Puts all the coordinates that are floors in a private HashMap
                    int[] corrArray = {x, y};
                    storeFloorCorr.add(corrArray);


                    //Checks if the distance is greater than 0.3 * width (arbitrary) if currDistance == 0
                    if (currDistance == 0) {
                        if (findDistance(x1, y1, x, y) > 0.3 * width) {
                            //Stores the coordinates in a temp int[] array
                            int[] tempArray = {x, y};
                            distanceCoorArray.add(tempArray);
                        }
                    }
                    //Finds the second endpoint if currDistance is not 0
                    else {
                        if (findDistance(x1, y1, x, y) == currDistance) {
                            int[] tempArray = {x, y};
                            distanceCoorArray.add(tempArray);
                        }
                    }
                }
            }
        }
        //Getting a randomIndex
        int randomIndex = random.nextInt(distanceCoorArray.size() - 1);
        int[] endingCoor = distanceCoorArray.get(randomIndex);

        //Sets currDistance if it was 0
        if (currDistance == 0) {
            currDistance = findDistance(x1, y1, endingCoor[0], endingCoor[1]);
        }

    }

    public void placeCoins(int num) {
        for (int i = 0; i < num; i++) {
            int randomIndex = random.nextInt(storeFloorCorr.size() - 1);
            int[] corrArray = storeFloorCorr.get(randomIndex);
            tiles[corrArray[0]][corrArray[1]] = Tileset.COIN;
        }
    }



    //Randomize filling the blank spaces
    private void fillWithRandom() {
        //Creates a method array of the different empty space fillings
        TETile[] fillArray = new TETile[3];
        fillArray[0] = Tileset.MOUNTAIN;
        fillArray[1] = Tileset.SAND;
        fillArray[2] = Tileset.GRASS;


        //Fills the empty spaces with one of the four methods
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y] != Tileset.WALL && tiles[x][y] != Tileset.FLOOR) {
                    int num = random.nextInt(2);
                    tiles[x][y] = fillArray[num];
                }
            }
        }
    }

    public int getHeight(){
        return height;
    }

    public int getWidth() {
        return width;
    }

    private void closeBoundaries() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0 || x == width - 1 || y < 4 || y > height - 2) {
                    if (tiles[x][y] == Tileset.FLOOR) {
                        tiles[x][y] = Tileset.WALL;
                    }
                }
            }
        }
    }


}