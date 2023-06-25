package byow.Core;


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;


import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Menu {
    private int intro_screen_width = 50;
    private int intro_screen_height = 40;


    private ArrayList<Character> actionArray = new ArrayList<>();
    private ArrayList<Character> replayArray = new ArrayList<>();


    private int size;

    public static HashMap<String, ArrayList<Character>> actionMap = new HashMap<>();

    private int midWidth;


    private int midHeight;

    private Engine engine;

    public Menu(Engine eng){
        this.engine = eng;
    }


    public String start() {
        startMenu();

        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (String.valueOf(key).equals("n") || String.valueOf(key).equals("N")) {
                return drawInputPrompt();
            } else if (String.valueOf(key).equals("l") || String.valueOf(key).equals("L")) {
                this.engine.changeReplay(false);
                return storeSaved();
            } else if (String.valueOf(key).equals("q") || String.valueOf(key).equals("Q")) {
                System.exit(0);
            }
            else if (String.valueOf(key).equals("r") || String.valueOf(key).equals("R")) {
                this.engine.changeReplay(true);
                return storeSaved();
            }

        }
    }


    public String drawInputPrompt() {
        int midWidth = intro_screen_width / 2;
        int midHeight = intro_screen_height / 2;
        String input = "";
        while (true) {
            StdDraw.clear();
            StdDraw.clear(Color.black);
            Font bigFont = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(bigFont);
            StdDraw.setPenColor(Color.white);
            StdDraw.text(midWidth, midHeight + 2, "ENTER THE SEED");
            StdDraw.text(midWidth, midHeight, "YOUR SEED: " + input);
            StdDraw.show();
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            String temp_key_string = String.valueOf(key);
            if (isNumeric(temp_key_string))
                input += String.valueOf(key);
            else if ((temp_key_string.equals("S") || temp_key_string.equals("s")) && input.length() != 0) {
                StdDraw.clear();
                break;
            }
            StdDraw.pause(100);
        }
        return ("N" + input + "S");
    }


    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public void startGame() {
        StdDraw.setCanvasSize(intro_screen_width * 16, intro_screen_height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, intro_screen_width);
        StdDraw.setYscale(0, intro_screen_height);
        StdDraw.clear(Color.BLACK);
    }


    public String seedBuilder(String input) {
        String seedString = "";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == 'N') {
                // Found the start of the seed value
                int j = i + 1;
                while (j < input.length() && Character.isDigit(input.charAt(j))) {
                    seedString += input.charAt(j);
                    j++;
                }
                break;
            }
        }
        return seedString;
    }


    public HashMap<String, ArrayList<Character>> getActionMap() {
        return actionMap;
    }


    public int size() {
        return size;
    }


    public void startMenu() {
        StdDraw.clear(Color.black);
        midWidth = intro_screen_width / 2;
        midHeight = intro_screen_height / 2;


        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight + 4.5, "(N)EW GAME");
        StdDraw.text(midWidth, midHeight + 1.5, "(L)OAD GAME");
        StdDraw.text(midWidth, midHeight - 1.5, "(R)EPLAY GAME");
        StdDraw.text(midWidth, midHeight - 4.5, "(Q)UIT GAME");
        StdDraw.show();


    }


    public void startSeed(String input) {
        StdDraw.clear();
        StdDraw.clear(Color.black);
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight + 2, "ENTER THE SEED");
        StdDraw.text(midWidth, midHeight, "YOUR SEED: " + input);
        StdDraw.show();
    }


    public void win() {
        StdDraw.clear();
        StdDraw.clear(Color.black);
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight + 2, "You Win!");
        StdDraw.show();
    }



    public void checkColor(){
        StdDraw.clear();
        StdDraw.clear(Color.black);
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight, "Choose color (R: Red)/ (B: Blue)!");
        StdDraw.show();
        StdDraw.pause(2000);
    }

    public String storeSaved() {
        In in = new In("output.txt");
        in.readLine();
        String line = in.readLine();
        String givenSeed =  "";

        int startIndex = line.indexOf('N'); // Find the index of 'n' in the line
        int endIndex = line.indexOf('S'); // Find the index of 's' in the line

        if (startIndex != -1 && endIndex != -1 && startIndex <= endIndex) { // Check if 'n' and 's' are found and in correct order
            givenSeed = line.substring(startIndex, endIndex + 1); // Extract the substring between 'n' and 's', including 'n' and 's'
        }

        while (in.hasNextLine()) {
            char action = in.readChar();
            actionArray.add(action);
            replayArray.add(action);
            size++;
        }
        return givenSeed;
    }



    public ArrayList<Character> getActionArray() {
        return actionArray;
    }


}


