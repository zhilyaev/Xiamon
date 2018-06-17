package diamon.xiamon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.IOException;

public class About extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        Get test = new Get();

        test.run("http://95.216.144.125:8082/api/country/643",
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //todo work with response, parse and etc...
                        System.out.println(response);
                        secret = response.body().string();
                    }
                });
    }
    public class Get {
        OkHttpClient client = new OkHttpClient();

        public void run(String url, Callback callback) {

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(callback);
        }
    }

    private int click = 0;
    private String secret;



    public void egg(View v) {
        click++;

        if (click == 5) {
            ImageView dog = (ImageView) findViewById(R.id.dog);
            dog.setVisibility(View.VISIBLE);
        } else if (click > 5) {
            Toast.makeText(this, secret, Toast.LENGTH_SHORT).show();
        }
        else if(click<5){
            Toast.makeText(this, "НЕ нажимай на меня!", Toast.LENGTH_SHORT).show();
        }
    }

}
