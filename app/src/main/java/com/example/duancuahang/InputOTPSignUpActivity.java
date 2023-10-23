package com.example.duancuahang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duancuahang.Class.Validates;

public class InputOTPSignUpActivity extends AppCompatActivity {

    EditText edtInputOTP;
    Button btnLayMaOTP, btnXacMinhOTP;
    TextView tvTimeResend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_otp_signupactivity);
        setControl();
        setEvent();
    }

    private void setEvent() {
        edtInputOTP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    if(Validates.validOTP(edtInputOTP.getText().toString()) == false){
                        edtInputOTP.selectAll();
                        edtInputOTP.requestFocus();
                        edtInputOTP.setError("OTP là dãy ký tự 4 số");
                    }
                }
                return false;
            }
        });
    }
    private void setControl() {
        edtInputOTP = findViewById(R.id.edtInputOTP);
        btnLayMaOTP = findViewById(R.id.btnLayMaOTP);
        btnXacMinhOTP = findViewById(R.id.btnXacMinhOTP);
        tvTimeResend = findViewById(R.id.tvTimeResend);
    }
}