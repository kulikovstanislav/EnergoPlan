package ru.stanislavkulikov.energoplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase {
    // имя базы данных
    private static final String DATABASE_NAME = "energoPlanDataBase.db";
    // версия базы данных
    private static final int DATABASE_VERSION = 1;
    // имя таблицы
    public static final String DATABASE_TABLE_HOMESTEAD = "homestead";
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String HOMESTEAD_NUMBER_COLUMN = "homestead_number";
    public static final String FIO_COLUMN = "fio";
    public static final String PHONE_COLUMN = "phone";
    public static final String FEEDER_COLUMN = "feeder";

    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DataBase(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDB.query(DATABASE_TABLE_HOMESTEAD, null, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addRec() {

        ContentValues values = new ContentValues();
        values.put(DataBase.HOMESTEAD_NUMBER_COLUMN, "123");
        values.put(DataBase.FIO_COLUMN, "Иванов Иван Иванович");
        values.put(DataBase.PHONE_COLUMN, "4954553443");
        values.put(DataBase.FEEDER_COLUMN, "3А");
        // Вставляем данные в таблицу
        mDB.insert(DATABASE_TABLE_HOMESTEAD, null, values);
    }

    // удалить запись из DB_TABLE
    public void delRec(long id) {
        mDB.delete(DATABASE_TABLE_HOMESTEAD, COLUMN_ID + " = " + id, null);
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + DATABASE_TABLE_HOMESTEAD + " ("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + HOMESTEAD_NUMBER_COLUMN + " integer not null, "
                    + FIO_COLUMN + " text, "
                    + PHONE_COLUMN + " text, "
                    + FEEDER_COLUMN + " text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Запишем в журнал
            Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);
            // Удаляем старую таблицу и создаём новую
            db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_HOMESTEAD);
            // Создаём новую таблицу
            onCreate(db);
        }
    }

}
