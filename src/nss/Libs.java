package nss;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.photoshop.PsdHeaderDirectory;
import com.sun.javafx.iio.jpeg.JPEGDescriptor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by president on 1/23/15.
 */
public class Libs {

    public HashMap hmImageMetadata = new HashMap();
    public ArrayList<String> md5List = new ArrayList<>();

    /**
     * ****************************************************************************************************************
     * CHECK FILE IS IMAGE *********************************************************************************************
     *
     * @param filePath path to file ***********************************************************************************
     * @return 0 - file isn't image ************************************************************************************
     * 1 - file is image ***************************************************************************************
     * 2 - file is jpg image ***********************************************************************************
     * ****************************************************************************************************************
     */
    public int fileIsImage(String filePath) {
        String fileFormat = getFileType(filePath).toLowerCase();
        if ((fileFormat.equals("nef") || fileFormat.equals("cr2") || fileFormat.equals("psd") ||
                fileFormat.equals("tif") || fileFormat.equals("png") || fileFormat.equals("gif") ||
                fileFormat.equals("orf") || fileFormat.equals("arw") || fileFormat.equals("rw2")))
            return 1;
        else if (fileFormat.equals("jpg") || fileFormat.equals("jpeg"))
            return 2;
        else
            return 0;
    }

    /**
     * ****************************************************************************************************************
     * GET FILE TYPE ***************************************************************************************************
     *
     * @param fullPathToImage path to file *****************************************************************************
     * @return file type ***********************************************************************************************
     * ****************************************************************************************************************
     */
    public String getFileType(String fullPathToImage) {
        String[] arrPath = fullPathToImage.split("\\.");
        String type = arrPath[(arrPath.length - 1)];
        return type;
    }

    /**
     * ****************************************************************************************************************
     * SHOW HOW MANY TIME OPERATION IS DOING ***************************************************************************
     *
     * @param start when operation start *******************************************************************************
     * @param stop  when operation end *********************************************************************************
     *              ****************************************************************************************************************
     */
    public static void calculateTime(Date start, Date stop) {
        DateFormat formatter = new SimpleDateFormat(Values.DATE_FORMAT);

        String startTime = formatter.format(start);
        String stopTime = formatter.format(stop);

        System.out.println(Values.TIMER_LINE);
        System.out.println(Values.START_TIME + startTime + "\t\t\t  |");
        System.out.println(Values.END_TIME + stopTime + "\t\t\t  |");

        long diff = stop.getTime() - start.getTime();
        long dSeconds = diff / 1000 % 60;
        long dMinutes = diff / (60 * 1000) % 60;
        long dHours = diff / (60 * 60 * 1000);

        String H = String.valueOf(dHours);
        String M = String.valueOf(dMinutes);
        String S = String.valueOf(dSeconds);

        if (dHours <= 9) {
            H = "0" + H;
        }
        if (dMinutes <= 9) {
            M = "0" + M;
        }
        if (dSeconds <= 9) {
            S = "0" + S;
        }

        System.out.println(Values.PROCESSING_TIME + Values.TIMER_TIME_H + H + ":"
                + Values.TIMER_TIME_M + M + ":" + Values.TIMER_TIME_S + S + " |");
        System.out.println(Values.TIMER_LINE);
    }

    /**
     * ****************************************************************************************************************
     * CHECK SOURCE AND TARGET PATH ************************************************************************************
     *
     * @param sPathToSourceFolder path to folder with photo ************************************************************
     * @param sPathToTargetFolder path to folder where user wont save result *******************************************
     * @return true - all done ****************************************************************************************
     * false - error/******************************************************************************************
     * ****************************************************************************************************************
     */
    public static boolean checkSourceAndTargetPath(String sPathToSourceFolder, String sPathToTargetFolder) {
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
        if (!sAlertMessages.equals("")) {                                    // there is some error message
            // Show Alert Dialog
            showAlertBox(sAlertMessages);
            return false;
        }
        return true;
    }

