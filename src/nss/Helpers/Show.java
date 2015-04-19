package nss.Helpers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Date;

/***********************************************************************************************************************
 * 10000000101111111110111111111 * Author: Sergei Nadolskiy       **************************************************** *
 * 10100000101000000010100000001 * @mail:  s.nadolskiy@gmail.com  **************************************************** *
 * 10010000101000000000100000000 * skype:  s.nadolskiy            **************************************************** *
 * 10001000101111111110111111111 ************************************************************************************* *
 * 10000100100000000010000000001 ************************************************************************************* *
 * 10000010101000000010100000001 * Project Name: PSMini   ************************************************************ *
 * 10000000101111111110111111111 **************************************************************** зроблено в Україні * *
 **********************************************************************************************************************/
public class Show {
    /*******************************************************************************************************************
     * Показати вікно з повідомленнями про помилки та інше *********************************************************** *
     * *************************************************************************************************************** *
     * @param sAlertMessages повідомлення з помилками/зауваженнями *************************************************** *
     ******************************************************************************************************************/
    public static void showAlertBox(String sAlertMessages) {                        // TODO: CUSTOMIZE
        final Stage dialogStage = new Stage();
        GridPane grd_pan = new GridPane();
        grd_pan.setAlignment(Pos.CENTER);
        grd_pan.setHgap(5);
        grd_pan.setVgap(5);
        Scene scene = new Scene(grd_pan);
        dialogStage.setScene(scene);
        dialogStage.setTitle(Values.ERROR_WINDOW_TITLE);                            // напис на вікні
        dialogStage.initModality(Modality.WINDOW_MODAL);

        ImageView image = new ImageView();
        image.setImage(new Image("img/img_alert.png"));                               // зображення
        image.setFitHeight(50);
        image.setFitWidth(50);
        Label lab_alert = new Label(sAlertMessages);                                // текст повідомлення
        grd_pan.add(image, 0,0);
        grd_pan.add(lab_alert, 1, 0);

        Button btn_ok = new Button(Values.BUTTON_CLOSE);
        // натиснута кнопка "Відміна"
        btn_ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                dialogStage.hide();
            }
        });
        grd_pan.add(btn_ok, 0, 2);
        dialogStage.show();
    }

    /*******************************************************************************************************************
     * Відлік часу сортування **************************************************************************************** *
     * *************************************************************************************************************** *
     * @param startTime   початок сортування ************************************************************************* *
     * @param currentTime теперішній час     ************************************************************************* *
     * @return час сортування                ************************************************************************* *
     ******************************************************************************************************************/
    public static String showWorkTime(Date startTime, Date currentTime) {

        long diff = currentTime.getTime() - startTime.getTime();                    // різниця між часом
        long dSeconds = diff / 1000 % 60;                                           // секунди
        long dMinutes = diff / (60 * 1000) % 60;                                    // хвилини
        long dHours = diff / (60 * 60 * 1000);                                      // години

        String H = String.valueOf(dHours);
        String M = String.valueOf(dMinutes);
        String S = String.valueOf(dSeconds);

        // якщо година менше чи дорівнює 10
        if (dHours <= 9)
            H = "0" + H;                                                            // додаємо 0 перед годиною
        // якщо хвилина менше чи дорівнює 10
        if (dMinutes <= 9)
            M = "0" + M;                                                            // додаємо 0 перед хвилиною
        // якщо секунда менше чи дорівнює 10
        if (dSeconds <= 9)
            S = "0" + S;                                                            // додаємо 0 перед секундою

        return H + ":" + M + ":" + S;                                               // вертаємо час сортування
    }


    /*******************************************************************************************************************
     * Розрахунок часу до закінчення сортування ********************************************************************** *
     * *************************************************************************************************************** *
     * @param iImages кількість оброблених зображень        ********************************************************** *
     * @param iReverseCounter кількість залишених зображень ********************************************************** *
     * @param dStartSorting час початку сортування          ********************************************************** *
     * @param dInProgressTime час сортування                ********************************************************** *
     * @return час до кінця                                 ********************************************************** *
     ******************************************************************************************************************/
    public static String showTimeToFinish(int iImages, int iReverseCounter, Date dStartSorting, Date dInProgressTime) {

        long time = dInProgressTime.getTime() - dStartSorting.getTime();            // різниця між часом
        long diff = 1;
        // якщо різниця між часом більше 0
        if (time > 0) {
            diff = (time/iImages)* iReverseCounter;                                 // розраховуємо час до кінця
        }
        long dSeconds = diff / 1000 % 60;                                           // секунди
        long dMinutes = diff / (60 * 1000) % 60;                                    // хвилини
        long dHours = diff / (60 * 60 * 1000);                                      // години

        String H = String.valueOf(dHours);
        String M = String.valueOf(dMinutes);
        String S = String.valueOf(dSeconds);

        // якщо година менше чи дорівнює 10
        if (dHours <= 9)
            H = "0" + H;                                                            // додаємо 0 перед годиною
        // якщо хвилина менше чи дорівнює 10
        if (dMinutes <= 9)
            M = "0" + M;                                                            // додаємо 0 перед хвилиною
        // якщо секунда менше чи дорівнює 10
        if (dSeconds <= 9)
            S = "0" + S;                                                            // додаємо 0 перед секундою

        return H + ":" + M + ":" + S;                                               // вертаємо час до кінця
    }
}