package com.appsinventiv.realcaller.Activities;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.appsinventiv.realcaller.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyCustomDialog extends Activity {
    TextView name, phone;
    String phone_no;
    Button dialog_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            int LAYOUT_FLAG;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            }

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            this.setFinishOnTouchOutside(false);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog);
            initializeContent();



            /*
            LayoutInflater layoutInflater =
                    (LayoutInflater)getBaseContext()
                            .getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.dialog, null);
            final PopupWindow popupWindow = new PopupWindow(
                    popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            /*WindowManager.LayoutParams params = getWindow().getAttributes();
            params.x = -100;
            params.height = 70;
            params.width = 1000;
            params.y = -50;
            this.getWindow().setAttributes(params);*/
            phone_no = getIntent().getExtras().getString("phone_no");

            phone.setText("" + phone_no + " is calling you");
            dialog_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyCustomDialog.this.finish();
//                    this.setFinishOnTouchOutside(false);
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            Log.d("Exception", e.toString());
            e.printStackTrace();
        }
    }

    private void initializeContent() {
        phone = (TextView) findViewById(R.id.phone);
    }
}