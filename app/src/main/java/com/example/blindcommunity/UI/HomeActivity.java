package com.example.blindcommunity.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.blindcommunity.R;
import com.example.blindcommunity.fragment.HomeScreenFragment;
import com.example.blindcommunity.fragment.LookupScreenFragment;
import com.example.blindcommunity.fragment.MyTraceScreenFragment;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {

    FragmentManager fm;
    HomeScreenFragment homeScreenFragment;
    LookupScreenFragment lookupScreenFragment;
    MyTraceScreenFragment myTraceScreenFragment;

    TextView TitleText;
    public int title;

    static final String HELLO =  "Welcome To YK World";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        homeScreenFragment = new HomeScreenFragment();
        lookupScreenFragment = new LookupScreenFragment();
        myTraceScreenFragment = new MyTraceScreenFragment();

        fragmentTransaction.add(R.id.middleFragment, homeScreenFragment).commit();

        TitleText = findViewById(R.id.titleText);




    }

    public void makeToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    public void onChangeFragment(int index){
        if(index==1){
            fm.beginTransaction().replace(R.id.middleFragment,lookupScreenFragment).commit();
            TitleText.setText("자유 게시판");
            title = 1;
        }
        else if(index==2){
            fm.beginTransaction().replace(R.id.middleFragment,lookupScreenFragment).commit();
            TitleText.setText("정보 게시판");
            title = 2;
        }
        else if(index==3){
            fm.beginTransaction().replace(R.id.middleFragment,lookupScreenFragment).commit();
            TitleText.setText("취업 게시판");
            title = 3;
        }
        else if(index==0){ //게시판 홈 화면
            fm.beginTransaction().replace(R.id.middleFragment,homeScreenFragment).commit();
            TitleText.setText(HELLO);
        }
        else if(index==-1){ //로그인 화면
            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
            startActivity(intent);
        }
        else if(index==4){ //myTrace화면
            fm.beginTransaction().replace(R.id.middleFragment,myTraceScreenFragment).commit();
            TitleText.setText("내가 쓴 게시물");
        }
    }

}
