package diamon.xiamon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class Records extends Activity {
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Record[] rs;
    ListView lv;
    protected class Record {
        public int id;
        public int score;
        public String name;
        public long timestamp;
    }

    protected void chooseDialog(final int pos) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(Records.this);
        builder.setTitle("What do u want?");

        // add a list
        final String[] animals = {"Edit", "Delete"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Record r = rs[pos];
                int id = r.id;
                switch (which) {
                    case 0: // Edit
                        showInputDialog(id);
                        break;
                    case 1: // Delete
                        db.delete("records", "id = ?", new String[] {String.valueOf(id)});
                        break;
                }
                fillListView(lv);
            }
        });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void showInputDialog(final int rID) {
        LayoutInflater layoutInflater = LayoutInflater.from(Records.this);
        View promptView = layoutInflater.inflate(R.layout.alert_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Records.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ContentValues cv = new ContentValues();
                        cv.put("name", String.valueOf(editText.getText()));
                        db.update("records", cv, "id = ?", new String[]{String.valueOf(rID)});
                        fillListView(lv);
                    }
                });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        lv = (ListView) findViewById(R.id.listview);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                // TODO Auto-generated method stub
                chooseDialog(pos);
                return true;
            }
        });
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        fillListView(lv);
    }

    private void fillListView (ListView lv){
        Cursor crs = db.rawQuery("select * from records ORDER BY score DESC", null);

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
