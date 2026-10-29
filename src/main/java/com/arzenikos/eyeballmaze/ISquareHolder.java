package com.arzenikos.eyeballmaze;
public interface ISquareHolder {
    void addSquare(Square square, int row, int column);

    Color getColorAt(int row, int column);

    Shape getShapeAt(int row, int column);
}
