package nss;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by president on 1/23/15.
 */
public class Libs {

    /*******************************************************************************************************************
     * CHECK FILE IS IMAGE *********************************************************************************************
     * @param filePath  path to file ***********************************************************************************
     * @return 0 - file isn't image ************************************************************************************
     *         1 - file is image ***************************************************************************************
     *         2 - file is jpg image ***********************************************************************************
     ******************************************************************************************************************/
    public int fileIsImage(String filePath) {
        System.out.println("- CHECK IS FILE IS IMAGE");                                                  // TODO: DELETE
        String fileFormat = getFileType(filePath).toLowerCase();
        if ( (fileFormat.equals("nef")   || fileFormat.equals("cr2") || fileFormat.equals("psd")  ||
                fileFormat.equals("tif") || fileFormat.equals("png") || fileFormat.equals("gif")  ||
                fileFormat.equals("orf") || fileFormat.equals("arw") || fileFormat.equals("rw2")))
            return 1;
        else if (fileFormat.equals("jpg") || fileFormat.equals("jpeg"))
            return 2;
        else
            return 0;
    }

    /*******************************************************************************************************************
     * GET FILE TYPE ***************************************************************************************************
     * @param fullPathToImage path to file *****************************************************************************
     * @return file type ***********************************************************************************************
     ******************************************************************************************************************/
    public String getFileType(String fullPathToImage) {
        System.out.println("- GET FILE TYPE");                                                           // TODO: DELETE
        String[] arrPath = fullPathToImage.split("\\.");
        String type = arrPath[(arrPath.length-1)];
        System.out.println("File " + fullPathToImage + " have " + type + " type.");                      // TODO: DELETE
        return type;
    }

    /*******************************************************************************************************************
     * SHOW HOW MANY TIME OPERATION IS DOING ***************************************************************************
     * @param start when operation start *******************************************************************************
     * @param stop  when operation end *********************************************************************************
     ******************************************************************************************************************/
    public static void calculateTime(Date start, Date stop) {
        System.out.println("- SHOW TIMER");                                                              // TODO: DELETE
        DateFormat formatter = new SimpleDateFormat(Values.DATE_FORMAT);

        String startTime = formatter.format(start);
        String stopTime  = formatter.format(stop);

        System.out.println(Values.TIMER_LINE);
        System.out.println(Values.START_TIME + startTime + "\t\t\t  |");
        System.out.println(Values.END_TIME + stopTime  + "\t\t\t  |");

        long diff = stop.getTime() - start.getTime();
        long dSeconds = diff / 1000 % 60;
        long dMinutes = diff / (60 * 1000) % 60;
        long dHours = diff / (60 * 60 * 1000);

        String H = String.valueOf(dHours);
        String M = String.valueOf(dMinutes);
        String S = String.valueOf(dSeconds);

        if (dHours <= 9)   { H = "0" + H; }
        if (dMinutes <= 9) { M = "0" + M; }
        if (dSeconds <= 9) { S = "0" + S; }

        System.out.println(Values.PROCESSING_TIME + Values.TIMER_TIME_H + H + ":"
                + Values.TIMER_TIME_M + M + ":" + Values.TIMER_TIME_S + S + " |");
        System.out.println(Values.TIMER_LINE);
    }

