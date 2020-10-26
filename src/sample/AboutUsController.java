package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class AboutUsController {

    @FXML
    void backbt(ActionEvent event) {
        Controller.stageAboutUs.close();
    }

}
