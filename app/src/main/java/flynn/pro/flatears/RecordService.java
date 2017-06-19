package flynn.pro.flatears;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.nio.MappedByteBuffer;
//import ua.mobius.media.server.impl.dsp.audio.g711.alaw.Encoder;
//import org.mobicents.media.codecs
//import ua.mobius.media.server.impl.dsp.audio.g711.alaw.EncoderData;


public class RecordService 
    extends Service
    implements MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener
{
    public static final String TAG = "RECRDSRV";
    private short[] mPCMBuffer;



    public final static String ACTION_NAME = "action_type";
    public final static int ACTION_INVALID = 0;
    public final static int ACTION_START_RECORDING = 1;
    public final static int ACTION_STOP_RECORDING = 2;
    public final static int ACTION_ENABLE_MONITOR_REMAIN_TIME = 3;
    public final static int ACTION_DISABLE_MONITOR_REMAIN_TIME = 4;
    public final static String ACTION_PARAM_FORMAT = "format";
    public final static String ACTION_PARAM_PATH = "path";
    public final static String ACTION_PARAM_HIGH_QUALITY = "high_quality";
    public final static String ACTION_PARAM_MAX_FILE_SIZE = "max_file_size";

    private static MediaRecorder mRecorder = null;
    private final Handler mHandler = new Handler();

    private KeyguardManager mKeyguardManager;
    private Notification mLowStorageNotification;
    private static final int FRAME_COUNT = 160;

    public static final String DEFAULT_STORAGE_LOCATION = Environment.getExternalStorageDirectory().getPath();
    public static final String FLATEARS_FOLDER_RELATIVE = "/FLATEARS/";
    public static final String FLATEARS_STORAGE_LOCATION = DEFAULT_STORAGE_LOCATION + FLATEARS_FOLDER_RELATIVE;

    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static final int RECORDER_BPP = 8;
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private final static int cClip = 32635;
    private static byte aLawCompressTable[] = new byte[]{
            1, 1, 2, 2, 3, 3, 3, 3,
            4, 4, 4, 4, 4, 4, 4, 4,
            5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5,
            6, 6, 6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6, 6, 6,
            7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7
    };
    private static final int[] ALAW_DECODE = {-5504, -5248, -6016, -5760, -4480,
            -4224, -4992, -4736, -7552, -7296, -8064, -7808, -6528, -6272, -7040, -6784,
            -2752, -2624, -3008, -2880, -2240, -2112, -2496, -2368, -3776, -3648, -4032,
            -3904, -3264, -3136, -3520, -3392, -22016, -20992, -24064, -23040, -17920,
            -16896, -19968, -18944, -30208, -29184, -32256, -31232, -26112, -25088,
            -28160, -27136, -11008, -10496, -12032, -11520, -8960, -8448, -9984, -9472,
            -15104, -14592, -16128, -15616, -13056, -12544, -14080, -13568, -344, -328,
            -376, -360, -280, -264, -312, -296, -472, -456, -504, -488, -408, -392,
            -440, -424, -88, -72, -120, -104, -24, -8, -56, -40, -216, -200, -248, -232,
            -152, -136, -184, -168, -1376, -1312, -1504, -1440, -1120, -1056, -1248,
            -1184, -1888, -1824, -2016, -1952, -1632, -1568, -1760, -1696, -688, -656,
            -752, -720, -560, -528, -624, -592, -944, -912, -1008, -976, -816, -784,
            -880, -848, 5504, 5248, 6016, 5760, 4480, 4224, 4992, 4736, 7552, 7296,
            8064, 7808, 6528, 6272, 7040, 6784, 2752, 2624, 3008, 2880, 2240, 2112,
            2496, 2368, 3776, 3648, 4032, 3904, 3264, 3136, 3520, 3392, 22016, 20992,
            24064, 23040, 17920, 16896, 19968, 18944, 30208, 29184, 32256, 31232, 26112,
            25088, 28160, 27136, 11008, 10496, 12032, 11520, 8960, 8448, 9984, 9472,
            15104, 14592, 16128, 15616, 13056, 12544, 14080, 13568, 344, 328, 376, 360,
            280, 264, 312, 296, 472, 456, 504, 488, 408, 392, 440, 424, 88, 72, 120,
            104, 24, 8, 56, 40, 216, 200, 248, 232, 152, 136, 184, 168, 1376, 1312,
            1504, 1440, 1120, 1056, 1248, 1184, 1888, 1824, 2016, 1952, 1632, 1568,
            1760, 1696, 688, 656, 752, 720, 560, 528, 624, 592, 944, 912, 1008, 976,
            816, 784, 880, 848};



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

    private AudioRecord recorder = null;
    //private Encoder decoder = null;
    private int bufferSize = 0;
    public static boolean isRecording = false;
    long timeStart, timeFinish;
    ContentValues cv = new ContentValues();


    /*
    private static void test() throws java.security.NoSuchAlgorithmException
    {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        Key publicKey = kp.getPublic();
        Key privateKey = kp.getPrivate();
    }
    */

    public void onCreate() {
        super.onCreate();
        L.d(TAG, "Объект MediaRecorder создан");
        //updateNotification ("IDLE", "");

    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        L.d(TAG, "Получена команда запуска сервиса записи");

        if (isRecording) {
            L.i(TAG, "Запись уже производится");
            return START_NOT_STICKY;
        }

        try {

            boolean shouldRecord = P.getInstance().getState(P.NEED_RECORD_CALLS);
            if (!shouldRecord) {
                L.i(TAG, "Запись разговоров ОТКЛЮЧЕНА в настройках");
                return START_STICKY;
            }

        } catch (Exception e ){

            e.printStackTrace();
        }

        try {
            initAudioRecorder();
        } catch (Exception e) {
            e.printStackTrace();
        }
        startRecording();
        new Thread() {

            @Override
            public void run() {
                try {

                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                    isRecording = true;
                    while (isRecording) {
                        int readSize = recorder.read(mPCMBuffer, 0, bufferSize);
                        if (readSize > 0) {
                            calculateRealVolume(mPCMBuffer, readSize);
                        }
                    }

                    recorder.stop();
                    recorder.release();
                    recorder = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void calculateRealVolume(short[] buffer, int readSize) {
                double sum = 0;
                for (int i = 0; i < readSize; i++) {

                    sum += buffer[i] * buffer[i];
                }
                if (readSize > 0) {
                    double amplitude = sum / readSize;
                    mVolume = (int) Math.sqrt(amplitude);
                }
            };
        }.start();

        return START_STICKY_COMPATIBILITY;
    }

    private int mVolume;
    public int getVolume(){
        return mVolume;
    }
    private static final int MAX_VOLUME = 2000;
    public int getMaxVolume(){
        return MAX_VOLUME;
    }

    public void onDestroy()
    {
        super.onDestroy();
        L.d(TAG, "Получена команда останова сервиса записи");
        if (isRecording) stopRecording();
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
    

    public void onInfo(MediaRecorder mr, int what, int extra)
    {
        L.d(TAG, "MediaRecorder вернул onInfo с параметрами :: what: " + what + " extra: " + extra);
        isRecording = false;
        //updateNotification ("IDLE", "");
    }


    public void onError(MediaRecorder mr, int what, int extra)
    {
        L.e(TAG, "MediaRecorder вернул onError с параметрами :: what: " + what + " extra: " + extra);
        isRecording = false;
        mr.release();
        //updateNotification ("IDLE", "");
    }


    private String getFilename(){
        if (isDirExist()) {
            // TODO :: ПОЛУЧАТЬ ИЗ ГЛОБАЛС
            // TODO :: SIMID-LINTIME-NUMBER,wav
            TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = telemamanger.getSimSerialNumber();
            String resultname = simSerialNumber + "-" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_WAV;
            cv.put(DatabaseHelper.KEY_LINK, resultname);
            return resultname;
        }
        else return null;
    }


    private String getTempFilename(){
        if (isDirExist()) {
            File tempFile = new File(DEFAULT_STORAGE_LOCATION, AUDIO_RECORDER_TEMP_FILE);
            if (tempFile.exists())
                tempFile.delete();
            return AUDIO_RECORDER_TEMP_FILE;
        }
        else return null;
    }


    private boolean isDirExist() {
        File dir  = new File(DEFAULT_STORAGE_LOCATION, FLATEARS_FOLDER_RELATIVE);
        if(!dir.exists()){
            // :: если директория не существует, пробуем создать
            try {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
                L.i(TAG, "Директория успешно создана по адресу " + dir);
                // :: теперь существует, иначе выпадет Exception
                return true;
            } catch (Exception e) {
                L.e(TAG, "Невозможно содать директорию " + dir + " :: " + e);
                return false;
            }
        }
        else return true;
    }



    private void startRecording() {

        //int[] samplingRates = {8000, 11025, 16000, 22050, 44100};
        //initRecorderParameters(samplingRates);

        try {
            int audiosource = Integer.parseInt(P.getInstance().getParam(P.AUDIO_SOURCE));
            L.i(TAG, "MediaRecorder будет записывать из источника " + audioSources.get(audiosource));

            bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            if (bufferSize < 1024) bufferSize=1024;
            L.d(TAG, "Размер буфера установлен равным " + bufferSize);

            // :: try to create recorder with selected audiosource
            try {
                recorder = new AudioRecord(audiosource, RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize );
            }
            catch (java.lang.IllegalArgumentException e) {
                L.w(TAG, "Неверный источник записи: "+e);
                // :: recreate recorder with mic audiosource
                recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                        RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize );
                // :: downgrade record source
                P.getInstance().setParam(P.AUDIO_SOURCE, ""+MediaRecorder.AudioSource.DEFAULT);
                L.i(TAG, "Источник записи в настройках установлен на DEFAULT");
                audiosource = MediaRecorder.AudioSource.DEFAULT;
            }

            // :: MAKE SOME TEST, If WE CANNOT READ THEN CHANGE SOURCE AND RECREATE RECORDER ::
            if (audiosource == MediaRecorder.AudioSource.VOICE_CALL) {
                if (recorder.read(new byte[2], 0, 2) == AudioRecord.ERROR_INVALID_OPERATION) {
                    L.w(TAG, "Чтение из установленного источника записи невозможно");
                    // :: recreate recorder with mic audiosource
                    recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                            RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize );
                    // :: downgrade record source
                    P.getInstance().setParam(P.AUDIO_SOURCE, ""+MediaRecorder.AudioSource.DEFAULT);
                    L.i(TAG, "Источник записи в настройках установлен на DEFAULT");
                }
            }

            // :: finally check state
            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                recorder.startRecording();
                timeStart = new Date().getTime();
                isRecording = true;
                // :: start recording thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        writeAudioDataToFile();
                    }
                },"AudioRecorder Thread").start();
                // :: write initial data to db
                writeData();
            }
            else L.w(TAG,"Не удалось инициализировать рекордер. ЗАПИСЬ НЕВОЗМОЖНА");

        } catch (Exception e) {
            e.printStackTrace();


        }




    }

    private void initAudioRecorder() throws IOException {
            bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
                    RECORDER_CHANNELS, AudioFormat.ENCODING_PCM_16BIT);

            int bytesPerFrame = AudioFormat.ENCODING_PCM_16BIT;

            int frameSize = bufferSize / bytesPerFrame;
            if (frameSize % FRAME_COUNT != 0) {
                frameSize += (FRAME_COUNT - frameSize % FRAME_COUNT);
                bufferSize = frameSize * bytesPerFrame;
            }


            recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                    RECORDER_SAMPLERATE, RECORDER_CHANNELS, AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize);

