package flynn.pro.flatears;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

/**
 * Play mediafile by Mediaplayer
 */
public class PlayService     extends Service
        implements MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener
{
    private static String TAG = "PLAYSRVC";

    public static final String EXTRA_FILENAME = "filename";

    private MediaPlayer player = null;
    private boolean isPlaying = false;

    public void onCreate()
    {
        super.onCreate();
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnInfoListener(this);
        player.setOnErrorListener(this);
        L.i(TAG, "MediaPlayer создан");
    }

    public void onStart(Intent intent, int startId) {
        L.i(TAG, "Запуск. Уже воспроизводится: " + isPlaying);

        if (isPlaying) return;

        String recording = intent.getStringExtra(EXTRA_FILENAME);

        if (recording == null) {
            L.w(TAG, "Запущен с пустым параметром");
            return;
        }

        L.i(TAG, "Будет воспроизведена запись " + recording);
        try {
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
            player.setDataSource(recording);
            player.setLooping(false);
            player.prepare();
            L.d(TAG, "MediaPlayer подготовлен");
            player.start();
            isPlaying = true;
            L.i(TAG, "MediaPlayer запущен");
            //updateNotification(true);
        } catch (java.io.IOException e) {
            L.e(TAG, "Ошибка ввода вывода на стадии подготовки: " + e);
        } catch (java.lang.Exception e) {
            L.e(TAG, "Ошибка при попытке запуска" + e);
        }

    }

    public void onDestroy()
    {
        if (null != player) {
            L.i(TAG, "PlayService::onDestroy calling player.release()");
            isPlaying = false;
            player.release();
        }

        //updateNotification(false);
        super.onDestroy();
    }


    // methods to handle binding the service

    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public boolean onUnbind(Intent intent)
    {
        return false;
    }

    public void onRebind(Intent intent)
    {
    }

    /*
    private void updateNotification(Boolean status)
    {
        Context c = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

        if (status) {
            int icon = R.drawable.rec;
            CharSequence tickerText = "Recording call from channel " + prefs.getString(P.PREF_AUDIO_SOURCE, "1");
            long when = System.currentTimeMillis();

            Notification notification = new Notification(icon, tickerText, when);

            Context context = getApplicationContext();
            CharSequence contentTitle = "CallRecorder Status";
            CharSequence contentText = "Recording call from channel...";
            Intent notificationIntent = new Intent(this, PlayService.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
            mNotificationManager.notify(RECORDING_NOTIFICATION_ID, notification);
        } else {
            mNotificationManager.cancel(RECORDING_NOTIFICATION_ID);
        }
    }
    */

    // MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mp)
    {
        L.i(TAG, "PlayService got MediaPlayer onCompletion callback");
        isPlaying = false;
    }

    // MediaPlayer.OnInfoListener
    public boolean onInfo(MediaPlayer mp, int what, int extra)
    {
        L.i(TAG, "PlayService got MediaPlayer onInfo callback with what: " + what + " extra: " + extra);
        return false;
    }

    // MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mp, int what, int extra)
    {
        L.e(TAG, "PlayService got MediaPlayer onError callback with what: " + what + " extra: " + extra);
        isPlaying = false;
        mp.reset();
        return true;
    }
}