    /**
     * ****************************************************************************************************************
     * CHECK IS PATH TO FOLDER IS FOLDER *******************************************************************************
     *
     * @param sPathToFolder path to folder ****************************************************************************
     * @return true - this is folder ***********************************************************************************
     * false - there is no folder ******************************************************************************
     * ****************************************************************************************************************
     */
    public static String checkPathToFolder(String sPathToFolder) {
        String message = "";
        File file = new File(sPathToFolder);
        // Check is file exist
        if (!file.exists())
            message += Values.CHECK_NO_DIR_FOUND + sPathToFolder + "\n";
        // Check is file is directory
        if (!file.isDirectory())
            message += Values.CHECK_NOT_A_DIR + sPathToFolder + "\n";
        return message;
    }

    /**
     * ****************************************************************************************************************
     * SHOW ALERT DIALOG WINDOW ****************************************************************************************
     *
     * @param sAlertMessages message with error ************************************************************************
     *                       ****************************************************************************************************************
     */
    public static void showAlertBox(String sAlertMessages) {                                          // TODO: CUSTOMIZE
        final Stage dialogStage = new Stage();
        GridPane grd_pan = new GridPane();
        grd_pan.setAlignment(Pos.CENTER);
        grd_pan.setHgap(5);
        grd_pan.setVgap(5);
        Scene scene = new Scene(grd_pan);
        dialogStage.setScene(scene);
        dialogStage.setTitle(Values.ERROR_WINDOW_TITLE);
        dialogStage.initModality(Modality.WINDOW_MODAL);

        Label lab_alert = new Label(sAlertMessages);
        grd_pan.add(lab_alert, 0, 1);

        Button btn_ok = new Button(Values.BUTTON_CLOSE);
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

    /**
     * ****************************************************************************************************************
     * GET INFORMATION ABOUT IMAGE *************************************************************************************
     *
     * @param pathToFile path to image *********************************************************************************
     *                   ****************************************************************************************************************
     */
    public void getImageMetaData(String pathToFile) {
        System.out.println("---------- GETTING INFORMATION ABOUT IMAGE ----------");                     // TODO: DELETE
        int imageWidth = 0;                // ширина изображения
        int imageHeight = 0;                // высота изображения

        int imageYear = 0;                 // год снимка
        int imageMonth = 0;                 // месяц снимка
        int imageDay = 0;                 // день снимка

        int imageHour = 0;
        int imageMinutes = 0;
        int imageSeconds = 0;

        boolean imageRedacted = false;

        String imageCameraBrand = Values.NO_CAMERA_BRAND;     // марка аппарата
        String imageCameraModel = "";     // модель аппарата

        String imageType;                   // расширение снимка

        File image = new File(pathToFile);
        Metadata metadata = null;
        try {
            System.out.println("TRYING TO GET METADATA");                                                // TODO: DELETE
            metadata = ImageMetadataReader.readMetadata(image);
        } catch (ImageProcessingException e) {
            System.out.println("ERROR GETTING METADATA 1");                                              // TODO: DELETE
        } catch (IOException e) {
            System.out.println("ERROR GETTING METADATA 2");                                              // TODO: DELETE
        } catch (NoClassDefFoundError e) {
            System.out.println("ERROR GETTING METADATA 3");                                              // TODO: DELETE
        }
        if (metadata == null) {

        }

        // Если meta-данные удалось считать:
        if (metadata != null) {
            System.out.println("WE GET IMAGE METADATA");                                                 // TODO: DELETE
            ExifIFD0Directory exifIFDOD = metadata.getDirectory(ExifIFD0Directory.class);
            ExifSubIFDDirectory exifSIFDD = metadata.getDirectory(ExifSubIFDDirectory.class);

            PsdHeaderDirectory psdHeaderDirectory = metadata.getDirectory(PsdHeaderDirectory.class);

            System.out.println("CHECK exifIFDOD");                                                       // TODO: DELETE

            boolean exifSIFDDDATE = false;

            if (exifSIFDD != null) {
                Date date1;
                if (exifSIFDD.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL) != null) {
                    date1 = exifSIFDD.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                } else {
                    date1 = exifSIFDD.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
                }
                if (date1 != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date1);

                    imageYear = calendar.get(Calendar.YEAR);
                    imageMonth = calendar.get(Calendar.MONTH) + 1;
                    imageDay = calendar.get(Calendar.DAY_OF_MONTH);

                    imageHour = calendar.get(Calendar.HOUR);
                    imageMinutes = calendar.get(Calendar.MINUTE);
                    imageSeconds = calendar.get(Calendar.SECOND);

                    if (calendar.get(Calendar.AM_PM) == 1)
                        imageHour += 12;

                    exifSIFDDDATE = true;
                }
            }

            if ((exifIFDOD != null)) {
                System.out.println("exifIFDOD not NULL:");                                               // TODO: DELETE

                // Получаем марку и модель аппрата
                if (exifIFDOD.getString(ExifIFD0Directory.TAG_MAKE) != null) {
                    imageCameraBrand = exifIFDOD.getString(ExifIFD0Directory.TAG_MAKE);
                    imageCameraModel = exifIFDOD.getString(ExifIFD0Directory.TAG_MODEL);
                }

                if (exifSIFDD != null && imageYear < 1975) {
                    // Получаем дату снимка
                    Date date = exifIFDOD.getDate(ExifIFD0Directory.TAG_DATETIME);
                    System.out.println("TRYING TO GET DATE");                                                // TODO: DELETE

                    if (date != null) {
                        System.out.println("WE GET DATE");                                                   // TODO: DELETE
                        imageRedacted = true;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);

                        imageYear = calendar.get(Calendar.YEAR);
                        imageMonth = calendar.get(Calendar.MONTH) + 1;
                        imageDay = calendar.get(Calendar.DAY_OF_MONTH);

                        imageHour = calendar.get(Calendar.HOUR);
                        imageMinutes = calendar.get(Calendar.MINUTE);
                        imageSeconds = calendar.get(Calendar.SECOND);

                        if (calendar.get(Calendar.AM_PM) == 1)
                            imageHour += 12;
                    }
                }

                if (!exifSIFDDDATE) {
                    // Получаем дату снимка
                    Date date1 = exifIFDOD.getDate(ExifIFD0Directory.TAG_DATETIME);
                    System.out.println("TRYING TO GET DATE");                                                // TODO: DELETE

                    if (date1 != null) {
                        System.out.println("WE GET DATE");                                                   // TODO: DELETE
                        imageRedacted = true;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date1);

                        imageYear = calendar.get(Calendar.YEAR);
                        imageMonth = calendar.get(Calendar.MONTH) + 1;
                        imageDay = calendar.get(Calendar.DAY_OF_MONTH);

                        imageHour = calendar.get(Calendar.HOUR);
                        imageMinutes = calendar.get(Calendar.MINUTE);
                        imageSeconds = calendar.get(Calendar.SECOND);

                        if (calendar.get(Calendar.AM_PM) == 1)
                            imageHour += 12;
                    }
                }

