package nss.Helpers;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.photoshop.PsdHeaderDirectory;
import nss.Generators.ImageCameraName;
import nss.Generators.ImageDateCreated;
import nss.Generators.ImageSize;
import nss.MetaData.CameraInfo;
import nss.MetaData.DateInfo;
import nss.MetaData.SizeInfo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/***********************************************************************************************************************
 * 10000000101111111110111111111 * Author: Sergei Nadolskiy       **************************************************** *
 * 10100000101000000010100000001 * @mail:  s.nadolskiy@gmail.com  **************************************************** *
 * 10010000101000000000100000000 * skype:  s.nadolskiy            **************************************************** *
 * 10001000101111111110111111111 ************************************************************************************* *
 * 10000100100000000010000000001 ************************************************************************************* *
 * 10000010101000000010100000001 * Project Name: PSMini   ************************************************************ *
 * 10000000101111111110111111111 **************************************************************** зроблено в Україні * *
 **********************************************************************************************************************/
@SuppressWarnings("ALL")
public class ImageInfo {

    private final HashMap hmImageMetadata = new HashMap();                               // інформація про зображення

    /*******************************************************************************************************************
     * Отримання інформації про зображення *************************************************************************** *
     * *************************************************************************************************************** *
     * @param pathToFile шлях до зображення ************************************************************************** *
     * @param choiceBoxSortingPosition
     ******************************************************************************************************************/
    public void getImageMetaData(String pathToFile, int choiceBoxSortingPosition) {
        File image = new File(pathToFile);
        Metadata metadata;

        int[] iImageCreatingDate = new int[]{                                             // порожня дата зйомки
                Values.IMAGE_CREATING_YEAR,
                Values.IMAGE_CREATING_MONTH,
                Values.IMAGE_CREATING_DAY,
                Values.IMAGE_CREATING_HOURS,
                Values.IMAGE_CREATING_MINUTES,
                Values.IMAGE_CREATING_SECONDS};

        String[] imageCamera = new String[] {                                             // порожня марка та модель
                Values.NO_CAMERA_NAME,
                Values.NO_CAMERA_MODEL};

        int[] imageSize = new int[]{                                                      // порожні розміри зображення
                Values.NO_IMAGE_SIZE_WIDTH,
                Values.NO_IMAGE_SIZE_HEIGHT};

        String imageType;

        try {
            metadata = ImageMetadataReader.readMetadata(image);

            ExifIFD0Directory exifIFDOD = metadata.getDirectory(ExifIFD0Directory.class);
            ExifSubIFDDirectory exifSIFDD = metadata.getDirectory(ExifSubIFDDirectory.class);

            PsdHeaderDirectory psdHeaderDirectory = metadata.getDirectory(PsdHeaderDirectory.class);

            // якщо exifIFDOD директорія не порожня
            if (exifIFDOD != null) {
                imageSize = SizeInfo.getSize(exifIFDOD);                                  // отримуємо розмір зображення
                // якщо сортування за апаратом
                if (choiceBoxSortingPosition == Values.SORT_BY_CAMERA) {
                    imageCamera = CameraInfo.getCameraNameAndModel(exifIFDOD);            // отримуємо марку та модель
                    // якщо марка не відсутня
                    if (imageCamera[0] != null) {
                        // якщо марка Nikon
                        if (imageCamera[0].toLowerCase().split(" ")[0].equals("nikon"))
                            imageSize = SizeInfo.getSize(image);                          // отримуємо розмір зображення
                    }
                }
            }

            // якщо exifSIFDD директорія не порожня
            if (exifSIFDD != null) {
                // якщо рік зйомки дорівнює 0, або менший ніж 1975
                if (iImageCreatingDate[Values.IMAGE_CREATING_YEAR] == 0 ||
                    iImageCreatingDate[Values.IMAGE_CREATING_YEAR] < Values.IMAGE_MIN_YEAR_CREATED)
                    iImageCreatingDate = DateInfo.getImageCreatingDate(exifSIFDD);        // отримуємо дату зйомки
            }

            // якщо psdHeader директорія не порожня і широта зображення дорівнює 0
            if (imageSize[Values.IMAGE_WIDTH] == 0 && psdHeaderDirectory != null)
                imageSize = SizeInfo.getSize(psdHeaderDirectory);                         // отримуємо розмір зображення

        } catch (ImageProcessingException | IOException | NoClassDefFoundError ignored) {
        }

        // якщо широта зображення дорівнює 0
        if (imageSize[Values.IMAGE_WIDTH] == 0)
            imageSize = SizeInfo.getSize(pathToFile);                                     // отримуємо розмір зображення

        imageType = Files.getFileType(pathToFile);                                        // отримуємо тип зображення

        hmImageMetadata.put("imageYear",    iImageCreatingDate[Values.IMAGE_CREATING_YEAR]);    // рік
        hmImageMetadata.put("imageMonth",   iImageCreatingDate[Values.IMAGE_CREATING_MONTH]);   // місяць
        hmImageMetadata.put("imageDay",     iImageCreatingDate[Values.IMAGE_CREATING_DAY]);     // день
        hmImageMetadata.put("imageHour",    iImageCreatingDate[Values.IMAGE_CREATING_HOURS]);   // година
        hmImageMetadata.put("imageMinutes", iImageCreatingDate[Values.IMAGE_CREATING_MINUTES]); // хвилина
        hmImageMetadata.put("imageSeconds", iImageCreatingDate[Values.IMAGE_CREATING_SECONDS]); // секунда
        hmImageMetadata.put("imageCameraName",  imageCamera[Values.REAL_CAMERA_NAME]);          // марка камери
        hmImageMetadata.put("imageCameraModel", imageCamera[Values.REAL_CAMERA_MODEL]);         // модель камери
        hmImageMetadata.put("imageWidth", imageSize[Values.IMAGE_WIDTH]);                       // широта зображення
        hmImageMetadata.put("imageHeight", imageSize[Values.IMAGE_HEIGHT]);                     // висота зображення
        hmImageMetadata.put("imageType", imageType);                                            // тип зображення

    }

