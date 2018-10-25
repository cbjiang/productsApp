package product.base;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by cbjiang on 2018/10/17.
 *
 * @author cbjiang
 */
public class BaseController {

    protected Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage=primaryStage;
    }

    protected Scene scene;

    public void setScene(Scene scene){
        this.scene=scene;
    }

}
