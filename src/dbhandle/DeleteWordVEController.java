package dbhandle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Controller;
import sample.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DeleteWordVEController {

    @FXML
    private TextField txtDelete;

    @FXML
    private Button btnDelete;

    public void deleteWord(ActionEvent event) {
        if (txtDelete.getText().trim().isEmpty()) {
            AlertController.showInfoAlert("Warning!", null,
                    "You haven't entered the word!");
        } else {
            String word = txtDelete.getText();
            if (DatabaseConnection.isContains("va", word)) {
                AlertController.showConfirmAlert("Confirmation", "Delete this word?", null);
                Alert alert = AlertController.getAlertConfirm();
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    DatabaseConnection.deleteWordDB("va", word);
                }
            } else {
                AlertController.showInfoAlert("Notification", "Word you entered isn't exist!", null);
            }
            Controller.trieVE.delete(word);
            Stage current = (Stage) txtDelete.getScene().getWindow();
            current.close();
        }
    }
}
