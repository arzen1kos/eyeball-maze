import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arzenikos.eyeballmaze.Color;
import com.arzenikos.eyeballmaze.Direction;
import com.arzenikos.eyeballmaze.ErrorCode;
import com.arzenikos.eyeballmaze.Game;
import com.arzenikos.eyeballmaze.GameException;
import com.arzenikos.eyeballmaze.PlayableSquare;
import com.arzenikos.eyeballmaze.Shape;

class GameLevelAndGoalTest {
    @Test
    void tracksLevelsAndRestoresEachBoardsState() {
        Game game = new Game();
        game.addLevel(2, 3);
        game.addSquare(new PlayableSquare(Color.GREEN, Shape.STAR), 1, 2);
        game.addLevel(4, 5);
        game.addSquare(new PlayableSquare(Color.RED, Shape.CROSS), 3, 4);

        assertEquals(2, game.getLevelCount());
        assertEquals(4, game.getLevelHeight());
        assertEquals(5, game.getLevelWidth());

        game.setLevel(0);
        assertEquals(2, game.getLevelHeight());
        assertEquals(3, game.getLevelWidth());
        assertEquals(Color.GREEN, game.getColorAt(1, 2));
    }

    @Test
    void completingGoalUpdatesCountsAndKeepsGoalSquarePlayableUntilPlayerLeaves() {
        Game game = new Game();
        game.addLevel(1, 3);
        game.addSquare(new PlayableSquare(Color.BLUE, Shape.DIAMOND), 0, 0);
        game.addSquare(new PlayableSquare(Color.BLUE, Shape.CROSS), 0, 1);
        game.addSquare(new PlayableSquare(Color.RED, Shape.CROSS), 0, 2);
        game.addGoal(0, 1);
        game.addEyeball(0, 0, Direction.RIGHT);

        game.moveTo(0, 1);
        assertFalse(game.hasGoalAt(0, 1));
        assertEquals(0, game.getGoalCount());
        assertEquals(1, game.getCompletedGoalCount());
        assertEquals(Color.BLUE, game.getColorAt(0, 1));

        game.moveTo(0, 2);
        assertEquals(Color.BLANK, game.getColorAt(0, 1));
    }

    @Test
    void validatesCoordinatesAndReportsErrorCodes() {
        Game game = new Game();
        game.addLevel(2, 2);

        GameException exception = assertThrows(GameException.class, () -> game.addGoal(2, 0));
        assertEquals(ErrorCode.INDEX_OUT_OF_BOUNDS, exception.getErrorCode());

        GameException missingSquare = assertThrows(GameException.class, () -> game.getColorAt(0, 0));
        assertEquals(ErrorCode.SQUARE_NOT_FOUND, missingSquare.getErrorCode());
    }

    @Test
    void rejectsInvalidLevelIndex() {
        Game game = new Game();
        game.addLevel(1, 1);

        assertThrows(GameException.class, () -> game.setLevel(1));
    }

    @Test
    void addingSameGoalTwiceDoesNotDoubleCountIt() {
        Game game = new Game();
        game.addLevel(1, 1);

        game.addGoal(0, 0);
        game.addGoal(0, 0);

        assertTrue(game.hasGoalAt(0, 0));
        assertEquals(1, game.getGoalCount());
    }
}
