package com.example.blindcommunity.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.blindcommunity.fragment.WritePostFragment;
import com.example.blindcommunity.fragment.lowerFragment;
import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {

    FragmentManager fm;
    HomeScreenFragment homeScreenFragment;
    LookupScreenFragment lookupScreenFragment;
    MyTraceScreenFragment myTraceScreenFragment;
    WritePostFragment writePostFragment;

    TextView TitleText;
    public int title;
    public String cur_user_id;

    static final String HELLO =  "Welcome To YK World";
    private long backKeyPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        backKeyPressedTime = 0;

        fm = getSupportFragmentManager(); //fragment 초기 설정
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        homeScreenFragment = new HomeScreenFragment();
        lookupScreenFragment = new LookupScreenFragment();
        myTraceScreenFragment = new MyTraceScreenFragment();
        writePostFragment = new WritePostFragment();

        fragmentTransaction.add(R.id.middleFragment, homeScreenFragment).commit();

        TitleText = findViewById(R.id.titleText);

        Intent intent = getIntent();
        cur_user_id = intent.getExtras().getString("cur_user_id");



    }

    public void makeToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    public void onChangeFragment(int index){
        if(index==1){ //자유게시판으로 이동
            fm.beginTransaction().replace(R.id.middleFragment,lookupScreenFragment).commit();
            TitleText.setText("자유 게시판");
            title = 1;
            lowerFragment lf = (lowerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_lowerBar);
            lf.setRedToYellow();

        }
        else if(index==2){ //정보게시판으로 이동
            fm.beginTransaction().replace(R.id.middleFragment,lookupScreenFragment).commit();
            TitleText.setText("정보 게시판");
            title = 2;
            //lowerFragment에 있는 setRedToYellow메소드 호출-> lowerFragment 이미지버튼 색상 노란색으로 변경
            lowerFragment lf = (lowerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_lowerBar);
            lf.setRedToYellow();
        }
        else if(index==3){ //취업게시판으로 이동
            fm.beginTransaction().replace(R.id.middleFragment,lookupScreenFragment).commit();
            TitleText.setText("취업 게시판");
            title = 3;
            lowerFragment lf = (lowerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_lowerBar);
            lf.setRedToYellow();
        }
        else if(index==0){ //게시판 홈 화면
            fm.beginTransaction().replace(R.id.middleFragment,homeScreenFragment).commit();
            TitleText.setText(HELLO);

        }
        else if(index==-1){ //로그인 화면으로 이동할지 먼저 물어봄
            showLogoutDialog();
        }
        else if(index==4){ //myTrace화면으로 이동
            fm.beginTransaction().replace(R.id.middleFragment,myTraceScreenFragment).commit();
            TitleText.setText("내가 쓴 게시물");
        }
        else if(index==5){ //글쓰기. writepost화면으로 이동
            fm.beginTransaction().replace(R.id.middleFragment,writePostFragment).commit();
        }
    }
    public void moveToInsidePostActivity(String arg){ //게시물로 이동했을 때의 intent에 데이터를 putExtra
        Intent intent = new Intent(this,InsidePostActivity.class);
        intent.putExtra("post_id",arg);
        intent.putExtra("user_id",cur_user_id);
        intent.putExtra("employ_type",title); //1:free 2:info 3:employ
        startActivity(intent);
    }

    void showLogoutDialog(){ //로그아웃하기 전에 다이얼로그 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("로그아웃 하시겠습나까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    @Override
    public void onBackPressed(){ //뒤로가기 버튼 눌렀을 때 한번 더 누르고 종료하도록 하기
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            makeToast("'뒤로' 버튼을 한번 더 누르시면 종료됩니다.");
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }

}
