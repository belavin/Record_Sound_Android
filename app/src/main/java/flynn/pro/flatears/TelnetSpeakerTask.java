package flynn.pro.flatears;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * intercommunicate with Telnet Server
 */

public class TelnetSpeakerTask extends AsyncTask<Void, Void, Void> {

    int dstPort = 1510;
    String response;
    String filename;
    public static String TAG = "TLNTSPKR";

    TelnetSpeakerTask(String name){
        filename = name;
    }

    @Override
    protected Void doInBackground(Void... params) {

        final DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.getContext());
        ContentValues cv = db.getValues(filename);
        String serverip = P.getInstance().getParam(P.SERVER_IP);

        Socket socket = null;
        PrintWriter out;
        boolean isanswer = false, isauthen = false, isauthor = false;

        try {
            socket = new Socket(serverip, dstPort);
            out = new PrintWriter(socket.getOutputStream(), true);

            InputStream in = socket.getInputStream();
            StringBuilder sb=new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String read;

            read=br.readLine();
            sb.append(read);
            response = sb.toString();
            L.d(TAG, "HELLO RESPONSE> "+response);
            sb.setLength(0);

            if (response.contains("Welcome")) {
                isanswer = true;
            }
            else L.d(TAG, "NO ANSWER");

            if (isanswer) {
                out.println("tokenauth: 34346HnKms*9);L\r\n");
                L.d(TAG, "AUTH SEND>");

                read=br.readLine();
                sb.append(read);
                response = sb.toString();
                L.d(TAG,"AUTH RESPONSE> "+response);
                sb.setLength(0);

                if (response.contains("200: OK")) {
                    isauthen = true;
                }
                else L.d(TAG,"AUTH FAIL");

                if (isauthen) {
                    out.println("simid:"+cv.get(DatabaseHelper.KEY_ANUM)+"\r\n");
                    L.d(TAG,"ID SEND> "+cv.get(DatabaseHelper.KEY_ANUM));

                    read=br.readLine();
                    sb.append(read);
                    response = sb.toString();
                    L.d(TAG,"ID RESPONSE> "+response);
                    sb.setLength(0);

                    if (response.contains("200: OK")) {
                        isauthor = true;
                    }

                    if (isauthor) {

                        String time = (""+cv.get(DatabaseHelper.KEY_TIME)).replace(":", ".");
                        SimpleDateFormat sdf_norm = new SimpleDateFormat("dd.MM.yyyy");
                        SimpleDateFormat sdf_flat = new SimpleDateFormat("yyyy-MM-dd");
                        String durationInSec = ""+cv.get(DatabaseHelper.KEY_DURATION);
                        long millis = Integer.parseInt(durationInSec)*1000;
                        final long hr = TimeUnit.MILLISECONDS.toHours(millis);
                        final long min = TimeUnit.MILLISECONDS.toMinutes(millis - TimeUnit.HOURS.toMillis(hr));
                        final long sec = TimeUnit.MILLISECONDS.toSeconds(millis - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
                        @SuppressLint("DefaultLocale") String timeFormated = String.format("%02d.%02d.%02d", hr, min, sec);
                        String dateFormated = sdf_flat.format(sdf_norm.parse(""+cv.get(DatabaseHelper.KEY_DATE)));

                        String insertRecord = "insertrecord:"+
                                cv.get(DatabaseHelper.KEY_CALLTYPE)+":"+
                                dateFormated+":"+ time+":"+
                                timeFormated+":"+
                                cv.get(DatabaseHelper.KEY_LINK)+":"+
                                cv.get(DatabaseHelper.KEY_BNUM);
                        out.println(insertRecord +"\r\n");
                        L.d(TAG, "INSERT > " + insertRecord);
                        //out.println("insertrecord:outgoing:21.05.16:13.14:0:filename.example:89123456789\r\n");

                        read=br.readLine();
                        sb.append(read);
                        response = sb.toString();
                        L.d(TAG, "INSERT RESPONSE > "+response);

                        if (response.contains("200: OK")) {
                            db.setState(""+cv.get(DatabaseHelper.KEY_LINK),"IDENTIFIED");
                        } else {
                            db.setState(""+cv.get(DatabaseHelper.KEY_LINK),"METAFAILED");
                        }
                    }
                    else {
                        L.i(TAG,"AUTHORIZATION FAIL. НЕОБХОДИМО ДОБАВИТЬ ПОЛЬЗОВАТЕЛЯ");
                        db.setState(""+cv.get(DatabaseHelper.KEY_LINK),"AUTHFAIL");
                        P.getInstance().setState("AUTHORIZED", false);
                    }
                }
            }
        } catch (ConnectException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            response = "ConnectException: " + e;
            db.setState(""+cv.get(DatabaseHelper.KEY_LINK),"TELNETFAIL");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            L.e(TAG, "IOEXception " + e);
            e.printStackTrace();
            response = "IOException: " + e.toString();
            db.setState(""+cv.get(DatabaseHelper.KEY_LINK),"TELNETFAIL");
        } catch (ParseException e) {
            e.printStackTrace();
            db.setState(""+cv.get(DatabaseHelper.KEY_LINK),"TELNETFAIL");
        } finally {
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
