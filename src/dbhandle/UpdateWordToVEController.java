package dbhandle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Controller;
import sample.DatabaseConnection;

import java.util.Optional;

public class UpdateWordToVEController {
    @FXML
    Label lblEnglish;

    @FXML
    Label lblVietnamese;

    @FXML
    Label lblDescription;

    @FXML
    TextField txtEnglish;

    @FXML
    TextField txtVietnamese;

    @FXML
    TextArea txtDescription;

    @FXML
    Button btnUpdate;

    public void updateNewWord(ActionEvent event) {
        if (txtEnglish.getText().trim().isEmpty() || txtVietnamese.getText().trim().isEmpty()) {
            AlertController.showInfoAlert("Thông báo!", null,
                    "Enter word in English and Vietnamese!");
        } else {
            String word, html;
            String description = "";
            description = txtDescription.getText();
            word = txtVietnamese.getText();
            String explain = txtEnglish.getText();
            html = AddWordToEVController.wordToHtml(word, explain, description, "");
            if (DatabaseConnection.isContains("va", word)) {
                AlertController.showConfirmAlert("Confirmation", "Update this word?", null);
                Alert alert = AlertController.getAlertConfirm();
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    DatabaseConnection.updateWordToDB("va", word, html, description, "");
                }
            } else {
                AlertController.showInfoAlert("Notification", "This word isn't in the dictionary!", null);
            }
            Controller.trieVE.insert(word, html);
            Stage current = (Stage) txtEnglish.getScene().getWindow();
            current.close();
        }
    }
}
