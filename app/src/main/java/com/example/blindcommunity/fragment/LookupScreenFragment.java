package com.example.blindcommunity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.blindcommunity.Model.JsonTaskModel;
import com.example.blindcommunity.R;
import com.example.blindcommunity.UI.HomeActivity;
import com.example.blindcommunity.UI.InsidePostActivity;
import com.example.blindcommunity.UI.SignInActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LookupScreenFragment extends Fragment {

    HomeActivity homeActivity;
    ArrayList<HashMap<String,String>> post_list;

    SimpleAdapter adapter;
    int count;
    Button writePostButton;
    Button readMoreButton;

    ListView listview;

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

        count = 0;

        post_list = new ArrayList<HashMap<String,String>>();

        adapter = new SimpleAdapter(getActivity(), post_list,android.R.layout.simple_list_item_2,
                new String[]{"item1","item2","item3"},new int[]{android.R.id.text1,android.R.id.text2});

        listview = (ListView)view.findViewById(R.id.MyListview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> m_item = (HashMap<String, String>) adapter.getItem(position);
                String post_id = m_item.get("item3");

                homeActivity.moveToInsidePostActivity(post_id);
            }


        });

        writePostButton = view.findViewById(R.id.writePostButton);
        writePostButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeActivity.onChangeFragment(5);

            }
        });
        readMoreButton = view.findViewById(R.id.readmorebutton);
        readMoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                count+=10;
                sendJSONData();
            }
        });

        return view;
    }

    @Override
    public void onResume() { //이 화면으로 돌아왔을 때 게시물을 삭제하거나 변경사항이 있을 수 있으니 list 업데이트
        super.onResume();
        post_list.clear();
        count = 0;
        sendJSONData();
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
                Log.e("no post","자유게시판에 더이상 게시글이 없습니다.");
                count-=10;
                if(count<0){
                    count=0;
                }
                homeActivity.makeToast("더이상 게시물이 없습니다.");
            }
            else if(result.equals("00")){
                homeActivity.makeToast("더이상 게시물이 없습니다.");
                count-=10;
                if(count<0){
                    count=0;
                }
            }
            else if(result.equals("000")){
                homeActivity.makeToast("더이상 게시물이 없습니다.");
                count-=10;
                if(count<0){
                    count=0;
                }
            }
            else{
                Log.e("post exist","현재 게시판에 게시글이 있습니다.");

                try {
                    JSONArray jsonResult = new JSONArray(result);

                    HashMap<String,String> item;

                    for(int i=0;i<jsonResult.length();i++){
                        JSONObject parsedResult = new JSONObject(jsonResult.getString(i));
                        Log.e("received data : ","nickname : "+parsedResult.getString("nickname")
                                +", title : "+parsedResult.getString("title")
                                +", post_id : "+parsedResult.getString("post_id"));
                        item = new HashMap<String,String>();
                        item.put("item1",parsedResult.getString("title"));
                        item.put("item2"," - "+parsedResult.getString("nickname"));
                        item.put("item3",parsedResult.getString("post_id"));
                        post_list.add(item);

                    }
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
