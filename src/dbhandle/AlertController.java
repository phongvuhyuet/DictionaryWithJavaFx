package dbhandle;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertController {
    private static Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
    private static Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);

    public AlertController() {
    }

    public static Alert getAlertConfirm() {
        return alertConfirm;
    }

    public static void showInfoAlert(String title, String headerText, String contentText) {
        alertInfo.setTitle(title);
        alertInfo.setHeaderText(headerText);
        alertInfo.setContentText(contentText);
        alertInfo.showAndWait();
    }

    public static void showConfirmAlert(String title, String headerText, String contentText) {
        alertConfirm.setTitle(title);
        alertConfirm.setHeaderText(headerText);
        alertConfirm.setContentText(contentText);
    }
}
