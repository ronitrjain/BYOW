package byow.Core;


import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;


import java.util.HashMap;


public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 40;


    // HashMap for storing the Seed with the corresponding world
    private HashMap<String, TETile[][]> worldCache = new HashMap<>();


    // Hashmap for storing the seed with corresponding action sequence


    private boolean replay;

    private Avatar avatar;
    private World world;
    private String inputString;
    private TERenderer ter;
    private Menu menu = new Menu(this);
    private HUD hud;

    public static int coinMini = 5;


    public static int coinCounter = 10;
    private long seed;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        // Starts the game
        menu.startGame();
        inputString = menu.start();
        menu.checkColor();
        interactWithInputString(inputString);

    }


    public void decrementCoin() {
        coinCounter--;
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        TETile[][] finalWorldFrame;


        world = new World(WIDTH, HEIGHT, input);
        finalWorldFrame = world.accessGrid();


        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + 8, 0, 4);
        ter.renderFrame(world.accessGrid());


        action();

        return createWorld(input);


    }


    public void changeReplay(boolean repl) {
        this.replay = repl;
    }


    public TETile[][] createWorld(String input) {
        return world.accessGrid();
    }


    public void action() {
        // Creates the new Avatar
        avatar = new Avatar(world, this);
        avatar.draw();
        TETile[][] worldFrame = world.accessGrid();


        // Creates the HUD
        hud = new HUD(world);

        // get storeFloorCoor
        world.storeFloor(avatar.get_x_coord(), avatar.get_y_coord());


        int counter = 10;
        world.placeCoins(counter);

        String move = "";

        for (Character c : menu.getActionArray()) {
            move += c;
        }

        String store = "\n" + inputString + "\n" + move;


        if (menu.size() > 0) {
            if (replay) {
                if (coinCounter == 0) {
                    menu.win();
                }
                //Get zIndex
                int zIndex = 0;
                for (int i = menu.getActionArray().size() - 1; i > 0; i--) {
                    char checkIfZ = menu.getActionArray().get(i);
                    if (checkIfZ == 'Z') {
                        zIndex = i;
                        break;
                    }
                }

                //If zIndex is still 0, it means its the first load and replay
                if (zIndex == 0) {
                    for (int i = 0; i < menu.getActionArray().size() - 1; i++) {
                        char showAction = menu.getActionArray().get(i);
                        avatar.takeAction(showAction);
                        ter.renderFrame(world.accessGrid());
                        StdDraw.pause(200);
                    }
                } else {
                    //Does action, but doesn't render up until Z index
                    for (int i = 0; i < zIndex; i++) {
                        char nonShowAction = menu.getActionArray().get(i);
                        avatar.takeAction(nonShowAction);
                    }
                    ter.renderFrame(world.accessGrid());
                    //Does action and renders the frame
                    for (int i = zIndex + 1; i < menu.getActionArray().size() - 1; i++) {
                        char showAction = menu.getActionArray().get(i);
                        avatar.takeAction(showAction);
                        ter.renderFrame(world.accessGrid());
                        StdDraw.pause(100);
                    }
                }
            }

            //If not replay, just load
            else {
                for (Character c : menu.getActionArray()) {
                    char preAction = c;
                    avatar.takeAction(preAction);
                }
            }
        }
        ter.renderFrame(world.accessGrid());


        // Declare variables to store previous and current input
        char prevInput = ' ';
        char currentInput = ' ';

        store += 'Z';

        // Move avatar
        while (true) {
            hud.frame(hud.updateMouse());
            if (coinCounter == 0) {
                menu.win();
            }
            while (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(10);
            }
            if (StdDraw.hasNextKeyTyped()) {


                // Update previous and current input
                prevInput = currentInput;
                currentInput = StdDraw.nextKeyTyped();


                //Quit if given :Q or :q
                if (prevInput == ':') {
                    if (currentInput == 'q' || currentInput == 'Q') {
                        Out out = new Out("output.txt");
                        out.println(store);
                        System.exit(0);
                    }
                }


                // Use both previous and current input to take action
                avatar.takeAction(currentInput);
                ter.renderFrame(worldFrame);
                store += currentInput;
            }
        }
    }
}
