package nss;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.photoshop.PsdHeaderDirectory;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

class MyRunnable implements Runnable{

    Libs libs = new Libs();

    private final int IMAGE_YEAR      = 0;
    private final int IMAGE_MONTH     = 1;
    private final int IMAGE_FULL_DATA = 2;

    private final int IMAGE_CAMERA_BRAND = 3;
    private final int IMAGE_CAMERA_MODEL = 4;

    private final int IMAGE_TYPE = 5;
    private final int IMAGE_SIZE = 6;

    private final int DIR = 0;
    private final int IMAGE = 1;


    ArrayList<String> alImages = new ArrayList<String>();
    HashMap hmImageMetadata = new HashMap();

    Button btSetSourceFolder;
    Button btSetTargetFolder;
    Button btStart;
    public ArrayList<String> md5List = new ArrayList<>();
    public ArrayList<String> duplicateList = new ArrayList<>();

    int count;
    int size;

    int z = 5;
    Image image;
    ImageView ivImageView;

    Label lbAllImagesFound;
    Label lbLeftImage;

    String sSourceFolderPath;
    String sTargetFolderPath;
    int iSortNumber;
    boolean bSaveOriginal;
    boolean bSaveDuplicates;

    /*******************************************************************************************************************
     * TAKE PARAMETERS *************************************************************************************************
     * @param sSourceFolderPath path to folder with images *************************************************************
     * @param sTargetFolderPath path to folder where we wont save new photo ********************************************
     * @param iSortNumber       how sort image *************************************************************************
     * @param bSaveOriginal     save original **************************************************************************
     * @param bSaveDuplicates   save duplicate *************************************************************************
     * @param ivImageView       image **********************************************************************************
     * @param lbAllImagesFound  how many photo we found ****************************************************************
     * @param lbLeftImage       how mane image left ********************************************************************
     * @param btStart           button start sorting *******************************************************************
     * @param btSetSourceFolder button select source folder ************************************************************
     * @param btSetTargetFolder button select target folder ************************************************************
     ******************************************************************************************************************/
    public MyRunnable(
            String sSourceFolderPath, String sTargetFolderPath,
            int iSortNumber,
            boolean bSaveOriginal, boolean bSaveDuplicates,
            ImageView ivImageView,
            Label lbAllImagesFound, Label lbLeftImage,
            Button btStart, Button btSetSourceFolder, Button btSetTargetFolder) {
        System.out.println("---------- MyRunnable START ----------");                                    // TODO: DELETE

        System.out.println(
                "\nsSourceFolderPath - " + sSourceFolderPath +
                "\nSTaegetFolderPath - " + sTargetFolderPath +
                "\niSortNumber - " + iSortNumber +
                "\nbSaveOriginal - " + bSaveOriginal);                                                   // TODO: DELETE

        this.sSourceFolderPath = sSourceFolderPath;
        this.sTargetFolderPath = sTargetFolderPath;

        this.iSortNumber = iSortNumber;

        this.bSaveOriginal = bSaveOriginal;
        this.bSaveDuplicates = bSaveDuplicates;

        this.ivImageView = ivImageView;

        this.lbAllImagesFound = lbAllImagesFound;
        this.lbLeftImage = lbLeftImage;

        this.btSetSourceFolder = btSetSourceFolder;
        this.btSetTargetFolder = btSetTargetFolder;
        this.btStart = btStart;
    }

    /*******************************************************************************************************************
     * RUN *************************************************************************************************************
     ******************************************************************************************************************/
    @Override
    public void run() {
        System.out.println("---------- RUN STARTED ----------");                                         // TODO: DELETE
       Date dStart = new Date();
        // Finding all images
        findAllImages(sSourceFolderPath);
        // Sorting array
        Collections.sort(alImages);
        // Sorting images
        startSorting();
        Date dEnd = new Date();
        System.out.println("TIME AT ALL:");                                                              // TODO: DELETE
        Libs.calculateTime(dStart, dEnd);
    }


