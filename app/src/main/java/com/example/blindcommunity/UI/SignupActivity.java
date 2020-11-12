package com.example.blindcommunity.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blindcommunity.Model.JsonTaskModel;
import com.example.blindcommunity.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SignupActivity extends Activity {

    EditText id;
    EditText pw;
    EditText nickname;
    Button confirmButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        id = findViewById(R.id.signupId);
        pw = findViewById(R.id.signupPw);
        nickname = findViewById(R.id.signupNickname);
        confirmButton = findViewById(R.id.confirmBtn);
        cancelButton = findViewById(R.id.cancelBtn);

        confirmButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){

                JSONTaskGET task = new JSONTaskGET();
                String m_id = id.getText().toString();
                String m_pw = pw.getText().toString();
                String m_nick = nickname.getText().toString();
                if(m_id == null || m_id.length()>=50){
                    makeToast("id 길이는 50자 이내로 작성해주세요");
                    return;
                }
                else if(m_pw ==null || m_pw.length()>=50){
                    makeToast("pw 길이는 50자 이내로 작성해주세요");
                    return;
                }
                else if(m_nick == null || m_nick.length()>=50){
                    makeToast("nickname 길이는 50자 이내로 작성해주세요");
                }
                String parameter = "?id=" + m_id + "&pw="+m_pw + "&nickname=" + m_nick;
                task.execute("http://13.125.232.199:3000/sign_up"+parameter);

            }
        });
        cancelButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                //Toast.makeText(SignupActivity.this, "회원가입 취소합니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });

    }

    private void makeToast(String string){
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }


    private class JSONTaskGET extends JsonTaskModel {
        //ArrayList<Data> data = new ArrayList<>();
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
                Log.i("success","회원가입 성공");
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
            else if(result.equals("0")){
                makeToast("중복된 아이디가 있습니다");
            }
            else if(result.equals("-1")){
                makeToast("입력한 정보에 문제가 있습니다");
            }
        }
    }


}
