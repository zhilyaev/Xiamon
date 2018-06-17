package diamon.xiamon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Records extends Activity {
    Record[] rs;
    protected class Record {
        public int id;
        public int score;
        public String name;
        public long timestamp;
    }

    protected void chooseDialog(int pos) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(Records.this);
        builder.setTitle("Choose an animal");

        // add a list
        final String[] animals = {"Edit", "Delete"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Edit

                    case 1: // Delete

                }
            }
        });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        ListView lv = (ListView) findViewById(R.id.listview);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                // TODO Auto-generated method stub
                chooseDialog(pos);
                return true;
            }
        });
        DatabaseHelper dbHelper = new DatabaseHelper(this, "mydb.db", null, 1);
        Cursor crs = dbHelper.getReadableDatabase().rawQuery("select * from records ORDER BY score DESC", null);

        rs = new Record[crs.getCount()];
        String[] inlist = new String[crs.getCount()];

        for (int i = 0; crs.moveToNext() ; i++){
            Record r = new Record();
            r.id = crs.getInt(crs.getColumnIndex("id"));
            r.score = crs.getInt(crs.getColumnIndex("score"));
            r.name = crs.getString(crs.getColumnIndex("name"));
            r.timestamp = crs.getLong(crs.getColumnIndex("timestamp"));
            rs[i] = r;

            String uscore = crs.getString(crs.getColumnIndex("score"));
            String udate = crs.getString(crs.getColumnIndex("timestamp"));
            String name = crs.getString(crs.getColumnIndex("name"));
            inlist[i] = name + " - " + udate + " : " +uscore;
        }



        // используем адаптер данных
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1, inlist);
        lv.setAdapter(adapter);
    }
}
