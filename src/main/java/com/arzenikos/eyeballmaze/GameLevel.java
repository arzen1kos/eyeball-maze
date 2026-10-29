package com.arzenikos.eyeballmaze;
public class GameLevel {
    protected final int levelNumber;
    protected final int levelWidth;
    protected final int levelHeight;
    public int totalGoalCount;
    public int completedGoalCount;

    public GameLevel(int levelNumber, int levelHeight, int levelWidth) {
        if (levelNumber < 0 || levelHeight <= 0 || levelWidth <= 0) {
            throw new IllegalArgumentException("Level number must be non-negative and dimensions must be positive.");
        }
        this.levelNumber = levelNumber;
        this.levelHeight = levelHeight;
        this.levelWidth = levelWidth;
    }

    public int getLevelWidth() {
        return levelWidth;
    }

    public int getLevelHeight() {
        return levelHeight;
    }

    public int getLevelNumber() {
        return levelNumber;
    }
}
