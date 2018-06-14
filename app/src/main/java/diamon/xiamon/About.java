package diamon.xiamon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class About extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

    }

    private int click = 0;
    private String secret;

    public void egg(View v) {
        click++;

        if (click == 5) {
            ImageView dog = (ImageView) findViewById(R.id.dog);
            dog.setVisibility(View.VISIBLE);
        } else if (click > 5) {
            Toast.makeText(this, "Все хорош уже, ты что не из "+secret, Toast.LENGTH_SHORT).show();
        }
        else if(click<5){
            Toast.makeText(this, "НЕ нажимай на меня!", Toast.LENGTH_SHORT).show();
        }
    }

}
