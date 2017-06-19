package flynn.pro.flatears;



import android.graphics.Typeface;
import android.media.MediaRecorder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public final class L {

    public static String LOG_TAG = MainActivity.TAG;

    public static Map<String, Integer> mapLevelColors;
    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("VERBOSE", R.color.Gray_Goose);
        map.put("DEBUG", R.color.Green_Peas);
        map.put("INFO", R.color.Blue_Ivy);
        map.put("WARNING", R.color.Corn_Yellow);
        map.put("ERROR", R.color.Lava_Red);
        mapLevelColors = Collections.unmodifiableMap(map);
    }

    public static Map<String, Integer> mapClassColors;
    static {
        Map<String, Integer> map = new HashMap<>();
        map.put(MainActivity.TAG, R.color.Shamrock_Green);
        map.put(DatabaseHelper.TAG, R.color.Blue_Ivy);
        map.put(P.TAG, R.color.Granite);
        map.put(NetworkUtil.TAG, R.color.Earth_Blue);
        map.put(CallBroadcastReceiver.TAG, R.color.Cranberry);
        map.put(RecordService.TAG, R.color.Lava_Red);
        map.put(PhoneListener.TAG, R.color.Bee_Yellow);
        map.put(FTPUploader.TAG, R.color.Green_Peas);
        map.put(TelnetSpeakerTask.TAG, R.color.Teal);
        map.put(Transmitter.TAG, R.color.gray);
        mapClassColors = Collections.unmodifiableMap(map);
    }

    public static Map<Integer, String> audioSources;
    static {
        Map<Integer, String> map = new HashMap<>();
        map.put(MediaRecorder.AudioSource.DEFAULT, "MIC_");
        map.put(MediaRecorder.AudioSource.MIC, "MIC");
        map.put(MediaRecorder.AudioSource.VOICE_CALL, "LINE");
        map.put(MediaRecorder.AudioSource.VOICE_DOWNLINK, "VOICE_DOWNLINK");
        map.put(MediaRecorder.AudioSource.VOICE_UPLINK, "VOICE_UPLINK");
        map.put(MediaRecorder.AudioSource.VOICE_COMMUNICATION, "VOICE_UPLINK");

        audioSources = Collections.unmodifiableMap(map);
    }



    public static void e(String message, Throwable cause) {
        Log.e(LOG_TAG, "[" + message + "]", cause);
    }

    /**
     *@see #e(String, Throwable)
     */
    public static void e(String msg) {
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();

        String callerClassName = elements[1].getFileName();
        Log.e(LOG_TAG, "[" + callerClassName + "] " + msg);
    }


    public static void w(String message, Throwable cause) {
        Log.w(LOG_TAG, "[" + message + "]", cause);
    }


    public static void w(String msg) {
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();

        String callerClassName = elements[1].getFileName();
        Log.w(LOG_TAG, "[" + callerClassName + "] " + msg);
    }


    public static void i(String message, Throwable cause) {
        int i = Log.i(LOG_TAG, "[" + message + "]", cause);
    }


    public static void i(String msg) {
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();

        String callerClassName = elements[1].getFileName();
        Log.i(LOG_TAG, "[" + callerClassName + "] " + msg);
    }


    public static void d(String msg, Throwable cause) {
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, msg, cause);
        }
    }


    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();

            String callerClassName = elements[1].getFileName();
            L.d(LOG_TAG, "[" + callerClassName + "] " + msg);
        }
    }


    public static void v(String msg, Throwable cause) {
        if (BuildConfig.DEBUG) {
            Log.v(LOG_TAG, msg, cause);
        }
    }


    public static void v(String msg) {
        if (BuildConfig.DEBUG) {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();

            String callerClassName = elements[1].getFileName();
            Log.v(LOG_TAG, "[" + callerClassName + "] " + msg);
        }
    }



    public static void v(String TAG, String msg) {
        Log.v(TAG, msg);
        //final DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.getContext());
        //db.log(getTime(), TAG, msg);
        //writeLog(TAG, "VRB", msg);
        showLog(TAG, "VERBOSE", msg);
    }


    public static void d(String TAG, String msg) {
        Log.d(TAG, msg);
        //final DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.getContext());
        //db.log(getTime(), TAG, msg);
        //writeLog(TAG, "DBG", msg);
        showLog(TAG, "DEBUG", msg);
    }


    public static void i(String TAG, String msg) {

        try {
            Log.i(LOG_TAG, msg);
            final DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.getContext());
            db.log(getTime(), TAG, msg);
            writeLog(TAG, "INF", msg);
            showLog(TAG, "INFO", msg);
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public static void w(String TAG, String msg) {
        try {
            Log.w(LOG_TAG, msg);
            final DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.getContext());
            db.log(getTime(), TAG, msg);
            writeLog(TAG, "WRN", msg);
            showLog(TAG, "WARNING", msg);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void e(String TAG, String msg) {
        Log.e(LOG_TAG, msg);

        final DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.getContext());
        db.log(getTime(), TAG, msg);
        writeLog(TAG, "ERR", msg);
        showLog(TAG, "ERROR", msg);
    }


    private static void writeLog(String TAG, String LEVEL, String msg) {
        PrintWriter flog = null;
        try {
            flog = new PrintWriter(new BufferedWriter(new FileWriter(getLogFileName(), true)));
            flog.println("["+TAG+"] "+getTime()+" ["+LEVEL+"] "+msg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(flog != null){
                flog.close();
            }
        }
    }

    private static void showLog(String TAG, String LEVEL, final String msg){
        try {
            final TextView tvNum = (TextView) ((AppCompatActivity)MainActivity.getContext()).findViewById(R.id.log_view);
            String header = " :: [" + TAG + "] " + getTime() + " [" + LEVEL + "] :: " + "\r\n";
            int fgColor = ContextCompat.getColor(MainActivity.getContext(), mapLevelColors.get(LEVEL));
            System.out.println(TAG+"_________________");
            int bgColor = ContextCompat.getColor(MainActivity.getContext(), mapClassColors.get(TAG));

            final SpannableString spanedHeader = new SpannableString(header);
            spanedHeader.setSpan(new StyleSpan(Typeface.BOLD), 6, TAG.length()+8, 0);
            spanedHeader.setSpan(new StyleSpan(Typeface.BOLD), header.length()-LEVEL.length()-8, header.length() , 0);
            spanedHeader.setSpan(new TypefaceSpan("monospace"), 4, header.length()-6, 0);
            spanedHeader.setSpan(new ForegroundColorSpan(fgColor), header.length()-LEVEL.length()-8, header.length(), 0);
            spanedHeader.setSpan(new BackgroundColorSpan(bgColor), 0, 4, 0);

            final SpannableString spanedBody = new SpannableString("    " + msg + "\r\n");
            spanedBody.setSpan(new BackgroundColorSpan(bgColor), 0, 4, 0);

            ((AppCompatActivity)MainActivity.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (tvNum != null) tvNum.append(TextUtils.concat(spanedHeader, spanedBody));
                }
            });
        } catch (Exception e ){
            e.printStackTrace();
        }

    }

//    public static String splitString(String msg, int lineSize) {
//        String res = "";
//
//        Pattern p = Pattern.compile("\\b.{1," + (lineSize-1) + "}\\b\\W?");
//        Matcher m = p.matcher(msg);
//
//        while(m.find()) {
//            System.out.println(m.group().trim());
//            res += m.group() + "\r\n";
//            (Spanned) TextUtils.concat(res)
//        }
//        return res;
//    }

    public static String getTime() {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
    }

    public static String getDate() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static String getLogFileName () {
        return RecordService.FLATEARS_STORAGE_LOCATION+getSimID()+"-"+getDate()+".log";
        //return getSimID()+"-"+getDate()+".log";
    }

    public static String getSimID() {
        return "XXXX";
    }



}