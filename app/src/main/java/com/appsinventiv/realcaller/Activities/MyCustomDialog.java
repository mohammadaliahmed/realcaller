package com.appsinventiv.realcaller.Activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.view.Window;
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