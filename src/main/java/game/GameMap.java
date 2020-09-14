package game;

import javafx.application.Platform;
import models.GridCell;
import models.GridPosition;
import models.Robot;
import ui.JFXArena;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameMap {
    private final GridCell[][] gameMap;
    private final GridPosition TOP_LEFT;
    private final GridPosition TOP_RIGHT;
    private final GridPosition BOTTOM_LEFT;
    private final GridPosition BOTTOM_RIGHT;

    public GameMap(int gridWidth, int gridHeight) {
        this.gameMap = new GridCell[gridWidth][gridHeight];
        TOP_LEFT = new GridPosition(0,0);
        TOP_RIGHT = new GridPosition(gridWidth-1,0);
        BOTTOM_LEFT = new GridPosition(0,gridHeight-1);
        BOTTOM_RIGHT = new GridPosition(gridWidth-1, gridHeight-1);

        for (GridCell[] row: gameMap)
            Arrays.fill(row, new GridCell());
    }

    public boolean isPositionOccupied(GridPosition position) {
        return gameMap[position.getGridX()][position.getGridY()].isOccupied();
    }

    public GridPosition getNextRandomValidSpawnPosition() {
        ArrayList<GridPosition> validPositions = new ArrayList<>();
        GridPosition gridPosition;

        if(!isPositionOccupied(TOP_LEFT)) validPositions.add(TOP_LEFT);
        if(!isPositionOccupied(TOP_RIGHT)) validPositions.add(TOP_RIGHT);
        if(!isPositionOccupied(BOTTOM_LEFT)) validPositions.add(BOTTOM_LEFT);
        if(!isPositionOccupied(BOTTOM_RIGHT)) validPositions.add(BOTTOM_RIGHT);

        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(validPositions.size());
        gridPosition = validPositions.get(index);
        return gridPosition;
    }

    public boolean isValidSpawnPosition() {
        return !isPositionOccupied(TOP_LEFT) ||
                !isPositionOccupied(TOP_RIGHT) ||
                !isPositionOccupied(BOTTOM_LEFT) ||
                !isPositionOccupied(BOTTOM_RIGHT);
    }

    public ArrayList<GridPosition> getListOfValidMovesForPosition(GridPosition gridPosition) {
        ArrayList<GridPosition> moveList = new ArrayList<>();
        return moveList;
    }

    public void moveRobotIntoPosition(Robot robot) {
        GridPosition gridPosition = robot.gridPosition();
        gameMap[gridPosition.getGridX()][gridPosition.getGridY()].setOccupant(robot);
    }
}

