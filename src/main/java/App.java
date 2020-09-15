import game.GameEngine;
import game.GameMap;
import game.GameState;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ui.JFXArena;
import ui.Logger;

public class App
{
    //wrapper class to fix JAVAFX gradle build issue within IDE
    public static void main(String[] args) 
    {
        UI.main(args);
    }

}
