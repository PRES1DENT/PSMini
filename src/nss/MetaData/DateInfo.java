package nss.MetaData;

import com.drew.metadata.exif.ExifSubIFDDirectory;
import nss.Helpers.Values;

import java.util.Calendar;
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
public class DateInfo {
    private static final int[] iaImageCreatingDate = new int[]{0, 0, 0, 0, 0, 0};           // масив с порожньою датою
    /*******************************************************************************************************************
     * Отримуємо дату створення зображення ExifSubIFDDirectory ******************************************************* *
     * *************************************************************************************************************** *
     * @param exifSIFDD ExifSubIFDDirectory              ************************************************************* *
     * @return масив з даними про дату зйомки зображення ************************************************************* *
     * ****************************************************************************************************************/
    public static int[] getImageCreatingDate(ExifSubIFDDirectory exifSIFDD) {
        // якщо мета-директорія з даними про дату не відсутня
        if (exifSIFDD != null) {
            Date imageCreatingDate;                                                         // дата створення знімку
            // якщо є дата створення знімку
            if (exifSIFDD.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL) != null)
                imageCreatingDate =
                        exifSIFDD.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);       // отримуємо дату зйомки
                // інакше (якщо є дата запису на диск)
            else
                imageCreatingDate =
                        exifSIFDD.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);      // отримуємо дату зйомки
            // якщо отримана дата не відсутня
            if (imageCreatingDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(imageCreatingDate);

                int[] creatingDate = new int[6];
                creatingDate[Values.IMAGE_CREATING_YEAR]  = calendar.get(Calendar.YEAR);          // рік зйомки
                creatingDate[Values.IMAGE_CREATING_MONTH] = calendar.get(Calendar.MONTH) + 1;     // місяць зйомки
                creatingDate[Values.IMAGE_CREATING_DAY]   = calendar.get(Calendar.DAY_OF_MONTH);  // день зйомки

                int imageHours = calendar.get(Calendar.HOUR);                                     // година зйомки
                // якщо знімок був зроблений у другій половини дня
                if (calendar.get(Calendar.AM_PM) == 1)
                    imageHours += 12;                                                             // додаємо 12 годин

                creatingDate[Values.IMAGE_CREATING_HOURS]   = imageHours;                         // відредагований час
                creatingDate[Values.IMAGE_CREATING_MINUTES] = calendar.get(Calendar.MINUTE);      // хвилина зйомки
                creatingDate[Values.IMAGE_CREATING_SECONDS] = calendar.get(Calendar.SECOND);      // секунда зйомки

                // якщо час зйомки знімку = 0
                if (creatingDate[Values.IMAGE_CREATING_HOURS]   == 0 &&
                    creatingDate[Values.IMAGE_CREATING_MINUTES] == 0 &&
                    creatingDate[Values.IMAGE_CREATING_SECONDS] == 0)
                    return iaImageCreatingDate;                                             // вертаємо нульову дату

                return creatingDate;                                                        // вертаємо дату зйомки
                // інакше (якщо відсутня отримана дата)
            } else
                return iaImageCreatingDate;                                                 // вертаємо нульову дату
            // інакше (якщо мета-директорія відсутня (порожня))
        } else
            return iaImageCreatingDate;                                                     // вертаємо нульову дату
    }
}