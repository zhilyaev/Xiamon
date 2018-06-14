package diamon.xiamon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import java.util.Random;

public class MainActivity extends Activity{

    private ImageView rad;
    private Button startgame;
    private TextView tvGameOver, tvScore;
    private Switch sw_north, sw_south, sw_west,sw_east;
    private boolean clockwised;
    private boolean isGameOver;
    private Random random;
    private Handler handler;
    private static int SCORE;
    private boolean isControllerShowed = false;
    private LinearLayout layoutController;
    Animation anim;

    private boolean PAUSED = false;
    private Thread rotating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniz();

        startgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, Sound.class));
                if(PAUSED){
                    PAUSED = false;
                }else{
                    clockwised=random.nextBoolean();
                    SCORE=0;
                }

                if(isGameOver){
                    tvGameOver.setVisibility(View.INVISIBLE);
                    tvScore.setVisibility(View.INVISIBLE);
                }
                rad.startAnimation(anim);
                isGameOver = false;
                rotating = breakThread();
                startgame.setEnabled(false);
                rotating.start();
            }
        });
    }

    private void iniz(){
        random = new Random();
        handler = new Handler();


        layoutController = (LinearLayout) findViewById(R.id.controller);
        anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rad);

        tvGameOver = (TextView) findViewById(R.id.GameOver);
        tvScore = (TextView) findViewById(R.id.SCORE);
        rad = (ImageView) findViewById(R.id.rad);
        startgame = (Button) findViewById(R.id.StartGame);

        sw_north = (Switch) findViewById(R.id.North);
        sw_north.setRotation(-90);
        sw_south = (Switch) findViewById(R.id.Sorth);
        sw_south.setRotation(90);
        sw_west = (Switch) findViewById(R.id.West);
        sw_west.setRotation(180);
        sw_east = (Switch) findViewById(R.id.East);


        /* Управление */
        RelativeLayout hide_layout = (RelativeLayout) findViewById(R.id.layout_main);
        hide_layout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                up();
            }
            public void onSwipeRight() {
                right();
            }
            public void onSwipeLeft() {
                left();
            }
            public void onSwipeBottom() {
                down();
            }

        });
    }

    private int getDegrees(){
        int res = (int) rad.getRotation() % 360;
        if(res<0){
            res*=-1;
            res = 360-res;
        }
        return res;
    }

    private Thread breakThread(){
       return new Thread(new Runnable() {
            @Override
            public void run()  {
                do{
                    SCORE++;
                    step();
                    // 1/400 * (1000/sleep) ~ 25% в сек
                    if(random.nextInt(400)==42) clockwised = !clockwised;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            startgame.setText(String.valueOf(SCORE));
                        }
                    });

                    if(random.nextInt(500)==42 && anim.hasEnded()){
                        rad.post(new Runnable() {
                            @Override
                            public void run() {
                                    rad.startAnimation(anim);
                            }
                        });

                    }

                    if     ((sw_north.isChecked() && (getDegrees()==0 || getDegrees()==360)) ||
                            (sw_east.isChecked() && getDegrees()==90) ||
                            (sw_south.isChecked() && getDegrees()==180) ||
                            (sw_west.isChecked() && getDegrees()==270)){
                                handler.post(new Runnable() {
                            @Override
                            public void run() {
                                GameOver();
                            }
                        });

                        break;
                    }

                    final int d = SCORE / 1000;
                    try {Thread.sleep((10-d<5) ? 5 : 10-d);}
                    catch (InterruptedException e) {e.printStackTrace();}
                }
                while(!PAUSED);
            }
        });
    }

    private void GameOver(){
        vibr();
        startgame.setText("Let`s try again?");
        startgame.setEnabled(true);
        tvGameOver.setVisibility(View.VISIBLE);
        tvScore.setVisibility(View.VISIBLE);
        tvScore.setText("$> "+String.valueOf(SCORE)+" <$");
        showInputDialog();
        isGameOver = true;
    }

    private void vibr() {
        Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibr.vibrate(500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, Sound.class));
        if(rotating!=null){
            if(rotating.isAlive()){
                PAUSED = true;
                try {
                    rotating.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PAUSED){
            startgame.setText("Continue");
            startgame.setEnabled(true);
        }
    }

    void up(){
        if(!PAUSED){
            sw_north.setChecked(true);
            sw_south.setChecked(false);
        }
    }

    public void up(View v){
        up();
    }

    void down(){
        if(!PAUSED){
            sw_north.setChecked(false);
            sw_south.setChecked(true);
        }
    }

    public void down(View v){
        down();
    }

    void left(){
        if(!PAUSED){
            sw_east.setChecked(false);
            sw_west.setChecked(true);
        }
    }

    public void left(View v){
        left();
    }

    void right(){
        if(!PAUSED){
            sw_east.setChecked(true);
            sw_west.setChecked(false);
        }
    }

    public void right(View v){
        right();
    }

    private void step() {
        if(clockwised)rad.setRotation(rad.getRotation()+1);
        else  rad.setRotation(rad.getRotation()-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(1).setChecked(isControllerShowed);
        if(isControllerShowed)layoutController.setVisibility(View.VISIBLE);
        else layoutController.setVisibility(View.INVISIBLE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_about:
                intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
                return true;
            case R.id.action_records:
                intent = new Intent(MainActivity.this, Records.class);
                startActivity(intent);
                return true;
            case R.id.action_controller:
                isControllerShowed=!isControllerShowed;
                item.setChecked(isControllerShowed);
                if(isControllerShowed)layoutController.setVisibility(View.VISIBLE);
                else layoutController.setVisibility(View.INVISIBLE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void showInputDialog() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.alert_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(), "mydb.db", null, 1);
                        SQLiteDatabase sdb = dbHelper.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put("name", String.valueOf(editText.getText()));
                        cv.put("score", SCORE);
                        sdb.insert("records", null, cv);
                    }
                });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
