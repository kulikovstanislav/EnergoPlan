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
    private static final int DATABASE_VERSION = 3;
    // имя таблицы
    public static final String DATABASE_TABLE_HOMESTEAD = "homestead";
    public static final String DATABASE_TABLE_COUNTER = "counter";
    // названия столбцов
    public static final String COLUMN_ID = "_id";

    public static final String HOMESTEAD_NUMBER_COLUMN = "homestead_number";
    public static final String FIO_COLUMN = "fio";
    public static final String PHONE_COLUMN = "phone";
    public static final String FEEDER_COLUMN = "feeder";

    public static final String COUNTER_NAME_COLUMN = "counter_name";
    public static final String HOMESTEAD_ID_COLUMN = "homestead_id";

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
    public Cursor getAllHomesteadData() {
        return mDB.query(DATABASE_TABLE_HOMESTEAD, null, null, null, null, null, null);
    }

    public Cursor getAllCounterData(Integer id) {
        return mDB.rawQuery("select * from " + DATABASE_TABLE_COUNTER + " where " + HOMESTEAD_ID_COLUMN + " = " + Integer.toString(id), null);
    }

    //получить запись по id
    public HomesteadModel getHomesteadRec(int id) {
        Cursor cursor = mDB.rawQuery("select * from " + DATABASE_TABLE_HOMESTEAD + " where " + COLUMN_ID + " = " + Integer.toString(id), null);
        HomesteadModel homesteadModel = new HomesteadModel();
        if (cursor.moveToFirst()) {
            homesteadModel.setHomesteadNumberColumn(cursor.getString(cursor.getColumnIndex(HOMESTEAD_NUMBER_COLUMN)));
            homesteadModel.setFioColumn(cursor.getString(cursor.getColumnIndex(FIO_COLUMN)));
            homesteadModel.setPhoneColumn(cursor.getString(cursor.getColumnIndex(PHONE_COLUMN)));
            homesteadModel.setFeederColumn(cursor.getString(cursor.getColumnIndex(FEEDER_COLUMN)));
        }
        cursor.close();
        return homesteadModel;
    }

    // добавить запись в DB_TABLE
    public void addHomesteadRec(HomesteadModel homesteadModel) {

        ContentValues values = new ContentValues();
        values.put(DataBase.HOMESTEAD_NUMBER_COLUMN, homesteadModel.getHomesteadNumberColumn());
        values.put(DataBase.FIO_COLUMN, homesteadModel.getFioColumn());
        values.put(DataBase.PHONE_COLUMN, homesteadModel.getPhoneColumn());
        values.put(DataBase.FEEDER_COLUMN, homesteadModel.getFeederColumn());
        // Вставляем данные в таблицу
        mDB.insert(DATABASE_TABLE_HOMESTEAD, null, values);
    }

    public void addCounterRec(CounterModel counterModel) {

        ContentValues values = new ContentValues();
        values.put(DataBase.COUNTER_NAME_COLUMN, counterModel.getName());
        values.put(DataBase.HOMESTEAD_ID_COLUMN, counterModel.getHomesteadId());
        // Вставляем данные в таблицу
        mDB.insert(DATABASE_TABLE_COUNTER, null, values);
    }

    // удалить запись из DB_TABLE
    public void delHomesteadRec(long id) {
        mDB.delete(DATABASE_TABLE_HOMESTEAD, COLUMN_ID + " = " + id, null);
    }

    public void delCounterRec(long id) {
        mDB.delete(DATABASE_TABLE_COUNTER, COLUMN_ID + " = " + id, null);
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
                    + HOMESTEAD_NUMBER_COLUMN + " text not null, "
                    + FIO_COLUMN + " text, "
                    + PHONE_COLUMN + " text, "
                    + FEEDER_COLUMN + " text);");
            db.execSQL("create table " + DATABASE_TABLE_COUNTER + " ("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + HOMESTEAD_ID_COLUMN + " integer not null, "
                    + COUNTER_NAME_COLUMN + " text not null);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Запишем в журнал
            Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);
            // Удаляем старую таблицу и создаём новую
            db.execSQL("DROP TABLE IF IT EXISTS '" + DATABASE_TABLE_HOMESTEAD + "'");
            // Создаём новую таблицу
            onCreate(db);
        }
    }

}
