package game;

import models.GridPosition;

public class FireCommand {
    GridPosition gridPosition;
    long fireTime;

    public FireCommand(GridPosition gridPosition, long fireTime) {
        this.gridPosition = gridPosition;
        this.fireTime = fireTime;
    }

    public GridPosition getGridPosition() {
        return gridPosition;
    }

    public long getFireTime() {
        return fireTime;
    }
}