//            mPCMBuffer = new short[bufferSize];
    }



    private void writeAudioDataToFile() {

        L.v(TAG, "------------------recording");
        byte data[] = new byte[bufferSize];
        byte resdata[] = new byte[bufferSize/2];
        String filename = FLATEARS_STORAGE_LOCATION + getTempFilename();
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (null != os) {
            int size = 0, counter = 0;
            while (isRecording) {
                int result = recorder.read(data, 0, bufferSize);

                switch (result) {
                    case AudioRecord.ERROR_INVALID_OPERATION: {
                        L.e(TAG, "Чтение из источника: Invalid operation error");
                        break;
                    }
                    case AudioRecord.ERROR_BAD_VALUE: {
                        L.e(TAG, "Чтение из источника: Bad value error");
                        break;
                    }
                    case AudioRecord.ERROR: {
                        L.e(TAG, "Чтение из источника: Unknown error");
                        break;
                    }
                    default: {
                        try {
                            int n = 0;

                            for (int i = 0; i < bufferSize; i=i+2)
                            {

                                ByteBuffer bb = ByteBuffer.allocate(2);
                                bb.order(ByteOrder.LITTLE_ENDIAN);
                                bb.put(data[i]);
                                bb.put(data[i+1]);
                                short shortVal = bb.getShort(0);
                                resdata[n]=linearToALawSample(shortVal);
                             //    resdata[n]=linearToALawSample[tmpbuf & '\uffff'];
                                n=n+1;

                                //data[i]=ALAW_DECODE[data[i]];

                            }

                           // decoder=new Encoder();
                           // decoder.process(data);

                            os.write(resdata);
                          //  os.write(data);
                            counter += bufferSize;
                            if (counter > 100000) {
                                L.v(TAG,"---------------record bytes > 10000");
                                size += counter;
                                counter = 0;
                            //    L.v(TAG, "Записано " + String.format("%.2f", size / 1024 / 1024) + "Мб...");
                            }
                        } catch (IOException e) {
                            L.e(TAG, "Ошибка ввода/вывода при записи в файл RAW");
                        }
                    }
                }
            }
            size += counter;
       //     L.v(TAG, "Записано " + String.format("%.2f", size / 1024 / 1024) + "Мб!");
            L.i(TAG, "Запись в файл RAW завершена");

            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void writeData() {
        L.v(TAG, "Запись начальных данных в БД");
        ContentValues cv = new ContentValues();
        Date date = new Date();

        try {
            cv.put(DatabaseHelper.KEY_LINTIME, ""+(date.getTime()));
            cv.put(DatabaseHelper.KEY_ANUM, P.getInstance().getParam(P.SIMID));
            cv.put(DatabaseHelper.KEY_DEVID, P.getInstance().getParam(P.DEVID));
            cv.put(DatabaseHelper.KEY_ANDROIDID, P.getInstance().getParam(P.AN_ID));
            cv.put(DatabaseHelper.KEY_FORMAT, "WAVe");
            cv.put(DatabaseHelper.KEY_SOURCE, audioSources.get(Integer.parseInt(P.getInstance().getParam(P.AUDIO_SOURCE))));
            cv.put(DatabaseHelper.KEY_LINK, getFilename());
            cv.put(DatabaseHelper.KEY_STATUS, "INIT");
            //TODO :: change format to FLATFORMAT
            cv.put(DatabaseHelper.KEY_DATE, ""+new SimpleDateFormat("dd.MM.yyyy").format(date));
            cv.put(DatabaseHelper.KEY_TIME, ""+new SimpleDateFormat("HH.mm.ss").format(date));
            cv.put(DatabaseHelper.KEY_DURATION, "0");
            cv.put(DatabaseHelper.KEY_CALLTYPE, P.getInstance().getParam(P.CUR_TYPE));
            cv.put(DatabaseHelper.KEY_BNUM, P.getInstance().getParam(P.CUR_NUMB));
        } catch (Exception e){
            e.printStackTrace();
        }



        DatabaseHelper db =  DatabaseHelper.getInstance(getApplicationContext());
        db.addInitial(cv);

    }

    private void stopRecording(){
        if(null != recorder){
            int i = recorder.getState();
            if(i==1)
                recorder.stop();
            recorder.release();
            recorder = null;
            isRecording = false;
        }
        L.i(TAG, "Запись прекращена");

        timeFinish = new Date().getTime();
        DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.getContext());
        int duration = (int) ((timeFinish - timeStart)/1000);
        L.e(TAG, "Посчитали длительность: "+duration);
        // TODO :: increase statistics
        cv.put(DatabaseHelper.KEY_DURATION,""+duration);
        cv.put(DatabaseHelper.KEY_STATUS,"RECORDED");
        db.updateLast (cv);

        try {
            String outputFile = cv.get(DatabaseHelper.KEY_LINK).toString();
            copyWaveFile(getTempFilename(),outputFile);
            //updateNotification ("RECDONE", outputFile);
            deleteTempFile();

            L.d(TAG, "ЗАПИСЬ ЗАВЕРШЕНА. ЗАПУСКАЮ ВЫГРУЗКУ");
            Transmitter.transmit(outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void deleteTempFile() {
        File file = new File(getTempFilename());
        file.delete();
    }

    private void copyWaveFile(String inFilename,String outFilename){
        FileInputStream in;
        FileOutputStream out;
        long totalAudioLen;
        long totalDataLen;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 1;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels /8;

        byte[] data = new byte[bufferSize];

        L.d(TAG, "Файлу записи назначено имя " + outFilename);

        try {
            in = new FileInputStream(FLATEARS_STORAGE_LOCATION + inFilename);
            out = new FileOutputStream(FLATEARS_STORAGE_LOCATION + outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;
            L.d(TAG, "Итоговый размер файла должен составить: " + totalDataLen + " байт");

            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);

            while(in.read(data) != -1){
                out.write(data);
            }
            L.i(TAG, "Файл " + outFilename + " успешно создан");
            in.close();
            out.close();
            L.d(TAG, "Работа с файлами завершена");
        } catch (IOException e) {
            L.e(TAG, "Формирование файла записи " + outFilename + " завершилось с ошибкой " + e);
        }
    }


    private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException {

        L.d(TAG, "Запись WAV заголовка :: " + channels + " chann.; " + longSampleRate + " frames per sec; " + byteRate + " bytes per sec;");

         /*
         <fmt > (format description
            PCM format
            1 channel
            11025 frames per sec
            22050 bytes per sec
            2 bytes per frame
            16 bits per sample
        <data> (waveform data - 564224 bytes)
        */

        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 6; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (channels * RECORDER_BPP / 8); // block align
        header[33] = 0;
        header[34] = RECORDER_BPP; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);

        L.d(TAG, "Запись заголовка успешно завершена");
    }



    public void initRecorderParameters(int[] sampleRates){

        for (int sampleRate : sampleRates) {
            try {
                L.i(TAG, "Indexing " + sampleRate + "Hz Sample Rate");
                int tmpBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);

                // Test the minimum allowed buffer size with this configuration on this device.
                if (tmpBufferSize != AudioRecord.ERROR_BAD_VALUE) {
                    // Seems like we have ourself the optimum AudioRecord parameter for this device.
                    AudioRecord tmpRecoder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                            sampleRate,
                            AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT,
                            tmpBufferSize);
                    // Test if an AudioRecord instance can be initialized with the given parameters.
                    if (tmpRecoder.getState() == AudioRecord.STATE_INITIALIZED) {
                        String configResume = "initRecorderParameters(sRates) has found recorder settings supported by the device:"
                                + "\nSource   = MICROPHONE"
                                + "\nsRate    = " + sampleRate + "Hz"
                                + "\nChannel  = MONO"
                                + "\nEncoding = 16BIT";
                        L.i(TAG, configResume);

                        //+++Release temporary recorder resources and leave.
                        tmpRecoder.release();
                        tmpRecoder = null;

                        return;
                    }
                } else {
                    L.w(TAG, "Incorrect buffer size. Continue sweeping Sampling Rate...");
                }
            } catch (IllegalArgumentException e) {
                L.e(TAG, "The " + sampleRate + "Hz Sampling Rate не поддерживается устройством");
            }
        }
    }


    public byte linearToALawSample(short sample) {
        int sign;
        int exponent;
        int mantissa;
        int s;

        sign = ((~sample) >> 8) & 0x80;
        if (!(sign == 0x80)) {
            sample = (short) -sample;
        }
        if (sample > cClip) {
            sample = cClip;
        }
        if (sample >= 256) {
            exponent = (int) aLawCompressTable[(sample >> 8) & 0x7F];
            mantissa = (sample >> (exponent + 3)) & 0x0F;
            s = (exponent << 4) | mantissa;
        } else {
            s = sample >> 4;
        }
        s ^= (sign ^ 0x55);
        return (byte) s;
    }
}
