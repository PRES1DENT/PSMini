package nss;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    Thread myRunnableThread;
    MyRunnable myRunnable;

    FXMLLoader fxmlLoader;
    Stage sPrimaryStage;

    // SOURCE FOLDER
    TextField tfSourceFolder;
    Button btSetSourceFolder;

    String sSourceFolderPath = "";

    // TARGET FOLDER
    TextField tfTargetFolder;
    Button btSetTargetFolder;

    String sTargetFolderPath = "";

    // SORTING METHOD
    ChoiceBox cbSortBy;
    int iSortNumber;

    // SAVE ORIGINAL
    CheckBox cbSaveOriginals;
    boolean bSaveOriginal;

    // SAVE DUPLICATES;
    CheckBox cbSaveDuplicates;
    boolean bSaveDuplicates;

    // START BUTTON
    Button btStart;

    // IMAGE
    ImageView ivHorizontalImage1;
    ImageView ivHorizontalImage2;

    ImageView ivVerticalImage1;
    ImageView ivVerticalImage2;

    Label lbDuplicateFound;
    Label lbNewFolderCreated;
    Label lbImageRenamed;
    Label lbAllImagesFound;
    Label lbImagesLeft;

    // PROGRESS INDICATOR
    ProgressIndicator piProgress;

    // Список значений для имён папок
    ObservableList<String> olSortBy =
            FXCollections.observableArrayList(
                    "датe",
                    "марке аппарата",
                    "размеру",
                    "формату"

            );

    @Override
    public void start(Stage primaryStage) throws Exception{

        fxmlLoader = new FXMLLoader(getClass().getResource("photo_sort_mini.fxml")); // форма приложения
        Scene scene = new Scene(fxmlLoader.load(),1045,341);

        sPrimaryStage = primaryStage;

        initialization();

        // when SOURCE FOLDER BUTTON pressed
        btSetSourceFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Select folder with images");

                if (!sSourceFolderPath.equals("") && !sSourceFolderPath.equals(null))   // if path to folder not null or empty
                    directoryChooser.setInitialDirectory(new File(sSourceFolderPath));  // open previous folder
                File selectedDirectory =
                        directoryChooser.showDialog(sPrimaryStage);

                if (selectedDirectory != null) {
                    sSourceFolderPath = selectedDirectory.getAbsolutePath();
                    tfSourceFolder.setText(sSourceFolderPath);
                }
            }
        });

        // when TARGET FOLDER BUTTON pressed
        btSetTargetFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Select folder where you wont save images");

                if (!sTargetFolderPath.equals("") && !sTargetFolderPath.equals(null))   // if path to folder not null or empty
                    directoryChooser.setInitialDirectory(new File(sTargetFolderPath));  // open previous folder
                File selectedDirectory =
                        directoryChooser.showDialog(sPrimaryStage);

                if (selectedDirectory != null) {
                    sTargetFolderPath = selectedDirectory.getAbsolutePath();
                    tfTargetFolder.setText(sTargetFolderPath);
                }
            }
        });


        // when START BUTTON pressed
        btStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (checkAllData()){            // if user set all data
                    sSourceFolderPath = tfSourceFolder.getText();
                    sTargetFolderPath = tfTargetFolder.getText();

                    iSortNumber = cbSortBy.getSelectionModel().getSelectedIndex();
                    bSaveOriginal = cbSaveOriginals.isSelected();
                    bSaveDuplicates = cbSaveDuplicates.isSelected();


                    myRunnable = new MyRunnable(sSourceFolderPath, sTargetFolderPath,
                            iSortNumber, bSaveOriginal, bSaveDuplicates,
                            piProgress,
                            ivHorizontalImage1, ivHorizontalImage2, ivVerticalImage1, ivVerticalImage2,
                            lbAllImagesFound, lbDuplicateFound, lbNewFolderCreated, lbImageRenamed, lbImagesLeft);
                    myRunnableThread = new Thread(myRunnable);
                    myRunnableThread.start();



                }
            }
        });





        primaryStage.setTitle("Photo Sort Mini");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }







    private boolean checkAllData() {

        String sAlertMessages = "";

        if (tfSourceFolder.getText().equals("")) {
            sAlertMessages += "NO SOURCE PATH FOUND. Please set path to your FOLDER with IMAGES\n";
        } else {
            if (!checkPathToFolder(tfSourceFolder.getText()).equals(""))
                sAlertMessages += checkPathToFolder(tfSourceFolder.getText());
        }

        if (tfTargetFolder.getText().equals("")) {
            sAlertMessages += "NO TARGET PATH FOUND. Please set path to your FOLDER where you wont save IMAGES\n";
        } else {
            if (!checkPathToFolder(tfTargetFolder.getText()).equals("")) {
                sAlertMessages += checkPathToFolder(tfSourceFolder.getText());
            }
        }

        if (!sAlertMessages.equals("")){
            showAlertBox(sAlertMessages);
            return true;

        }

        return true;
    }

    private void showAlertBox(String sAlertMessages) {
        final Stage dialogStage = new Stage();
        GridPane grd_pan = new GridPane();
        grd_pan.setAlignment(Pos.CENTER);
        grd_pan.setHgap(5);
        grd_pan.setVgap(5);
        Scene scene =new Scene(grd_pan);
        dialogStage.setScene(scene);
        dialogStage.setTitle("ERROR");
        dialogStage.initModality(Modality.WINDOW_MODAL);

        Label lab_alert= new Label(sAlertMessages);
        grd_pan.add(lab_alert, 0, 1);

        Button btn_ok = new Button("CLOSE");
        btn_ok.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                // TODO Auto-generated method stub
                dialogStage.hide();

            }
        });
        grd_pan.add(btn_ok, 0, 2);

        dialogStage.show();
    }

    private String checkPathToFolder(String sPathToFolder) {
        String message = "";
        File file = new File(sPathToFolder);

        if(!file.exists())
            message += "NO DIR FOUND with this PATH: " + sPathToFolder + "\n";

        if(!file.isDirectory())
            message += "THIS IS NOT A DIR: " + sPathToFolder + "\n";

        return message;
    }

    private void initialization() {
        // SOURCE FOLDER
        tfSourceFolder = (TextField) fxmlLoader.getNamespace().get("tfSourceDir");
        btSetSourceFolder = (Button) fxmlLoader.getNamespace().get("btSetSourceDir");

        // TARGET FOLDER
        tfTargetFolder = (TextField) fxmlLoader.getNamespace().get("tfTargetDir");
        btSetTargetFolder = (Button) fxmlLoader.getNamespace().get("btSetTargetDir");

        // SORTING METHOD
        cbSortBy = (ChoiceBox) fxmlLoader.getNamespace().get("cbSortType");
        cbSortBy.setItems(olSortBy);
        cbSortBy.getSelectionModel().select(0);
        // SAVE ORIGINAL
        cbSaveOriginals = (CheckBox) fxmlLoader.getNamespace().get("cbSaveOriginal");

        // SAVE DUPLICATES;
        cbSaveDuplicates = (CheckBox) fxmlLoader.getNamespace().get("cbSaveDuplicate");

        // START BUTTON
        btStart = (Button) fxmlLoader.getNamespace().get("btStartSorting");

        // IMAGE
        ivHorizontalImage1 = (ImageView) fxmlLoader.getNamespace().get("ivHorizontalImage1");
        ivHorizontalImage1.setFitWidth(160);
        ivHorizontalImage1.setFitHeight(120);

        ivHorizontalImage2 = (ImageView) fxmlLoader.getNamespace().get("ivHorizontalImage2");
        ivHorizontalImage2.setFitWidth(160);
        ivHorizontalImage2.setFitHeight(120);

        ivVerticalImage1 = (ImageView) fxmlLoader.getNamespace().get("ivVerticalImage1");
        ivVerticalImage1.setFitWidth(90);
        ivVerticalImage1.setFitHeight(120);

        ivVerticalImage2 = (ImageView) fxmlLoader.getNamespace().get("ivVerticalImage2");
        ivVerticalImage2.setFitWidth(90);
        ivVerticalImage2.setFitHeight(120);


        lbAllImagesFound = (Label) fxmlLoader.getNamespace().get("lbAllImageFound");
        lbDuplicateFound = (Label) fxmlLoader.getNamespace().get("lbDuplicateFound");
        lbNewFolderCreated = (Label) fxmlLoader.getNamespace().get("lbNewFolderCreated");
        lbImageRenamed = (Label) fxmlLoader.getNamespace().get("lbImageRenamed");
        lbImagesLeft = (Label) fxmlLoader.getNamespace().get("lbImageLeft");


        // PROGRESS INDICATOR
        piProgress = (ProgressIndicator) fxmlLoader.getNamespace().get("piProgress");

    }


    public static void main(String[] args) {
        launch(args);
    }
}
