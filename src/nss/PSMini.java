package nss;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nss.Helpers.Checker;
import nss.Helpers.Values;

import java.io.File;
/***********************************************************************************************************************
 * 10000000101111111110111111111 * Author: Sergei Nadolskiy       **************************************************** *
 * 10100000101000000010100000001 * @mail:  s.nadolskiy@gmail.com  **************************************************** *
 * 10010000101000000000100000000 * skype:  s.nadolskiy            **************************************************** *
 * 10001000101111111110111111111 ************************************************************************************* *
 * 10000100100000000010000000001 ************************************************************************************* *
 * 10000010101000000010100000001 * Project Name: PSMini   ************************************************************ *
 * 10000000101111111110111111111 **************************************************************** зроблено в Україні * *
 **********************************************************************************************************************/
public class PSMini extends Application {

    private FXMLLoader fxmlLoader;
    private Stage sPrimaryStage;
    private Sort sort;
    private Thread runnableThread;

    private Button btSetSourceFolder;                                    // кнопка: шлях до теці з зображеннями
    private Button btSetTargetFolder;                                    // кнопка: шлях до кінцевої теці
    private Button btStart;                                              // кнопка: розпочати сортування

    private ChoiceBox cbSortBy;                                          // тип сортування

    private ImageView ivImage;                                           // "полотно" для відтворення зображення

    private Label lbAllFoundImagesCount;                                 // напис-лічильник: знайдено зображень
    private Label lbImagesLeftCount;                                     // напис-лічильник: обробити зображень
    private Label lbTimeInWorkCount;                                     // напис-лічильник: час сортування
    private Label lbTimeToFinishCount;                                   // напис-лічильник: залишилось до кінця

    private StackPane spImageFrame;                                      // "рамка" зображення
    private String sPathToSourceFolder = Values.NO_PATH_TO_SOURCE_FOLDER;// шлях до теці з зображеннями
    private String sPathToTargetFolder = Values.NO_PATH_TO_TARGET_FOLDER;// шлях до теці для збереження зображень

    private TextField tfSourceFolder;                                    // поле для запису шляху до початкової теці
    private TextField tfTargetFolder;                                    // поля для запису шляху до кінцевої теці

