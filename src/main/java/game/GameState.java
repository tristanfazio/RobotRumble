package game;

import javafx.application.Platform;
import models.GridPosition;
import models.Robot;
import ui.JFXArena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.SynchronousQueue;

public class GameState {
    private boolean isGameFinished;
    private final GameMap gameMap;
    private final HashMap<String, Robot> robotRepo;
    JFXArena arena;
    final Object lock = new Object();
    private SynchronousQueue<Boolean> endGameNotificationQueue = new SynchronousQueue<>();
    private int gameScore;
    private EndGameListener endGameListener = null;

    public GameState(GameMap gameMap, JFXArena arena){
        this.isGameFinished = true;
        this.gameMap = gameMap;
        robotRepo = new HashMap<>();
        this.arena = arena;
        gameScore = 0;
    }
    public void attachEndGameListener(EndGameListener endGameListener) {
        this.endGameListener = endGameListener;
    }

    public void setGameStart() {
        isGameFinished = false;
    }

    public boolean isGameFinished() {
        synchronized (lock){
            return isGameFinished;
        }
    }

    public boolean isValidSpawnPositions() {
        synchronized (lock){
            return gameMap.isValidSpawnPosition();
        }
    }

    public GridPosition getNextRandomSpawnPosition() {
        synchronized (lock){
           return gameMap.getNextRandomValidSpawnPosition();
        }
    }

    public void spawnRobot(Robot robot) {
        synchronized (lock) {
            gameMap.moveRobotIntoPosition(robot);
            updateRobotRepo(robot);
        }
    }

    public void handleRobotMovementToNewPosition(Robot robot, GridPosition newGridPosition) {
        synchronized (lock) {
            if(robot.isAlive()) {
                //get old position out of robot
                GridPosition oldPosition = robot.gridPosition();
                //set new position to occupied
                robot.setGridPosition(newGridPosition);
                gameMap.moveRobotIntoPosition(robot);
                //update visuals with movement
                updateRobotRepo(robot);
                //unOccupy old position
                gameMap.moveRobotOutOfOldPosition(oldPosition);
                //checkLose
                checkLoseCondition();
            }
        }
    }

    private void checkLoseCondition() {
        synchronized (lock) {
            if(gameMap.isCentralSquareOccupied()) {
                isGameFinished = true;
                endGameListener.endGame();
            }
        }
    }

    public ArrayList<GridPosition> getValidMoveListFromPosition(GridPosition gridPosition) {
        synchronized (lock) {
            return gameMap.getValidMoveListFromPosition(gridPosition);
        }
    }

    private void updateRobotRepo(Robot robot) {
        robotRepo.put(robot.getRobotId(),robot);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                arena.updateRobotInfo(robotRepo);
            }
        });
    }

    public int getScore() {
        synchronized (lock) {
            return gameScore;
        }
    }

    public void setScore(int gameScore) {
        synchronized (lock) {
            this.gameScore = gameScore;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    arena.updateScoreLabel(gameScore);
                }
            });
        }
    }

    public boolean isRobotInGridPosition(GridPosition firingPosition) {
        synchronized (lock) {
            return gameMap.isPositionOccupied(firingPosition);
        }
    }

    public Robot getRobotFromPosition(GridPosition firingPosition) {
        synchronized (lock) {
            return gameMap.getRobotFromPosition(firingPosition);
        }
    }

    public void removeRobotFromRepo(String robotId) {
        synchronized (lock) {
            robotRepo.remove(robotId);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    arena.updateRobotInfo(robotRepo);
                }
            });
        }
    }
}
