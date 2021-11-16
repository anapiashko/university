import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class StreamMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        WebView webView = new WebView();
        webView.getEngine().load("https://www.youtube.com/watch?v=linlz7-Pnvw");
        webView.setPrefSize(640,390);

        Stage stage = new Stage();
        stage.setScene(new Scene(webView));
        stage.show();
    }
}