    /*******************************************************************************************************************
     * START SORTING ***************************************************************************************************
     ******************************************************************************************************************/
    private void startSorting()  {
        System.out.println("---------- START SORTING IMAGES ----------");                                // TODO: DELETE
        Date dStartSorting = new Date();
        String pathToFile;
        String newPathToDire;
        String newImageFile;
        for (int i = 0; i < size; i++){ // LOOK ALL PHOTO //////////////////////////////////////////////////////////////
            count = (size-1) - i;
            Date dStart = new Date();
            pathToFile = alImages.get(i);
            System.out.println(" +++ " + (i+1) + ". " + pathToFile + ":");                               // TODO: DELETE
            if (isFileIsDuplicate(pathToFile)) {     // if file is duplicate
                if (bSaveOriginal) {                      // if save original
                    System.out.println("File " + pathToFile + " is duplicate");                          // TODO: DELETE
                } else {                                  // if original file can be removed
                    System.out.println("File " + pathToFile + " was deleted");                           // TODO: DELETE
                    deleteFile(pathToFile);
                }
            } else {                                      // file don't have any duplicates
                File file = new File(pathToFile);
                // CHECK IF NEED TO CHANGE IMAGE////////////////////////////////////////////////////////////////////////
                if (count%z == 0 ) {         // SHOW NEW PHOTO
                    if (libs.getFileType(pathToFile).toLowerCase().equals("jpg") ||
                            libs.getFileType(pathToFile).toLowerCase().equals("jpeg")) {
                        System.out.println("THIS IMAGE TRY TO BE SHOW");                                 // TODO: DELETE
                        Date dStartShowImage = new Date();
                        try {
                            image = new Image(file.toURI().toURL().toString());
                            // SET NEW IMAGE
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("IMAGE HAS BEEN SHOWED");                         // TODO: DELETE
                                    ivImageView.setImage(image);
                                }
                            });
                            try {
                                System.out.println("TRY TO SLEEP THREAD");                               // TODO: DELETE
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                System.out.println("ERROR WITH TRYING TO SLEEP");                        // TODO: DELETE
                                ex.printStackTrace();
                            }
                        } catch (MalformedURLException e) {
                            System.out.println("ERROR WITH TRYING TO SET IMAGE");                        // TODO: DELETE
                            e.printStackTrace();
                        }

                        Date dStopShowImage = new Date();
                        System.out.println("WE NEED TIME TO SHOW IMAGE: ");                              // TODO: DELETE
                        Libs.calculateTime(dStartShowImage, dStopShowImage);
                    }
                }

                // Getting image information
                getImageMetaData(pathToFile);

               newPathToDire = sTargetFolderPath + File.separator + getNewPathToFile(DIR);

                File dir = new File(newPathToDire);
                if (!dir.exists())
                    dir.mkdirs();

                newImageFile = dir + File.separator + getNewPathToFile(IMAGE);

