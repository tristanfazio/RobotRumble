import game.GameEngine;
import game.GameState;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import game.GameMap;
import ui.JFXArena;
import ui.Logger;

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
        int SPAWNTIMER = 2000;
        int GRIDWIDTH = 9;
        int GRIDHEIGHT = 9;
        ToolBar toolbar = new ToolBar();
        JFXArena arena = new JFXArena(GRIDWIDTH,GRIDHEIGHT);
        TextArea textArea = new TextArea();
        Logger logger = new Logger(textArea);
        GameMap gameMap = new GameMap(GRIDWIDTH, GRIDHEIGHT);
        GameState gameState = new GameState(gameMap,arena);
        GameEngine gameEngine = new GameEngine(gameState,logger,SPAWNTIMER);

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
