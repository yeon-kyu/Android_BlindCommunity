package com.example.blindcommunity.Model;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class JsonTaskModel extends AsyncTask<String, String, String> {
    ArrayList<Data> data = new ArrayList<>();
    public void setData(ArrayList<Data> data){
        this.data = data;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
            JSONObject jsonObject = new JSONObject();
            for (int i=0; i<data.size(); i++){
                jsonObject.accumulate(data.get(i).key, data.get(i).value);
            }
            HttpURLConnection con = null;
            BufferedReader reader = null;
            try{
                URL url = new URL(urls[0]);
                //연결을 함
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                con.connect();
                //서버로 부터 데이터를 받음
                reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con != null){
                    con.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();//버퍼를 닫아줌
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result == null){
            Log.e("onPostExecute warning","result == null");
            return;
        }

    }
    public class Data {
        public String key;
        public String value;
        Data(String key, String value){
            this.key = key;
            this.value = value;
        }
    }
}