    @Override
    public void start(Stage primaryStage) throws Exception{
        fxmlLoader = new FXMLLoader(getClass().getResource(Values.PATH_TO_MAIN_LAYOUT));
        Scene scene = new Scene((Parent) fxmlLoader.load(),Values.APP_WIDTH,Values.APP_HEIGHT);
        sPrimaryStage = primaryStage;

        System.out.println(Values.WELCOME_MESSAGE);

        initialization();                                               // ініціалізація

        // дії при натисканні кнопки зміни теці з зображеннями /////////////////////////////////////////////////////////
        btSetSourceFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle(                              // назва вікна
                        Values.DIR_CHOOSER_SOURCE_FOLDER_TITLE);
                // якщо шлях був вказаний раніше, то намагаємося відкрити його зараз
                if (!sPathToSourceFolder.equals(Values.NO_PATH_TO_SOURCE_FOLDER) && !sPathToSourceFolder.equals(null))
                    directoryChooser.setInitialDirectory(new File(sPathToSourceFolder));
                File selectedDirectory =
                        directoryChooser.showDialog(sPrimaryStage);
                // якщо користувач вказав вірний шлях - записуємо його
                if (selectedDirectory != null) {
                    sPathToSourceFolder = selectedDirectory.getAbsolutePath();
                    tfSourceFolder.setText(sPathToSourceFolder);        // записуємо новий шлях у поле
                }
            }
        });

        // дії при натисканні кнопки зміни кінцевої теці ///////////////////////////////////////////////////////////////
        btSetTargetFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle(Values.DIR_CHOOSER_TARGET_FOLDER_TITLE);
                // якщо шлях був вказаний раніше, то намагаємося відкрити його зараз
                if (!sPathToTargetFolder.equals(Values.NO_PATH_TO_TARGET_FOLDER) && !sPathToTargetFolder.equals(null))
                    directoryChooser.setInitialDirectory(new File(sPathToTargetFolder));
                File selectedDirectory =
                        directoryChooser.showDialog(sPrimaryStage);
                // якщо користувач вказав вірний шлях - записуємо його
                if (selectedDirectory != null) {
                    sPathToTargetFolder = selectedDirectory.getAbsolutePath();
                    tfTargetFolder.setText(sPathToTargetFolder);        // записуємо новий шлях у поле
                }
            }
        });

        // Дії при натисканні кнопки "СТАРТ" ///////////////////////////////////////////////////////////////////////////
        btStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sPathToSourceFolder = tfSourceFolder.getText();         // шлях до теці з зображеннями
                sPathToTargetFolder = tfTargetFolder.getText();         // шлях до теці для зберігання зображень

                String sSortType =                                      // тип сортування
                        cbSortBy.getSelectionModel().getSelectedItem().toString();
                switch (sSortType) {
                    case "дате":                                        // за датою
                        sSortType = "за датою";
                        break;
                    case "моделе аппрата":                              // за моделлю апарата
                        sSortType = "за моделлю апарату";
                        break;
                    case "размеру изображения":                         // за розміром зображення
                        sSortType = "за розміром зображення";
                        break;
                    case "формату":                                     // за форматом
                        sSortType = "за форматом";
                        break;
                    default:
                        sSortType = "вибачте, але не вдалося визначити тип сортування =(";
                        break;
                }
                System.out.println(
                        "Шлях до теці з зображеннями: '"           + sPathToSourceFolder + "'\n" +
                        "Шлях до теці для збереження зображень: '" + sPathToTargetFolder + "'\n" +
                        "Тип сортування: '" + sSortType + "'\n");
                // якщо користувач ввів вірно всі данні, то починаємо обробку */
                if (Checker.checkSourceAndTargetPath(sPathToSourceFolder, sPathToTargetFolder)){
                    // дезактивуємо на час сортування кнопки
                    btStart.setText(Values.BUTTON_TEXT_SORTING);
                    btStart.setDisable(true);
                    btSetSourceFolder.setDisable(true);
                    btSetTargetFolder.setDisable(true);
                    // передамо необхідні параметри
                    sort = new Sort(sPathToSourceFolder, sPathToTargetFolder,
                            cbSortBy.getSelectionModel().getSelectedIndex(),
                            ivImage, spImageFrame,
                            lbAllFoundImagesCount, lbImagesLeftCount,
                            btStart, btSetSourceFolder, btSetTargetFolder,
                            lbTimeInWorkCount, lbTimeToFinishCount);
                    runnableThread = new Thread(sort);
                    runnableThread.start();                             // почнемо сортування
                }
            }
        });

        // обробка закриття програми ///////////////////////////////////////////////////////////////////////////////////
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.setTitle(Values.TITLE);                            // назва вікна
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(Values.PATH_TO_ICON));    // іконка
        primaryStage.show();
    }

    /*******************************************************************************************************************
     * Ініціалізація ************************************************************************************************* *
     ******************************************************************************************************************/
    private void initialization() {

        tfSourceFolder = (TextField) fxmlLoader.getNamespace().get("tfSourceDir");
        btSetSourceFolder = (Button) fxmlLoader.getNamespace().get("btSetSourceDir");

        tfTargetFolder = (TextField) fxmlLoader.getNamespace().get("tfTargetDir");
        btSetTargetFolder = (Button) fxmlLoader.getNamespace().get("btSetTargetDir");

        cbSortBy = (ChoiceBox) fxmlLoader.getNamespace().get("cbSortType");
        cbSortBy.setItems(Values.olSortingList);                        // заносимо типи сортування
        cbSortBy.getSelectionModel().select(0);                         // обираємо перше за замовченням

        btStart = (Button) fxmlLoader.getNamespace().get("btStartSorting");

        ivImage = (ImageView) fxmlLoader.getNamespace().get("ivImage");
        ivImage.setFitHeight(Values.IMAGE_VIEW_HEIGHT);                 // вказуємо висоту

        spImageFrame = (StackPane) fxmlLoader.getNamespace().get("spImage");
        spImageFrame.setMaxHeight(Values.IMAGE_HEIGHT);                 // вказуємо висоту

        lbAllFoundImagesCount = (Label) fxmlLoader.getNamespace().get("lbAllImageFound");
        lbImagesLeftCount     = (Label) fxmlLoader.getNamespace().get("lbImageLeft");

        lbTimeInWorkCount   = (Label) fxmlLoader.getNamespace().get("lbTimeInWork");
        lbTimeToFinishCount = (Label) fxmlLoader.getNamespace().get("lbTimeToFinish");
    }

    public static void main(String[] args) {
        launch(args);
    }
}