package nss;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import nss.Helpers.*;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.*;

/***********************************************************************************************************************
 * 10000000101111111110111111111 * Author: Sergei Nadolskiy       **************************************************** *
 * 10100000101000000010100000001 * @mail:  s.nadolskiy@gmail.com  **************************************************** *
 * 10010000101000000000100000000 * skype:  s.nadolskiy            **************************************************** *
 * 10001000101111111110111111111 ************************************************************************************* *
 * 10000100100000000010000000001 ************************************************************************************* *
 * 10000010101000000010100000001 * Project Name: PSMini   ************************************************************ *
 * 10000000101111111110111111111 **************************************************************** зроблено в Україні * *
 **********************************************************************************************************************/
class Sort implements Runnable {
    private final Checker checker = new Checker();
    private final ImageInfo imageInfo = new ImageInfo();

    private final ArrayList<String> arrayWithPathToImages = new ArrayList<>();  // масив з шляхами до зображень
    private final ArrayList<String> imagesForShow = new ArrayList<>();          // масив з зображеннями для показу
    private final Button btSetSourceFolder;                                     // кнопка: теця з зображеннями
    private final Button btSetTargetFolder;                                     // кнопка: теця для збереження
    private final Button btStart;                                               // кнопка: початок сортування

    private Date dateStartSortingAllImage;                                      // початок сортування зображень
    private Date dateStartSortingCurrentImage;                                  // початок роботи з зображенням

    private Image image;                                                        // зображення
    private final StackPane imageFrame;                                         // "рамка" зображення
    private final ImageView canvas;                                             // "полотно" для відтворення зображення

    private int countAllImageFound;                                             // лічильник: всього зображень
    private int countReverseImagePosition;                                      // лічильник: зображень залишилось
    private int countFoundDuplicateImage = 0;                                   // лічильник: дублікатів знайдено
    private int countFoundOriginalImages = 0;                                   // лічильник: оригіналів знайдено
    private int countCurrentImagePosition = 0;                                  // лічильник: зображень оброблено

    private final int choiceBoxSortingPosition;                                 // тип сортування

    private final Label countAllImagesFound;                                    // напис-лічильник: всього зображень
    private final Label countLeftImage;                                         // напис-лічильник: залишилось обробити
    private final Label timerTimeInWork;                                        // напис-лічильник: час роботи
    private final Label timerTimeToFinish;                                      // напис-лічильник: залишилось до кінця

    private final String pathToSourceFolder;                                    // шлях до теці з зображеннями
    private final String pathToTargetFolder;                                    // шлях до теці зберігання результатів
    private String currentTime;                                                 // теперішній час

    private int lastTime = 0;                                                   // останній час зміни зображення

