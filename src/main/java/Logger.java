import javafx.scene.control.TextArea;

public class Logger {
    private TextArea logger;
    public Logger(TextArea logger) {
        this.logger = logger;
    }

    public void log(String message) {
        logger.appendText(message + "\n");
    }
}
