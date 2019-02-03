package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage auto;

    @Override
    public void start(Stage primaryStage) throws Exception{
        auto=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../Resources/fxml_page/mainWin.fxml"));
        auto.setTitle("Информационная система гостиничного комплекса");
        auto.setScene(new Scene(root, 1350, 800));
        auto.setResizable(false);
        auto.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
