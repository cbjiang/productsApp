package product;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import product.main.MainController;

/**
 *
 * @author cbjiang
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main/main.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, -1, -1);

        MainController mainController = fxmlLoader.getController();
        mainController.setPrimaryStage(primaryStage);
        mainController.setScene(scene);

        primaryStage.setTitle("库存管理");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
