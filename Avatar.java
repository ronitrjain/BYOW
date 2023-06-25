
package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class Avatar {
    private int[] avatarPos;

    private TETile player;

    private World world;

    private TETile[][] orgTile;

    private int xCoor;

    private Engine eng;

    private int yCoor;

    private boolean position_changed;

    public Avatar(World world, Engine engine) {
        this.avatarPos = world.getRandomAvatarPosition();
        this.player = Tileset.AVATAR_WHITE;
        this.world = world;

        this.orgTile = world.accessGrid();
        xCoor = avatarPos[0];
        yCoor = avatarPos[1];
        this.eng = engine;
    }

    public int[] get_position(){
        return avatarPos;
    }

    public void updateCoordinates(){
        int temp_x_coord_mouse = (int) StdDraw.mouseX();
        int temp_y_coord_mouse = (int) StdDraw.mouseY();

        if ((temp_x_coord_mouse == xCoor) && (temp_y_coord_mouse == yCoor)) {
            position_changed = false;
        }
        else {
            xCoor = temp_x_coord_mouse;
            yCoor = temp_y_coord_mouse;
            position_changed = true;
        }
    }

    public int get_x_coord() {
        return xCoor;
    }

    public int get_y_coord() {
        return yCoor;
    }

    public boolean has_mouse_moved() {
        return position_changed;
    }
    public void draw(){
        orgTile[get_x_coord()][get_y_coord()] = this.player;
    }

    public void takeAction(char actionInstruction) {
        if (actionInstruction == 'w') {
            moveUp();
            draw();
        } else if (actionInstruction == 'a') {
            moveLeft();
            draw();
        } else if (actionInstruction == 's') {
            moveDown();
            draw();
        } else if (actionInstruction == 'd') {
            move_right();
            draw();
        } else if (actionInstruction == 'R') {
            turnAvatarRed();
            draw();
        }
        else if (actionInstruction == 'Y') {
            turnAvatarYellow();
            draw();
        }
        else if (actionInstruction == 'B') {
            turnAvatarBlue();
            draw();
        }
    }

    private void moveAvatar(int newX, int newY) {
        if (checkTile(newX, newY)) {
            orgTile[xCoor][yCoor] = Tileset.FLOOR;
            xCoor = newX;
            yCoor = newY;
        }


    }


    private void moveUp() {
        if(checkCoin(xCoor, yCoor+1)) {
            eng.decrementCoin();
        }
        moveAvatar(xCoor, yCoor + 1);
    }

    private void moveLeft() {
        if(checkCoin(xCoor-1, yCoor)) {
            eng.decrementCoin();
        }
        moveAvatar(xCoor - 1, yCoor);
    }

    private void moveDown() {
        if(checkCoin(xCoor, yCoor-1)) {
            eng.decrementCoin();
        }
        moveAvatar(xCoor, yCoor - 1);
    }

    private void move_right() {
        if(checkCoin(xCoor+1, yCoor)) {
            eng.decrementCoin();
        }
        moveAvatar(xCoor + 1, yCoor);

    }

    private boolean checkTile(int x, int y) {

        if (orgTile[x][y].equals(Tileset.WALL) || orgTile[x][y].equals(Tileset.MOUNTAIN) || orgTile[x][y].equals(Tileset.SAND))
            return false;


        else
            return true;
    }

    public boolean checkCoin(int x, int y){
        if (orgTile[x][y].equals(Tileset.COIN)) {
            return true;
        }

        else {
            return false;
        }
    }

    public void displayColorOptions() {
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 18));
        StdDraw.textLeft(0.01, 0.95, "Press 'R' for Red");
        StdDraw.textLeft(0.01, 0.90, "Press 'B' for Blue");
        StdDraw.textLeft(0.01, 0.85, "Press 'Y' for Yellow");

    }

    public void turnAvatarRed() {
        this.player = Tileset.AVATAR_RED;
        StdDraw.show();
    }

    public void turnAvatarBlue() {
        this.player = Tileset.AVATAR_BLUE;
        StdDraw.show();
    }

    public void turnAvatarYellow() {
        this.player = Tileset.AVATAR_YELLOW;
        StdDraw.show();
    }
}