    /*******************************************************************************************************************
     * Отримуємо параметри ******************************************************************************************* *
     * *************************************************************************************************************** *
     * @param sSourceFolderPath шлях до теці з зображеннями             ********************************************** *
     * @param sTargetFolderPath шлях до теці для зберігання результатів ********************************************** *
     * @param iSortNumber       тип сортування                          ********************************************** *
     * @param ivImageView       полотно для зміни зображень             ********************************************** *
     * @param spImage           полотно                                 ********************************************** *
     * @param lbAllImagesFound  напис "Всього знайдено зображень"       ********************************************** *
     * @param lbLeftImage       напис "скільки зображень залишилось"    ********************************************** *
     * @param btStart           кнопка початку сортування               ********************************************** *
     * @param btSetSourceFolder кнопка теця з зображеннями              ********************************************** *
     * @param btSetTargetFolder кнопка теця для зберігання зображень    ********************************************** *
     * @param lbTimeInWork      напис час в роботі                      ********************************************** *
     * @param lbTimeToFinish    напис до закінчення залишилось          ********************************************** *
     ******************************************************************************************************************/
    public Sort(
            String sSourceFolderPath, String sTargetFolderPath, int iSortNumber,
            ImageView ivImageView, StackPane spImage, Label lbAllImagesFound, Label lbLeftImage, Button btStart,
            Button btSetSourceFolder, Button btSetTargetFolder, Label lbTimeInWork, Label lbTimeToFinish) {

        this.pathToSourceFolder = sSourceFolderPath;                            // шлях до теці з зображеннями
        this.pathToTargetFolder = sTargetFolderPath;                            // шлях до теці зберігання результатів

        this.choiceBoxSortingPosition = iSortNumber;                            // тип сортування

        this.canvas = ivImageView;                                              // "полотно" для відтворення зображення
        this.imageFrame = spImage;                                              // "рамка" зображення

        this.countAllImagesFound = lbAllImagesFound;                            // напис-лічильник: всього зображень
        this.countLeftImage = lbLeftImage;                                      // напис-лічильник: залишилось обробити

        this.btSetSourceFolder = btSetSourceFolder;                             // кнопка: теця з зображеннями
        this.btSetTargetFolder = btSetTargetFolder;                             // кнопка: теця для збереження
        this.btStart = btStart;                                                 // кнопка: початок сортування

        this.timerTimeInWork = lbTimeInWork;                                    // напис-лічильник: час роботи
        this.timerTimeToFinish = lbTimeToFinish;                                // напис-лічильник: залишилось до кінця
    }

