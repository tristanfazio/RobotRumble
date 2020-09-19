package models;

public class GridPosition {
    int gridX;
    int gridY;
    double animationX;
    double animationY;

    public GridPosition() {

    }
    public GridPosition(int gridX, int gridY){
        this.gridX = gridX;
        this.gridY = gridY;
        animationX = gridX;
        animationY = gridY;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void incrementAnimationX() {
        animationX = animationX + 0.1;
    }

    public void incrementAnimationY() {
         animationY = animationY + 0.1;
    }

    public void decrementAnimationX() {
        animationX = animationX - 0.1;
    }

    public void decrementAnimationY() {
        animationY = animationY - 0.1;
    }

    public double getAnimationX() {
        return animationX;
    }

    public double getAnimationY() {
        return animationY;
    }

    public void setAnimationX(double animationX) {
        this.animationX = animationX;
    }

    public void setAnimationY(double animationY) {
        this.animationY = animationY;
    }

    @Override
    public String toString() {
        return "("+gridX+","+gridY+")";
    }
}
