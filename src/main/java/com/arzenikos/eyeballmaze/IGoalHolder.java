package com.arzenikos.eyeballmaze;
public interface IGoalHolder {
    void addGoal(int row, int column);

    int getGoalCount();

    boolean hasGoalAt(int targetRow, int targetColumn);

    int getCompletedGoalCount();
}