    /*******************************************************************************************************************
     * Запуск сортування ********************************************************************************************* *
     ******************************************************************************************************************/
    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btStart.setText(Values.BUTTON_TEXT_FINDING);
            }
        });
        System.out.println("Початок сортування:\n");
        System.out.println("Пошук зображень...");
        findAllImages(pathToSourceFolder);                                      // шукаємо зображення
        System.out.println("Знайдено " + arrayWithPathToImages.size() + " зображень\n");
        Collections.sort(arrayWithPathToImages);                                // сортуємо знайдені зображення
        countAllImageFound = arrayWithPathToImages.size();                      // кількість знайдених зображень
        startSorting();                                                         // сортуємо зображення
    }

    /*******************************************************************************************************************
     * Сортування зображень ****************************************************************************************** *
     ******************************************************************************************************************/
    private void startSorting() {
        Random rnd = new Random(System.currentTimeMillis());                    // ініціалізуємо генератор
        // перехід до графічного потоку
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btStart.setText(Values.BUTTON_TEXT_SORT);                       // змінюємо назву кнопки
            }
        });
        dateStartSortingAllImage = new Date();                                  // дата початку сортування
        // обхід масиву зі знайденими зображеннями
        for (int i = arrayWithPathToImages.size(); i > 0; i--) {
            dateStartSortingCurrentImage = new Date();                          // дата початку обробки зображення
            String pathToImage = arrayWithPathToImages.get(i - 1);              // шлях до зображення
            countCurrentImagePosition = countAllImageFound - i;                 // лічильник: оброблено зображень
            countReverseImagePosition = arrayWithPathToImages.size();           // лічильник: залишилось зображень
            String currentImageName =
                    pathToImage.split("-___created_by_Sergei_Nadolskiy___-")[1];// шлях до зображення (чистий)
            File currentImage = new File(currentImageName);                     // зображення

            currentTime =                                                       // час в роботі
                    Show.showWorkTime(dateStartSortingAllImage, dateStartSortingCurrentImage);
            // перехід до графічного потоку
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    timerTimeInWork.setText(currentTime);                       // змінюємо час в роботі
                    timerTimeToFinish.setText(Show.showTimeToFinish(            // змінюємо час до кінця
                            countCurrentImagePosition, countReverseImagePosition,
                            dateStartSortingAllImage, dateStartSortingCurrentImage));
                }
            });

            String[] sec = currentTime.split(":");                              // показник секунд
            int second = Integer.parseInt(sec[2]);                              // секунди
            int imagesForShowSize = imagesForShow.size();                       // розмір масиву для показу

            // якщо оброблюється перше зображення, чи пройшло 5 секунд з показу останнього і в масиві є шляхи
            if ((imagesForShowSize != 0) && ((second % 5 == 0 && lastTime != second) || countAllImageFound == i)) {
                lastTime = second;                                              // записуємо секунди
                int number = rnd.nextInt(imagesForShowSize);                    // випадкове значення
                String pathToJPGImage = imagesForShow.get(number);              // шлях до зображення для показу
                imagesForShow.remove(number);                                   // видаляємо шлях з масиву
                imagesForShow.trimToSize();                                     // обрізаємо масив
                // якщо шлях не пустий
                if (!pathToJPGImage.equals(Values.EMPTY_VALUE)) {
                    System.out.println("* Змінюємо зображення на : '" + pathToJPGImage + "';");
                    changeImage(new File(pathToJPGImage));                      // змінюємо зображення
                }
            }
            System.out.print("[" + (countCurrentImagePosition + 1) + "/" + countAllImageFound + "] '" +
                    currentImageName + "' -");

            // якщо зображення - дублікат
            if (checker.checkIsFileADuplicate(currentImageName)) {
                System.out.print(" дублікат\n");
                countFoundDuplicateImage++;                                     // додаємо +1 до лічильника дублікатів
            // інакше (якщо зображення - оригінал)
            } else {
                countFoundOriginalImages++;                                     // додаємо +1 до лічильника оригіналів
                imageInfo.getImageMetaData(currentImageName);                   // отримуємо інформацію з зображення

                String newPathToDir = pathToTargetFolder +                      // генеруємо нових шлях
                        File.separator +
                        imageInfo.getNewPathToFile(
                                Values.DIR,
                                choiceBoxSortingPosition,
                                pathToTargetFolder);

                File dirForImage = new File(newPathToDir);                      // нова теця для зображення

                // якщо такої теці немає
                if (!dirForImage.exists())
                    dirForImage.mkdirs();                                       // створюємо нову директорію

                String newImageName = dirForImage +                             // генеруємо нове ім'я зображення
                        File.separator +
                        imageInfo.getNewPathToFile(
                                Values.IMAGE,
                                choiceBoxSortingPosition,
                                pathToTargetFolder);

                File newImage = new File(newImageName);                         // зображення з новим ім'ям

                // намагаємося скопіювати зображення на нове місце з новим ім'ям
                try {
                    Files.copy(currentImage.toPath(), newImage.toPath());       // копіювання зображення
                    System.out.print("-> '" + newImageName + "'\n");
                // у разі виникнення помилки
                } catch (IOException ignored) {}                                // нічого не робимо
            }

            // перехід до графічного потоку
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    countAllImagesFound.setText(                                // змінюємо напис знайдених зображень
                            countAllImageFound + " " + Values.SHORT_THING + " ( " +
                                    countFoundOriginalImages + " " + Values.SHORT_ORIGINAL + " + " +
                                    countFoundDuplicateImage + " " + Values.SHORT_DUPLICATE + ")");
                    countLeftImage.setText(                                     // змінюємо напис залишилось обробити
                            countReverseImagePosition + " " + Values.SHORT_THING);
                }
            });
            arrayWithPathToImages.remove(i - 1);                                // видаляємо шлях до зображення з масиву
            arrayWithPathToImages.trimToSize();                                 // обрізаємо масив
        }
        changeImage(new File(Values.PATH_TO_FINAL_IMAGE));                      // змінюємо зображення на фінальне
        System.out.println("\nПрограма закінчила сортування. Дякую за очікування =)");

        // перехід до графічного потоку
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btSetSourceFolder.setDisable(false);                            // активуємо кнопки
                btSetTargetFolder.setDisable(false);                            // активуємо кнопки
                btStart.setDisable(false);                                      // активуємо кнопки
                btStart.setText(Values.START_SORTING);                          // змінюємо напис кнопки
                countLeftImage.setText(0 + " " + Values.SHORT_THING);           // змінюємо напис лічильника
            }
        });
    }

    /*******************************************************************************************************************
     * Зміна зображення ********************************************************************************************** *
     * *************************************************************************************************************** *
     * @param file зображення **************************************************************************************** *
     ******************************************************************************************************************/
    private void changeImage(final File file) {
        // якщо зображення не порожнє
        if (file != null) {
            // намагаємося його відтворити
            try {
                image = new Image(file.toURI().toURL().toString());             // зображення
                // якщо ширина зображення більше ніж 0
                if (image.getWidth() != 0) {
                    // перехід до графічного потоку
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // якщо ширина зображення більша ніж висота
                            if (image.getWidth() > image.getHeight()) {
                                canvas.setFitWidth(Values.IMAGE_VIEW_WIDTH);    // задаємо ширину полотна
                                imageFrame.setMaxHeight(                        // задаємо максимальну висоту рамки
                                        (Values.IMAGE_VIEW_WIDTH / image.getWidth()) * image.getHeight());
                            // інакше (якщо висота зображення більша ніж ширина)
                            } else {
                                canvas.setFitHeight(Values.IMAGE_VIEW_HEIGHT);  // задаємо висоту полотна
                            }
                            canvas.setImage(image);                             // змінюємо зображення
                        }
                    });
                }
            // у разі виникнення помилки
            } catch (MalformedURLException ignored) {}                          // нічого не робимо
        }
    }

    /*******************************************************************************************************************
     * Пошук зображень *********************************************************************************************** *
     * *************************************************************************************************************** *
     * @param pathFrom шлях до теці з зображеннями ******************************************************************* *
     ******************************************************************************************************************/
    private void findAllImages(String pathFrom) {
        File mainDir = new File(pathFrom);                                      // головна директорія
        String[] mainDirList = mainDir.list();                                  // список файлів у директорії
        String size;
        int length;
        int countFoundFilesInDir = 0;
        // якщо головна директорія не порожня
        if (mainDirList != null)
            countFoundFilesInDir = mainDirList.length;                          // задаємо кількість файлів
        String pathToFile;
        // пошук всіх файлів у теці
        for (int i = 0; i < countFoundFilesInDir; i++) {
            pathToFile = pathFrom + File.separator + mainDirList[i];            // шлях до файлу
            File file = new File(pathToFile);                                   // файл
            // якщо файл - це файл
            if (file.isFile()) {
                int type = Checker.fileIsImage(pathToFile);                     // перевіряємо тип файлу
                // якщо файл - це зображення
                if (type == 1 || type == 2) {
                    size = "" + file.length();                                  // розмір файлу
                    length = size.length();                                     // кількість символів у розмірі
                    size = length + "." + size;                                 // розмір файлу
                    arrayWithPathToImages.add(                                  // додаємо розмір і шлях до масиву
                            size + "-___created_by_Sergei_Nadolskiy___-" + pathToFile);
                    // якщо це jpg зображення і кількість символів у його розмірі не більше 7
                    if (type == 2 && length < 8)
                        imagesForShow.add(pathToFile);                          // додаємо шлях до масиву показу
                    // перехід до графічного потоку
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            countAllImagesFound.setText(                        // змінюємо показник знайдених зображень
                                    (arrayWithPathToImages.size()) + " " + Values.SHORT_THING);}
                    });
                }
            // інакше (якщо файл - це директорія)
            } else {
                findAllImages(pathToFile);                                      // шукаємо зображення у цій теці
            }
        }
        // перехід до графічного потоку
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                countAllImagesFound.setText(                                    // змінюємо показник знайдених зображень
                        (arrayWithPathToImages.size()) + " " + Values.SHORT_THING);
            }
        });
    }
}