    /*******************************************************************************************************************
     * CHECK SOURCE AND TARGET PATH ************************************************************************************
     * @param sPathToSourceFolder path to folder with photo ************************************************************
     * @param sPathToTargetFolder path to folder where user wont save result *******************************************
     * @return  true - all done ****************************************************************************************
     *          false - error/******************************************************************************************
     ******************************************************************************************************************/
    public static boolean checkSourceAndTargetPath(String sPathToSourceFolder, String sPathToTargetFolder) {
        System.out.println( "- START CHECKING DATA: \nfolder with photo - " + sPathToSourceFolder +
                                                      "\nfolder to save - " + sPathToTargetFolder);      // TODO: DELETE
        Date dStart = new Date();                                                                        // TODO: DELETE
        String sAlertMessages = "";
        // Check if path to source dir not empty
        if (sPathToSourceFolder.equals("")) {                               // path is empty
            sAlertMessages += Values.CHECK_NO_SOURCE_PATH_FOUND;
        } else {                                                            // path not empty
            // Check if path to folder is folder
            if (!checkPathToFolder(sPathToSourceFolder).equals(""))         // there is no folder
                sAlertMessages += checkPathToFolder(sPathToSourceFolder);
        }
        // Check if path to target dir not empty
        if (sPathToTargetFolder.equals("")) {                               // path is empty
            sAlertMessages += Values.CHECK_NO_TARGET_PATH_FOUND;
        } else {                                                            // path not empty
            // Check if path to folder is folder
            if (!checkPathToFolder(sPathToTargetFolder).equals("")) {
                sAlertMessages += checkPathToFolder(sPathToTargetFolder);   // there is no folder
            }
        }

        // Check if there any error message
        if (!sAlertMessages.equals("")){                                    // there is some error message
            // Show Alert Dialog
            showAlertBox(sAlertMessages);
            System.out.println("Check is fall:\n" + sAlertMessages);                                     // TODO: DELETE
            Date dStop = new Date();                                                                     // TODO: DELETE
            calculateTime(dStart, dStop);                                                                // TODO: DELETE
            return false;
        }

        System.out.println("Check is done");                                                             // TODO: DELETE
        Date dStop = new Date();                                                                         // TODO: DELETE
        calculateTime(dStart, dStop);                                                                    // TODO: DELETE
        return true;
    }

    /*******************************************************************************************************************
     * CHECK IS PATH TO FOLDER IS FOLDER *******************************************************************************
     * @param sPathToFolder  path to folder ****************************************************************************
     * @return true - this is folder ***********************************************************************************
     *         false - there is no folder ******************************************************************************
     ******************************************************************************************************************/
    public static String checkPathToFolder(String sPathToFolder) {
        System.out.println("- CHECK PATH TO FOLDER");                                                    // TODO: DELETE
        String message = "";
        File file = new File(sPathToFolder);
        // Check is file exist
        if(!file.exists())
            message += Values.CHECK_NO_DIR_FOUND + sPathToFolder + "\n";
        // Check is file is directory
        if(!file.isDirectory())
            message += Values.CHECK_NOT_A_DIR + sPathToFolder + "\n";

        System.out.println("Result of checking (empty is good): " + message);                            // TODO: DELETE
        return message;
    }

    /*******************************************************************************************************************
     * SHOW ALERT DIALOG WINDOW ****************************************************************************************
     * @param sAlertMessages message with error ************************************************************************
     ******************************************************************************************************************/
    public static void showAlertBox(String sAlertMessages) {                                          // TODO: CUSTOMIZE
        System.out.println("- SHOW ALERT WINDOW;");                                                      // TODO: DELETE
        final Stage dialogStage = new Stage();
        GridPane grd_pan = new GridPane();
        grd_pan.setAlignment(Pos.CENTER);
        grd_pan.setHgap(5);
        grd_pan.setVgap(5);
        Scene scene =new Scene(grd_pan);
        dialogStage.setScene(scene);
        dialogStage.setTitle(Values.ERROR_WINDOW_TITLE);
        dialogStage.initModality(Modality.WINDOW_MODAL);

        Label lab_alert= new Label(sAlertMessages);
        grd_pan.add(lab_alert, 0, 1);

        Button btn_ok = new Button(Values.BUTTON_CLOSE);
        btn_ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                System.out.println("---> USER PRESS CLOSE BUTTON <---");                                 // TODO: DELETE
                // TODO Auto-generated method stub
                dialogStage.hide();
            }
        });
        grd_pan.add(btn_ok, 0, 2);
        dialogStage.show();
    }

}
