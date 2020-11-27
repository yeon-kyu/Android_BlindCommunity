package com.example.blindcommunity.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
    TextView m_date;
    EditText m_commentText;
    ImageButton m_sendCommentButton;

    Button editButton, deleteButton;
    ImageButton refreshButton;

    ArrayList<HashMap<String,String>> comment_list;
    CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insidepost);

        m_nickname = findViewById(R.id.postInsideNickname);
        m_title = findViewById(R.id.postInsideTitle);
        m_content = findViewById(R.id.postInsideContent);
        m_date = findViewById(R.id.postDate);

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
        refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateComment();
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
                else if(comment.length()==0){
                    makeToast("내용을 입력해주세요");
                    return;
                }

                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String DateAndTime = sdfNow.format(date);

                String para = "?post_id="+m_post_id+"&user_id="+cur_user_id+"&comment_content="+comment+"&comment_id="+DateAndTime+cur_user_id;

                task2.execute("http://13.125.232.199:3000/write_comment" + para);

            }

        });



//        adapter = new SimpleAdapter(this, comment_list,android.R.layout.simple_list_item_2,
//                new String[]{"item1","item2","item3"},new int[]{android.R.id.text1,android.R.id.text2});
        //item1 : 댓글 내용, item2 : 댓글 쓴 닉네임, item3: 댓글 쓴 id

        adapter = new CustomListAdapter();

        final ListView listview = (ListView)findViewById(R.id.CommentListview);
        listview.setAdapter(adapter);

        //adapter.addItem("1","2","3","4","5");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem m_item = (ListViewItem) adapter.getItem(position);
                String user_id = m_item.user_id; //TODO 나중에 댓글 삭제할 때 여기에 저장한 item3의 user_id를 사용함
                //makeToast("1. "+m_item.get("item1")+", 2. "+m_item.get("item2")+", 3. "+m_item.get("item3")+", 4. "+m_item.get("item4"));
                //1. 댓글 내용 2. 글쓴 닉네임, 3: 글쓴 아이디 4.comment_id
                if(m_item.user_id.equals(cur_user_id)){
                    //makeToast("댓글을 작성한 사람입니다.");
                    showDeleteCommentDialog(m_item.comment_id);
                    //TODO 이때 삭제기능 추가해야함
                }


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
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();
    }

    void showDeleteCommentDialog(final String comment_id){ //삭제하기 전에 다이얼로그 띄우고 삭제 명령이면 서버에 전송하기
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("댓글을 삭제할 수 있습니다. 삭제하시겠습니까?");
        builder.setPositiveButton("삭제",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        JSONDeleteCommentTaskGET task6 = new JSONDeleteCommentTaskGET();
                        String parameter = "?comment_id="+comment_id;
                        task6.execute("http://13.125.232.199:3000/delete_comment" + parameter);
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    private void updateComment(){
        //댓글 list 업데이트
        //comment_list.clear();
        adapter.getclear();

        String parameter = "?post_id="+m_post_id;
        JSONSearchCommentTaskGET task3 = new JSONSearchCommentTaskGET();
        task3.execute("http://13.125.232.199:3000/search_comment" + parameter);
    }

    //Toast를 짧은 코드로 사용하기 위한 함수
    private void makeToast(String string){
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }

    public class CustomListAdapter extends BaseAdapter {
        private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

        public void getclear(){
            listViewItemList.clear();
        }

        @Override
        public int getCount(){
            return listViewItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return listViewItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position,View convertView, ViewGroup parent){
            final int pos = position;
            final Context context = parent.getContext();
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.custom_layout, parent, false);
            }

            TextView writerTextView = (TextView) convertView.findViewById(R.id.writer);
            TextView contentTextView = (TextView) convertView.findViewById(R.id.content);
            TextView dateTextView = (TextView) convertView.findViewById(R.id.date);

            ListViewItem listViewItem = listViewItemList.get(position);

            writerTextView.setText(listViewItem.writer);
            contentTextView.setText(listViewItem.content);
            dateTextView.setText(listViewItem.date);

            return convertView;

        }
        public void addItem(String writer, String content, String date,String user_id, String comment_id){
            ListViewItem item = new ListViewItem();

            item.writer = writer;
            item.content = content;
            item.date = date;
            item.user_id = user_id;
            item.comment_id = comment_id;
            listViewItemList.add(item);
        }
    }

    private class ListViewItem{
        public String writer,content,date,user_id,comment_id;

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
                        String dateString = m_post_id.substring(2,4)+"/"+m_post_id.substring(4,6)+"/"+m_post_id.substring(6,8)
                                +" "+m_post_id.substring(9,11)+":"+m_post_id.substring(11,13);
                        m_date.setText(dateString);

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

                updateComment();

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
                                +", user_id : "+parsedResult.getString("user_id")
                                +", comment_id : "+parsedResult.getString("comment_id"));

                        //TODO 닉네임과 코멘트 내용으로 listview 구현
                        String comment_id=parsedResult.getString("comment_id");
                        String date = comment_id.substring(2,4)+"/"+comment_id.substring(4,6)+"/"
                                +comment_id.substring(6,8)+" "+comment_id.substring(9,11)+":"+comment_id.substring(11,13);

                        adapter.addItem(parsedResult.getString("nickname"),parsedResult.getString("comment_content"),
                                date,parsedResult.getString("user_id"),comment_id);

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
                //TODO 글쓴이에게 수정 기능 제공
                //editButton.setVisibility(View.VISIBLE);
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
    private class JSONDeleteCommentTaskGET extends JsonTaskModel{
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
                Log.e("fail","댓글 삭제에 실패하였습니다.");

           }
            else if(result.equals("1")){
                Log.e("success","댓글 삭제 성공");
                //댓글 list 업데이트
                updateComment();
            }
        }
    }
}
