package com.example.app2803;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "films.db";
    final static String TABLE_NAME = "filmlist";
    //TODO: Не применяется, нужно удалять
    final static String DROP = "DROP TABLE " + TABLE_NAME;
    final static String CREATE = "CREATE TABLE " + TABLE_NAME + "( `_id` INTEGER PRIMARY KEY AUTOINCREMENT, `title` TEXT NOT NULL, `director` TEXT NOT NULL, `genre` TEXT NOT NULL, `year` INTEGER NOT NULL, `duration` INTEGER NOT NULL )";
    // при изменении структуры БД меняется и версия
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // выполняется, если базы данных нет
        //db.execSQL(DROP);
        db.execSQL(CREATE);
        db.execSQL("INSERT INTO filmlist VALUES (1, 'Birdman', 'Алехандро Гонсалес', 'Трагикомедия', 2014, 120)");
        db.execSQL("INSERT INTO filmlist VALUES (2, 'Reservoir dogs', 'Квентин Тарантино', 'Триллер', 1992, 100)");
        db.execSQL("INSERT INTO filmlist VALUES (3, 'The Big lebowski', 'Итан Коэн, Джоэл Коэн', 'Комедия', 1998, 119)");
        db.execSQL("INSERT INTO filmlist VALUES (4, 'Titanic', 'James Cameron', 'Катастрофа', 1997, 195)");
        //db.execSQL("INSERT INTO filmlist (title, director, genre, year) values ('Titanic', 'James Cameron', 'Забыл', 2002)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // выполняется, если версия базы данных отличается
        db.execSQL("DROP DATABASE " + DB_NAME);
        this.onCreate(db);
    }
}
