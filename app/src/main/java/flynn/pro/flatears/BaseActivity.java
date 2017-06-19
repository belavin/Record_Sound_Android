package flynn.pro.flatears;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;




public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
    }


    public class TopExceptionHandler implements Thread.UncaughtExceptionHandler {
        public TopExceptionHandler(Activity app) {
            Thread.getDefaultUncaughtExceptionHandler();
        }


        public void uncaughtException(Thread t, Throwable e)
        {
            StackTraceElement[] arr = e.getStackTrace();
            String report = e.toString() + "\n\n";

            report += "--------- Stack trace ---------\n\n";

            for (int i = 0; i < arr.length; i++)
            {
                report += "    " + arr[i].toString() + "\n";
            }
            report += "-------------------------------\n\n";




            report += "--------- Cause ---------\n\n";


            Throwable cause = e.getCause();
            if (cause != null) {
                report += cause.toString() + "\n\n";
                arr = cause.getStackTrace();
                for (int i = 0; i < arr.length; i++)
                {
                    report += "    " + arr[i].toString() + "\n";
                }
            }




            report += "-------------------------------\n\n";


            report += "-------------------------------\n\n";

            report += "--------- Device ---------\n\n";

            report += "Brand: " + Build.BRAND + "\n";

            report += "Device: " + Build.DEVICE + "\n";

            report += "Model: " + Build.MODEL + "\n";

            report += "Id: " + Build.ID + "\n";

            report += "Product: " + Build.PRODUCT + "\n";

            report += "-------------------------------\n\n";

            report += "--------- Firmware ---------\n\n";

            report += "SDK: " + Build.VERSION.SDK + "\n";

            report += "Release: " + Build.VERSION.RELEASE + "\n";

            report += "Incremental: " + Build.VERSION.INCREMENTAL + "\n";

            report += "-------------------------------\n\n";


            L.e("Report ::", report);


            Intent i1 = new Intent(BaseActivity.this, Error.class);

            i1.putExtra("Error", report.toString());

            i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(i1);


            System.exit(0);


        }

    }

}