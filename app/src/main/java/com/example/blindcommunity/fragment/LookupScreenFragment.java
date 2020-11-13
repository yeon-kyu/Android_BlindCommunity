package com.example.blindcommunity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.blindcommunity.Model.JsonTaskModel;
import com.example.blindcommunity.R;
import com.example.blindcommunity.UI.HomeActivity;
import com.example.blindcommunity.UI.SignInActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LookupScreenFragment extends Fragment {

    HomeActivity homeActivity;
    ArrayList<String> post_list;
    ArrayAdapter adapter;
    int count;
    Button writePostButton;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.fragment_lookupscreen,container,false);

        post_list = new ArrayList<String>();

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,post_list);

        ListView listview = (ListView)view.findViewById(R.id.MyListview);
        listview.setAdapter(adapter);

        sendJSONData();

        writePostButton = view.findViewById(R.id.writePostButton);
        writePostButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeActivity.onChangeFragment(5);

            }
        });

        return view;
    }
    private void sendJSONData(){
        int classifyPost = homeActivity.title;
        JSONTaskGET task = new JSONTaskGET();
        String parameter = "?cnt="+count;

        if(classifyPost==1) {
            task.execute("http://13.125.232.199:3000/search_free" + parameter);
        }
        else if(classifyPost ==2){
            task.execute("http://13.125.232.199:3000/search_info" + parameter);
        }
        else if(classifyPost==3){
            task.execute("http://13.125.232.199:3000/search_employee" + parameter);
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
            if(result.equals("0")){
                Log.e("no post","자유게시판에 게시글이 없습니다.");
                homeActivity.makeToast("자유게시판에 게시물이 없습니다.");
            }
            else if(result.equals("00")){
                homeActivity.makeToast("정보게시판에 게시물이 없습니다.");
            }
            else if(result.equals("000")){
                homeActivity.makeToast("취업게시판에 게시물이 없습니다.");
            }
            else{
                Log.e("post exist","현재 게시판에 게시글이 있습니다.");

                try {
                    JSONArray jsonResult = new JSONArray(result);

                    for(int i=0;i<jsonResult.length();i++){
                        JSONObject parsedResult = new JSONObject(jsonResult.getString(i));
                        Log.e("received data : ","nickname : "+parsedResult.getString("nickname")
                                +", title : "+parsedResult.getString("title")
                                +", post_id : "+parsedResult.getString("post_id"));
                        post_list.add(parsedResult.getString("title"));

                    }
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
