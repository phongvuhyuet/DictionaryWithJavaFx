package sample;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCRHandleController {
    @FXML
    TextArea textArea;

    @FXML
    Button btTranslate;

    @FXML
    TextArea txtResult;

    public static boolean modeEV;

    public void initialize() {
        txtResult.setEditable(false);
    }


    public File getFileChooser() {
        Stage stage = new Stage();
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose an image");

        //Filter
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fc.getExtensionFilters().add(imageFilter);
        File file = fc.showOpenDialog(stage);
        return file;
    }

    public String getTextOCR(String language) {
        File file = this.getFileChooser();
        ITesseract tesseract = new Tesseract();
        tesseract.setLanguage(language);
        String text = "";
        if (file == null) {
            return null;
        }

        try {
            text = tesseract.doOCR(file);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        if (language.equals("eng")) modeEV = true;
        else modeEV = false;
        return text;
    }

    public void Translate() {
        Translate translater = new Translate();
        String text = textArea.getText();
        String source = "en";
        String target = "vi";
        txtResult.setWrapText(true);
        if (modeEV == false) {
            source = "vi";
            target = "en";

            txtResult.setText(translater.translate(text, source, target));
        }
        else {

            txtResult.setText(translater.translate(text, source, target));
        }
    }
}
