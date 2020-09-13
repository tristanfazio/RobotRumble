package models;

public class Robot {
    private final int MAX_MOVE_DELAY = 2000;
    private final int MIN_MOVE_DELAY = 500;

    private final int robotId;
    private final int delay;
    private GridPosition gridPosition;
    GameMap gameMap;

    public Robot(int robotId, GameMap gameMap) {
        this.robotId = robotId;
        this.delay = getRandomDelay();
        this.gameMap = gameMap;
        this.gridPosition = new GridPosition();
    }

    public int getRobotId() {
        return robotId;
    }

    public int getDelay() {
        return delay;
    }

    public GridPosition gridPosition() {
        return  gridPosition;
    }

    public void setGridPosition(GridPosition gridPosition) {
        this.gridPosition = gridPosition;
    }


    private int getRandomDelay() {
        int delay;
        delay = (int) ((Math.random() * (MAX_MOVE_DELAY - MIN_MOVE_DELAY)) + MIN_MOVE_DELAY);
        return delay;
    }

    @Override
    public String toString() {
        return "Robot[robotId:"+robotId+", delay:"+ delay+",grid position:("+gridPosition.gridX+","+gridPosition.gridY+")]";
    }
}
