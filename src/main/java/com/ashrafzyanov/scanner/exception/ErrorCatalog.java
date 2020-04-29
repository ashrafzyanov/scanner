package com.ashrafzyanov.scanner.exception;

final public class ErrorCatalog {
    /**
     * Нет доступа к диску/каталогу
     */
    public final static byte CE_NotAccessDisk = 1;
    public final static String MSG_NotAccessDisk = "Нет доступа к диску/каталогу";

    /**
     * Исходный пункт не является диском/каталогом
     */
    public final static byte CE_InitialNotNorm = 2;
    public final static String MSG_InitialNotNorm = "Исходный пункт не является диском/каталогом";

    /**
     * Выбранный диск отсуствует
     */
    public final static byte CE_UsedDiskNotFound = 3;
    public final static String MSG_UsedDiskNotFound = "Выбранный диск/каталог отсуствует";


    //Ошибка выполнения запроса к базе данных
    public final static byte CE_ErrorQueryDB = 4;
    public final static String MSG_ErrorQueryDB = "Ошибка выполнения запроса к базе данных";


    //Ошибка создания объекта команд для базы данных
    public final static byte CE_ErrorCreateStatement = 5;
    public final static String MSG_ErrorCreateStatement = "Ошибка при создании запроса к базе данных";



    //Ошибка, неудалось подключиться к серверу базы данных
    public final static byte CE_ConnectErrorDataBase = 6;
    public final static String MSG_ConnectErrorDataBase = "Не удалось подключится к серверу базы данных";


    //Ошибка, ненайден класс для драйвера
    public final static byte CE_ClassNotFound = 7;
    public final static String MSG_ClassNotFound  = "Ненайден класс для работы с дайвером";


    //Данного диска нет в базе данных
    public final static byte CE_DiskNotDB = 8;
    public final static String MSG_DiskNotDB = "Данного диска нет в базе данных";

}
