package ui;

import javafx.scene.canvas.*;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import models.Robot;

import java.io.InputStream;
import java.util.*;

/**
 * A JavaFX GUI element that displays a grid on which you can draw images, text and lines.
 */
public class JFXArena extends Pane
{
    // Represents the image to draw. You can modify this to introduce multiple images.
    private static final String ROBOT_IMAGE_FILE = "1554047213.png";
    private static final String CITY_IMAGE_FILE = "ville.png";
    private Image robotImage;
    private Image cityImage;
    HashMap<String,Robot> robotRepo;
    
    // The following values are arbitrary, and you may need to modify them according to the 
    // requirements of your application.
    private int gridWidth;
    private int gridHeight;
    private int centreX;
    private int centreY;

    private double gridSquareSize; // Auto-calculated
    private Canvas canvas; // Used to provide a 'drawing surface'.

    private List<ArenaListener> arenaListeners = null;
    private ScoreListener scoreListener = null;
    private final Object lock = new Object();

    /**
     * Creates a new arena object, loading the robot image and initialising a drawing surface.
     */
    public JFXArena(int gridWidth,int gridHeight)
    {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        centreX = gridWidth/2;
        centreY = gridHeight/2;

        robotRepo = new HashMap<>();
        // Here's how you get an Image object from an image file (which you provide in the 
        // 'resources/' directory).
        
        InputStream is = getClass().getClassLoader().getResourceAsStream(ROBOT_IMAGE_FILE);
        if(is == null)
        {
            throw new AssertionError("Cannot find image file " + ROBOT_IMAGE_FILE);
        }
        robotImage = new Image(is);
        is = getClass().getClassLoader().getResourceAsStream(CITY_IMAGE_FILE);
        if(is == null)
        {
            throw new AssertionError("Cannot find image file " + CITY_IMAGE_FILE);
        }
        cityImage = new Image(is);
        
        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        getChildren().add(canvas);
    }
    
    /**
     * Adds a callback for when the user clicks on a grid square within the arena. The callback 
     * (of type ui.ArenaListener) receives the grid (x,y) coordinates as parameters to the
     * 'squareClicked()' method.
     */
    public void addClickListener(ArenaListener newListener)
    {
        if(arenaListeners == null)
        {
            arenaListeners = new LinkedList<>();
            setOnMouseClicked(event ->
            {
                int gridX = (int)(event.getX() / gridSquareSize);
                int gridY = (int)(event.getY() / gridSquareSize);
                
                if(gridX < gridWidth && gridY < gridHeight)
                {
                    for(ArenaListener listener : arenaListeners)
                    {   
                        listener.squareClicked(gridX, gridY);
                    }
                }
            });
        }
        arenaListeners.add(newListener);
    }

    public void addScoreUpdateListener(ScoreListener newListener) {
        scoreListener = newListener;
    }

