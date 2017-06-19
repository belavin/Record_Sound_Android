package flynn.pro.flatears;

import android.content.Context;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;


public class FTPUploader  {
    public FTPClient mFTPClient = null;
    public static final String TAG = "FTPUPLDR";

    static  FTPUploader ftpclient = new FTPUploader();

    public boolean ftpConnect(String host, String username, String password, int port) {
        try {
            mFTPClient = new FTPClient();

            mFTPClient.connect(host, port);

            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {

                boolean status = mFTPClient.login(username, password);

                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();
                return status;
            }
        } catch (Exception e) {
            L.w(TAG, "Не могу подключиться к серверу " + host);
            L.e(TAG, "ERR: "+e);
        }
        return false;
    }

    public boolean ftpDisconnect() {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            L.v(TAG, "Успешное отключение от FTP");
            return true;
        } catch (Exception e) {
            L.w(TAG, "Отключение от сервера завершилось ошибкой.");
        }
        return false;
    }

    public boolean ftpUpload(String srcFilePath, String desFileName) {
        boolean status = false;
        try {
            FileInputStream srcFileStream = new FileInputStream(srcFilePath);

            status = mFTPClient.storeFile(desFileName, srcFileStream);

            srcFileStream.close();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            L.d(TAG, "Процесс выгрузки завершился ошибкой: " + e);
        }
        return status;
    }

    public static void uploadFile (final String filename) {
        Context context = MainActivity.getContext();
        final DatabaseHelper db = DatabaseHelper.getInstance(context);
        boolean isConnect, isUpload;

        // TODO :: GET DIR FROM PREFS
        String dpath = RecordService.FLATEARS_STORAGE_LOCATION;
        File f = new File(dpath + filename);

        if(f.exists() && !f.isDirectory()) {
            isConnect = ftpclient.ftpConnect(
                    P.getInstance().getParam(P.SERVER_IP), "rec", "123", 21);
            if (isConnect) {
                L.v(TAG, "Успешное подключение к FTP");
                isUpload = ftpclient.ftpUpload(dpath + filename, filename);
                if (isUpload) {
                    L.v(TAG, "Успешная выгрузка файла " + filename);
                    db.setState(filename, "UPLOADED");
                    Transmitter.transmit(filename);
                } else {
                    L.w(TAG, "Выгрузка файла " + filename + " не удалась");
                    db.setState(filename, "FAILED");
                }
                ftpclient.ftpDisconnect();
            } else {
                L.w(TAG, "Подключиться не удалось");
                db.setState(filename, "FAILED");
            }
        }
        else {
            L.i(TAG, "Файл " + filename + " не найден");
            db.setState(filename, "DELETED");
        }
    }

    public static void deleteFile(final String filename) {
        Context context = MainActivity.getContext();
        final DatabaseHelper db = DatabaseHelper.getInstance(context);

        if (new File(RecordService.FLATEARS_STORAGE_LOCATION + filename).delete()) {
            L.v(TAG, "Успешное удаление файла " + filename);
            db.setState(filename, "DELETED");
        } else
            L.w(TAG, "Безуспешное удаление файла :)" + filename);
    }

    public static String ftpStatus() {
        if (ftpclient.ftpConnect(
                P.getInstance().getParam(P.SERVER_IP), "rec", "123", 21)) {
            return "ONLINE";
        } else {
            return "OFFLINE";
        }
    }


}
