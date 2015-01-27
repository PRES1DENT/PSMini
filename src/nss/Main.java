package nss;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;

public class Main extends Application {

    Thread myRunnableThread;
    MyRunnable myRunnable;

    FXMLLoader fxmlLoader;
    Stage sPrimaryStage;

    // List with values with sorting type
    ObservableList<String> olSortingList =
            FXCollections.observableArrayList(
                    Values.SORT_BY_DATE,
                    Values.SORT_BY_CAMERA,
                    Values.SORT_BY_SIZE,
                    Values.SORT_BY_TYPE
            );

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
    ImageView ivImage;

    Label lbAllImagesFound;
    Label lbImagesLeft;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("- STARTING APPLICATION -");                                                  // TODO: DELETE
        fxmlLoader = new FXMLLoader(getClass().getResource("photo_sort_mini.fxml"));
        Scene scene = new Scene((Parent) fxmlLoader.load(),Values.APP_WIDTH,Values.APP_HEIGHT);
        sPrimaryStage = primaryStage;

        // Start initialization
        initialization();

        // when SOURCE FOLDER BUTTON pressed
        btSetSourceFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("---> USER PRESSED 'SOURCE FOLDER' BUTTON <---");                      // TODO: DELETE
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle(Values.DIR_CHOOSER_SOURCE_FOLDER_TITLE);

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
                System.out.println("---> USER PRESSED 'TARGET FOLDER' BUTTON <---");                     // TODO: DELETE
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle(Values.DIR_CHOOSER_TARGET_FOLDER_TITLE);

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
                System.out.println("---> USER PRESSED 'START' BUTTON <---");                             // TODO: DELETE

                sSourceFolderPath = tfSourceFolder.getText();
                sTargetFolderPath = tfTargetFolder.getText();

                if (Libs.checkSourceAndTargetPath(sSourceFolderPath, sTargetFolderPath)){            // if user set all data
                    iSortNumber = cbSortBy.getSelectionModel().getSelectedIndex();
                    bSaveOriginal = cbSaveOriginals.isSelected();
                    bSaveDuplicates = cbSaveDuplicates.isSelected();

                    btStart.setText(Values.BUTTON_TEXT_SORTING);
                    btStart.setDisable(true);
                    btSetSourceFolder.setDisable(true);
                    btSetTargetFolder.setDisable(true);

                    myRunnable = new MyRunnable(sSourceFolderPath, sTargetFolderPath,
                            iSortNumber, bSaveOriginal, bSaveDuplicates,
                            ivImage,
                            lbAllImagesFound, lbImagesLeft,
                            btStart, btSetSourceFolder, btSetTargetFolder);
                    myRunnableThread = new Thread(myRunnable);
                    myRunnableThread.start();
                }
            }
        });

        // WHEN USER SHUTDOWN APPLICATION
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.println("USER CLOSE APPLICATION");                                            // TODO: DELETE
                Platform.exit();
                System.exit(0);
                // TODO: close application;
            }
        });

        primaryStage.setTitle(Values.TITLE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void initialization() {
        System.out.println("- INITIALIZATION");                                                          // TODO: DELETE
        // SOURCE FOLDER
        tfSourceFolder = (TextField) fxmlLoader.getNamespace().get("tfSourceDir");
        btSetSourceFolder = (Button) fxmlLoader.getNamespace().get("btSetSourceDir");

        // TARGET FOLDER
        tfTargetFolder = (TextField) fxmlLoader.getNamespace().get("tfTargetDir");
        btSetTargetFolder = (Button) fxmlLoader.getNamespace().get("btSetTargetDir");

        // SORTING METHOD
        cbSortBy = (ChoiceBox) fxmlLoader.getNamespace().get("cbSortType");
        cbSortBy.setItems(olSortingList);
        cbSortBy.getSelectionModel().select(0);
        // SAVE ORIGINAL
        cbSaveOriginals = (CheckBox) fxmlLoader.getNamespace().get("cbSaveOriginal");

        // SAVE DUPLICATES;
        cbSaveDuplicates = (CheckBox) fxmlLoader.getNamespace().get("cbSaveDuplicate");

        // START BUTTON
        btStart = (Button) fxmlLoader.getNamespace().get("btStartSorting");

        // IMAGE
        ivImage = (ImageView) fxmlLoader.getNamespace().get("ivImage");
        ivImage.setFitHeight(Values.IMAGE_HEIGHT);

        lbAllImagesFound = (Label) fxmlLoader.getNamespace().get("lbAllImageFound");
        lbImagesLeft = (Label) fxmlLoader.getNamespace().get("lbImageLeft");

    }


    public static void main(String[] args) {
        launch(args);
    }
}
