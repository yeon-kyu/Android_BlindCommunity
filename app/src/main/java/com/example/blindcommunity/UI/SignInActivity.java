package com.example.blindcommunity.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blindcommunity.Model.JsonTaskModel;
import com.example.blindcommunity.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class SignInActivity extends AppCompatActivity {
    static int SIGNUP_COMMAND = 1;

    EditText idText;
    EditText pwText;
    Button loginButton;
    Button signupButton;
    CheckBox autologin;

    String ID;

    boolean autoLoginFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        idText = findViewById(R.id.id);
        pwText = findViewById(R.id.pw);
        loginButton = findViewById(R.id.signinBtn);
        signupButton = findViewById(R.id.signupBtn);
        autologin = findViewById(R.id.checkbox);

        loginButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){

                JSONTaskGET task = new JSONTaskGET();
                String m_id = idText.getText().toString();
                String m_pw = pwText.getText().toString();
                if(m_id.equals("") || m_id.length()>=50){
                    makeToast("id 길이는 50자 이내로 작성해주세요");
                    return;
                }
                else if(m_pw.equals("") || m_pw.length()>=50){
                    makeToast("pw 길이는 50자 이내로 작성해주세요");
                    return;
                }
                if(m_id.contains("'")||m_pw.contains("'")){
                    makeToast("'문자는 사용할 수 없습니다.");
                    return;
                }
                if(m_id.contains("|")||m_pw.contains("|")){
                    makeToast("|문자는 사용할 수 없습니다.");
                    return;
                }



                ID = m_id;
                //setMyID(m_id);
                String parameter = "?id="+m_id+"&pw="+m_pw;
                task.execute("http://13.125.232.199:3000/sign_in"+parameter);

            }
        });

        signupButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SignInActivity.this, SignupActivity.class);
                startActivityForResult(intent,SIGNUP_COMMAND);
            }
        });

        try {
            readAutoLoginFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    private void readAutoLoginFile() throws FileNotFoundException {
        FileInputStream fis = null;
        fis = openFileInput("loginData.txt");
        BufferedReader iReader = new BufferedReader(new InputStreamReader((fis)));

        try {
            StringBuffer buffer = new StringBuffer();

//            while(data!=null){
//                buffer.append(data);
//                data = iReader.readLine();
//            }
//            buffer.append("\n");
//            iReader.close();

            String data = iReader.readLine();//파일에서 한줄 읽기
            buffer.append(data);

            String loginData = buffer.toString();
            //makeToast(loginData);

            if(loginData.equals("")||loginData.equals("0")){
                autologin.setChecked(false);
                autoLoginFlag = false;
            }
            else{
                autoLoginFlag = true;
                Log.e("로그인 데이터",loginData);
                String[] idpw = loginData.split("'");
                ID = idpw[0];
                //setMyID(m_id);
                String parameter = "?id="+idpw[0]+"&pw="+idpw[1];
                JSONTaskGET task = new JSONTaskGET();
                task.execute("http://13.125.232.199:3000/sign_in"+parameter);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void writeAutoLoginFile(String loginData){
        FileOutputStream fos = null;// data/data/BlindCommunity에 저장될듯
        try{
            fos = openFileOutput("loginData.txt", Context.MODE_PRIVATE);
            fos.write(loginData.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

                if(autologin.isChecked()){//자동 로그인 체크되어있으면 파일로 저장
                    String m_id = idText.getText().toString();
                    String m_pw = pwText.getText().toString();
                    writeAutoLoginFile(m_id+"'"+m_pw);
                }
                else if(autoLoginFlag==true){
                    //로그인파일 수정안함
                }
                else{ //체크 안되어있으면 파일 내용 삭제
                    writeAutoLoginFile("0");

                }
                Intent intent = new Intent(SignInActivity.this,HomeActivity.class);
                intent.putExtra("cur_user_id",ID);
                startActivity(intent);
                finish();
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