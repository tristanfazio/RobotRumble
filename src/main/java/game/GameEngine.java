package game;

import models.GridPosition;
import models.Robot;
import ui.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameEngine {
    private Logger logger;
    private final RobotFactory robotFactory;
    private final GameState gameState;
    private final int spawnTimer;
    private ExecutorService executor = Executors.newCachedThreadPool();

    public GameEngine(GameState gameState, Logger logger, int spawnTimer) {
        this.logger = logger;
        this.robotFactory = new RobotFactory();
        this.spawnTimer = spawnTimer;
        this.gameState = gameState;
    }

    public void startGame() {
        executor.execute(robotFactory);
        executor.execute(this::spawnRobotAtNextValidSpawnPosition);
    }

    public void spawnRobotAtNextValidSpawnPosition() {
        try {
            while (!gameState.isGameFinished())
            {
                Thread.sleep(spawnTimer);
                if(gameState.isValidSpawnPositions()) {
                    GridPosition nextValidSpawn = gameState.getNextRandomSpawnPosition();
                    Robot robot = robotFactory.getNextRobot();
                    robot.setGridPosition(nextValidSpawn);
                    robot.attachGameState(gameState);
                    gameState.spawnRobot(robot);
                    executor.execute(robot);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
