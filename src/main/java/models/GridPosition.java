package models;

public class GridPosition {
    int gridX;
    int gridY;

    public GridPosition() {

    }
    public GridPosition(int gridX, int gridY){
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    @Override
    public String toString() {
        return "("+gridX+","+gridY+")";
    }
}