    public void updateScoreLabel(int score) {
        scoreListener.scoreUpdated(score);
    }
        
        
    /**
     * This method is called in order to redraw the screen, either because the user is manipulating 
     * the window, OR because you've called 'requestLayout()'.
     *
     * You will need to modify the last part of this method; specifically the sequence of calls to
     * the other 'draw...()' methods. You shouldn't need to modify anything else about it.
     */
    @Override
    public void layoutChildren()
    {
        super.layoutChildren(); 
        GraphicsContext gfx = canvas.getGraphicsContext2D();
        gfx.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
        
        // First, calculate how big each grid cell should be, in pixels. (We do need to do this
        // every time we repaint the arena, because the size can change.)
        gridSquareSize = Math.min(
            getWidth() / (double) gridWidth,
            getHeight() / (double) gridHeight);
            
        double arenaPixelWidth = gridWidth * gridSquareSize;
        double arenaPixelHeight = gridHeight * gridSquareSize;
            
            
        // Draw the arena grid lines. This may help for debugging purposes, and just generally
        // to see what's going on.
        gfx.setStroke(Color.DARKGREY);
        gfx.strokeRect(0.0, 0.0, arenaPixelWidth - 1.0, arenaPixelHeight - 1.0); // Outer edge

        for(int gridX = 1; gridX < gridWidth; gridX++) // Internal vertical grid lines
        {
            double x = (double) gridX * gridSquareSize;
            gfx.strokeLine(x, 0.0, x, arenaPixelHeight);
        }
        
        for(int gridY = 1; gridY < gridHeight; gridY++) // Internal horizontal grid lines
        {
            double y = (double) gridY * gridSquareSize;
            gfx.strokeLine(0.0, y, arenaPixelWidth, y);
        }

        // Invoke helper methods to draw things at the current location.
        // ** You will need to adapt this to the requirements of your application. **
        drawImage(gfx,cityImage,centreX,centreY);
        synchronized (lock) {
            for (Map.Entry<String, Robot> entry : robotRepo.entrySet()) {
                Robot robot = entry.getValue();
                String robotId = robot.getRobotId();
                double robotX = robot.gridPosition().getAnimationX();
                double robotY = robot.gridPosition().getAnimationY();
                drawImage(gfx,robotImage,robotX ,robotY);
                drawLabel(gfx,robotId,robotX,robotY);
            }
        }
    }
    
    
    /** 
     * Draw an image in a specific grid location. *Only* call this from within layoutChildren(). 
     *
     * Note that the grid location can be fractional, so that (for instance), you can draw an image 
     * at location (3.5,4), and it will appear on the boundary between grid cells (3,4) and (4,4).
     *     
     * You shouldn't need to modify this method.
     */
    private void drawImage(GraphicsContext gfx, Image image, double gridX, double gridY)
    {
        // Get the pixel coordinates representing the centre of where the image is to be drawn. 
        double x = (gridX + 0.5) * gridSquareSize;
        double y = (gridY + 0.5) * gridSquareSize;
        
        // We also need to know how "big" to make the image. The image file has a natural width 
        // and height, but that's not necessarily the size we want to draw it on the screen. We 
        // do, however, want to preserve its aspect ratio.
        double fullSizePixelWidth = image.getWidth();
        double fullSizePixelHeight = image.getHeight();
        
        double displayedPixelWidth, displayedPixelHeight;
        if(fullSizePixelWidth > fullSizePixelHeight)
        {
            // Here, the image is wider than it is high, so we'll display it such that it's as 
            // wide as a full grid cell, and the height will be set to preserve the aspect 
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        }
        else if(fullSizePixelWidth < fullSizePixelHeight)
        {
            // Otherwise, it's the other way around -- full height, and width is set to 
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }
        else {
            //it's a perfect square, make it the full size of a grid minus a tiny amount for border visibility
            displayedPixelHeight = gridSquareSize - image.getHeight()*0.005;
            displayedPixelWidth = gridSquareSize - image.getWidth()*0.005;
        }

        // Actually put the image on the screen.
        gfx.drawImage(image,
            x - displayedPixelWidth / 2.0,  // Top-left pixel coordinates.
            y - displayedPixelHeight / 2.0, 
            displayedPixelWidth,              // Size of displayed image.
            displayedPixelHeight);
    }
    
    
    /**
     * Displays a string of text underneath a specific grid location. *Only* call this from within 
     * layoutChildren(). 
     *     
     * You shouldn't need to modify this method.
     */
    private void drawLabel(GraphicsContext gfx, String label, double gridX, double gridY)
    {
        gfx.setTextAlign(TextAlignment.CENTER);
        gfx.setTextBaseline(VPos.TOP);
        gfx.setStroke(Color.BLUE);
        gfx.strokeText(label, (gridX + 0.5) * gridSquareSize, (gridY + 1.0) * gridSquareSize);
    }
    
    /** 
     * Draws a (slightly clipped) line between two grid coordinates.
     *     
     * You shouldn't need to modify this method.
     */
    private void drawLine(GraphicsContext gfx, double gridX1, double gridY1, 
                                               double gridX2, double gridY2)
    {
        gfx.setStroke(Color.RED);
        
        // Recalculate the starting coordinate to be one unit closer to the destination, so that it
        // doesn't overlap with any image appearing in the starting grid cell.
        final double radius = 0.5;
        double angle = Math.atan2(gridY2 - gridY1, gridX2 - gridX1);
        double clippedGridX1 = gridX1 + Math.cos(angle) * radius;
        double clippedGridY1 = gridY1 + Math.sin(angle) * radius;
        
        gfx.strokeLine((clippedGridX1 + 0.5) * gridSquareSize, 
                       (clippedGridY1 + 0.5) * gridSquareSize, 
                       (gridX2 + 0.5) * gridSquareSize, 
                       (gridY2 + 0.5) * gridSquareSize);
    }

    public void updateRobotInfo(HashMap<String,Robot> robotRepo) {
        synchronized (lock) {
            this.robotRepo = robotRepo;
            layoutChildren();
        }
    }
}
