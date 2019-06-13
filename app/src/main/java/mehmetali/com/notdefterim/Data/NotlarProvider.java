package mehmetali.com.notdefterim.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NotlarProvider extends ContentProvider {


    //Database ve tablolar ile ilgili kısım
    private static final String DATABASE_NAME = "notlar.db";
    private static final int DATABASE_VERSION = 1;
    private static final String NOTLAR_TABLE_NAME = "notlar";
    private static final String CREATE_NOTLAR_TABLE = "CREATE TABLE " + NOTLAR_TABLE_NAME
            + "(id INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + "noticerik TEXT NOT NULL,"
            + "notEklenmeTarih INTEGER,"
            + "notTarih INTEGER,"
            + "tamamlandi INTEGER DEFAULT 0);";
    //

    //ContentProvider Kısmı
    private static final String CONTENT_AUTHORITY = "mehmetali.com.notdefterim.notlarprovider";
    private static final String PATH_NOTLAR = "notlar";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTLAR);
    private static final UriMatcher matcher;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(CONTENT_AUTHORITY, PATH_NOTLAR, 1);
    }

    //
    SQLiteDatabase database;
    DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext(), DATABASE_NAME, null, 1);
        database = databaseHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        switch (matcher.match(uri)) {

            case 1:
                cursor = database.query(NOTLAR_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                return cursor;
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (matcher.match(uri)) {

            case 1:
                Long insertedRowId = database.insert(NOTLAR_TABLE_NAME, null, values);
                if (insertedRowId > 0) {

                    return ContentUris.withAppendedId(uri, insertedRowId);
                }
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int effectedRows = 0;
        switch (matcher.match(uri)) {

            case 1:
                effectedRows = database.delete(NOTLAR_TABLE_NAME, selection, selectionArgs);
                break;
        }

        return effectedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int effectedRows = 0;
        switch (matcher.match(uri)) {

            case 1:
                effectedRows = database.update(NOTLAR_TABLE_NAME, values, selection, selectionArgs);
                break;
        }

        return effectedRows;

    }

    private class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_NOTLAR_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + NOTLAR_TABLE_NAME);
            onCreate(db);
        }
    }
}
