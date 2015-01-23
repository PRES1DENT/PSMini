package nss;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class MyRunnable implements Runnable{

    Libs lb = new Libs();

    ArrayList<String> alImages = new ArrayList<String>();

    BufferedImage bf = null;
    WritableImage wr = null;

    int h = 0;
    int v = 0;
    int currentPosition;
    float progress;
    int count;

    ImageView ivHorizontalImage1;
    ImageView ivHorizontalImage2;
    ImageView ivVerticalImage1;
    ImageView ivVerticalImage2;

    Label lbAllImagesFound;
    Label lbDuplicateFound;
    Label lbNewFolderCreated;
    Label lbImageRenamed;
    Label lbLeftImage;

    ProgressIndicator piProgress;
    String sSourceFolderPath;
    String sTargetFolderPath;
    int iSortNumber;
    boolean bSaveOriginal;
    boolean bSaveDuplicates;

    public MyRunnable(
            String sSourceFolderPath, String sTargetFolderPath, int iSortNumber, boolean bSaveOriginal, boolean bSaveDuplicates,
            ProgressIndicator piProgress,
            ImageView ivHorizontalImage1, ImageView ivHorizontalImage2, ImageView ivVerticalImage1, ImageView ivVerticalImage2,
            Label lbAllImagesFound, Label lbDuplicateFound, Label lbNewFolderCreated, Label lbImageRenamed, Label lbLeftImage) {

        this.sSourceFolderPath = sSourceFolderPath;
        this.sTargetFolderPath = sTargetFolderPath;

        this.iSortNumber = iSortNumber;
        this.bSaveOriginal = bSaveOriginal;
        this.bSaveDuplicates = bSaveDuplicates;

        this.piProgress = piProgress;
        this.ivHorizontalImage1 = ivHorizontalImage1;
        this.ivHorizontalImage2 = ivHorizontalImage2;
        this.ivVerticalImage1 = ivVerticalImage1;
        this.ivVerticalImage2 = ivVerticalImage2;
        this.lbAllImagesFound = lbAllImagesFound;
        this.lbDuplicateFound = lbDuplicateFound;
        this.lbNewFolderCreated = lbNewFolderCreated;
        this.lbImageRenamed = lbImageRenamed;
        this.lbLeftImage = lbLeftImage;

    }

    @Override
    public void run() {
        findAllImages(sSourceFolderPath);
        Collections.sort(alImages);
        startSorting();

    }

    private void startSorting() {

        int size = alImages.size();

        for (int i = 0; i < size; i++){

            count = (size-1) - i;
            progress = round(((i+1f)/size),2);
            currentPosition = i;

            if (count%14 == 0) {

                try {
                    bf = ImageIO.read(new File(alImages.get(currentPosition)));
                } catch (IOException ex) {
                    System.out.println("Image (" + alImages.get(currentPosition) + ") failed to load.");
                }


                if (bf != null) {
                    wr = new WritableImage(bf.getWidth(), bf.getHeight());
                    PixelWriter pw = wr.getPixelWriter();
                    for (int x = 0; x < bf.getWidth(); x++) {
                        for (int y = 0; y < bf.getHeight(); y++) {
                            pw.setArgb(x, y, bf.getRGB(x, y));
                        }
                    }
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {

                            if (bf.getWidth() > bf.getHeight()) {
                                if (h == 0) {
                                    ivHorizontalImage1.setImage(wr);
                                    h = 1;
                                } else {
                                    ivHorizontalImage2.setImage(wr);
                                    h = 0;
                                }
                            } else {
                                if (v == 0) {
                                    ivVerticalImage1.setImage(wr);
                                    v = 1;
                                } else {
                                    ivVerticalImage2.setImage(wr);
                                    v = 0;
                                }
                            }


                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(JavaFX_TimerTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }


            // SHOW HOW MANY IMAGES LEFT ///////////////////////////////////////////////////////////////////////////////
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lbLeftImage.setText(count + " шт.");
                    piProgress.setProgress(progress);

                }
            });

        }
    }

    /**
     * Find all images and save path to one array list
     * @param pathFrom  path to main folder
     */
    private void findAllImages(String pathFrom) {
        File mainDir = new File(pathFrom);
        String[] mainDirList = mainDir.list();
        String filePath;

        for (int i = 0; i < mainDirList.length; i++) {
            filePath = pathFrom + File.separator + mainDirList[i];
            File file = new File(filePath);

            if (file.isFile()) {                    // IF FILE
                if (lb.fileIsImage(filePath))          // IF IMAGE
                    alImages.add(filePath);         // save full path to image
            } else {                                // IF FOLDER
                findAllImages(filePath);
            }

        }

        // SHOW HOW MANY IMAGES PROGRAM FIND ///////////////////////////////////////////////////////////////////////////
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbAllImagesFound.setText((alImages.size()-1) + " шт.");
            }
        });

    }

    private static float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }

}