                try {
                    System.out.println("TRYING GET IMAGE SIZE");                                         // TODO: DELETE

                    BufferedImage photo = ImageIO.read(new File(pathToFile));
                    if (photo != null) {
                        // Получаем ширину изображения
                        imageWidth = photo.getWidth();
                        // Получаем высоту изображения
                        imageHeight = photo.getHeight();
                        System.out.println("WIDTH: " + imageWidth +
                                "\nHEIGHT: " + imageHeight);                                 // TODO: DELETE

                    }
                } catch (IOException e) {
                    System.out.println("CAN'T GET SIZE");                                                // TODO: DELETE

                }

                // Если разрешения полученные из metadata равны 0
                if (imageWidth == 0 || imageHeight == 0) {
                    System.out.println("SIZE = 0");                                                      // TODO: DELETE
                    // Получаем разрешение изображения
                    try {
                        System.out.println("TRYING TO GET SIZE ANOTHER METHOD");                         // TODO: DELETE

                        imageWidth = Integer.parseInt(exifIFDOD.getString(256));
                        imageHeight = Integer.parseInt(exifIFDOD.getString(257));
                        System.out.println("WIDTH: " + imageWidth + "\nHEIGHT: " + imageHeight);         // TODO: DELETE

                    } catch (NumberFormatException nfe) {
                        System.out.println("CAN'T GET SIZE");                                            // TODO: DELETE
                    }
                }
            } else if (psdHeaderDirectory != null) {
                System.out.println("TRYING TO GET META FORM psdHeaderDirectory");                        // TODO: DELETE
                imageWidth = Integer.parseInt(psdHeaderDirectory.getString(PsdHeaderDirectory.TAG_IMAGE_WIDTH));
                imageHeight = Integer.parseInt(psdHeaderDirectory.getString(PsdHeaderDirectory.TAG_IMAGE_HEIGHT));
                System.out.println("WIDTH: " + imageWidth + "\nHEIGHT: " + imageHeight);                 // TODO: DELETE
            }
            // Если meta-данные считать не удалось:
            else {
                System.out.println("CAN'T READ ANY META-DATA");                                          // TODO: DELETE
                try {
                    System.out.println("TRY BUFFERED IMAGE");                                            // TODO: DELETE
                    BufferedImage photo = ImageIO.read(new File(pathToFile));
                    if (photo != null) {
                        // Получаем ширину изображения
                        imageWidth = photo.getWidth();
                        // Получаем высоту изображения
                        imageHeight = photo.getHeight();
                        System.out.println("WIDTH: " + imageWidth + "\nHEIGHT: " + imageHeight);          // TODO: DELETE
                    } else {
                        System.out.println("NO DATA");                                                   // TODO: DELETE
                    }
                } catch (IOException e) {
                    System.out.println("ERROR BUFFERED 1");                                              // TODO: DELETE

                } catch (NullPointerException npe) {
                    System.out.println("ERROR BUFFERED 2");                                              // TODO: DELETE

                }
            }
        }

        // Если meta-данные считать не удалось:
        else {
            System.out.println("NO META-DATA AT ALL");                                                   // TODO: DELETE
            try {
                System.out.println("TRY BUFFERED IMAGE");                                                // TODO: DELETE
                BufferedImage photo = ImageIO.read(new File(pathToFile));
                if (photo != null) {
                    // Получаем ширину изображения
                    imageWidth = photo.getWidth();
                    // Получаем высоту изображения
                    imageHeight = photo.getHeight();
                    System.out.println("WIDTH: " + imageWidth + "\nHEIGHT: " + imageHeight);              // TODO: DELETE
                } else {
                    System.out.println("NO DATA");                                                       // TODO: DELETE
                }
            } catch (IOException e) {
                System.out.println("ERROR BUFFERED 1");                                                  // TODO: DELETE

            } catch (NullPointerException npe) {
                System.out.println("ERROR BUFFERED 2");                                                  // TODO: DELETE

            }
        }

        // Получаем расширение изображения
        imageType = getFileType(pathToFile);
        System.out.println("IMAGE TYPE - " + imageType);                                                 // TODO: DELETE


        if (imageType == null)
            imageType = "";
        if (imageCameraModel == null)
            imageCameraModel = "";
        if (imageCameraBrand == null)
            imageCameraBrand = "";

        if (imageHour == 0 && imageMinutes == 0 && imageSeconds == 0) {
            imageYear = 0;
            imageMonth = 0;
            imageDay = 0;
        }


        // Вносим полученные данные в список
        hmImageMetadata.put("imageWidth", imageWidth);
        hmImageMetadata.put("imageHeight", imageHeight);
        hmImageMetadata.put("imageYear", imageYear);
        hmImageMetadata.put("imageMonth", imageMonth);
        hmImageMetadata.put("imageHour", imageHour);
        hmImageMetadata.put("imageMinutes", imageMinutes);
        hmImageMetadata.put("imageSeconds", imageSeconds);
        hmImageMetadata.put("imageDay", imageDay);
        hmImageMetadata.put("imageCameraBrand", imageCameraBrand);
        hmImageMetadata.put("imageCameraModel", imageCameraModel);
        hmImageMetadata.put("imageType", imageType);
        hmImageMetadata.put("imageRedacted", imageRedacted);
        System.out.println("TO GET INFORMATION FROM (" + pathToFile + ") WE NEED");                      // TODO: DELETE
    }

    /**
     * ****************************************************************************************************************
     * GETTING NAME ****************************************************************************************************
     *
     * @param iValue - IMAGE_YEAR      - year, when image was created *******************************************************
     *               - IMAGE_MONTH     - month, when image was created ******************************************************
     *               - IMAGE_FULL_DATA - (day-month-year), when photo was created *******************************************
     *               - CAMERA_BRAND    - camera brand ***********************************************************************
     *               - CAMERA_MODEL    - camera model ***********************************************************************
     *               - CAMERA_TYPE     - image type *************************************************************************
     *               - CAMERA_SIZE     - iFoundImages of image (example (1600x900)) *************************************************
     * @param type
     * @return name ****************************************************************************************************
     * ****************************************************************************************************************
     */

    public String getName(int iValue, int type) {
        System.out.println("---------- GETTING NAME -----------");                                       // TODO: DELETE
        int imageYear;
        int imageMonth;
        int imageDay;

        int imageHour;
        int imageMinutes;
        int imageSeconds;

        String returnedParameter = null;

        switch (iValue) {
            case Values.IMAGE_YEAR:
                if (hmImageMetadata.get("imageYear").equals("0")) {
                    returnedParameter = Values.NO_YEAR;
                } else {
                    imageYear = (int) hmImageMetadata.get("imageYear");
                    if (imageYear < 1975)
                        returnedParameter = Values.NO_YEAR;
                    else
                        returnedParameter = String.valueOf(imageYear);
                }
                break;
            case Values.IMAGE_MONTH:
                if (hmImageMetadata.get("imageYear").equals("0")) {
                    returnedParameter = "";
                } else {
                    imageYear = (int) hmImageMetadata.get("imageYear");
                    if (imageYear < 1975) {
                        returnedParameter = "";
                    } else {
                        if (hmImageMetadata.get("imageMonth") == null) {
                            returnedParameter = "";
                        } else {
                            imageMonth = (int) hmImageMetadata.get("imageMonth");
                            switch (imageMonth) {
                                case 1:
                                    returnedParameter = "(01)Январь";
                                    break;
                                case 2:
                                    returnedParameter = "(02)Февраль";
                                    break;
                                case 3:
                                    returnedParameter = "(03)Март";
                                    break;
                                case 4:
                                    returnedParameter = "(04)Апрель";
                                    break;
                                case 5:
                                    returnedParameter = "(05)Май";
                                    break;
                                case 6:
                                    returnedParameter = "(06)Июнь";
                                    break;
                                case 7:
                                    returnedParameter = "(07)Июль";
                                    break;
                                case 8:
                                    returnedParameter = "(08)Август";
                                    break;
                                case 9:
                                    returnedParameter = "(09)Сентябрь";
                                    break;
                                case 10:
                                    returnedParameter = "(10)Октябрь";
                                    break;
                                case 11:
                                    returnedParameter = "(11)Ноябрь";
                                    break;
                                case 12:
                                    returnedParameter = "(12)Декабрь";
                                    break;
                                default:
                                    returnedParameter = "";
                                    break;
                            }
                        }
                    }
                }
                break;
            case Values.IMAGE_FULL_DATA:
                imageYear = (int) hmImageMetadata.get("imageYear");
                imageMonth = (int) hmImageMetadata.get("imageMonth");
                imageDay = (int) hmImageMetadata.get("imageDay");
                imageHour = (int) hmImageMetadata.get("imageHour");
                imageMinutes = (int) hmImageMetadata.get("imageMinutes");
                imageSeconds = (int) hmImageMetadata.get("imageSeconds");

                if (imageYear < 1975) {
                    returnedParameter = "";
                } else {
                    String month = null;
                    switch (imageMonth) {
                        case 1:
                            month = "янв";
                            break;
                        case 2:
                            month = "фев";
                            break;
                        case 3:
                            month = "март";
                            break;
                        case 4:
                            month = "апр";
                            break;
                        case 5:
                            month = "май";
                            break;
                        case 6:
                            month = "июнь";
                            break;
                        case 7:
                            month = "июль";
                            break;
                        case 8:
                            month = "авг";
                            break;
                        case 9:
                            month = "сен";
                            break;
                        case 10:
                            month = "окт";
                            break;
                        case 11:
                            month = "ноя";
                            break;
                        case 12:
                            month = "дек";
                            break;
                        default:
                            break;
                    }

                    String day;
                    if (imageDay < 10)
                        day = "0" + imageDay;
                    else
                        day = String.valueOf(imageDay);

                    if (imageHour == 0 && imageMinutes == 0 && imageSeconds == 0) {
                        if (type == Values.FOR_DATE && !(boolean)hmImageMetadata.get("imageRedacted"))
                            returnedParameter = "(" + day + "-" + month + "-" + imageYear + ")";
                        else {
                            String sMonth;
                            if (imageMonth < 10)
                                sMonth = "0" + imageMonth;
                            else
                                sMonth = String.valueOf(imageMonth);

                            returnedParameter = "(" + imageYear + "-" + sMonth + "-" + day + ")";
                        }
                    } else {
                        String sHour;
                        String sMinutes;
                        String sSeconds;
                        if (imageHour < 10)
                            sHour = "0" + imageHour;
                        else
                            sHour = String.valueOf(imageHour);

                        if (imageMinutes < 10)
                            sMinutes = "0" + imageMinutes;
                        else
                            sMinutes = String.valueOf(imageMinutes);

                        if (imageSeconds < 10)
                            sSeconds = "0" + imageSeconds;
                        else
                            sSeconds = String.valueOf(imageSeconds);

                        if (type == Values.FOR_DATE && !(boolean)hmImageMetadata.get("imageRedacted"))
                            returnedParameter = "(" + day + "-" + month + "-" + imageYear + "--"
                                    + sHour + ":" + sMinutes + ":" + sSeconds + ")";
                        else {
                            String sMonth;
                            if (imageMonth < 10)
                                sMonth = "0" + imageMonth;
                            else
                                sMonth = String.valueOf(imageMonth);
                            returnedParameter = "(" + imageYear + "-" + sMonth + "-" + day + "--"
                                    + sHour + ":" + sMinutes + ":" + sSeconds + ")";
                        }



                    }
                }
                break;

            case Values.IMAGE_CAMERA_BRAND:
                    returnedParameter = (String) hmImageMetadata.get("imageCameraBrand");
                    if (!returnedParameter.equals(Values.NO_CAMERA_BRAND))
                    {
                        String[] allName = returnedParameter.split(" ");
                        returnedParameter = allName[0];
                    }
                if (returnedParameter.equals("Corelogic"))
                    returnedParameter = "Samsung";
                break;
            case Values.IMAGE_CAMERA_MODEL:
                if (hmImageMetadata.get("imageCameraModel") == null)
                    returnedParameter = Values.NO_CAMERA_MODEL;
                else
                    returnedParameter = (String) hmImageMetadata.get("imageCameraModel");
                break;
            case Values.IMAGE_TYPE:
                returnedParameter = (String) hmImageMetadata.get("imageType");
                break;
            case Values.IMAGE_SIZE: {
                int imageWidth = (int) hmImageMetadata.get("imageWidth");
                int imageHeight = (int) hmImageMetadata.get("imageHeight");

                if (imageWidth == 0 || imageHeight == 0) {
                    returnedParameter = "";
                } else {
                    returnedParameter = "(" + imageWidth + "x" + imageHeight + ")";
                }
                break;
            }
        }

        if (returnedParameter == null)
            returnedParameter = "";
        System.out.println(returnedParameter);                                                           // TODO: DELETE
        return returnedParameter;
    }

    /**
     * ****************************************************************************************************************
     * CHECK IS FILE HAS NO DUPLICATES *********************************************************************************
     *
     * @param pathToFile path to image *********************************************************************************
     * @return true  - file is original *******************************************************************************
     * false - file is duplicate ******************************************************************************
     * ****************************************************************************************************************
     */
    public boolean isFileIsDuplicate(String pathToFile) {
        MessageDigest md = null;
        FileInputStream fis;

        byte[] dataBytes = new byte[1024];

        int nread;

        try {
            md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(pathToFile);
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
        } catch (NoSuchAlgorithmException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        byte[] mdbytes = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        String md5 = sb.toString();

        if (md5List.size() == 0) {
            md5List.add(md5);
            return false;
        } else {
            for (int i = 0; i < md5List.size(); i++) {
                if (md5List.get(i).equals(md5)) {
                    //duplicateList.add(sFullPathToFile);
                    System.out.println(pathToFile + " - duplicate");                                     // TODO: DELETE
                    return true;
                }
            }
            md5List.add(md5);
            System.out.println("ADDING NEW MD5");                                                        // TODO: DELETE
        }
        System.out.println(pathToFile + " - original");                                                 // TODO: DELETE
        return false;
    }

    /**
     * ****************************************************************************************************************
     * DELETING FILE ***************************************************************************************************
     *
     * @param pathToFile path to file that we need to delete ***********************************************************
     *                   ****************************************************************************************************************
     */
    public void deleteFile(String pathToFile) {
        System.out.println("---------- DELETING FILE (" + pathToFile + ") ----------"); // TODO: DELETE
        File file = new File(pathToFile);
        file.delete();
    }

    /**
     * ****************************************************************************************************************
     * GENERATING NEW NAME *********************************************************************************************
     *
     * @param type 0 - name for folder *********************************************************************************
     *             1 - name for image **********************************************************************************
     * @return new name ************************************************************************************************
     * ****************************************************************************************************************
     */
    public String getNewPathToFile(int type, int iSortNumber, String sTargetFolderPath) {
        String currentDirName = Values.MAIN_DIR_NAME + File.separator;                       // root dir
        String currentImageName = Values.MAIN_IMAGE_NAME + "_";                              // main name

        switch (iSortNumber) {
            case Values.SORT_BY_DATE:
                if ((boolean)hmImageMetadata.get("imageRedacted")) {
                    currentDirName += Values.NO_YEAR;
                } else {
                    currentDirName +=
                            getName(Values.IMAGE_YEAR, 0) + File.separator +                        // year dir
                                    getName(Values.IMAGE_MONTH, 0);                                         // month dir
                }
                currentImageName +=
                        getName(Values.IMAGE_FULL_DATA, Values.FOR_DATE) + "_" +                      // full image date
                                getName(Values.IMAGE_SIZE, 0);                                  // image size
                break;
            case Values.SORT_BY_CAMERA:
                currentDirName +=
                        getName(Values.IMAGE_CAMERA_BRAND, 0) + File.separator +               // camera firm
                                getName(Values.IMAGE_CAMERA_MODEL, 0);                                 // camera model

                currentImageName +=
                        getName(Values.IMAGE_FULL_DATA, 0) + "_" +                     // full image date
                                getName(Values.IMAGE_SIZE, 0);                                 // image size
                break;
            case Values.SORT_BY_SIZE: // sort by size
                currentDirName +=
                        getName(Values.IMAGE_SIZE, 0);                                         // size (WxH)
                currentImageName +=
                        getName(Values.IMAGE_FULL_DATA, 0) + "_" +                             // full image date
                                getName(Values.IMAGE_SIZE, 0);                                         // image size
                break;
            case Values.SORT_BY_TYPE: // sort by format
                currentDirName +=
                        getName(Values.IMAGE_TYPE, 0);                                         // type
                currentImageName +=
                        getName(Values.IMAGE_FULL_DATA, 0) + "_" +                             // full image date
                                getName(Values.IMAGE_SIZE, 0);                                         // image size
                break;
        }

        currentDirName = currentDirName.replace("null" + File.separator, "");
        File dirForImage = new File(sTargetFolderPath + File.separator + currentDirName);

        if (dirForImage.exists()) {
            int folderElementsCount = dirForImage.list().length + 1;
            currentImageName += "_№" + folderElementsCount + "." + getName(Values.IMAGE_TYPE, 0);
        } else
            currentImageName += "_№1." + getName(Values.IMAGE_TYPE, 0);

        currentImageName = currentImageName.replace("null_", "");
        currentImageName = currentImageName.replace("____", "_");
        currentImageName = currentImageName.replace("___", "_");
        currentImageName = currentImageName.replace("__", "_");


        if (type == Values.DIR) {
            System.out.println("New dir name - " + currentDirName);                                      // TODO: DELETE
            return currentDirName;
        } else {
            System.out.println("New image name - " + currentImageName);                                  // TODO: DELETE
            return currentImageName;
        }
    }
}