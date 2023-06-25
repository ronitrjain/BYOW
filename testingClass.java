package byow.Core;
import byow.TileEngine.TETile;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class testingClass {
    @Test

    public void agTest() {

        Engine e1 = new Engine();

        Engine e2 = new Engine();

        TETile[][] result = e1.interactWithInputString("n5643591630821615871swwaawd");

        e2.interactWithInputString("n7313251667695476404sasdw");

        TETile[][] result2 = e2.interactWithInputString("l");

        assertThat(result2).isNotEqualTo(result);

    }
}
