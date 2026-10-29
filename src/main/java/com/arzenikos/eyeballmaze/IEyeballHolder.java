package com.arzenikos.eyeballmaze;
public interface IEyeballHolder {
    void addEyeball(int row, int column, Direction direction);

    int getEyeballRow();

    int getEyeballColumn();

    Direction getEyeballDirection();
}
