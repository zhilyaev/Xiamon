package diamon.xiamon;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Records extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        ListView lv = (ListView) findViewById(R.id.listview);

        DatabaseHelper dbHelper = new DatabaseHelper(this, "mydb.db", null, 1);
        SQLiteDatabase sdb = dbHelper.getWritableDatabase();
        String[] cols = new String[] {"score", "timestamp"};
        Cursor crs = dbHelper.getReadableDatabase().rawQuery("select * from records ORDER BY score DESC", null);
        // лучше прямой запрос!
        String[] inlist = new String[crs.getCount()];

        //crs.moveToFirst();// Извращенья
        int i = 0;
        while(crs.moveToNext()){
            String uscore = crs.getString(crs.getColumnIndex("score"));
            String udate = crs.getString(crs.getColumnIndex("timestamp"));
            inlist[i] = udate + " : " +uscore;
            i++;
        }


        // используем адаптер данных
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1, inlist);
        lv.setAdapter(adapter);
    }
}
