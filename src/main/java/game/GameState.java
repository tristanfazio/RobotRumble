package game;

import javafx.application.Platform;
import models.GridPosition;
import models.Robot;
import ui.JFXArena;

import java.util.HashMap;

public class GameState {
    private boolean isGameFinished;
    private final GameMap gameMap;
    private final HashMap<String, Robot> robotRepo;
    JFXArena arena;
    final Object lock = new Object();

    public GameState(GameMap gameMap, JFXArena arena){
        this.isGameFinished = false;
        this.gameMap = gameMap;
        robotRepo = new HashMap<>();
        this.arena = arena;
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

    private void updateRobotRepo(Robot robot) {
        robotRepo.put(robot.getRobotId(),robot);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                arena.updateRobotInfo(robotRepo);
            }
        });
    }
}
