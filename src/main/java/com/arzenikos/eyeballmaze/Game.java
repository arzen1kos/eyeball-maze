package com.arzenikos.eyeballmaze;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Game implements ILevelHolder, IGoalHolder, ISquareHolder, IEyeballHolder, IMoving {
    private Map<String, Square> squareCollection = new HashMap<>();
    private final List<GameLevel> levelCollection = new ArrayList<>();
    private final List<Map<String, Square>> squaresByLevel = new ArrayList<>();
    private final List<Set<String>> goalsByLevel = new ArrayList<>();
    private int levelCount;
    private EyeBall theEyeball;
    private GameLevel gameLevel;

    @Override
    public void addLevel(int height, int width) {
        GameLevel newLevel = new GameLevel(levelCount, height, width);
        Map<String, Square> newSquares = new HashMap<>();
        Set<String> newGoals = new HashSet<>();
        levelCollection.add(newLevel);
        squaresByLevel.add(newSquares);
        goalsByLevel.add(newGoals);
        levelCount++;
        gameLevel = newLevel;
        squareCollection = newSquares;
        theEyeball = null;
    }

    @Override
    public int getLevelWidth() {
        return requireLevel().getLevelWidth();
    }

    @Override
    public int getLevelHeight() {
        return requireLevel().getLevelHeight();
    }

    @Override
    public int getLevelCount() {
        return levelCount;
    }

    @Override
    public void setLevel(int newLevel) {
        if (newLevel < 0 || newLevel >= levelCollection.size()) {
            throw new GameException(ErrorCode.INDEX_OUT_OF_BOUNDS, "Level index is outside the available levels.");
        }
        gameLevel = levelCollection.get(newLevel);
        squareCollection = squaresByLevel.get(newLevel);
        theEyeball = null;
    }

    @Override
    public void addGoal(int row, int column) {
        validateCoordinate(row, column);
        String key = generateCoordinateKey(row, column);
        if (goalsByLevel.get(gameLevel.getLevelNumber()).add(key)) {
            gameLevel.totalGoalCount++;
            Square square = squareCollection.get(key);
            if (square instanceof PlayableSquare playableSquare) {
                playableSquare.setAsGoal();
            }
        }
    }

    @Override
    public int getGoalCount() {
        return requireLevel().totalGoalCount;
    }

    @Override
    public boolean hasGoalAt(int targetRow, int targetColumn) {
        validateCoordinate(targetRow, targetColumn);
        return goalsByLevel.get(gameLevel.getLevelNumber()).contains(generateCoordinateKey(targetRow, targetColumn));
    }

    @Override
    public int getCompletedGoalCount() {
        return requireLevel().completedGoalCount;
    }

    @Override
    public Color getColorAt(int row, int column) {
        return getSquareAt(row, column).getColor();
    }

    @Override
    public Shape getShapeAt(int row, int column) {
        return getSquareAt(row, column).getShape();
    }

    @Override
    public void addSquare(Square square, int row, int column) {
        if (square == null) {
            throw new IllegalArgumentException("Square cannot be null.");
        }
        validateCoordinate(row, column);
        String key = generateCoordinateKey(row, column);
        if (hasGoalAt(row, column) && square instanceof PlayableSquare playableSquare) {
            playableSquare.setAsGoal();
        }
        squareCollection.put(key, square);
    }

    @Override
    public void addEyeball(int currentY, int currentX, Direction direction) {
        validateCoordinate(currentY, currentX);
        if (direction == null) {
            throw new IllegalArgumentException("Eyeball direction cannot be null.");
        }
        theEyeball = new EyeBall(currentY, currentX, direction);
    }

    @Override
    public int getEyeballRow() {
        return requireEyeball().getYPosition();
    }

    @Override
    public int getEyeballColumn() {
        return requireEyeball().getXPosition();
    }

    @Override
    public Direction getEyeballDirection() {
        return requireEyeball().getDirection();
    }

    @Override
    public boolean canMoveTo(int row, int column) {
        validateCoordinate(row, column);
        if (theEyeball == null || !isDirectionOK(row, column) || !hasBlankFreePathTo(row, column)) {
            return false;
        }
        Square source = squareCollection.get(generateCoordinateKey(getEyeballRow(), getEyeballColumn()));
        Square target = squareCollection.get(generateCoordinateKey(row, column));
        return source != null && target != null && !source.isBlank() && !target.isBlank()
                && isMatchingShapeOrColor(target);
    }

    @Override
    public boolean isDirectionOK(int destinationRow, int destinationColumn) {
        validateCoordinate(destinationRow, destinationColumn);
        if (theEyeball == null) {
            return false;
        }
        int rowDifference = destinationRow - getEyeballRow();
        int columnDifference = destinationColumn - getEyeballColumn();
        if ((rowDifference == 0 && columnDifference == 0)
                || (rowDifference != 0 && columnDifference != 0)) {
            return false;
        }
        Direction travelDirection = directionBetween(getEyeballRow(), getEyeballColumn(),
                destinationRow, destinationColumn);
        return travelDirection != opposite(theEyeball.getDirection());
    }

    @Override
    public Message checkDirectionMessage(int row, int column) {
        validateCoordinate(row, column);
        if (theEyeball == null) {
            return Message.BACKWARDS_MOVE;
        }
        int rowDifference = row - getEyeballRow();
        int columnDifference = column - getEyeballColumn();
        if (rowDifference != 0 && columnDifference != 0) {
            return Message.MOVING_DIAGONALLY;
        }
        if (rowDifference == 0 && columnDifference == 0) {
            return Message.BACKWARDS_MOVE;
        }
        Direction travelDirection = directionBetween(getEyeballRow(), getEyeballColumn(), row, column);
        return travelDirection == opposite(theEyeball.getDirection()) ? Message.BACKWARDS_MOVE : Message.OK;
    }

    @Override
    public boolean hasBlankFreePathTo(int row, int column) {
        validateCoordinate(row, column);
        if (theEyeball == null) {
            return false;
        }
        int rowDifference = Integer.compare(row, getEyeballRow());
        int columnDifference = Integer.compare(column, getEyeballColumn());
        if (rowDifference != 0 && columnDifference != 0) {
            return false;
        }
        int currentRow = getEyeballRow() + rowDifference;
        int currentColumn = getEyeballColumn() + columnDifference;
        while (currentRow != row || currentColumn != column) {
            Square square = squareCollection.get(generateCoordinateKey(currentRow, currentColumn));
            if (square == null || square.isBlank()) {
                return false;
            }
            currentRow += rowDifference;
            currentColumn += columnDifference;
        }
        return true;
    }

    @Override
    public Message checkMessageForBlankOnPathTo(int row, int column) {
        return hasBlankFreePathTo(row, column) ? Message.OK : Message.MOVING_OVER_BLANK;
    }

    @Override
    public Message messageIfMovingTo(int row, int column) {
        validateCoordinate(row, column);
        if (theEyeball == null) {
            return Message.DIFFERENT_SHAPE_OR_COLOR;
        }
        Square target = squareCollection.get(generateCoordinateKey(row, column));
        if (target == null) {
            return Message.DIFFERENT_SHAPE_OR_COLOR;
        }
        Square source = squareCollection.get(generateCoordinateKey(getEyeballRow(), getEyeballColumn()));
        if (source == null || source.isBlank() || target.isBlank() || !isMatchingShapeOrColor(target)) {
            return Message.DIFFERENT_SHAPE_OR_COLOR;
        }
        Message directionMessage = checkDirectionMessage(row, column);
        if (directionMessage != Message.OK) {
            return directionMessage;
        }
        return checkMessageForBlankOnPathTo(row, column);
    }

    @Override
    public void moveTo(int destinationRow, int destinationColumn) {
        validateCoordinate(destinationRow, destinationColumn);
        if (!canMoveTo(destinationRow, destinationColumn)) {
            throw new GameException(ErrorCode.INVALID_MOVE, "The requested move is not valid.");
        }

        String destinationKey = generateCoordinateKey(destinationRow, destinationColumn);
        Set<String> goals = goalsByLevel.get(gameLevel.getLevelNumber());
        if (goals.remove(destinationKey)) {
            gameLevel.totalGoalCount--;
            gameLevel.completedGoalCount++;
            Square goalSquare = squareCollection.get(destinationKey);
            if (goalSquare != null) {
                goalSquare.isGoal = false;
            }
        }

        updateGoalSquare();
        theEyeball.updateEyeball(destinationRow, destinationColumn);
    }

    private GameLevel requireLevel() {
        if (gameLevel == null) {
            throw new GameException(ErrorCode.INDEX_OUT_OF_BOUNDS, "Add a level before using the game.");
        }
        return gameLevel;
    }

    private EyeBall requireEyeball() {
        if (theEyeball == null) {
            throw new GameException(ErrorCode.INVALID_MOVE, "Add the eyeball before requesting its state.");
        }
        return theEyeball;
    }

    private void validateCoordinate(int row, int column) {
        requireLevel();
        if (!isValidCoordinate(row, column)) {
            throw new GameException(ErrorCode.INDEX_OUT_OF_BOUNDS, "Coordinate is outside the current level.");
        }
    }

    private boolean isValidCoordinate(int row, int column) {
        return gameLevel != null && row >= 0 && column >= 0 && row < gameLevel.getLevelHeight()
                && column < gameLevel.getLevelWidth();
    }

    private Square getSquareAt(int row, int column) {
        validateCoordinate(row, column);
        Square square = squareCollection.get(generateCoordinateKey(row, column));
        if (square == null) {
            throw new GameException(ErrorCode.SQUARE_NOT_FOUND, "No square exists at (" + row + ", " + column + ").");
        }
        return square;
    }

    private String generateCoordinateKey(int row, int column) {
        return row + "," + column;
    }

    private boolean isMatchingShapeOrColor(Square targetSquare) {
        Square sourceSquare = squareCollection.get(generateCoordinateKey(getEyeballRow(), getEyeballColumn()));
        return sourceSquare != null
                && (sourceSquare.getColor() == targetSquare.getColor()
                || sourceSquare.getShape() == targetSquare.getShape());
    }

    private void updateGoalSquare() {
        squareCollection.put(generateCoordinateKey(getEyeballRow(), getEyeballColumn()), new BlankSquare());
    }

    private Direction directionBetween(int sourceRow, int sourceColumn, int destinationRow, int destinationColumn) {
        if (destinationRow < sourceRow) {
            return Direction.UP;
        }
        if (destinationRow > sourceRow) {
            return Direction.DOWN;
        }
        return destinationColumn < sourceColumn ? Direction.LEFT : Direction.RIGHT;
    }

    private Direction opposite(Direction direction) {
        return switch (direction) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
        };
    }
}
