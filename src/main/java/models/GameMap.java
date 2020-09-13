package models;

import java.util.ArrayList;
import java.util.Random;

public class GameMap {
    private final boolean[][] gameMap;
    private final GridPosition TOP_LEFT;
    private final GridPosition TOP_RIGHT;
    private final GridPosition BOTTOM_LEFT;
    private final GridPosition BOTTOM_RIGHT;

    public GameMap(int gridWidth, int gridHeight) {
        this.gameMap = new boolean[gridWidth][gridHeight];

        TOP_LEFT = new GridPosition(0,0);
        TOP_RIGHT = new GridPosition(gridWidth-1,0);
        BOTTOM_LEFT = new GridPosition(0,gridHeight-1);
        BOTTOM_RIGHT = new GridPosition(gridWidth-1, gridHeight-1);
    }

    public void occupyPosition(GridPosition position) {
        gameMap[position.gridX][position.gridY] = true;
    }

    public void unOccupyPosition(GridPosition position) {
        gameMap[position.gridX][position.gridY] = false;
    }

    public boolean isPositionOccupied(GridPosition position) {
        return gameMap[position.gridX][position.gridY];
    }

    public GridPosition getNextValidSpawnPosition() {
        ArrayList<GridPosition> validPositions = new ArrayList<>();
        GridPosition gridPosition;

        if(!isPositionOccupied(TOP_LEFT)) validPositions.add(TOP_LEFT);
        if(!isPositionOccupied(TOP_RIGHT)) validPositions.add(TOP_RIGHT);
        if(!isPositionOccupied(BOTTOM_LEFT)) validPositions.add(BOTTOM_LEFT);
        if(!isPositionOccupied(BOTTOM_RIGHT)) validPositions.add(BOTTOM_RIGHT);

        Random randomGenerator = new Random();
        int index = validPositions.size() > 1 ? randomGenerator.nextInt(validPositions.size()) : 0;
        gridPosition = validPositions.get(index);
        return gridPosition;
    }

    public boolean isValidSpawnPosition() {
        return !isPositionOccupied(TOP_LEFT) ||
                !isPositionOccupied(TOP_RIGHT) ||
                !isPositionOccupied(BOTTOM_LEFT) ||
                !isPositionOccupied(BOTTOM_RIGHT);
    }
}

