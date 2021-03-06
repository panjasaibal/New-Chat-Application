package com.example.newchatapplication.password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newchatapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class ResetPasswordActivity extends AppCompatActivity {

    private Button retryBtn, closeBtn, resetMsgBtn;
    private LinearLayout llresetView, llresetMsgView;
    private TextView email, tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.crimson)));
        bar.setDisplayShowTitleEnabled(false);  // required to force redraw, without, gray color
        bar.setDisplayShowTitleEnabled(true);
        llresetMsgView = findViewById(R.id.llMessageReset);
        llresetView = findViewById(R.id.llresetView);

        email = findViewById(R.id.resetEmail);
        retryBtn = findViewById(R.id.retryBtn);
        closeBtn = findViewById(R.id.closeBtn);
        resetMsgBtn = findViewById(R.id.sentResetMsg);
        tvMessage = findViewById(R.id.tvMessage);

        resetMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().equals("")){
                    email.setError(getString(R.string.valid_email));
                }else{

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            llresetView.setVisibility(View.GONE);
                            llresetMsgView.setVisibility(View.VISIBLE);

                            if(task.isSuccessful()){

                                tvMessage.setText(getString(R.string.reset_pswd_instruction, email.getText().toString()));

                                new CountDownTimer(6000, 1000){
                                    @Override
                                    public void onTick(long l) {

                                        retryBtn.setText(getString(R.string.retry_in,String.valueOf(l/1000)));
                                        retryBtn.setOnClickListener(null);
                                    }

                                    @Override
                                    public void onFinish() {
                                        retryBtn.setText(getString(R.string.retry));
                                        retryBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                llresetView.setVisibility(View.VISIBLE);
                                                llresetMsgView.setVisibility(View.GONE);
                                            }
                                        });

                                    }
                                }.start();

                            }else{
                                tvMessage.setText(getString(R.string.error_in_sent_email, task.getException()));

                                retryBtn.setText(getString(R.string.retry));
                                retryBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        llresetView.setVisibility(View.VISIBLE);
                                        llresetMsgView.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}