                if (bSaveOriginal){                       // if save original
                    File newImage = new File(newImageFile);
                    try {
                        Files.copy(file.toPath(), newImage.toPath());
                    } catch (IOException e) { }
                } else {                                  // if original file can be removed
                     file.renameTo(new File(newImageFile));
                }

            }
            // SHOW HOW MANY IMAGES LEFT
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lbLeftImage.setText(count + " шт.");
                }
            });
            System.out.println("WE NEED TIME TO SORT ONE (" + (pathToFile) + ") IMAGE");                 // TODO: DELETE
            Date dStop = new Date();
            Libs.calculateTime(dStart, dStop);
        }
        // ENABLING AND RENAME BUTTONS
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btSetSourceFolder.setDisable(false);
                btSetTargetFolder.setDisable(false);
                btStart.setDisable(false);
                btStart.setText("Начать сортировку!");
                lbLeftImage.setText(0 + " шт.");
            }
        });

        System.out.println("WE NEED TO SORT ALL (" + size + ") IMAGES");                                 // TODO: DELETE
        Date dEndSorting = new Date();
        Libs.calculateTime(dStartSorting, dEndSorting);
    }

    /*******************************************************************************************************************
     * GENERATING NEW NAME *********************************************************************************************
     * @param type 0 - name for folder *********************************************************************************
     *             1 - name for image **********************************************************************************
     * @return new name ************************************************************************************************
     ******************************************************************************************************************/
    private String getNewPathToFile(int type) {
        System.out.println("---------- GENERATING NEW NAME ----------");                                 // TODO: DELETE
        Date dStart = new Date();

        String currentDirName = null;
        String currentImageName = "Фото_" + getName(IMAGE_FULL_DATA) + "_" + getName(IMAGE_SIZE);

        switch (iSortNumber) {
            case 0:
                currentDirName = "Фотографии" + File.separator +
                        getName(IMAGE_YEAR) + File.separator +
                        getName(IMAGE_MONTH);
                break;
            case 1:
                currentDirName = "Фотографии" + File.separator +
                        getName(IMAGE_CAMERA_BRAND) + File.separator +
                        getName(IMAGE_CAMERA_MODEL);
                break;
            case 2:
                currentDirName = "Фотографии" + File.separator +
                        getName(IMAGE_SIZE);
                break;
            case 3:
                currentDirName = "Фотографии" + File.separator +
                        getName(IMAGE_TYPE);
                break;
        }

        currentDirName = currentDirName.replace("null" + File.separator, "");
        File dirWithImages = new File(sTargetFolderPath + File.separator + currentDirName);
        if (dirWithImages.exists()) {
            int folderElementsCount = dirWithImages.list().length + 1;
            currentImageName += "_№" + folderElementsCount + "." + getName(IMAGE_TYPE);
        } else
            currentImageName += "_№1." + getName(IMAGE_TYPE);

        currentImageName = currentImageName.replace("null_", "");
        currentImageName = currentImageName.replace("____", "_");
        currentImageName = currentImageName.replace("___","_");
        currentImageName = currentImageName.replace("__","_");

        Date dEnd = new Date();

        if (type == DIR) {
            System.out.println("New dir name - " + currentDirName);                                      // TODO: DELETE
            Libs.calculateTime(dStart,dEnd);
            return currentDirName;
        }else{
            System.out.println("New image name - " + currentImageName);                                  // TODO: DELETE
            Libs.calculateTime(dStart,dEnd);
            return currentImageName;
        }
    }

    /*******************************************************************************************************************
     * DELETING FILE ***************************************************************************************************
     * @param pathToFile path to file that we need to delete ***********************************************************
     ******************************************************************************************************************/
    private void deleteFile(String pathToFile) {
        System.out.println("---------- DELETING FILE (" + pathToFile + ") ----------"); // TODO: DELETE
        Date dStart = new Date();
        File file = new File(pathToFile);
        file.delete();
        Date dEnd = new Date();
        Libs.calculateTime(dStart,dEnd);
     }

    /*******************************************************************************************************************
     * CHECK IS FILE HAS NO DUPLICATES *********************************************************************************
     * @param pathToFile path to image *********************************************************************************
     * @return  true  - file is original *******************************************************************************
     *          false - file is duplicate ******************************************************************************
     ******************************************************************************************************************/
    private boolean isFileIsDuplicate(String pathToFile) {
        System.out.println("---------- CHECKING IS IMAGE HAS DUPLICATES ----------");                    // TODO: DELETE
        Date dStart = new Date();
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
        }
        else {
            for (int i=0; i < md5List.size(); i++) {
                if (md5List.get(i).equals(md5)) {
                    //duplicateList.add(pathToFile);
                    System.out.println(pathToFile + " - duplicate");                                     // TODO: DELETE
                    Date dEnd = new Date();
                    Libs.calculateTime(dStart, dEnd);
                    return true;
                }
            }
            md5List.add(md5);
            System.out.println("ADDING NEW MD5");                                                        // TODO: DELETE
        }
        System.out.println(pathToFile + " - original");                                                 // TODO: DELETE
        Date dEnd = new Date();
        Libs.calculateTime(dStart,dEnd);
        return false;
    }

    /*******************************************************************************************************************
     * GETTING NAME ****************************************************************************************************
     * @param i - IMAGE_YEAR      - year, when image was created *******************************************************
     *          - IMAGE_MONTH     - month, when image was created ******************************************************
     *          - IMAGE_FULL_DATA - (day-month-year), when photo was created *******************************************
     *          - CAMERA_BRAND    - camera brand ***********************************************************************
     *          - CAMERA_MODEL    - camera model ***********************************************************************
     *          - CAMERA_TYPE     - image type *************************************************************************
     *          - CAMERA_SIZE     - size of image (example (1600x900)) *************************************************
     * @return name ****************************************************************************************************
     ******************************************************************************************************************/
    private String getName(int i) {
        System.out.println("---------- GETTING NAME -----------");                                       // TODO: DELETE
        Date dStart = new Date();
        int imageYear;
        int imageMonth;
        int imageDay;
        String returnedParameter = null;

        switch (i) {
            case IMAGE_YEAR:
                if (hmImageMetadata.get("imageYear") == null) {
                    returnedParameter = "XXXX";
                } else {
                    imageYear = (int) hmImageMetadata.get("imageYear");
                    if (imageYear < 1975)
                        returnedParameter = "XXXX";
                    else
                        returnedParameter = String.valueOf(imageYear);
                }
                break;
            case IMAGE_MONTH:
                if (hmImageMetadata.get("imageYear") == null) {
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
                                    returnedParameter = "Январь";
                                    break;
                                case 2:
                                    returnedParameter = "Февраль";
                                    break;
                                case 3:
                                    returnedParameter = "Март";
                                    break;
                                case 4:
                                    returnedParameter = "Апрель";
                                    break;
                                case 5:
                                    returnedParameter = "Май";
                                    break;
                                case 6:
                                    returnedParameter = "Июнь";
                                    break;
                                case 7:
                                    returnedParameter = "Июль";
                                    break;
                                case 8:
                                    returnedParameter = "Август";
                                    break;
                                case 9:
                                    returnedParameter = "Сентябрь";
                                    break;
                                case 10:
                                    returnedParameter = "Октябрь";
                                    break;
                                case 11:
                                    returnedParameter = "Ноябрь";
                                    break;
                                case 12:
                                    returnedParameter = "Декабрь";
                                    break;
                                default:
                                    returnedParameter = "";
                                    break;
                            }
                        }
                    }
                }
                break;
            case IMAGE_FULL_DATA:
                imageYear = (int) hmImageMetadata.get("imageYear");
                imageMonth = (int) hmImageMetadata.get("imageMonth");
                imageDay = (int) hmImageMetadata.get("imageDay");

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

                    returnedParameter = "(" + month + "-" + day + "-" +imageYear + ")";
                }
                break;

            case IMAGE_CAMERA_BRAND:
                returnedParameter = (String) hmImageMetadata.get("imageCameraBrand");
                break;
            case IMAGE_CAMERA_MODEL:
                returnedParameter  = (String) hmImageMetadata.get("imageCameraModel");
                break;

            case IMAGE_TYPE:
                returnedParameter = (String) hmImageMetadata.get("imageType");
                break;

            case IMAGE_SIZE: {
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
        Date dEnd = new Date();
        System.out.println(returnedParameter);                                                           // TODO: DELETE
        Libs.calculateTime(dStart, dEnd);
        return returnedParameter;
    }

    /*******************************************************************************************************************
     * GET INFORMATION ABOUT IMAGE *************************************************************************************
     * @param pathToFile path to image *********************************************************************************
     ******************************************************************************************************************/
    private void getImageMetaData(String pathToFile) {
        System.out.println("---------- GETTING INFORMATION ABOUT IMAGE ----------");                     // TODO: DELETE
        Date dStart = new Date();
        int imageWidth  = 0;                // ширина изображения
        int imageHeight = 0;                // высота изображения

        int imageYear  = 0;                 // год снимка
        int imageMonth = 0;                 // месяц снимка
        int imageDay   = 0;                 // день снимка

        String imageCameraBrand = null;     // марка аппарата
        String imageCameraModel = null;     // модель аппарата

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

            // Если meta-данные имеются
            if ((exifIFDOD != null)) {
                System.out.println("exifIFDOD not NULL:");                                               // TODO: DELETE

                // Получаем марку и модель аппрата
                imageCameraBrand = exifIFDOD.getString(ExifIFD0Directory.TAG_MAKE);
                imageCameraModel = exifIFDOD.getString(ExifIFD0Directory.TAG_MODEL);
                System.out.println("CAMERA BRAND: " + imageCameraBrand +
                                  "\nCAMERA MODEL " + imageCameraModel);                                 // TODO: DELETE


                // Получаем дату снимка
                Date date = exifIFDOD.getDate(ExifIFD0Directory.TAG_DATETIME);
                System.out.println("TRYING TO GET DATE");                                                // TODO: DELETE

                if (date != null) {
                    System.out.println("WE GET DATE");                                                   // TODO: DELETE

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    imageYear = calendar.get(Calendar.YEAR);
                    imageMonth = calendar.get(Calendar.MONTH)+1;
                    imageDay = calendar.get(Calendar.DAY_OF_MONTH);

                    System.out.println("YEAR - " + imageYear +
                                        "\nMONTH - " + imageMonth +
                                        "\nDAY - " + imageDay);                                          // TODO: DELETE

                } else {
                    System.out.println("CAN'T GET DATE FROM exifIFDOD\n" +
                            "IF exifSIFDD not NULL");                                                    // TODO: DELETE
                    if (exifSIFDD != null) {
                        Date date1 = exifSIFDD.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
                        if (date1 != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date1);

                            imageYear = calendar.get(Calendar.YEAR);
                            imageMonth = calendar.get(Calendar.MONTH)+1;
                            imageDay = calendar.get(Calendar.DAY_OF_MONTH);

                            System.out.println("exifSIFDD DATE:" +
                                    "\nYEAR - " + imageYear +
                                    "\nMONTH - " + imageMonth +
                                    "\nDAY - " + imageDay);                                              // TODO: DELETE

                        }
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
            } else if (psdHeaderDirectory != null){
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
                        imageHeight= photo.getHeight();
                        System.out.println("WIDTH: " + imageWidth + "\nHEIGHT: "+ imageHeight);          // TODO: DELETE
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
                    imageHeight= photo.getHeight();
                    System.out.println("WIDTH: " + imageWidth + "\nHEIGHT: "+ imageHeight);              // TODO: DELETE
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
        imageType = libs.getFileType(pathToFile);
        System.out.println("IMAGE TYPE - " + imageType);                                                 // TODO: DELETE

        if (imageType == null)
            imageType = "";
        if (imageCameraModel == null)
            imageCameraModel = "";
        if (imageCameraBrand == null)
            imageCameraBrand = "";


        // Вносим полученные данные в список
        hmImageMetadata.put("imageWidth", imageWidth);
        hmImageMetadata.put("imageHeight", imageHeight);
        hmImageMetadata.put("imageYear", imageYear);
        hmImageMetadata.put("imageMonth", imageMonth);
        hmImageMetadata.put("imageDay", imageDay);
        hmImageMetadata.put("imageCameraBrand", imageCameraBrand);
        hmImageMetadata.put("imageCameraModel", imageCameraModel);
        hmImageMetadata.put("imageType", imageType);

        System.out.println("TO GET INFORMATION FROM (" + pathToFile + ") WE NEED");                      // TODO: DELETE
        Date dEnd = new Date();
        Libs.calculateTime(dStart, dEnd);
    }

    /*******************************************************************************************************************
     * Find all images and save path to one array list *****************************************************************
     * @param pathFrom  path to main folder ****************************************************************************
     ******************************************************************************************************************/
    private void findAllImages(String pathFrom) {
        System.out.println("- START FINDING IMAGES");                                                    // TODO: DELETE
        Date dStart = new Date();
        File mainDir = new File(pathFrom);
        String[] mainDirList = mainDir.list();
        String filePath;
        int iLength = mainDirList.length;
        System.out.println("In folder " + pathFrom + " - " + iLength + " files");                        // TODO :DELETE
        for (int i = 0; i < iLength; i++) {
            filePath = pathFrom + File.separator + mainDirList[i];
            File file = new File(filePath);

            if (file.isFile()) {                    // IF FILE
                if (libs.fileIsImage(filePath))          // IF IMAGE
                    alImages.add(filePath);         // save full path to image
            } else {                                // IF FOLDER
                findAllImages(filePath);
            }
        }

        size = alImages.size();
        System.out.println("Found " + size + " images.");                                                // TODO: DELETE


        if (size > 1000)
            z = 10;

        // SHOW HOW MANY IMAGES PROGRAM FIND ///////////////////////////////////////////////////////////////////////////
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbAllImagesFound.setText((alImages.size()-1) + " шт.");
            }
        });
        Date dEnd = new Date();
        Libs.calculateTime(dStart, dEnd);
    }
}