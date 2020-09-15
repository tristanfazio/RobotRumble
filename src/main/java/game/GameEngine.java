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

    public GameEngine(GameState gameState, Logger logger, int spawnTimer) {
        this.logger = logger;
        this.robotFactory = new RobotFactory();
        this.spawnTimer = spawnTimer;
        this.gameState = gameState;
        gameState.attachEndGameListener(() -> {
            endGame();
        });
    }

    public void startGame() {
        executor.execute(robotFactory);
        executor.execute(this::spawnRobotAtNextValidSpawnPosition);
        executor.execute(this::scoreOverTimeCounter);
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
            System.out.println("Spawning stopped");
        }
    }

    private void scoreOverTimeCounter() {
        try {
            while (!gameState.isGameFinished()) {
                Thread.sleep(1000);
                int score = gameState.getScore() + 10;
                gameState.setScore(score);
            }
        } catch (InterruptedException e) {
            System.out.println("Score Counter Stopped");
        }
    }

    private void endGame() {
        logger.log("\n====================");
        logger.log("\n\tGAME OVER");
        logger.log("\n\tFinal Score: " + String.valueOf(gameState.getScore()));
        logger.log("\n====================");
        //TODO: CLEANUP THREADS NICELY
        executor.shutdownNow();
    }
}
