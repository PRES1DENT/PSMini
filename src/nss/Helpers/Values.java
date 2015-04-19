package nss.Helpers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/***********************************************************************************************************************
 * 10000000101111111110111111111 * Author: Sergei Nadolskiy       **************************************************** *
 * 10100000101000000010100000001 * @mail:  s.nadolskiy@gmail.com  **************************************************** *
 * 10010000101000000000100000000 * skype:  s.nadolskiy            **************************************************** *
 * 10001000101111111110111111111 ************************************************************************************* *
 * 10000100100000000010000000001 ************************************************************************************* *
 * 10000010101000000010100000001 * Project Name: PSMini   ************************************************************ *
 * 10000000101111111110111111111 **************************************************************** зроблено в Україні * *
 **********************************************************************************************************************/
public class Values {

    public static final String WELCOME_MESSAGE =
            " ***********************************************************************************************************************\n" +
            " * 10000000101111111110111111111 * Вітаю у PSMini =)   *************************************************************** *\n" +
            " * 10100000101000000010100000001 ************************************************************************************* *\n" +
            " * 10010000101000000000100000000 ************************************************************************************* *\n" +
            " * 10001000101111111110111111111 * Author: Sergei Nadolskiy       **************************************************** *\n" +
            " * 10000100100000000010000000001 * @mail:  s.nadolskiy@gmail.com  **************************************************** *\n" +
            " * 10000010101000000010100000001 * skype:  s.nadolskiy            **************************************************** *\n" +
            " * 10000000101111111110111111111 **************************************************************** зроблено в Україні * *\n" +
            " ***********************************************************************************************************************\n";

    public static final String TITLE =
            "-= Photo Sort Mini =- by Sergei Nadolskiy (s.nadolskiy@gmail.com)";    // назва програми

    public static final String ERROR_WINDOW_TITLE = "Что-то не так =(";             // назва вікна з помилками

    public static final int APP_WIDTH  = 1045;                                      // широта головного вікна програми
    public static final int APP_HEIGHT = 341;                                       // висота головного вікна програми

    public static final int IMAGE_VIEW_WIDTH  = 400;                                // широта  зображення
    public static final int IMAGE_VIEW_HEIGHT = 230;                                // висота зображення

    private static final String SORT_BY_DATE_VALUE   = "дате";                      // назва типу сортування
    private static final String SORT_BY_CAMERA_VALUE = "моделе аппрата";            // назва типу сортування
    private static final String SORT_BY_SIZE_VALUE   = "размеру изображения";       // назва типу сортування
    private static final String SORT_BY_TYPE_VALUE   = "формату";                   // назва типу сортування

    public static final String DIR_CHOOSER_SOURCE_FOLDER_TITLE =
            "Укажите папку с изображениями, которые следует отсортировать =)";      // назва вікна для обирання
    public static final String DIR_CHOOSER_TARGET_FOLDER_TITLE =
            "Укажите папку,куда будут сохраняться отсортированные изображения =)";  // назва вікна для обирання

    public static final String BUTTON_TEXT_SORTING = "Идёт сортировка..";           // назва кнопки під час сортування
    public static final String BUTTON_TEXT_FINDING = "Поиск изображений..";         // назва кнопки під час пошуку
    public static final String BUTTON_TEXT_SORT = "Сортировка..";                   // назва кнопки під час сортування

    public static final String BUTTON_CLOSE = "Закрыть";                            // назва кнопки для закриття

    public static final String CHECK_NO_SOURCE_PATH_FOUND =                         // текст зауваження
            "НЕ УКАЗАН ПУТЬ К ПАПКЕ С ИЗОБРАЖЕНИЯМИ.\n " +
            "(пожалуйста, укажите папку с изображениями для сортировки)\n";
    public static final String CHECK_NO_TARGET_PATH_FOUND =                         // текст зауваження
            "НЕ УКАЗАН ПУТЬ К ПАПКЕ ДЛЯ СОХРАНЕНИЯ ОТСОРТИРОВАННЫХ ИЗОБРАЖЕНИЙ.\n " +
            "(пожалуйста, укажите папку куда будут сохраняться отсортированные изображения)\n";
    public static final String CHECK_NO_DIR_FOUND =                                 // текст зауваження
            "ПО УКАЗАННОМУ ПУТИ НЕТ ПАПКИ: ";
    public static final String CHECK_NOT_A_DIR    =
            "УКАЗАННАЯ ПАПКА НЕ ЯВЛЯЕТСЯ ПАПКОЙ: ";                                 // текст зауваження

    public static final String START_SORTING = "Начать сортировку!";                // назва кнопки

    public static final String PATH_TO_FINAL_IMAGE = "src/img/img_finally_photo.jpg";         // шлях до фінального зображення

