package models;

import game.GameMap;
import game.GameState;

public class Robot {
    private final int MAX_MOVE_DELAY = 2000;
    private final int MIN_MOVE_DELAY = 500;

    private final String robotId;
    private final int delay;
    private GridPosition gridPosition;
    GameState gameState;

    public Robot(String robotId) {
        this.robotId = robotId;
        this.delay = getRandomDelay();
        this.gridPosition = new GridPosition();
    }

    public String getRobotId() {
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
        return "Robot{robotId["+robotId+"], delay["+ delay+"], gridPosition:["+gridPosition.toString()+"]}";
    }
}
