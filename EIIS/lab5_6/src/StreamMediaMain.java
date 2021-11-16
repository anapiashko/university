import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class StreamMediaMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        WebView webView = new WebView();
        webView.getEngine().load("https://www.youtube.com/embed/PIzRk3t1gB4?autoplay=1");
        webView.setPrefSize(640,390);

        Stage stage = new Stage();
        stage.setScene(new Scene(webView));
        stage.show();
    }
}