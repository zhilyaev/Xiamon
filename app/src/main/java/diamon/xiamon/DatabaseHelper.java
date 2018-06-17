package diamon.xiamon;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "mydb.db";
    private static final String DATABASE_TABLE = "records";

    private static final String DATABASE_CREATE_SCRIPT =
            "create table " + DATABASE_TABLE + " ( "
            + "id integer primary key autoincrement not null, "
            + "name varchar(50), "
            + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + "score long);";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);

    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper (Context ctx) {
        super(ctx, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF IT EXIST " + DATABASE_TABLE);
        // Создаём новую таблицу
        onCreate(db);
    }

    public static void deleteById (final String id, DatabaseHelper dh){
        dh.getWritableDatabase()
                .delete(DATABASE_TABLE, "id = ?", new String[] {id});
    }
}