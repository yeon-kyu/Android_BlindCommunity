package com.example.blindcommunity.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blindcommunity.Model.JsonTaskModel;
import com.example.blindcommunity.R;
import com.example.blindcommunity.fragment.LookupScreenFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class InsidePostActivity extends Activity {

    String m_post_id;
    String cur_user_id;
    int employ_type;

    TextView m_nickname, m_title, m_content;
    EditText m_commentText;
    ImageButton m_sendCommentButton;

    Button editButton, deleteButton;

    ArrayList<HashMap<String,String>> comment_list;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insidepost);

        m_nickname = findViewById(R.id.postInsideNickname);
        m_title = findViewById(R.id.postInsideTitle);
        m_content = findViewById(R.id.postInsideContent);

        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showEditDialog();
            }
        });
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDiaglog();
            }
        });

        Intent intent = getIntent();
        m_post_id = intent.getStringExtra("post_id");
        employ_type = intent.getIntExtra("employ_type",0);
        cur_user_id = intent.getStringExtra("user_id");

        m_commentText = findViewById(R.id.commentText);
        m_sendCommentButton = findViewById(R.id.sendCommentButton);
        m_sendCommentButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                JSONAddCommentTaskGET task2 = new JSONAddCommentTaskGET();
                String comment = m_commentText.getText().toString();

                if(comment.length()>200){
                    makeToast("댓글은 200자 이내로 작성해 주십시오");
                    return;
                }

                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String DateAndTime = sdfNow.format(date);

                String para = "?post_id="+m_post_id+"&user_id="+cur_user_id+"&comment_content="+comment+"&comment_id="+DateAndTime;

                task2.execute("http://13.125.232.199:3000/write_comment" + para);

            }

        });

        comment_list = new ArrayList<HashMap<String,String>>();

        adapter = new SimpleAdapter(this, comment_list,android.R.layout.simple_list_item_2,
                new String[]{"item1","item2","item3"},new int[]{android.R.id.text1,android.R.id.text2});
        //item1 : 댓글 내용, item2 : 댓글 쓴 닉네임, item3: 댓글 쓴 id

        final ListView listview = (ListView)findViewById(R.id.CommentListview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> m_item = (HashMap<String, String>) adapter.getItem(position);
                String user_id = m_item.get("item3"); //TODO 나중에 댓글 삭제할 때 여기에 저장한 item3의 user_id를 사용함

            }


        });

        //해당 게시물의 내용을 보기 위해 post_id를 서버에 전송하여 그 내용을 받아온다.
        JSONTaskGET task1 = new JSONTaskGET();
        String parameter = "?post_id="+m_post_id;

        if(employ_type==1)
            task1.execute("http://13.125.232.199:3000/search_free_content" + parameter);
        else if(employ_type==2)
            task1.execute("http://13.125.232.199:3000/search_info_content" + parameter);
        else if(employ_type==3)
            task1.execute("http://13.125.232.199:3000/search_employ_content" + parameter);

        //해당 게시물에 달린 댓글을 확인하여 그 갯수만큼 Listview에 추가한다.
        JSONSearchCommentTaskGET task3 = new JSONSearchCommentTaskGET();
        task3.execute("http://13.125.232.199:3000/search_comment" + parameter);

        //해당 게시물의 글쓴이가 현재 사용자인지 확인하여 맞으면 수정/삭제 버튼 보이도록 한다.
        JSONCheckIfWriterTaskGET task4 = new JSONCheckIfWriterTaskGET();
        task4.execute("http://13.125.232.199:3000/check_writerOrNot" + parameter+"&user_id="+cur_user_id);


    }
    void showDeleteDiaglog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("AlertDialog Title");
        builder.setMessage("삭제하시겠습나까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                        JSONDeletePostTaskGET task5 = new JSONDeletePostTaskGET();
                        String parameter = "?post_id="+m_post_id+"&post_type="+employ_type;
                        task5.execute("http://13.125.232.199:3000/delete_post" + parameter);

                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    void showEditDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("수정하시겠습나까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    //Toast를 짧은 코드로 사용하기 위한 함수
    private void makeToast(String string){
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
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
            if(result.equals("-1")){
                Log.e("fail","해당 post를 찾지 못했습니다.");
                makeToast("해당 post를 찾지 못했습니다.");
            }
            else{
                try {
                    JSONArray jsonResult = new JSONArray(result);

                    for(int i=0;i<jsonResult.length();i++){ //search_free_content
                        JSONObject parsedResult = new JSONObject(jsonResult.getString(i));
                        Log.e("received data : ","nickname : "+parsedResult.getString("nickname")
                                +", title : "+parsedResult.getString("title")
                                +", content : "+parsedResult.getString("content"));
                        m_nickname.setText(parsedResult.getString("nickname"));
                        m_title.setText(parsedResult.getString("title"));
                        m_content.setText(parsedResult.getString("content"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class JSONAddCommentTaskGET extends JsonTaskModel{
        public void setData(ArrayList<Data> data){
            super.setData(data);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result == null){
                return;
            }
            if(result.equals("-1")){
                Log.e("fail","댓글을 쓰기에 실패하였습니다.");
                makeToast("댓글을 쓰기에 실패하였습니다.");
            }
            else if(result.equals("1")){
                Log.e("success","댓글 쓰기 성공.");
                m_commentText.setText("");
                makeToast("댓글을 작성하였습니다.");

                //댓글 list 업데이트
                comment_list.clear();

                String parameter = "?post_id="+m_post_id;
                JSONSearchCommentTaskGET task3 = new JSONSearchCommentTaskGET();
                task3.execute("http://13.125.232.199:3000/search_comment" + parameter);

            }
        }
    }

    private class JSONSearchCommentTaskGET extends JsonTaskModel{
        public void setData(ArrayList<Data> data){
            super.setData(data);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result == null){
                return;
            }
            if(result.equals("-1")){
                Log.e("fail","댓글을 검색 에러");
                makeToast("댓글 찾기에 실패하였습니다.");
            }
            else if(result.equals("0")){
                Log.e("success","댓글 찾기 성공. 그러나 댓글 존재하지 않음");
            }
            else{
                try {
                    JSONArray jsonResult = new JSONArray(result);
                    HashMap<String,String> item;

                    for(int i=0;i<jsonResult.length();i++){ //search_free_content
                        JSONObject parsedResult = new JSONObject(jsonResult.getString(i));
                        Log.e("received comment : ","nickname : "+parsedResult.getString("nickname")
                                +", comment_content : "+parsedResult.getString("comment_content")
                                +", user_id : "+parsedResult.getString("user_id"));

                        //TODO 닉네임과 코멘트 내용으로 listview 구현
                        item = new HashMap<String,String>();
                        item.put("item1",parsedResult.getString("comment_content"));
                        item.put("item2"," - "+parsedResult.getString("nickname"));
                        item.put("item3",parsedResult.getString("user_id"));
                        comment_list.add(item);

                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class JSONCheckIfWriterTaskGET extends JsonTaskModel{
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
                Log.e("not writer","글쓴이가 아닙니다");

            }
            else if(result.equals("1")){
                Log.e("you are the writer","글쓴이 입니다.");
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private class JSONDeletePostTaskGET extends JsonTaskModel{
        public void setData(ArrayList<Data> data){
            super.setData(data);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result == null){
                return;
            }
            if(result.equals("-1")){
                Log.e("fail","게시글 삭제에 실패하였습니다.");

            }
            else if(result.equals("1")){
                Log.e("success","게시글 삭제 성공");
                finish();
            }
        }
    }
}
