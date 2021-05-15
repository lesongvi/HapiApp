package com.example.scheduleproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.scheduleproject.Fragment.EditProfile;
import com.example.scheduleproject.Model.SQLSever;

public class ForgotPassword extends AppCompatActivity {
    public static final String EXTRA_CODE = "com.example.application.example.EXTRA_CODE";

    private NotificationCompat.Builder mBuilder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgot_password);
        final EditText OTP = (EditText) findViewById(R.id.txtOTP);
        final Button send = (Button) findViewById(R.id.btn_send);
        final Button bt_backqmk = (Button) findViewById(R.id.bt_backqmk);
        final SQLSever sqlUser = new SQLSever(this);

        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String otp = OTP.getText().toString();
                        boolean ketqua = false;
                        if (otp.equals("")) {
                            Toast.makeText(ForgotPassword.this, "Vui Lòng Nhập Mã OTP!!", Toast.LENGTH_SHORT).show();
                        } else {
                            //nếu bằng với OTP
                            ketqua = true;
                        }

                        if (ketqua == true) {
                            //thay đổi pass
                            replaceFragment(new EditProfile());
                        } else {
                            Toast.makeText(ForgotPassword.this, "OTP không chính xác!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
//

                });

        bt_backqmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLogin();
            }
        });


    }
    public void OpenLogin() {
        Intent intent = new Intent(ForgotPassword.this, Login.class);
        startActivity(intent);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ForgotPass,fragment);
        fragmentTransaction.commit();
    }
}
