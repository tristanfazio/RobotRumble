import models.GameMap;
import models.GridPosition;
import models.Robot;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameEngine {
    private Logger logger;
    private final RobotFactory robotFactory;
    private final int spawnTimer;
    private boolean isGameFinished;
    private final GameMap gameMap;
    private ExecutorService executor = Executors.newCachedThreadPool();

    public GameEngine(Logger logger, int spawnTimer, int gridWidth, int gridHeight) {
        this.logger = logger;
        this.gameMap = new GameMap(gridWidth,gridHeight);
        this.robotFactory = new RobotFactory(gameMap);
        this.spawnTimer = spawnTimer;
        isGameFinished = false;
    }

    public void startGame() {
        executor.execute(robotFactory);
        executor.execute(() -> {
            //main game loop
            while(!isGameFinished()){
                spawnRobotAtNextValidSpawnPosition();
            }
        });
    }

    private boolean isGameFinished() {
        return isGameFinished;
    }

    public void spawnRobotAtNextValidSpawnPosition() {
        try {
            Thread.sleep(spawnTimer);
            System.out.println(gameMap.isValidSpawnPosition());
            if(gameMap.isValidSpawnPosition()) {
                GridPosition nextValidSpawn = gameMap.getNextValidSpawnPosition();
                Robot robot = robotFactory.getNextRobot();
                robot.setGridPosition(nextValidSpawn);
                gameMap.occupyPosition(robot.gridPosition());
                System.out.println(robot.toString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
