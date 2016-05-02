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
    public static final String DATABASE_TABLE_INDICATION = "indication";
    public static final String DATABASE_TABLE_PAYMENT = "payment";
    // названия столбцов
    public static final String COLUMN_ID = "_id";

    public static final String HOMESTEAD_NUMBER_COLUMN = "homestead_number";
    public static final String FIO_COLUMN = "fio";
    public static final String PHONE_COLUMN = "phone";
    public static final String FEEDER_COLUMN = "feeder";

    public static final String COUNTER_NAME_COLUMN = "counter_name";
    public static final String HOMESTEAD_ID_COLUMN = "homestead_id";

    public static final String COUNTER_ID_COLUMN = "counter_id";
    public static final String INDICATIONS_COLUMN = "indications_column";
    public static final String PAYMENT_COLUMN = "payment_column";
    public static final String DATE_INDICATIONS_COLUMN = "date_indications_column";
    public static final String DATE_PAYMENT_COLUMN = "date_payment_column";

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

    public Cursor getAllHomesteadDataLefJoin() {
        return mDB.rawQuery("select hom.*, ind." + INDICATIONS_COLUMN + ", ind." + DATE_INDICATIONS_COLUMN + " from " + DATABASE_TABLE_HOMESTEAD
                + " hom left join ( select " + INDICATIONS_COLUMN + ", " + HOMESTEAD_ID_COLUMN + ", max(" + DATE_INDICATIONS_COLUMN + ") as "
                + DATE_INDICATIONS_COLUMN + " from " + DATABASE_TABLE_INDICATION  + " group by " + HOMESTEAD_ID_COLUMN + ") ind on hom."
                + COLUMN_ID + " = ind." + HOMESTEAD_ID_COLUMN, null);
    }

    public Cursor getAllCounterData(Integer id) {
        return mDB.rawQuery("select * from " + DATABASE_TABLE_COUNTER + " where " + HOMESTEAD_ID_COLUMN + " = " + Integer.toString(id), null);
    }

    public Cursor getAllIndicationsData(Integer id) {
        return mDB.rawQuery("select * from " + DATABASE_TABLE_INDICATION + " where " + COUNTER_ID_COLUMN + " = " + Integer.toString(id), null);
    }

    public Cursor getAllPaymentData(Integer id) {
        return mDB.rawQuery("select * from " + DATABASE_TABLE_PAYMENT + " where " + COUNTER_ID_COLUMN + " = " + Integer.toString(id), null);
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

    public void addIndicationRec(IndicationModel indicationModel) {
        ContentValues values = new ContentValues();
        values.put(DataBase.COUNTER_ID_COLUMN, indicationModel.getCounterId());
        values.put(DataBase.HOMESTEAD_ID_COLUMN, indicationModel.getHomesteadId());
        values.put(DataBase.DATE_INDICATIONS_COLUMN, indicationModel.getDate().getTime()/1000);
        values.put(DataBase.INDICATIONS_COLUMN, indicationModel.getIndication());
        // Вставляем данные в таблицу
        mDB.insert(DATABASE_TABLE_INDICATION, null, values);
    }

    public void addPaymentRec(PaymentModel paymentModel) {
        ContentValues values = new ContentValues();
        values.put(DataBase.COUNTER_ID_COLUMN, paymentModel.getCounterId());
        values.put(DataBase.HOMESTEAD_ID_COLUMN, paymentModel.getHomesteadId());
        values.put(DataBase.DATE_PAYMENT_COLUMN, paymentModel.getDate().getTime()/1000);
        values.put(DataBase.PAYMENT_COLUMN, paymentModel.getPayment());
        // Вставляем данные в таблицу
        mDB.insert(DATABASE_TABLE_PAYMENT, null, values);
    }

    // удалить запись из DB_TABLE
    public void delHomesteadRec(long id) {
        mDB.delete(DATABASE_TABLE_HOMESTEAD, COLUMN_ID + " = " + id, null);
    }

    public void delCounterRec(long id) {
        mDB.delete(DATABASE_TABLE_COUNTER, COLUMN_ID + " = " + id, null);
    }

    public void delIndicationRec(long id) {
        mDB.delete(DATABASE_TABLE_INDICATION, COLUMN_ID + " = " + id, null);
    }

    public void delPaymentRec(long id) {
        mDB.delete(DATABASE_TABLE_PAYMENT, COLUMN_ID + " = " + id, null);
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
            db.execSQL("create table " + DATABASE_TABLE_INDICATION + " ("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COUNTER_ID_COLUMN + " integer not null, "
                    + HOMESTEAD_ID_COLUMN + " integer not null, "
                    + DATE_INDICATIONS_COLUMN + " integer, "
                    + INDICATIONS_COLUMN + " real);");
            db.execSQL("create table " + DATABASE_TABLE_PAYMENT + " ("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COUNTER_ID_COLUMN + " integer not null, "
                    + HOMESTEAD_ID_COLUMN + " integer not null, "
                    + DATE_PAYMENT_COLUMN + " integer, "
                    + PAYMENT_COLUMN + " real);");
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
