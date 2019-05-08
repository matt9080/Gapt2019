package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_view);

        configureLessonsButton();
    }

    private void configureLessonsButton(){
        Button btn_lessons = (Button) findViewById( R.id.btn_signin);
        btn_lessons.setOnClickListener(new View.OnClickListener() {
            @Override

                public void onClick(View v) {
                    Intent myIntent = new Intent(SignUpActivity.this, MainTestActivity.class);
                    startActivity(myIntent);
                }

        });
    }
}
