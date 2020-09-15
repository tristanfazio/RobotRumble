package game;

import models.GridPosition;
import models.Robot;
import ui.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class GameEngine {
    private Logger logger;
    private final RobotFactory robotFactory;
    private final GameState gameState;
    private final int spawnTimer;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private SynchronousQueue<Boolean> endGameNotificationQueue = new SynchronousQueue<>();

    public GameEngine(GameState gameState, Logger logger, int spawnTimer) {
        this.logger = logger;
        this.robotFactory = new RobotFactory();
        this.spawnTimer = spawnTimer;
        this.gameState = gameState;
        gameState.attachQueue(endGameNotificationQueue);
    }

    public void startGame() {
        executor.execute(robotFactory);
        executor.execute(this::spawnRobotAtNextValidSpawnPosition);
        executor.execute(this::waitForEndGameNotification);
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

    private void waitForEndGameNotification() {
        try {
            if(endGameNotificationQueue.take()) {
                logger.log("GAME OVER");
                executor.shutdownNow();
                //TODO: CLEANUP THREADS NICELY
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
