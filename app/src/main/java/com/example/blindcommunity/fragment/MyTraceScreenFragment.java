package com.example.blindcommunity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.fragment.app.Fragment;

import com.example.blindcommunity.Model.JsonTaskModel;
import com.example.blindcommunity.R;
import com.example.blindcommunity.UI.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyTraceScreenFragment extends Fragment {

    HomeActivity homeActivity;
    ArrayList<HashMap<String,String>> post_list;

    SimpleAdapter adapter;

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

        View view = inflater.inflate(R.layout.fragment_tracescreen, container, false);
        post_list = new ArrayList<HashMap<String,String>>();

        adapter = new SimpleAdapter(getActivity(), post_list,android.R.layout.simple_list_item_2,
                new String[]{"item1","item2","item3"},new int[]{android.R.id.text1,android.R.id.text2});

        final ListView listview = (ListView)view.findViewById(R.id.TraceListview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> m_item = (HashMap<String, String>) adapter.getItem(position);
                String post_id = m_item.get("item3");

                homeActivity.moveToInsidePostActivity(post_id);
            }
        });

        JSONTaskGET task = new JSONTaskGET();
        String parameter = "?user_id="+homeActivity.cur_user_id;

        task.execute("http://13.125.232.199:3000/find_trace" + parameter);

        return view;

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
                Log.i("success","게시물 찾기 성공. 그러나 게시물 존재하지 않음");

            }
            else if(result.equals("-1")){
                Log.e("fail","게시물 찾기 에러");
            }
            else{
                try {
                    JSONArray jsonResult = new JSONArray(result);
                    HashMap<String,String> item;

                    for(int i=0;i<jsonResult.length();i++){ //search_free_content
                        JSONObject parsedResult = new JSONObject(jsonResult.getString(i));
                        Log.e("received posts : ","title : "+parsedResult.getString("title")
                                +", post_id : "+parsedResult.getString("post_id")
                                +", post_type : "+parsedResult.getString("post_type"));

                        //TODO 닉네임과 코멘트 내용으로 listview 구현
                        item = new HashMap<String,String>();
                        item.put("item1",parsedResult.getString("title"));
                        item.put("item2"," [ "+parsedResult.getString("post_type")+" ] ");
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