    /*******************************************************************************************************************
     * Генерація нового імені зображення ***************************************************************************** *
     * *************************************************************************************************************** *
     * @param iValue - IMAGE_YEAR      - рік зйомки        *********************************************************** *
     *               - IMAGE_MONTH     - місяць зйомки     *********************************************************** *
     *               - IMAGE_FULL_DATA - повна дата зйомки *********************************************************** *
     *               - CAMERA_BRAND    - марка камери      *********************************************************** *
     *               - CAMERA_MODEL    - модель камери     *********************************************************** *
     *               - CAMERA_TYPE     - тип зображення    *********************************************************** *
     *               - CAMERA_SIZE     - розмір зображення *********************************************************** *
     * @param type   тип сортування                        *********************************************************** *
     * @return нове ім'я                                   *********************************************************** *
     ******************************************************************************************************************/
    private String getName(int iValue, int type) {
        String returnedParameter = null;                                                  // ім'я
        // в залежності від вхідного значення формуємо нове ім'я
        switch (iValue) {
            case Values.IMAGE_YEAR:                                                       // рік
                returnedParameter = ImageDateCreated.getYear(
                        hmImageMetadata.get("imageYear"));
                break;
            case Values.IMAGE_MONTH:                                                      // місяць
                returnedParameter = ImageDateCreated.getMonth(
                        hmImageMetadata.get("imageYear"),
                        hmImageMetadata.get("imageMonth"),
                        Values.FULL_MONTH);
                break;
            case Values.IMAGE_FULL_DATA:                                                  // повну дату
                returnedParameter = ImageDateCreated.getFullDate(
                        hmImageMetadata.get("imageYear"),
                        hmImageMetadata.get("imageMonth"),
                        hmImageMetadata.get("imageDay"),
                        hmImageMetadata.get("imageHour"),
                        hmImageMetadata.get("imageMinutes"),
                        hmImageMetadata.get("imageSeconds"),
                        type);
                break;

            case Values.IMAGE_CAMERA_NAME:                                                // марка камери
                returnedParameter = ImageCameraName.getCameraName(
                        hmImageMetadata.get("imageCameraName"));

                break;
            case Values.IMAGE_CAMERA_MODEL:                                               // модель камери
                returnedParameter = ImageCameraName.getCameraModel(
                        hmImageMetadata.get("imageCameraModel"),
                        hmImageMetadata.get("imageCameraName"));
                break;
            case Values.IMAGE_TYPE:                                                       // тип зображення
                returnedParameter = (String) hmImageMetadata.get("imageType");
                break;
            case Values.IMAGE_SIZE: {                                                     // розмір зображення
                returnedParameter = ImageSize.getSize(
                        hmImageMetadata.get("imageWidth"),
                        hmImageMetadata.get("imageHeight"));
                break;
            }
        }

        // якщо ім'я відсутнє
        if (returnedParameter == null)
            returnedParameter = "";                                                       // записуємо порожнє ім'я
        return returnedParameter;                                                         // вертаємо нове ім'я
    }

