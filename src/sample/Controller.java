package sample;

import dbhandle.AlertController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class Controller implements Initializable {
    @FXML
    Button btnBack;

    @FXML
    Button btnSpeaker;

    @FXML
    TextField txtSearch;

    @FXML
    ListView<String> lvWords;

    @FXML
    ComboBox<String> cbLanguage;

    @FXML
    WebView webView;

    public static Trie trieEV;
    public static Trie trieVE;
    private Trie currentTrie;
    private static Stack<String> stackSearched;

    Connection conn = DatabaseConnection.ConnectDB();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       initComponent();
    }

    // Initialize all component
    public void initComponent() {
        trieEV = DatabaseConnection.getTrie("av");
        trieVE = DatabaseConnection.getTrie("va");

        initListView(getTrieData(trieEV));
        currentTrie = trieEV;
        initButton();
        initComboBox();
        stackSearched = new Stack<>();
    }

    public void initListView(List<String> words) {
        ObservableList<String> listWords = FXCollections.observableArrayList();
        if (words != null) {
            listWords.addAll(words);
            lvWords.getItems().clear();
            lvWords.getItems().addAll(listWords);
        } else {
            lvWords.getItems().clear();
        }
    }

    public Trie getDisplayTrie() {
        if (cbLanguage.getSelectionModel().getSelectedIndex() == 0) {
            return trieEV;
        }
        return trieVE;
    }

    public List<String> getTrieData(Trie trie) {
        return trie.getAllWord(trie.getRoot(), new char[100], 0, "");
    }

    public void initButton() {
        Image imgSpeaker = new Image("icons/speaker.png");
        ImageView ivSpeaker = new ImageView(imgSpeaker);

        ivSpeaker.setPreserveRatio(true);

        btnSpeaker.setGraphic(ivSpeaker);

        btnBack.setDisable(true);
    }

    public void initComboBox() {
        String mode1 = "English - Vietnamese";
        String mode2 = "Vietnamese - English";
        List<String> list = new ArrayList<>();
        list.add(mode1);
        list.add(mode2);
        ObservableList<String> oList = FXCollections.observableList(list);
        cbLanguage.setItems(oList);
        cbLanguage.getSelectionModel().select(0);
    }

    public void selectLanguage(ActionEvent event) {
        if (cbLanguage.getSelectionModel().getSelectedIndex() == 0) {
            currentTrie = trieEV;
            btnSpeaker.setDisable(false);
        } else {
            currentTrie = trieVE;
            btnSpeaker.setDisable(true);
        }
        List<String> dictionary = getTrieData(currentTrie);
        webView.getEngine().loadContent("");
        initListView(dictionary);
    }

    public void displaySelected() {
        String wordSelected = lvWords.getSelectionModel().getSelectedItem();
        Trie trie = getDisplayTrie();
        String html = trie.getHtml(wordSelected);
        txtSearch.setText(wordSelected);
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(html);

        //Stack searched
        if (stackSearched.size() == 0 || wordSelected != stackSearched.lastElement()) {
            stackSearched.push(wordSelected);
        }
        if (stackSearched.size() > 1) {
            btnBack.setDisable(false);
        }
    }

    public void searchWord() {
        lvWords.setVisible(true);
        String prefix = txtSearch.getText().trim();
        Trie trie = getDisplayTrie();
        List<String> result = trie.startsWith(trie.getRoot(), prefix);
        initListView(result);
    }

    public static boolean empty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void showAlertInformation(String message) {
        int mode = cbLanguage.getSelectionModel().getSelectedIndex();
        if (mode == 0) {
            if (empty(message)) {
                AlertController.showInfoAlert("Thông Báo!", null,
                        "Vui lòng nhập từ cần tra!");
            }
            else {
                AlertController.showInfoAlert("Translate!", message.trim(),
                        "Meaning");
            }
        }
        else {
            if (empty(message)) {
                AlertController.showInfoAlert("Alert!", null,
                        "Please enter the word!");
            }
            else {
                AlertController.showInfoAlert("Translate!", message.trim(),
                        "Meaning");
            }
        }
    }

    private void showAlertConfirmation() {
        int mode = cbLanguage.getSelectionModel().getSelectedIndex();
        if (mode == 0) {
            AlertController.showConfirmAlert("Confirmation Dialog", txtSearch.getText().trim(),
                    "Từ này không có trong từ điển.\n" + "Bạn muốn?");
        }
        else {
            AlertController.showConfirmAlert("Confirmation Dialog", txtSearch.getText().trim(),
                    "This word isn't in dictionary.\n" + "Choose your option.");
        }
        Alert alert = AlertController.getAlertConfirm();
        ButtonType buttonTranslate = new ButtonType("Translate");
        ButtonType buttonAdd = new ButtonType("Add this word");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTranslate, buttonAdd, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTranslate){
            if (mode == 0) {
                getTextTranslateEV();
            } else {
                getTextTranslateVE();
            }
        }
        else if (result.get() == buttonAdd) {
            int index = cbLanguage.getSelectionModel().getSelectedIndex();
            if (index == 0) {
                addWordEV();
            } else {
                addWordVE();
            }
        }
    }

    public void OnEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            String prefix = txtSearch.getText().trim();
            List<String> matchesPrefix = currentTrie.startsWith(currentTrie.getRoot(), prefix);
            if (empty(prefix)) {
                showAlertInformation(prefix);
            } else if (matchesPrefix == null) {
                showAlertConfirmation();
            } else {
                lvWords.getSelectionModel().select(0);
                displaySelected();
            }
        }
        if (event.getCode().equals(KeyCode.DOWN) || event.getCode().equals(KeyCode.UP)) {
            lvWords.requestFocus();
        }
    }

    public void handleEnterLVWord(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            displaySelected();
        }
    }

    //handler mouse event for bnBack
    public void backButtonHandle(MouseEvent event) {
        if (stackSearched.size() > 1) {
            stackSearched.pop();
            String lastWord = stackSearched.lastElement();
            lvWords.getSelectionModel().select(lastWord);
            txtSearch.setText(lastWord);
            String html = currentTrie.getHtml(lastWord);
            webView.getEngine().loadContent(html);
        }
        if (stackSearched.size() == 1) {
            btnBack.setDisable(true);
        }
    }

    public void addWordEV() {
        try {
            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/dbhandle/AddWordToEV.fxml"));
            Scene addScene = new Scene(root);
            newStage.setScene(addScene);
            newStage.setTitle("Thêm từ mới");
            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initListView(getTrieData(trieEV));
    }

    public void addWordVE() {
        try {
            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/dbhandle/AddWordToVE.fxml"));
            Scene addScene = new Scene(root);
            newStage.setResizable(false);
            newStage.setScene(addScene);
            newStage.setTitle("Adding new word");
            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initListView(getTrieData(trieVE));
    }

    public void deleteWordEV() {
        try {
            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/dbhandle/DeleteWordEV.fxml"));
            Scene addScene = new Scene(root);
            newStage.setResizable(false);
            newStage.setScene(addScene);
            newStage.setTitle("Xóa từ!");
            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initListView(getTrieData(trieEV));
    }

    public void deleteWordVE() {
        try {
            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/dbhandle/DeleteWordVE.fxml"));
            Scene addScene = new Scene(root);
            newStage.setResizable(false);
            newStage.setScene(addScene);
            newStage.setTitle("Deleting word");
            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initListView(getTrieData(trieVE));
    }

    public void updateWordEV() {
        try {
            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/dbhandle/UpdateWordToEV.fxml"));
            Scene addScene = new Scene(root);
            newStage.setScene(addScene);
            newStage.setTitle("Cập nhật từ điển");
            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initListView(getTrieData(trieEV));
    }

    public void updateWordVE() {
        try {
            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/dbhandle/UpdateWordToVE.fxml"));
            Scene addScene = new Scene(root);
            newStage.setScene(addScene);
            newStage.setTitle("Update dictionary");
            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initListView(getTrieData(trieVE));
    }

    //Text to Speech
    public void speak(ActionEvent event) {
        String text = txtSearch.getText().trim();
        Speaker speaker = new Speaker();
        speaker.say(text);
    }

    //Text OCR
    public void getTextFromImage(String language) {
        OCRHandleController ocr = new OCRHandleController();
        String text = ocr.getTextOCR(language);
        if (text != null && !text.equals("")) {
            Stage stage = new Stage();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("OCRHandle.fxml"));
                Scene scene = new Scene(root);
                Node node = scene.lookup("#textArea");
                TextArea textArea = (TextArea) node;
                textArea.setWrapText(true);
                textArea.setText(text);

                stage.setTitle("Text Preview");
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getTextEnglish() {
        getTextFromImage("eng");
    }

    public void getTextVietnamese() {
        getTextFromImage("vie");
    }

    public void getTextTranslateEV() {

        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("OCRHandle.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Dịch từ tiếng anh sang tiếng việt");
            stage.setScene(scene);
            OCRHandleController.modeEV = true;
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getTextTranslateVE() {

        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("OCRHandle.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Translate from Vietnamese to English");

            stage.setScene(scene);
            OCRHandleController.modeEV = false;
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage stageAboutUs;

    public void aboutUs() {
        stageAboutUs = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AboutUs.fxml"));
            Scene scene = new Scene(root);
            stageAboutUs.setTitle("About Us!");
            stageAboutUs.setScene(scene);
            stageAboutUs.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
