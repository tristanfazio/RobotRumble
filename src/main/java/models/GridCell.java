package models;

public class GridCell {
    boolean occupied;
    Robot robot;

    public GridCell(){
        occupied = false;
        robot = null;
    }
    public boolean isOccupied() {
        return occupied;
    }

    public Robot getOccupant() {
        return robot;
    }

    public void setOccupant(Robot robot) {
        this.robot = robot;
        this.occupied = true;
    }

    public void unOccupy() {
        occupied = false;
    }
}
