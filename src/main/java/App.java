import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application 
{
    public static void main(String[] args) 
    {
        launch();        
    }
    
    @Override
    public void start(Stage stage) 
    {
        stage.setTitle("Robot Rumble");
        int spawnTimer = 2000;
        int gridWidth = 9;
        int gridHeight = 9;
        JFXArena arena = new JFXArena(gridWidth,gridHeight);
        TextArea textArea = new TextArea();
        Logger logger = new Logger(textArea);
        ToolBar toolbar = new ToolBar();
        GameEngine gameEngine = new GameEngine(logger, spawnTimer, gridWidth, gridHeight);

        arena.addListener((x, y) ->
        {
            System.out.println("Arena click at (" + x + "," + y + ")");
        });

        Label label = new Label("Score: 999");
        Button startButton = new Button("Start Game");

        startButton.setOnAction((event) ->
        {
            logger.log("Starting Game");
            gameEngine.startGame();
        });

        toolbar.getItems().addAll(label, startButton);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, textArea);
        arena.setMinWidth(300.0);
        
        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);
        
        Scene scene = new Scene(contentPane, 800, 800);
        stage.setScene(scene);
        stage.show();
    }
}
