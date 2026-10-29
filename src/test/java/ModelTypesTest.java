import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arzenikos.eyeballmaze.BlankSquare;
import com.arzenikos.eyeballmaze.Color;
import com.arzenikos.eyeballmaze.PlayableSquare;
import com.arzenikos.eyeballmaze.Position;
import com.arzenikos.eyeballmaze.Shape;

class ModelTypesTest {
    @Test
    void positionUsesZeroBasedRowAndColumnAndValueEquality() {
        Position first = new Position(0, 2);
        Position same = new Position(0, 2);

        assertEquals(0, first.getRow());
        assertEquals(2, first.getColumn());
        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
        assertNotEquals(first, new Position(2, 0));
    }

    @Test
    void playableGoalCanBeMarkedAndBlankSquareUsesBlankValues() {
        PlayableSquare playableSquare = new PlayableSquare(Color.PURPLE, Shape.LIGHTNING);
        playableSquare.setAsGoal();

        assertTrue(playableSquare.isGoal);
        assertEquals(Color.PURPLE, playableSquare.getColor());
        assertEquals(Shape.LIGHTNING, playableSquare.getShape());

        BlankSquare blankSquare = new BlankSquare();
        assertEquals(Color.BLANK, blankSquare.getColor());
        assertEquals(Shape.BLANK, blankSquare.getShape());
        assertTrue(blankSquare.isBlank());
    }
}
