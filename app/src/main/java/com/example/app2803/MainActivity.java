package com.example.app2803;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    DBHelper helper;
    TextView filmCounter;
    SQLiteDatabase db;
    ListView playlist;
    SimpleCursorAdapter adapter;
    EditText setFilm, setDirector, setGenre, setYear, setDuration, findFilm;
    int count;
    Cursor tunes;
    Cursor durationCount;
    Cursor onChangeTunes;
    String[] playlistFields;
    String findThisFilm = "";

    int fieldCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playlist = findViewById(R.id.playlist);
        setFilm = findViewById(R.id.setFilm);
        setDirector = findViewById(R.id.setDirector);
        setGenre = findViewById(R.id.setGenre);
        setYear = findViewById(R.id.setYear);
        setDuration = findViewById(R.id.setDuration);
        findFilm = findViewById(R.id.findFilm);

        findFilm.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                count = onChangeTunes.getCount();
                setFilmCounter(findThisFilm);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                findThisFilm = findFilm.getText().toString();
                Log.d("myTag", "onTextChanged: " + findThisFilm);
                query();

            }
        });

        filmCounter = findViewById(R.id.filmCounter);
        DBHelper helper = new DBHelper(this);
        db = helper.getWritableDatabase();


        tunes = db.rawQuery("SELECT * FROM filmlist", null);
        count = tunes.getCount();
        setFilmCounter(findThisFilm);


        playlistFields = tunes.getColumnNames();
        int[] views = {R.id.id, R.id.title, R.id.director, R.id.genre, R.id.year, R.id.duration};
        adapter = new SimpleCursorAdapter(this, R.layout.playlist, tunes, playlistFields, views, 0);
        playlist.setAdapter(adapter);
        adapter.changeCursor(tunes);
    }

    public void onClick(View view) {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(setFilm.getText().toString());
        list.add(setDirector.getText().toString());
        list.add(setGenre.getText().toString());
        list.add(setYear.getText());
        list.add(setDuration.getText());
        Object[] array = list.toArray();


        db.execSQL("INSERT INTO filmlist (title, director, genre, year, duration) values (?,?,?,?,?)", array);
        count = count + 1;
        setFilmCounter(findThisFilm);

        query();


    }

    public void clear(View view) {
        try {
            db.execSQL("DELETE FROM filmlist");
        } catch (SQLException e) {
        }

        adapter.changeCursor(db.rawQuery("SELECT * FROM filmlist ORDER BY title", null));
        count = 0;

        setFilmCounter(findThisFilm);
    }

    public void setFilmCounter(String findThisFilm) {

        durationCount = db.rawQuery("SELECT SUM(duration) FROM filmlist WHERE TITLE LIKE" + "'%" + findThisFilm + "%'", null);
        durationCount.moveToFirst();
        filmCounter.setText("Фильмов к просмотру:" + " " + Integer.toString(count) + ", " + Integer.toString(durationCount.getInt(0)) + " минут");
    }




    public void orderBy(View v) {
        switch (v.getId()) {
            case R.id.id:
                adapter.changeCursor(db.rawQuery("SELECT * FROM filmlist ORDER BY _id", null));
                break;
            case R.id.titlename:
                adapter.changeCursor(db.rawQuery("SELECT * FROM filmlist ORDER BY title", null));
                break;
            case R.id.directorname:
                adapter.changeCursor(db.rawQuery("SELECT * FROM filmlist ORDER BY director", null));
                break;
            case R.id.genrename:
                adapter.changeCursor(db.rawQuery("SELECT * FROM filmlist ORDER BY genre", null));
                break;
            case R.id.yearname:
                adapter.changeCursor(db.rawQuery("SELECT * FROM filmlist ORDER BY year", null));
                break;
            case R.id.durationname:
                adapter.changeCursor(db.rawQuery("SELECT * FROM filmlist ORDER BY duration", null));
                break;
        }
    }


    public void query() {
        onChangeTunes = db.rawQuery("SELECT * FROM filmlist WHERE TITLE LIKE " + "'%" + findThisFilm + "%'", null);
        adapter.changeCursor(onChangeTunes);
    }

}
