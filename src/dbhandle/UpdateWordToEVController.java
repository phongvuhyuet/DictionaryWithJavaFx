package dbhandle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Controller;
import sample.DatabaseConnection;

import java.util.Optional;

public class UpdateWordToEVController {
    @FXML
    Label lblEnglish;

    @FXML
    Label lblVietnamese;

    @FXML
    Label lblDescription;

    @FXML
    Label lblPronounce;

    @FXML
    TextField txtEnglish;

    @FXML
    TextField txtVietnamese;

    @FXML
    TextArea txtDescription;

    @FXML
    TextField txtPronounce;

    @FXML
    Button btnUpdate;

    public void getNewWord(ActionEvent event) {
        if (txtEnglish.getText().trim().isEmpty() || txtVietnamese.getText().trim().isEmpty()) {
            AlertController.showInfoAlert("Thông báo!", null,
                    "Bạn phải nhập cả từ tiếng anh và tiếng việt!");
        } else {
            String word, html, vietnamese;
            String description = "", pronounce = "";
            description = txtDescription.getText();
            pronounce = txtPronounce.getText();
            word = txtEnglish.getText();
            vietnamese = txtVietnamese.getText();
            html = wordToHtml(word, vietnamese, description, pronounce);
            if (DatabaseConnection.isContains("av", word)) {
                AlertController.showConfirmAlert("Xác nhận", "Bạn có chắc muốn cập nhật?", null);
                Alert alert = AlertController.getAlertConfirm();
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    DatabaseConnection.updateWordToDB("av", word, html, description, pronounce);
                }
            } else {
                AlertController.showInfoAlert("Thông báo!", "Từ này không có trong từ điển!", null);
            }
            Controller.trieEV.insert(word, html);
            Stage current = (Stage) txtEnglish.getScene().getWindow();
            current.close();
        }
    }

    public static String wordToHtml(String word, String meaning, String description, String pronounce) {
        String result = "<h1>" + word
                + "</h1><h3><i>/" + pronounce
                + "/</i></h3><h2> + " + meaning
                + "</h2><ul><li>" + description
                + " </li></ul>";
        return result;
    }
}