    public static final int IMAGE_YEAR      = 0;                                    // рік
    public static final int IMAGE_MONTH     = 1;                                    // місяць
    public static final int IMAGE_FULL_DATA = 2;                                    // день
    public static final int IMAGE_CAMERA_NAME  = 3;                                 // марка камери
    public static final int IMAGE_CAMERA_MODEL = 4;                                 // модель камери
    public static final int IMAGE_TYPE = 5;                                         // тип зображення
    public static final int IMAGE_SIZE = 6;                                         // розмір зображення

    public static final int DIR   = 0;                                              // директорія
    public static final int IMAGE = 1;                                              // зображення

    public static final String MAIN_DIR_NAME = "Фотографии";                        // назва головної теці
    public static final String MAIN_IMAGE_NAME = "Фото";                            // перша назва зображення

    public static final int SORT_BY_DATE   = 0;                                     // сортування за датою
    public static final int SORT_BY_CAMERA = 1;                                     // сортування за камерою
    public static final int SORT_BY_SIZE   = 2;                                     // сортування за розміром
    public static final int SORT_BY_TYPE   = 3;                                     // сортування за типом

    public static final int FOR_DATE  = 1;                                          // для дати

    public static final String NO_YEAR = "Другие";                                  // назва директорії без року
    public static final String NO_MONTH = "";                                       // назва директорії без місяцю
    public static final String NO_CAMERA_NAME = "Другие";                           // назва директорії без марки
    public static final String NO_CAMERA_MODEL = "";                                // назва директорії без моделі

    public static final int IMAGE_CREATING_YEAR    = 0;                             // рік
    public static final int IMAGE_CREATING_MONTH   = 1;                             // місяць
    public static final int IMAGE_CREATING_DAY     = 2;                             // день
    public static final int IMAGE_CREATING_HOURS   = 3;                             // година
    public static final int IMAGE_CREATING_MINUTES = 4;                             // хвилина
    public static final int IMAGE_CREATING_SECONDS = 5;                             // секунда

    public static final int IMAGE_MIN_YEAR_CREATED = 1975;                          // мінімальний рік зйомки

    public static final int NO_IMAGE_SIZE_WIDTH    = 0;                             // ширина
    public static final int NO_IMAGE_SIZE_HEIGHT   = 1;                             // висота

    public static final int REAL_CAMERA_NAME  = 0;                                  // марка камери
    public static final int REAL_CAMERA_MODEL = 1;                                  // модель камери

    public static final int IMAGE_WIDTH  = 0;                                       // ширина
    public static final int IMAGE_HEIGHT = 1;                                       // висота

    public static final int FULL_MONTH  = 0;                                        // повний місяць
    public static final int SMALL_MONTH = 1;                                        // короткий місяць

    public static final String[] fullMonth = new String[]{                          // повний місяць
            "",
            "(01) Январь",
            "(02) Февраль",
            "(03) Март",
            "(04) Апрель",
            "(05) Май",
            "(06) Июнь",
            "(07) Июль",
            "(08) Август",
            "(09) Сентябрь",
            "(10) Октябрь",
            "(11) Ноябрь",
            "(12) Декабрь"};

    public static  final String[] month = new String[]{                             // короткий місяць
            "",
            "января",
            "февраля",
            "марта",
            "апреля",
            "мая",
            "июня",
            "июля",
            "августа",
            "сентября",
            "октября",
            "ноября",
            "декабря"};

    public static final ObservableList<String> olSortingList =                      // типи сортування
            FXCollections.observableArrayList(
                    Values.SORT_BY_DATE_VALUE,
                    Values.SORT_BY_CAMERA_VALUE,
                    Values.SORT_BY_SIZE_VALUE,
                    Values.SORT_BY_TYPE_VALUE
            );

    public static final String PATH_TO_MAIN_LAYOUT = "photo_sort_mini.fxml";        // головна форма

    public static final String NO_PATH_TO_SOURCE_FOLDER = "";                       // пустий шлях
    public static final String NO_PATH_TO_TARGET_FOLDER = "";                       // пустий шлях

    public static final String EMPTY_VALUE = "";                                    // пусте значення

    public static final String SHORT_ORIGINAL = "ориг.";                            // оригінал
    public static final String SHORT_DUPLICATE = "дуб.";                            // дублікат

    public static final String SHORT_THING = "шт.";                                 // одиниця

    public static final String CAMERA_NAME_CORELOGIC = "Corelogic";                 // марка камери
    public static final String CAMERA_NAME_SAMSUNG = "Samsung";                     // марка камери
    public static final String CAMERA_NAME_HTTP = "http";                           // назва

    public static final String PATH_TO_ICON = "file:src/img/icon.png";              // іконка
}