package diamon.xiamon;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Records extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        ListView lv = (ListView) findViewById(R.id.listview);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                // TODO Auto-generated method stub

                Toast toast = Toast.makeText(getApplicationContext(),
                        "POS := "+pos, Toast.LENGTH_SHORT);
                toast.show();

                return true;
            }
        });

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
            String name = crs.getString(crs.getColumnIndex("name"));
            inlist[i] = name + " - " + udate + " : " +uscore;
            i++;
        }


        // используем адаптер данных
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1, inlist);
        lv.setAdapter(adapter);
    }
}
