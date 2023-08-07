package com.example.tookindfriendchat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import adapter.MessageAdapter;
import model.Message;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView tv_welcome;
    EditText et_msg;
    ImageButton btn_send;

    ArrayList<Message> messageList;
    MessageAdapter messageAdapter;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    String MY_SECRET_KEY = BuildConfig.MY_SECRET_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        tv_welcome = findViewById(R.id.tv_welcome);
        et_msg = findViewById(R.id.et_msg);
        btn_send = findViewById(R.id.btn_send);

//        recycler_view.setHasFixedSize(true);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setStackFromEnd(true);
//        recycler_view.setLayoutManager(manager);
//
//        messageList = new ArrayList<>();
//        messageAdapter = new MessageAdapter(messageList, LayoutInflater.from(this));
//        recycler_view.setAdapter(messageAdapter);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, LayoutInflater.from(this));
        listView.setAdapter(messageAdapter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = et_msg.getText().toString().trim();
                addToChat("당신", question, getCurrentTime(), Message.SENT_BY_ME);
                et_msg.setText("");
                callAPI(question);
                tv_welcome.setVisibility(View.GONE);
            }
        });


    }

    void addToChat(String name, String message, String time, String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(name, message, time, sentBy));
                messageAdapter.notifyDataSetChanged();
                listView.setSelection(messageList.size() - 1);
            }
        });
    }

    void addResponse(String name, String response){
        messageList.remove(messageList.size()-1);
        addToChat(name, response, getCurrentTime(), Message.SENT_BY_FRIEND);
    }

    String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // Set your desired format
        return sdf.format(new Date());
    }

    void callAPI(String question){
        //okhttp
        messageList.add(new Message("완벽한 친구", "...", getCurrentTime(), Message.SENT_BY_FRIEND));

        JSONObject object = new JSONObject();
        try {
            object.put("model", "text-davinci-003");
            object.put("prompt", question);
            object.put("max_tokens", 4000);
            object.put("temperature", 0);
        } catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer "+ MY_SECRET_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("완벽한 친구", "Failed to load response due to "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse("완벽한 친구", result.trim());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                } else {
                    addResponse("완벽한 친구", "Failed to load response due to "+response.body().string());
                }
            }
        });
    }
}
