package com.arzenikos.eyeballmaze;
public interface ILevelHolder {
    void addLevel(int height, int width);

    int getLevelWidth();

    int getLevelHeight();

    void setLevel(int levelNumber);

    int getLevelCount();
}
