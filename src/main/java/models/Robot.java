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
    Random randomGenerator = new Random();

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
        try {
            ArrayList<GridPosition> validMoves = gameState.getValidMoveListFromPosition(gridPosition);
            if(validMoves.size()>0 && isAlive) {
                //get old position
                GridPosition oldPosition = gridPosition();
                //get new position
                int index = randomGenerator.nextInt(validMoves.size());
                GridPosition newGridPosition = validMoves.get(index);
                newGridPosition.setAnimationX(oldPosition.getGridX());
                newGridPosition.setAnimationY(oldPosition.getGridY());
                //occupy the new position
                setGridPosition(newGridPosition);
                gameState.moveRobotIntoPosition(this);
                //animate movement over 50milli increments, 10x50=500 milli total animation time
                for(int i = 0;i<10;i++){

                    if(oldPosition.getGridX() < newGridPosition.getGridX())newGridPosition.incrementAnimationX();
                    if(oldPosition.getGridX() > newGridPosition.getGridX())newGridPosition.decrementAnimationX();
                    if(oldPosition.getGridY() < newGridPosition.getGridY())newGridPosition.incrementAnimationY();
                    if(oldPosition.getGridY() > newGridPosition.getGridY())newGridPosition.decrementAnimationY();

                    setGridPosition(newGridPosition);
                    gameState.handleRobotMovementToNewPosition(this);

                    Thread.sleep(50);
                }
                //unoccupy old position
                gameState.moveRobotOutOfOldPosition(oldPosition);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            System.out.println("Robot:" + robotId + " shutdown");
        }
    }


    public void kill() {
        isAlive = false;
        gameState.removeRobotFromRepo(robotId);
    }
}
