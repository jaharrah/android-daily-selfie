package course.labs.dailyselfie;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DailySelfieDataSource {

    // Database fields
    private SQLiteDatabase database;
    private DailySelfieSQLiteHelper dbHelper;
    private String[] allColumns = { DailySelfieSQLiteHelper.COLUMN_ID,
            DailySelfieSQLiteHelper.COLUMN_NAME,
            DailySelfieSQLiteHelper.COLUMN_PATH };

    public DailySelfieDataSource(Context context) {
        dbHelper = new DailySelfieSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public DailySelfieItem createSelfie(String name, String path) {
        ContentValues values = new ContentValues();
        values.put(DailySelfieSQLiteHelper.COLUMN_NAME, name);
        values.put(DailySelfieSQLiteHelper.COLUMN_PATH, path);
        long insertId = database.insert(DailySelfieSQLiteHelper.TABLE_PICTURES, null,
                values);
        Cursor cursor = database.query(DailySelfieSQLiteHelper.TABLE_PICTURES,
                allColumns, DailySelfieSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DailySelfieItem newSelfie = cursorToSelfie(cursor);
        cursor.close();
        return newSelfie;
    }

    public void deleteSelfie(DailySelfieItem selfie) {
        long id = selfie.getId();
        System.out.println("Selfie deleted with id: " + id);
        database.delete(DailySelfieSQLiteHelper.TABLE_PICTURES, DailySelfieSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllSelfies() {
        System.out.println("Deleted all Selfies");
        database.delete(DailySelfieSQLiteHelper.TABLE_PICTURES, null, null);
    }

    public List<DailySelfieItem> getAllSelfies() {
        List<DailySelfieItem> selfies = new ArrayList<DailySelfieItem>();

        Cursor cursor = database.query(DailySelfieSQLiteHelper.TABLE_PICTURES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DailySelfieItem selfie = cursorToSelfie(cursor);
            selfies.add(selfie);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return selfies;
    }

    private DailySelfieItem cursorToSelfie(Cursor cursor) {
        DailySelfieItem comment = new DailySelfieItem();
        comment.setId(cursor.getLong(0));
        comment.setImageFileName(cursor.getString(1));
        comment.setFullPhotoPath(cursor.getString(2));
        return comment;
    }
}

