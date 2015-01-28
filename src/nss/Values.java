package nss;

/**
 * Created by president on 1/26/15.
 */
public class Values {

    // APP TITLE
    public static final String TITLE = "Photo Sort Mini";

    // ERROR WINDOW
    public static final String ERROR_WINDOW_TITLE = "ERROR";


    // APP SIZE
    public static final int APP_WIDTH  = 1045;
    public static final int APP_HEIGHT = 348;

    // IMAGE SIZE
    public static final int IMAGE_WIDTH  = 400;
    public static final int IMAGE_HEIGHT = 230;

    // THREAD
    public static final int THREAD_SLEEP_TIME = 1000;

    // SORTING TYPE
    public static final String SORT_BY_DATE_VALUE   = "дате";
    public static final String SORT_BY_CAMERA_VALUE = "моделе аппрата";
    public static final String SORT_BY_SIZE_VALUE   = "размеру изображения";
    public static final String SORT_BY_TYPE_VALUE   = "формату";

    // DIRECTORY CHOOSE STRINGS
    public static final String DIR_CHOOSER_SOURCE_FOLDER_TITLE = "Select folder with images";
    public static final String DIR_CHOOSER_TARGET_FOLDER_TITLE = "Select folder where you wont save images";

    // BUTTONS
    public static final String BUTTON_TEXT_SORTING = "Идёт сортировка..";
    public static final String BUTTON_CLOSE = "CLOSE";


    // CHECKING MESSAGES
    public static final String CHECK_NO_SOURCE_PATH_FOUND = "NO SOURCE PATH FOUND. Please set path to your FOLDER with IMAGES\n";
    public static final String CHECK_NO_TARGET_PATH_FOUND = "NO TARGET PATH FOUND. Please set path to your FOLDER where you wont save IMAGES\n";

    public static final String CHECK_NO_DIR_FOUND = "NO DIR FOUND with this PATH: ";
    public static final String CHECK_NOT_A_DIR    = "THIS IS NOT A DIR: ";

    // TIMER
    public static final String DATE_FORMAT = "HH:mm:ss";
    public static final String START_TIME = "| Время начала: ";
    public static final String END_TIME = "| Время конца:  ";
    public static final String PROCESSING_TIME = "| Время выполнения: ";
    public static final String TIMER_LINE = "+ ----------------------------------- +";
    public static final String TIMER_TIME_H = "(Ч)";
    public static final String TIMER_TIME_M = "(М)";
    public static final String TIMER_TIME_S = "(С)";

    // VALUES
    public static final int IMAGE_YEAR      = 0;
    public static final int IMAGE_MONTH     = 1;
    public static final int IMAGE_FULL_DATA = 2;

    public static final int IMAGE_CAMERA_BRAND = 3;
    public static final int IMAGE_CAMERA_MODEL = 4;

    public static final int IMAGE_TYPE = 5;
    public static final int IMAGE_SIZE = 6;

    public static final int DIR = 0;
    public static final int IMAGE = 1;

    public static final String MAIN_DIR_NAME = "Фотографии";
    public static final String MAIN_IMAGE_NAME = "Фото";

    // SORT BY
    public static final int SORT_BY_DATE   = 0;
    public static final int SORT_BY_CAMERA = 1;
    public static final int SORT_BY_SIZE   = 2;
    public static final int SORT_BY_TYPE   = 3;

    // TYPES
    public static final int FOR_DATE  = 1;

    // EMPTY DIRS
    public static final String NO_YEAR = "Другие";
    public static final String NO_CAMERA_BRAND = "Другие";
    public static final String NO_CAMERA_MODEL = "Другие";


    // REDUCT
    public static final String IMAGE_REDACTED = "Редактировались";


}

