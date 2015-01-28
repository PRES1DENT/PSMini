package nss;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.*;

/**
 * PSMini
 * @author Sergey Nadolskiy
 */

class Sort implements Runnable{

    Libs libs = new Libs();

    ArrayList<String> alFindImages = new ArrayList<>();                    // path to all search images

    Button btSetSourceFolder;
    Button btSetTargetFolder;
    Button btStart;

    int iReverseCounter;                                                   // reverse image count (how many images left)
    int iFoundImages;                                                      // found images
    int iIntensityImageDisplay = 5;                                        // intensity of the image display

    String sFullPathToFile;
    String sNewPathToDir;
    String sNewImageName;


    Image image;
    ImageView ivImageView;

    Label lbAllImagesFound;
    Label lbLeftImage;

    String sSourceFolderPath;
    String sTargetFolderPath;
    int iSortNumber;
    boolean bSaveOriginal;

    /*******************************************************************************************************************
     * TAKE PARAMETERS *************************************************************************************************
     * @param sSourceFolderPath path to folder with images *************************************************************
     * @param sTargetFolderPath path to folder where we wont save new photo ********************************************
     * @param iSortNumber       how sort image *************************************************************************
     * @param bSaveOriginal     save original **************************************************************************
     * @param ivImageView       image **********************************************************************************
     * @param lbAllImagesFound  how many photo we found ****************************************************************
     * @param lbLeftImage       how mane image left ********************************************************************
     * @param btStart           button start sorting *******************************************************************
     * @param btSetSourceFolder button select source folder ************************************************************
     * @param btSetTargetFolder button select target folder ************************************************************
     ******************************************************************************************************************/
    public Sort(
            String sSourceFolderPath, String sTargetFolderPath,
            int iSortNumber,
            boolean bSaveOriginal,
            ImageView ivImageView,
            Label lbAllImagesFound, Label lbLeftImage,
            Button btStart, Button btSetSourceFolder, Button btSetTargetFolder) {
        System.out.println("---------- MyRunnable START ----------");                                    // TODO: DELETE

        System.out.println(
                "\nsSourceFolderPath - " + sSourceFolderPath +
                "\nSTargetFolderPath - " + sTargetFolderPath +
                "\niSortNumber - " + iSortNumber +
                "\nbSaveOriginal - " + bSaveOriginal);                                                   // TODO: DELETE

        this.sSourceFolderPath = sSourceFolderPath;
        this.sTargetFolderPath = sTargetFolderPath;

        this.iSortNumber = iSortNumber;

        this.bSaveOriginal = bSaveOriginal;

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
        System.out.println("-------------------- FINDING IMAGES --------------------");                  // TODO: DELETE
        Date dStartFinding = new Date();
        // Finding all images
        findAllImages(sSourceFolderPath);
        Date dStopFinding = new Date();
        Libs.calculateTime(dStartFinding, dStopFinding);
        System.out.println("-------------------- FINDING DONE --------------------");                    // TODO: DELETE
        // Sorting array
        Collections.sort(alFindImages);
        iFoundImages = alFindImages.size();
        if (iFoundImages >= 1000)
            iIntensityImageDisplay = 10;
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
        System.out.println("-------------------- START SORTING IMAGES --------------------");            // TODO: DELETE
        Date dStartSorting = new Date();
        for (int i = 0; i < iFoundImages; i++){ // LOOK ALL PHOTO //////////////////////////////////////////////////////////////
            Date dStart = new Date();
            iReverseCounter = (iFoundImages -1) - i;
            sFullPathToFile = alFindImages.get(i);
            System.out.println("******* ******* " + (i+1) + ". " + sFullPathToFile + " ******* *******");                     // TODO: DELETE
            // check is image duplicate
            if (libs.isFileIsDuplicate(sFullPathToFile)) {     // image is duplicate
                // check what to do with original images
                if (!bSaveOriginal) {                     // original file can be deleted
                    System.out.println("Duplicate " + sFullPathToFile + " was deleted.");                     // TODO: DELETE
                    libs.deleteFile(sFullPathToFile);
                }
            } else {                                      // original image
                File file = new File(sFullPathToFile);
                // check if we need to change image
                if (iReverseCounter % iIntensityImageDisplay == 0 || i == 0)               // show new image
                    changeImage(file);

                libs.getImageMetaData(sFullPathToFile);        // getting image information

                sNewPathToDir =
                        sTargetFolderPath + File.separator +
                                libs.getNewPathToFile(Values.DIR, iSortNumber, sTargetFolderPath);  // new path to image

                File dir = new File(sNewPathToDir);
                // checking if dir exists
                if (!dir.exists())                          // there is no dir
                    dir.mkdirs();

                sNewImageName =
                        dir + File.separator +
                                libs.getNewPathToFile(Values.IMAGE, iSortNumber, sTargetFolderPath);   // new image name

                // check what to do with original images
                if (bSaveOriginal){                       // don't touch original images
                    // copy image to new folder with new name
                    File newImage = new File(sNewImageName);
                    try {
                        Files.copy(file.toPath(), newImage.toPath());
                    } catch (IOException e) { }
                } else {                                  // original images can be deleted
                    // remove image to new folder with new name
                    file.renameTo(new File(sNewImageName));
                }

            }
            // show how many images left
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lbLeftImage.setText(iReverseCounter + " шт.");
                }
            });
            System.out.println("");                 // TODO: DELETE
            Date dStop = new Date();
            Libs.calculateTime(dStart, dStop);
            System.out.println("******* ******* ******* ******* ");            // TODO: DELETE

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

        Date dEndSorting = new Date();
        Libs.calculateTime(dStartSorting, dEndSorting);
        System.out.println("-------------------- SORTING DONE --------------------");            // TODO: DELETE

    }

    /*******************************************************************************************************************
     * CHANGE IMAGE ****************************************************************************************************
     * @param file new image *******************************************************************************************
     ******************************************************************************************************************/
    private void changeImage(final File file) {
        // check if image type is JPG
        if (libs.fileIsImage(file.getPath()) == 2) {                                // this is JPG image
            System.out.println("+++ Trying to set new image +++");                                       // TODO: DELETE
            try {
                image = new Image(file.toURI().toURL().toString());
                // Setting new image
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        // check original size and set new size
                        if (((image.getWidth() > image.getHeight())))               // if width is bigger
                            ivImageView.setFitWidth(Values.IMAGE_WIDTH);
                        else
                            ivImageView.setFitHeight(Values.IMAGE_HEIGHT);          // if height is bigger
                        ivImageView.setImage(image);
                        System.out.println("Image '" + file.getAbsolutePath() + "' has been showed");    // TODO: DELETE
                    }
                });
                try {
                    System.out.println("Trying to sleep thread");                                        // TODO: DELETE
                    Thread.sleep(Values.THREAD_SLEEP_TIME);
                } catch (InterruptedException ex) {
                    System.out.println("!!! ERROR WITH TRYING TO SLEEP  (" + file.getAbsolutePath() + ") !!!");                            // TODO: DELETE
                    ex.printStackTrace();
                }
            } catch (MalformedURLException e) {
                System.out.println("!!! ERROR WITH TRYING TO SET IMAGE  (" + file.getAbsolutePath() + ") !!!");                        // TODO: DELETE
                e.printStackTrace();
            }
        } else {                                                                    // this is not JPG
            System.out.println("Image '" + file.getAbsolutePath() + "' have another type");              // TODO: DELETE
        }
    }

    /*******************************************************************************************************************
     * Find all images and save path to one array list *****************************************************************
     * @param pathFrom  path to main folder ****************************************************************************
     ******************************************************************************************************************/
    private void findAllImages(String pathFrom) {
        File mainDir = new File(pathFrom);
        String[] mainDirList = mainDir.list();
        String filePath;
        int iLength = mainDirList.length;

        for (int i = 0; i < iLength; i++) {
            filePath = pathFrom + File.separator + mainDirList[i];
            File file = new File(filePath);

            // check file is dir or file
            if (file.isFile()) {                         // file is file
                int type = libs.fileIsImage(filePath);
                // check if file is image
                if (type == 1 || type == 2)              // file is image
                    alFindImages.add(filePath);
            } else {                                     // file is folder
                findAllImages(filePath);
            }
        }
        // SHOW HOW MANY IMAGES PROGRAM FIND ///////////////////////////////////////////////////////////////////////////
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbAllImagesFound.setText((alFindImages.size()) + " шт.");
            }
        });
    }
}