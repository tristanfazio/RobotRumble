package game;

import javafx.application.Platform;
import models.GridCell;
import models.GridPosition;
import models.Robot;
import ui.JFXArena;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameMap {
    private final GridCell[][] gameMap;
    private final GridPosition TOP_LEFT;
    private final GridPosition TOP_RIGHT;
    private final GridPosition BOTTOM_LEFT;
    private final GridPosition BOTTOM_RIGHT;
    private final GridPosition CENTRE;
    private final int gridWidth;
    private final int gridHeight;

    public GameMap(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth-1;
        this.gridHeight = gridHeight-1;
        this.gameMap = new GridCell[gridWidth][gridHeight];
        TOP_LEFT = new GridPosition(0,0);
        TOP_RIGHT = new GridPosition(gridWidth-1,0);
        BOTTOM_LEFT = new GridPosition(0,gridHeight-1);
        BOTTOM_RIGHT = new GridPosition(gridWidth-1, gridHeight-1);
        CENTRE = new GridPosition(gridWidth/2,gridHeight/2);

        //instantiate map with empty cells
        for(int row = 0;row<gridWidth;row++) {
            for(int column=0;column<gridHeight;column++) {
                gameMap[row][column] = new GridCell();
            }
        }
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

    public ArrayList<GridPosition> getValidMoveListFromPosition(GridPosition gridPosition) {
        ArrayList<GridPosition> moveList = new ArrayList<>();

        validateMoveUpFromPosition(moveList,gridPosition);
        validateMoveDownFromPosition(moveList,gridPosition);
        validateMoveLeftFromPosition(moveList,gridPosition);
        validateMoveRightFromPosition(moveList,gridPosition);

        return moveList;
    }

    private void validateMoveRightFromPosition(ArrayList<GridPosition> moveList, GridPosition currentGridPosition) {
        if( currentGridPosition.getGridX() + 1 <= gridWidth ) {
            GridPosition positionToAttempt = new GridPosition(currentGridPosition.getGridX() + 1, currentGridPosition.getGridY());
            if(!isPositionOccupied(positionToAttempt))
                moveList.add(positionToAttempt);
        }
    }

    private void validateMoveLeftFromPosition(ArrayList<GridPosition> moveList, GridPosition currentGridPosition) {
        if( currentGridPosition.getGridX() - 1 >= 0 ) {
            GridPosition positionToAttempt = new GridPosition(currentGridPosition.getGridX() - 1, currentGridPosition.getGridY());
            if(!isPositionOccupied(positionToAttempt))
                moveList.add(positionToAttempt);
        }
    }

    private void validateMoveDownFromPosition(ArrayList<GridPosition> moveList, GridPosition currentGridPosition) {
        if( currentGridPosition.getGridY() + 1 <= gridHeight ) {
            GridPosition positionToAttempt = new GridPosition(currentGridPosition.getGridX(), currentGridPosition.getGridY() + 1);
            if(!isPositionOccupied(positionToAttempt))
                moveList.add(positionToAttempt);
        }
    }

    private void validateMoveUpFromPosition(ArrayList<GridPosition> moveList, GridPosition currentGridPosition) {
        if( currentGridPosition.getGridY() - 1 >= 0 ) {
            GridPosition positionToAttempt = new GridPosition(currentGridPosition.getGridX(), currentGridPosition.getGridY() - 1);
            if(!isPositionOccupied(positionToAttempt))
                moveList.add(positionToAttempt);
        }
    }

    public void moveRobotIntoPosition(Robot robot) {
        int gridPositionX = robot.gridPosition().getGridX();
        int gridPositionY = robot.gridPosition().getGridY();
        gameMap[gridPositionX][gridPositionY].setOccupant(robot);
    }

    public void moveRobotOutOfOldPosition(GridPosition oldPosition) {
        int gridPositionX = oldPosition.getGridX();
        int gridPositionY = oldPosition.getGridY();
        gameMap[gridPositionX][gridPositionY].unOccupy();
    }

    public boolean isCentralSquareOccupied() {
        return isPositionOccupied(CENTRE);
    }
}

