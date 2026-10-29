package com.arzenikos.eyeballmaze;
public interface IMoving {
    boolean canMoveTo(int destinationRow, int destinationColumn);

    Message messageIfMovingTo(int destinationRow, int destinationColumn);

    boolean isDirectionOK(int destinationRow, int destinationColumn);

    Message checkDirectionMessage(int destinationRow, int destinationColumn);

    boolean hasBlankFreePathTo(int destinationRow, int destinationColumn);

    Message checkMessageForBlankOnPathTo(int destinationRow, int destinationColumn);

    void moveTo(int destinationRow, int destinationColumn);
}
