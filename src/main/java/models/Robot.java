package models;

import game.GameMap;
import game.GameState;

import java.util.ArrayList;
import java.util.Random;

public class Robot implements Runnable{
    private final int MAX_MOVE_DELAY = 2000;
    private final int MIN_MOVE_DELAY = 500;

    private final String robotId;
    private final int delay;
    private GridPosition gridPosition;
    GameState gameState;
    private boolean isAlive;

    public Robot(String robotId) {
        this.robotId = robotId;
        this.delay = getRandomDelay();
        this.gridPosition = new GridPosition();
        isAlive = true;
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

    public boolean isAlive() {
        return isAlive;
    }

    public void attachGameState(GameState gameState) {
        this.gameState = gameState;
    }

    private void attemptMove() {
        ArrayList<GridPosition> validMoves = gameState.getValidMoveListFromPosition(gridPosition);
        if(validMoves.size()>0) {
            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(validMoves.size());
            GridPosition newGridPosition = validMoves.get(index);
            gameState.handleRobotMovementToNewPosition(this,newGridPosition);
        }
    }

    @Override
    public String toString() {
        return "Robot{robotId["+robotId+"], delay["+ delay+"], gridPosition:["+gridPosition.toString()+"]}";
    }

    @Override
    public void run() {
        try {
            while(isAlive() && !gameState.isGameFinished())
            {
                Thread.sleep(getDelay());
                attemptMove();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
