package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class HUD {
    private TETile[][] tiles;
    private int currX;
    private int currY;

    private int height;

    public HUD(World world) {
        this.tiles = world.accessGrid();
        this.height = world.getHeight();

    }


    public String updateMouse() {
        int newX = (int) StdDraw.mouseX();
        int newY = (int) StdDraw.mouseY();


        if (isValid() && (currX != newX || currY != newY)) {
            currX = newX;
            currY = newY;

        }
        return (isValid()) ? tiles[currX][currY - 4].description() : "";
    }

    public void frame(String input) {
        Font bigFont = new Font("Monaco", Font.BOLD, 16);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        if (isValid()) {
            StdDraw.text(5, height + 5, input);
        }

        StdDraw.show();
    }

    public boolean isValid() {
        int y = (int) StdDraw.mouseY();
        return (y - 4 < height && y - 4 >= 0);

    }
}
