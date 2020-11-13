package com.example.blindcommunity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.blindcommunity.Model.JsonTaskModel;
import com.example.blindcommunity.R;
import com.example.blindcommunity.UI.HomeActivity;
import com.example.blindcommunity.UI.SignInActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WritePostFragment extends Fragment {

    HomeActivity homeActivity;
    Button writeButton,cancelButton;
    EditText titleText,contentText;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        homeActivity = (HomeActivity)getActivity();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        homeActivity = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_writepost, container, false);

        titleText = view.findViewById(R.id.titleEditText);
        contentText = view.findViewById(R.id.contentEditText);

        writeButton = view.findViewById(R.id.writeButton);
        writeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                int classifyPost = homeActivity.title;
                sendJSONData(classifyPost);

            }
        });
        cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int classifyPost = homeActivity.title;
                homeActivity.onChangeFragment(classifyPost);

            }
        });


        return view;

    }
    private void sendJSONData(int index){//index 1:free 2:info 3:employ

        String m_title = titleText.getText().toString();
        String m_content = contentText.getText().toString();
        if(titleText.length()>100){
            homeActivity.makeToast("제목은 100자 이내로 작성해주세요");
            return;
        }
        else{
            titleText.setText("");
        }
        if(contentText.length()>1000){
            homeActivity.makeToast("내용은 1000자 이내로 작성해주세요");
            return;
        }
        else{
            contentText.setText("");
        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String DateAndTime = sdfNow.format(date);

        JSONTaskGET task = new JSONTaskGET();
        String parameter = "?post_id="+DateAndTime+"&title="+m_title+"&content="+m_content+"&user_id="+homeActivity.cur_user_id;
        if(index==1){
            task.execute("http://13.125.232.199:3000/write_free" + parameter);
        }
        else if(index==2){
            task.execute("http://13.125.232.199:3000/write_info" + parameter);
        }
        else if(index==3){
            task.execute("http://13.125.232.199:3000/write_employ" + parameter);
        }
        else{
            Log.e("writePost error","어느 게시판인지 모호합니다.");
        }

    }

    private class JSONTaskGET extends JsonTaskModel {

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
                Log.i("success","자유게시판 게시 성공");
                homeActivity.makeToast("게시 완료");
                homeActivity.onChangeFragment(homeActivity.title);
            }
            else if(result.equals("0")){
                Log.e("fail","자유게시판 게시 실패");
            }


        }
    }
}
