package game;

import models.GridPosition;
import models.Robot;
import ui.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class GameEngine {
    private Logger logger;
    private final RobotFactory robotFactory;
    private final GameState gameState;
    private final int spawnTimer;
    private ExecutorService executor;
    private BlockingQueue<FireCommand> firingQueue;

    public GameEngine(GameState gameState, Logger logger, int spawnTimer) {
        this.logger = logger;
        this.robotFactory = new RobotFactory();
        this.spawnTimer = spawnTimer;
        this.gameState = gameState;
        firingQueue = new LinkedBlockingQueue<>();
        gameState.attachEndGameListener(() -> {
            endGame();
        });
    }

    public void startGame() {
        if(!gameState.isGameFinished()) return; //guard to stop multiple starts

        try {
            Thread.sleep(1000); // initial wait to give player time to react
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor = Executors.newCachedThreadPool();
        gameState.setGameStart();

        executor.execute(robotFactory);
        executor.execute(this::spawnRobotAtNextValidSpawnPosition);
        executor.execute(this::firingMechanism);
        executor.execute(this::scoreOverTimeCounter);
    }

    private void firingMechanism() {
        try {
            while (!gameState.isGameFinished()) {
                FireCommand fireCommand = firingQueue.take();
                GridPosition gridPosition = fireCommand.getGridPosition();
                Thread.sleep(1000);
                boolean hitOrMiss = gameState.isRobotInGridPosition(gridPosition);
                if(hitOrMiss)
                {
                    //hit
                    logger.log("Hit: "+gridPosition.toString());
                    Robot robot = gameState.getRobotFromPosition(gridPosition);
                    robot.kill();
                    int timeDelay = (int)(System.currentTimeMillis()- fireCommand.getFireTime());
                    int killScore = 10 + (100*timeDelay/robot.getDelay());
                    logger.log("Robot " + robot.getRobotId() + " destroyed... + " + killScore + " points.");
                    int newScore = gameState.getScore() + killScore;
                    gameState.setScore(newScore);
                }
                else {
                    //miss
                    logger.log("Miss: "+gridPosition.toString());
                }
            }
        } catch (InterruptedException e) {
        System.out.println("Firing Mechanism stopped");
    }
    }

    public void spawnRobotAtNextValidSpawnPosition() {
        try {
            while (!gameState.isGameFinished())
            {
                if(gameState.isValidSpawnPositions()) {
                    GridPosition nextValidSpawn = gameState.getNextRandomSpawnPosition();
                    Robot robot = robotFactory.getNextRobot();
                    robot.setGridPosition(nextValidSpawn);
                    robot.attachGameState(gameState);
                    gameState.spawnRobot(robot);
                    executor.execute(robot);
                    logger.log("Robot" + robot.getRobotId() + " Spawned: " + nextValidSpawn.toString());
                }
                Thread.sleep(spawnTimer);
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

    public void passFiringCoords(int x, int y, long fireTime) {
        if(!gameState.isGameFinished()){
            GridPosition gridPosition = new GridPosition(x,y);
            FireCommand fireCommand = new FireCommand(gridPosition,fireTime);
            logger.log("Player Fired: " + gridPosition.toString());
            try {
                firingQueue.put(fireCommand);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
