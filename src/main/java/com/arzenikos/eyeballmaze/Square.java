package com.arzenikos.eyeballmaze;
public abstract class Square {
    public boolean isGoal;
    protected final Color color;
    protected final Shape shape;

    protected Square(Color color, Shape shape) {
        if (color == null || shape == null) {
            throw new IllegalArgumentException("A square must have a color and shape.");
        }
        this.color = color;
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }

    public Shape getShape() {
        return shape;
    }

    public boolean isBlank() {
        return color == Color.BLANK || shape == Shape.BLANK;
    }
}
