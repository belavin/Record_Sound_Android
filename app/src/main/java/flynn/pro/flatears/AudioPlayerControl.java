package flynn.pro.flatears;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.MediaController;



public class AudioPlayerControl     implements MediaController.MediaPlayerControl
{
    private static final String TAG = "AUPLYRCNTRL";

    private MediaPlayer player = null;
    String path = null;

    public AudioPlayerControl(String path, CallPlayer listenerActivity)
            throws java.io.IOException
    {
        L.i(TAG, "AudioPlayerControl constructed with path " + path);
        this.path = path;

        player = new MediaPlayer();
        player.setDataSource(path);

        player.setOnPreparedListener(listenerActivity);
        player.setOnInfoListener(listenerActivity);
        player.setOnErrorListener(listenerActivity);
        player.setOnCompletionListener(listenerActivity);
        /*
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    Log.i(TAG, "AudioPlayerControl onCompletion called");
                    player.reset();
                }
            });
        */
        player.prepareAsync();
    }

    //
    // MediaController.MediaPlayerControl implementation
    //
    public boolean canPause() { return true; }
    public boolean canSeekBackward() { return true; }
    public boolean canSeekForward() { return true; }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public int getBufferPercentage() {
        L.d(TAG, "AudioPlayerControl::getBufferPercentage returning 100");
        return 100;
    }

    public int getCurrentPosition() {
        int pos = player.getCurrentPosition();
        L.d(TAG, "AudioPlayerControl::getCurrentPosition returning " + pos);
        return pos;
    }

    public int getDuration() {
        int duration = player.getDuration();
        L.d(TAG, "AudioPlayerControl::getDuration returning " + duration);
        return duration;
    }

    public boolean isPlaying() {
        boolean isp = player.isPlaying();
        L.d(TAG, "AudioPlayerControl::isPlaying returning " + isp);
        return isp;
    }

    public void pause() {
        L.d(TAG, "AudioPlayerControl::pause");
        player.pause();
    }

    public void seekTo(int pos) {
        L.d(TAG, "AudioPlayerControl::seekTo " + pos);
        player.seekTo(pos);
    }

    public void start() {
        L.d(TAG, "AudioPlayerControl::start");
        player.start();
    }

    public void destroy() {
        L.i(TAG, "AudioPlayerControll::destroy shutting down player");
        if (player != null) {
            player.reset();
            player.release();
            player = null;
        }
    }
}
