package com.arzenikos.eyeballmaze;
public class EyeBall {
    protected Position currentPosition;
    protected Direction currentDirection;
    protected int currentYPosition;
    protected int currentXPosition;

    public EyeBall(int newYPosition, int newXPosition, Direction eyeballDirection) {
        if (eyeballDirection == null) {
            throw new IllegalArgumentException("Eyeball direction cannot be null.");
        }
        currentPosition = new Position(newYPosition, newXPosition);
        currentYPosition = newYPosition;
        currentXPosition = newXPosition;
        currentDirection = eyeballDirection;
    }

    public int getXPosition() {
        return currentXPosition;
    }

    public int getYPosition() {
        return currentYPosition;
    }

    public Direction getDirection() {
        return currentDirection;
    }

    public void updateEyeball(int row, int column) {
        Direction newDirection = getNewEyeballFacingDirection(row, column);
        currentPosition = new Position(row, column);
        currentYPosition = row;
        currentXPosition = column;
        currentDirection = newDirection;
    }

    public Direction getNewEyeballFacingDirection(int targetY, int targetX) {
        int rowDifference = targetY - currentYPosition;
        int columnDifference = targetX - currentXPosition;
        if (rowDifference == 0 && columnDifference == 0) {
            throw new GameException(ErrorCode.INVALID_MOVE, "The eyeball must move to a different square.");
        }
        if (rowDifference != 0 && columnDifference != 0) {
            throw new GameException(ErrorCode.INVALID_MOVE, "The eyeball cannot move diagonally.");
        }
        if (rowDifference < 0) {
            return Direction.UP;
        }
        if (rowDifference > 0) {
            return Direction.DOWN;
        }
        return columnDifference < 0 ? Direction.LEFT : Direction.RIGHT;
    }
}
