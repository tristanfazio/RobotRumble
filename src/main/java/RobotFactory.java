import models.GameMap;
import models.GridPosition;
import models.Robot;

import java.util.concurrent.SynchronousQueue;

public class RobotFactory implements Runnable{
    private SynchronousQueue<Robot> queue = new SynchronousQueue<>();
    private int idCounter = 1;
    GameMap gameMap;

    public RobotFactory(GameMap gameMap){
        this.gameMap = gameMap;
    }
    @Override
    public void run() {
        try {
            while(true)
            {
                //Produce Robots
                int robotId = idCounter;
                Robot robot = new Robot(robotId, gameMap);

                queue.put(robot);
                idCounter++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Robot getNextRobot() throws InterruptedException {
        return queue.take();
    }
}
