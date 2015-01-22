package nss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class Main extends Application {

    // SOURCE FOLDER
    TextField tfSourceFolder;
    Button btSourceFolder;

    // TARGET FOLDER
    TextField tfTargetFolder;
    Button btTargetFolder;

    // SORTING METHOD
    ChoiceBox cbSortBy;

    // SAVE ORIGINAL
    CheckBox cbSaveOriginals;

    // SAVE DUPLICATES;
    CheckBox cbSaveDuplicates;

    // START BUTTON
    Button btStart;

    // IMAGE
    ImageView ivImage;
    Label lbImageNewName;

    // PROGRESS INDICATOR
    ProgressIndicator piProgress;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("photo_sort_mini.fxml"));
        primaryStage.setTitle("Photo Sort Mini");
        primaryStage.setScene(new Scene(root, 1045, 341));                  // TODO: test on other device ang change to 1024
        primaryStage.setResizable(false);
        primaryStage.show();

        initialization(root);
    }

    private void initialization(Parent root) {
        // SOURCE FOLDER
        tfSourceFolder = (TextField) root.lookup("#tfSourceDir");
        btSourceFolder = (Button) root.lookup("#btSetSourceDir");

        // TARGET FOLDER
        tfTargetFolder = (TextField) root.lookup("#lbTargetDir");
        btTargetFolder = (Button) root.lookup("#btSetTargetDir");

        // SORTING METHOD
        cbSortBy = (ChoiceBox) root.lookup("#cbSortType");

        // SAVE ORIGINAL
        cbSaveOriginals = (CheckBox) root.lookup("#cbSaveOriginal");

        // SAVE DUPLICATES;
        cbSaveDuplicates = (CheckBox) root.lookup("#cbSaveDuplicate");

        // START BUTTON
        btStart = (Button) root.lookup("#btStartSorting");

        // IMAGE
        ivImage = (ImageView) root.lookup("#ivImage");
        lbImageNewName = (Label) root.lookup("#lbImageName");

        // PROGRESS INDICATOR
        piProgress = (ProgressIndicator) root.lookup("#piProgress");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