    /*******************************************************************************************************************
     * Генеруємо новий шлях до теці для зображення ******************************************************************* *
     * *************************************************************************************************************** *
     * @param type 0 - теця                           **************************************************************** *
     *             1 - файл                           **************************************************************** *
     * @param iSortNumber сортувати за:               **************************************************************** *
     *                    0 - датою                   **************************************************************** *
     *                    1 - камерою                 **************************************************************** *
     *                    2 - розміром                **************************************************************** *
     *                    3 - типом                   **************************************************************** *
     * @param sTargetFolderPath шлях до цільової теці **************************************************************** *
     * @return новий шлях до теці для зображення      **************************************************************** *
     ******************************************************************************************************************/
    public String getNewPathToFile(int type, int iSortNumber, String sTargetFolderPath) {
        String currentDirName   = Values.MAIN_DIR_NAME + File.separator;
        String currentImageName = Values.MAIN_IMAGE_NAME + "_";
        // залежно від типу сортування генеруємо шлях
        switch (iSortNumber) {
            case Values.SORT_BY_DATE:                                                   // за датою
                currentDirName +=                                                       // назва теці
                        getName(Values.IMAGE_YEAR, 0) + File.separator +
                                getName(Values.IMAGE_MONTH, 0);

                currentImageName +=                                                     // назва зображення
                        getName(Values.IMAGE_FULL_DATA, Values.FOR_DATE) + "_" +
                                getName(Values.IMAGE_SIZE, 0);
                break;
            case Values.SORT_BY_CAMERA:                                                 // ха камерою
                currentDirName +=                                                       // назва теці
                        getName(Values.IMAGE_CAMERA_NAME, 0) + File.separator +
                                getName(Values.IMAGE_CAMERA_MODEL, 0);

                currentImageName +=                                                     // назва зображення
                        getName(Values.IMAGE_FULL_DATA, 0) + "_" +
                                getName(Values.IMAGE_SIZE, 0);
                break;
            case Values.SORT_BY_SIZE:                                                   // за розміром
                currentDirName +=                                                       // назва теці
                        getName(Values.IMAGE_SIZE, 0);

                currentImageName +=                                                     // назва зображення
                        getName(Values.IMAGE_FULL_DATA, 0) + "_" +
                                getName(Values.IMAGE_SIZE, 0);
                break;
            case Values.SORT_BY_TYPE:                                                   // за типом
                currentDirName +=                                                       // назва теці
                        getName(Values.IMAGE_TYPE, 0).toUpperCase();

                currentImageName +=                                                     // назва зображення
                        getName(Values.IMAGE_FULL_DATA, 0) + "_" +
                                getName(Values.IMAGE_SIZE, 0);
                break;
        }

        currentDirName = currentDirName.replace("null" + File.separator, "");           // міняємо "null" на "" у назві
        File dirForImage =                                                              // директорія для зображення
                new File(
                        sTargetFolderPath +
                        File.separator +
                        currentDirName);
        // якщо така теця є
        if (dirForImage.exists()) {
            int folderElementsCount = dirForImage.list().length + 1;                    // кількість файлів у теці
            currentImageName +=                                                         // формуємо ім'я зображення
                    "_№" +
                    folderElementsCount +
                    "." +
                    getName(Values.IMAGE_TYPE, 0);
        // інакше (якщо такої теці немає)
        } else
            currentImageName +=                                                         // формуємо ім'я зображення
                    "_№1." +
                     getName(Values.IMAGE_TYPE, 0);

        // форматуємо назву
        currentImageName = currentImageName.replace("null_", "");
        currentImageName = currentImageName.replace("____", "_");
        currentImageName = currentImageName.replace("___", "_");
        currentImageName = currentImageName.replace("__", "_");

        // якщо потрібно вернути назву теці
        if (type == Values.DIR)
            return currentDirName;                                                      // вертаємо новий шлях до теці
        // інакше (якщо потрібно вернути назву зображення)
        else
            return currentImageName;                                                    // вертаємо нове ім'я зображення
    }
}