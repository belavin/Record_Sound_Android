package flynn.pro.flatears;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;


public class CallPlayer extends AppCompatActivity
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    public static final String DEFAULT_STORAGE_LOCATION = Environment.getExternalStorageDirectory().getPath()+"/FLATEARS/";
    public static final String BASE_PATH = "content://flynn.pro.flatears/records";
    private static final String TAG = "CALLPLYR";
    private AudioPlayerControl aplayer = null;
    private MediaController controller = null;
    private ViewGroup anchor = null;
    private MediaPlayer mediaPlayer = null ;

    private Handler handler = new Handler();


    @Override
    @TargetApi(24)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        String name = i.getData().getEncodedPath();
        String path = RecordService.FLATEARS_STORAGE_LOCATION + name;

        setContentView(R.layout.player_activity);
        anchor = (ViewGroup) findViewById(R.id.playerlayout);
//
        TextView tvNum = (TextView) findViewById(R.id.txtNum);
        TextView tvDat = (TextView) findViewById(R.id.txtDate);
        TextView tvSrc = (TextView) findViewById(R.id.txtSource);
        TextView tvFrm = (TextView) findViewById(R.id.txtFormat);
        TextView tvDur = (TextView) findViewById(R.id.txtDetails);
        TextView tvDrc = (TextView) findViewById(R.id.txtDirect);
//
        String where = DatabaseHelper.KEY_LINK + " = '" + name + "'";
        Cursor mCursor = getApplicationContext().getContentResolver().query(Uri.parse(BASE_PATH), null, where, null, null);

        String cDat="", cNum="", cDur = "", cLT = "", cTyp="", cSrc="", cFrm="";

        if (null == mCursor) {

            // If the Cursor is empty, the provider found no matches
        } else if (mCursor.getCount() < 1) {


        } else {
            mCursor.moveToLast();
            cDat = mCursor.getString(2)+"  "+mCursor.getString(3);
            cNum = mCursor.getString(6);
            cDur = mCursor.getString(7);
            cLT = mCursor.getString(1);
            cTyp = mCursor.getString(4);
            cSrc = mCursor.getString(8);
            cFrm = mCursor.getString(9);
        }

        if (cLT != "") {
            SimpleDateFormat sdf_date = new SimpleDateFormat("E, dd MMM HH:mm");
            Date nDate = new Date(Long.parseLong(cLT));
            cDat = sdf_date.format(nDate);
        }

        if (Objects.equals(cNum, "")) {cNum = " неизвестный";}
        if (Objects.equals(cTyp, "")) {cTyp = "   unknown"; cDur = ""+(Integer.parseInt("0"+cDur)/1000);}

        //String s= cNum+" "+cTyp+" \n"+cDat+"   ( "+cDur+" сек. )";
        //SpannableString ss1=  new SpannableString(s);
        //ss1.setSpan(new RelativeSizeSpan(1.2f), 0, 12, 0); // set size
        // set color
        if (cTyp == null) {cTyp ="";}
        switch (cTyp) {
            case "OUTGOING":
                cTyp = "ИСХОДЯЩИЙ";
                //ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Forest_Green)), 0, 12, 0);
                break;
            case "INCOMING":
                cTyp = "ВХОДЯЩИЙ";
                //ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Blue_Ribbon)), 0, 12, 0);
                break;
            case "MISSED":
                cTyp = "ПРОПУЩЕННЫЙ";
                //ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Love_Red)), 0, 12, 0);
                break;
            default:
                cTyp = "НЕОПРЕДЕЛЕННЫЙ";
                //ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Steel_Blue)), 0, 12, 0);
                break;
        }

        tvDrc.setText(cTyp);
        tvNum.setText(cNum);
        tvDat.setText(cDat);
        tvSrc.setText("Источник : " + cSrc);
        tvFrm.setText("Формат : " + cFrm);
        tvDur.setText("Продолжительность: " + cDur + "c");
//
        if (aplayer != null) {
            L.i(TAG, "CallPlayer onCreate called with aplayer already allocated, destroying old one.");
            aplayer.destroy();
            aplayer = null;
        }
        if (controller != null) {
            L.i(TAG, "CallPlayer onCreate called with controller already allocated, destroying old one.");
            controller = null;
        }
//
        L.i(TAG, "CallPlayer onCreate with data: " + path);
        try {
            aplayer = new AudioPlayerControl(path, this);
//

        } catch (java.io.IOException e) {
            L.e(TAG, "CallPlayer onCreate failed while creating AudioPlayerControl");
            Toast t = Toast.makeText(this, "CallPlayer received error attempting to create AudioPlayerControl: " + e, Toast.LENGTH_LONG);
            t.show();
            finish();
        }
    }

    public void onDestroy() {
        L.i(TAG, "CallPlayer onDestroy");
        if (aplayer != null) {
            aplayer.destroy();
            aplayer = null;
        }


        if (aplayer != null)

            super.onDestroy();
        mediaPlayer.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        controller.hide();
        try {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
        } catch (Exception e){e.printStackTrace();}
    }

    private class MyMediaController extends MediaController {
        public MyMediaController(Context c, boolean ff) {
            super(c, ff);
        }


        //==============================================================================================



        //        @Override
        public void start() {
            mediaPlayer.start();
        }

        //        @Override
        public void pause() {
            mediaPlayer.pause();
        }

        //        @Override
        public int getDuration() {
            return mediaPlayer.getDuration();
        }

        //        @Override
        public int getCurrentPosition() {
            return mediaPlayer.getCurrentPosition();
        }

        //        @Override
        public void seekTo(int pos) {
            mediaPlayer.seekTo(pos);
        }

        //        @Override
        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        //        @Override
        public int getBufferPercentage() {
            return 0;
        }

        //        @Override
        public boolean canPause() {
            return true;
        }

        //        @Override
        public boolean canSeekBackward() {
            return true;
        }

        //        @Override
        public boolean canSeekForward() {
            return true;
        }

        //        @Override
        public int getAudioSessionId() {
            return 0;
        }


    }



    public void onPrepared(MediaPlayer mp) {
        L.i(TAG, "CallPlayer onPrepared about to construct MediaController object");
        controller = new MediaController(this); // enble fast forward
        controller.setMediaPlayer(aplayer);
        controller.setAnchorView(anchor);

        handler.post(new Runnable() {
            public void run() {
                controller.setEnabled(true);
                controller.show(5000);
            }
        });
    }

    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        try {
            L.i(TAG, "CallPlayer onInfo with what " + what + " extra " + extra);
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        try {
            L.e(TAG, "CallPlayer onError with what " + what + " extra " + extra);
            Toast t = Toast.makeText(this, "CallPlayer received error (what:" + what + " extra:" + extra + ") from MediaPlayer attempting to play ", Toast.LENGTH_LONG);
            t.show();
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public void onCompletion(MediaPlayer mp) {



        try {
            L.e(TAG, "CallPlayer onCompletion, finishing activity");
//            mediaPlayer.stop();
//            finish();

            mp.reset();
            controller.hide();
            controller.setEnabled(false);

        }  catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        try {
            L.v(TAG, "+++ onBackPressed start +++");
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                // write your code here
                L.i(TAG, "CallPlayer onDestroy");
                if (aplayer != null) {
                    aplayer.destroy();
                    aplayer = null;
                }
                super.onDestroy();
            }
            return true;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return true;
    }



    @Override
    public void onBackPressed() {
        try {
            L.v(TAG, "=== onBackPressed start ===");
            super.onDestroy();
            L.v(TAG, "=== onBackPressed end ===");
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }



}
