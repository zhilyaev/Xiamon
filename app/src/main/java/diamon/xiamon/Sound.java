package diamon.xiamon;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class Sound extends Service {
    private MediaPlayer player;

    private static volatile Sound instance;

    public static Sound getInstance() {
        Sound localInstance = instance;
        if (localInstance == null) {
            synchronized (Sound.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Sound();
                }
            }
        }
        return localInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.strange);
        player.setLooping(true);
        player.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
