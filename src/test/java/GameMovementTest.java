import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.arzenikos.eyeballmaze.BlankSquare;
import com.arzenikos.eyeballmaze.Color;
import com.arzenikos.eyeballmaze.Direction;
import com.arzenikos.eyeballmaze.ErrorCode;
import com.arzenikos.eyeballmaze.Game;
import com.arzenikos.eyeballmaze.GameException;
import com.arzenikos.eyeballmaze.Message;
import com.arzenikos.eyeballmaze.PlayableSquare;
import com.arzenikos.eyeballmaze.Shape;

class GameMovementTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.addLevel(2, 6);
        game.addSquare(new PlayableSquare(Color.BLUE, Shape.DIAMOND), 0, 0);
        game.addSquare(new PlayableSquare(Color.BLUE, Shape.CROSS), 0, 1);
        game.addSquare(new PlayableSquare(Color.RED, Shape.CROSS), 0, 2);
        game.addSquare(new BlankSquare(), 0, 3);
        game.addSquare(new PlayableSquare(Color.RED, Shape.STAR), 0, 4);
        game.addSquare(new PlayableSquare(Color.YELLOW, Shape.STAR), 0, 5);
        game.addEyeball(0, 0, Direction.RIGHT);
    }

    @Test
    void allowsMoveWhenEitherColorOrShapeMatches() {
        assertTrue(game.canMoveTo(0, 1));
        assertEquals(Message.OK, game.messageIfMovingTo(0, 1));
    }

    @Test
    void allowsPerpendicularTurnAndUpdatesFacingToTravelDirection() {
        Game turnGame = new Game();
        turnGame.addLevel(2, 1);
        turnGame.addSquare(new PlayableSquare(Color.BLUE, Shape.DIAMOND), 0, 0);
        turnGame.addSquare(new PlayableSquare(Color.BLUE, Shape.CROSS), 1, 0);
        turnGame.addEyeball(1, 0, Direction.RIGHT);

        assertTrue(turnGame.canMoveTo(0, 0));
        turnGame.moveTo(0, 0);
        assertEquals(Direction.UP, turnGame.getEyeballDirection());
    }

    @Test
    void rejectsMoveWhenNeitherColorNorShapeMatches() {
        assertFalse(game.canMoveTo(0, 2));
        assertEquals(Message.DIFFERENT_SHAPE_OR_COLOR, game.messageIfMovingTo(0, 2));
    }

    @Test
    void rejectsReverseAndDiagonalMovesWithSpecificMessages() {
        assertEquals(Message.BACKWARDS_MOVE, game.checkDirectionMessage(0, 0));
        assertEquals(Message.MOVING_DIAGONALLY, game.checkDirectionMessage(1, 1));

        game.addEyeball(0, 2, Direction.LEFT);
        assertEquals(Message.BACKWARDS_MOVE, game.checkDirectionMessage(0, 3));
    }

    @Test
    void rejectsPathThatCrossesBlankSquare() {
        assertFalse(game.hasBlankFreePathTo(0, 4));
        assertEquals(Message.MOVING_OVER_BLANK, game.checkMessageForBlankOnPathTo(0, 4));
        assertFalse(game.canMoveTo(0, 4));
    }

    @Test
    void validMoveUpdatesPositionDirectionAndLeavesBlankSquareBehind() {
        game.moveTo(0, 1);

        assertEquals(0, game.getEyeballRow());
        assertEquals(1, game.getEyeballColumn());
        assertEquals(Direction.RIGHT, game.getEyeballDirection());
        assertEquals(Color.BLANK, game.getColorAt(0, 0));
        assertEquals(Shape.BLANK, game.getShapeAt(0, 0));
    }

    @Test
    void invalidMoveRaisesTypedModelError() {
        GameException exception = assertThrows(GameException.class, () -> game.moveTo(0, 2));
        assertEquals(ErrorCode.INVALID_MOVE, exception.getErrorCode());
    }
}
