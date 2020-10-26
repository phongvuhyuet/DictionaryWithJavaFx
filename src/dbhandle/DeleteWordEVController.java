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

public class DeleteWordEVController {

    @FXML
    private TextField txtDelete;

    @FXML
    private Button btnDelete;

    public void deleteWord(ActionEvent event) {
        if (txtDelete.getText().trim().isEmpty()) {
            AlertController.showInfoAlert("Thông báo!", null,
                    "Bạn chưa nhập từ!");
        } else {
            String word = txtDelete.getText();
            if (DatabaseConnection.isContains("av", word)) {
                AlertController.showConfirmAlert("Xác nhận", "Bạn có chắc muốn xóa từ này?", null);
                Alert alert = AlertController.getAlertConfirm();
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    DatabaseConnection.deleteWordDB("av", word);
                }
            } else {
                AlertController.showInfoAlert("Thông báo", "Từ bạn nhập không tồn tại!", null);
            }
            Controller.trieEV.delete(word);
            Stage current = (Stage) txtDelete.getScene().getWindow();
            current.close();
        }
    }
}
