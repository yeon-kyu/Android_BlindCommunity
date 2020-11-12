package com.example.blindcommunity.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blindcommunity.Model.JsonTaskModel;
import com.example.blindcommunity.R;

import java.util.ArrayList;


public class SignInActivity extends AppCompatActivity {
    static int SIGNUP_COMMAND = 1;

    EditText idText;
    EditText pwText;
    Button loginButton;
    Button signupButton;

    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        idText = findViewById(R.id.id);
        pwText = findViewById(R.id.pw);
        loginButton = findViewById(R.id.signinBtn);
        signupButton = findViewById(R.id.signupBtn);


        loginButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){

                JSONTaskGET task = new JSONTaskGET();
                String m_id = idText.getText().toString();
                String m_pw = pwText.getText().toString();
                if(m_id == null || m_id.length()>=50){
                    makeToast("id 길이는 50자 이내로 작성해주세요");
                    return;
                }
                else if(m_pw ==null || m_pw.length()>=50){
                    makeToast("pw 길이는 50자 이내로 작성해주세요");
                    return;
                }
                ID = m_id;
                //setMyID(m_id);
                String parameter = "?id="+m_id+"&pw="+m_pw;
                task.execute("http://13.125.232.199:3000/sign_in"+parameter);
                //TODO

            }
        });

        signupButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SignInActivity.this, SignupActivity.class);
                startActivityForResult(intent,SIGNUP_COMMAND);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGNUP_COMMAND) {
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                //TODO
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "회원가입 취소", Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void makeToast(String string){
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }

    private class JSONTaskGET extends JsonTaskModel{

        public void setData(ArrayList<Data> data){
            super.setData(data);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result == null){
                return;
            }
            if(result.equals("1")){
                Log.i("success","로그인 성공");
                makeToast("나의 아이디 : "+ID);

                Intent intent = new Intent(SignInActivity.this,HomeActivity.class);
                startActivity(intent);
            }
            else if(result.equals("0")){
                makeToast("해당 아이디가 없습니다.");
            }
            else if(result.equals("-1")){
                makeToast("비밀번호가 일치하지 않습니다");
            }
        }
    }



}