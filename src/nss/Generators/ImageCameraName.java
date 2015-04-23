package nss.Generators;

import nss.Helpers.Values;

/***********************************************************************************************************************
 * 10000000101111111110111111111 * Author: Sergei Nadolskiy       **************************************************** *
 * 10100000101000000010100000001 * @mail:  s.nadolskiy@gmail.com  **************************************************** *
 * 10010000101000000000100000000 * skype:  s.nadolskiy            **************************************************** *
 * 10001000101111111110111111111 ************************************************************************************* *
 * 10000100100000000010000000001 ************************************************************************************* *
 * 10000010101000000010100000001 * Project Name: PSMini   ************************************************************ *
 * 10000000101111111110111111111 **************************************************************** зроблено в Україні * *
 **********************************************************************************************************************/
public class ImageCameraName {

    /*******************************************************************************************************************
     * Генерація марки камери **************************************************************************************** *
     * *************************************************************************************************************** *
     * @param imageCameraName марка камери *************************************************************************** *
     * @return коректна марка камери       *************************************************************************** *
     ******************************************************************************************************************/
    public static String getCameraName(Object imageCameraName) {
        String cameraName = Values.NO_CAMERA_NAME;
        // якщо є марка камери
        if (imageCameraName != null) {
            // якщо марка камери не "Інакші"
            if (!imageCameraName.equals(cameraName)) {
                String[] allName = String.valueOf(imageCameraName).split(" ");
                cameraName = allName[0]; }                                          // зберігаємо першу частину марки
            // якщо марка камери "Corelogic"
            if (cameraName.equals(Values.CAMERA_NAME_CORELOGIC))
                return  Values.CAMERA_NAME_SAMSUNG;                                 // вертаємо назву "Samsung"
            // якщо марка камери http
            if (cameraName.split("://")[0].equals(Values.CAMERA_NAME_HTTP))
                return Values.NO_CAMERA_NAME;                                       // вертаємо назву "Інші"

            String[] cameraNameString = cameraName.split("");
            boolean firstLaterIsLower = false;
            // якщо перша літера марки камери маленька
            if (cameraNameString[0].equals(cameraNameString[0].toLowerCase()))
                firstLaterIsLower = true;
            int upperCount = 0;
            /* рахуємо скільки великих літер має ім’я камери */
            for (String aCameraNameString : cameraNameString) {
                // якщо літера велика
                if (aCameraNameString.equals(aCameraNameString.toUpperCase()))
                    upperCount++;                                                   // збільшуємо лічильник  на одиницю
            }
            // якщо перша літера імені маленька і ім’я має одну або більше великих літер
            if (firstLaterIsLower && upperCount >= 1)
                return cameraName;                                                  // вертаємо назву камери
            // якщо ім’я має менше за ніж 2-і великі літери, або кількість великих літер дорівнює кількості літер у імені
            if ((upperCount < 2 || upperCount == cameraNameString.length)) {
                cameraName = cameraNameString[0].toUpperCase();                     // робимо першу літеру велику
                // змінюємо всі крім першої літери на маленькі
                for (int i = 1; i < cameraNameString.length; i++) {
                    cameraName += cameraNameString[i].toLowerCase();                // робимо літеру маленькою
                }
            }
        }
        return cameraName;                                                          // вертаємо марку камери
    }

    /*******************************************************************************************************************
     * Генерація моделі камери               ************************************************************************* *
     * *************************************************************************************************************** *
     * @param imageCameraModel модель камери ************************************************************************* *
     * @param imageCameraName  марка камери  ************************************************************************* *
     * @return коректна модель камери        ************************************************************************* *
     ******************************************************************************************************************/
    public static String getCameraModel(Object imageCameraModel, Object imageCameraName) {
        String modelName = Values.NO_CAMERA_MODEL;
        // якщо є модель камери
        if (imageCameraModel != null) {
            // якщо модель камери пуста, то вертаємо його без змін, інакше проводимо обробку */
            if (imageCameraModel.equals(modelName)) {
                return modelName;                                                   // вертаємо порожню модель ""
            // якщо модель камери не пуста
            }else {
                // якщо камера модель і марка камери однакові
                if (getCameraName(imageCameraName).toLowerCase().equals(imageCameraModel.toString().toLowerCase()))
                    return Values.NO_CAMERA_NAME;                                   // вертаємо "Інші"
                String[] model = String.valueOf(imageCameraModel).split(" ");
                // якщо перше слово марки камери таке саме як і перше слово моделі
                if (getCameraName(imageCameraName).toLowerCase().equals(model[0].toLowerCase())) {
                    modelName = "";
                    for (int i = 1; i < model.length; i++) {
                        // якщо перше слово
                        if (i==1)
                            modelName += model[i];                                  // починаємо з цього слова, без " "
                        // інакше (другі слова)
                        else
                            modelName += " " + model[i];                            // додаємо слово з " "
                    }
                // інакше (якщо перше слово камери і марки не однакові)
                } else {
                    modelName = "";
                    int nextElement;
                    // обходимо марку
                    for (int i = 0; i < model.length; i++) {
                        nextElement = (i+1);
                        // якщо наступний це не останній елемент
                        if (nextElement <= model.length) {
                            if (!(model[i].equals(" ") && model[nextElement].equals(" "))) {
                                // якщо це не останній елемент ""
                                // якщо перше слово
                                if (i == 0)
                                    modelName += model[i];                      // починаємо з цього слова, без " "
                                // інакше (якщо другі слова)
                                else
                                    modelName += " " + model[i];                // додаємо слово з " "

                            }
                        }
                    }
                }

                modelName = modelName.replace("\\",",");
                modelName = modelName.replace("/",",");

                return modelName;                                                   // вертаємо модель камери
            }
        // інакше (якщо немає моделі камери)
        } else
            return  modelName;                                                      // вертаємо порожню модель ""
    }
}