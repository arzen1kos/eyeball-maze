package com.arzenikos.eyeballmaze;
public class PlayableSquare extends Square {
    public PlayableSquare() {
        this(Color.BLANK, Shape.BLANK);
    }

    public PlayableSquare(Color color, Shape shape) {
        super(color, shape);
    }

    public void setAsGoal() {
        isGoal = true;
    }
}
