package com.petopia.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends MainActivity {
    private EditText etMessage;
    private ImageView btnSend;
    private OkHttpClient client;
    private String email;
    private int senderId;
    private List<ChatMessage> messageList;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private ImageView btnBreed;

    String acceptingPetID;
    String acceptedPetID;
    ImageView backBtn;

    private Handler chatHandler;
    private static final int CHAT_REFRESH_INTERVAL = 2000; // 2 seconds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backBtn = findViewById(R.id.backBtn);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        setupRecyclerView();

        email = sharedPreferences.getString("email","");

        LoadPreviousChatMessagesTask task = new LoadPreviousChatMessagesTask();
        task.execute();

        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("Chat", Context.MODE_PRIVATE);

        int userId = sharedPreferences.getInt("user_id", -1); // -1 is the default value if the key is not found
        String userName = sharedPreferences.getString("receiver_name", "");
        acceptingPetID = sharedPreferences.getString("acceptingPetID", "");
        acceptedPetID = sharedPreferences.getString("acceptedPetID", "");

// You can now use these values as needed in your activity


        chatHandler = new Handler();
        startChatMessageRefresh();

        // Inside the onClick method of btnSend
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = etMessage.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    etMessage.setText("");

                    // Send the message to the PHP backend
                    sendMessageToBackend(messageText);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnBreed = findViewById(R.id.breedingrequest);

        btnBreed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, BreedActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startChatMessageRefresh() {
        chatHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadPreviousChatMessagesTask task = new LoadPreviousChatMessagesTask();
                task.execute();
                chatHandler.postDelayed(this, CHAT_REFRESH_INTERVAL); // Schedule the next refresh
            }
        }, CHAT_REFRESH_INTERVAL); // Start the first refresh
    }


    private void sendMessageToBackend(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Replace with your PHP backend URL
                    String url = "https://pawsomematch.online/android/send_message.php";

                    // Create JSON request body
                    JSONObject requestBody = new JSONObject();
                    requestBody.put("acceptingPetID", acceptingPetID);
                    requestBody.put("acceptedPetID", acceptedPetID);
                    requestBody.put("message", message);

                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, requestBody.toString());

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    client = new OkHttpClient();
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        final String responseData = response.body().string();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showMessageInChat(message, true); // Add the sent message to the chat view
                                Toast.makeText(ChatActivity.this, responseData, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.e("HTTP Request", "Unexpected response code: " + response.code());
                    }
                } catch (Exception e) {
                    Log.e("HTTP Request", "Exception: " + e.getMessage());
                }
            }
        }).start();
    }

    private class LoadPreviousChatMessagesTask extends AsyncTask<Void, Void, List<ChatMessage>> {

        @Override
        protected List<ChatMessage> doInBackground(Void... voids) {
            // Replace with your PHP backend URL
            String url = "https://pawsomematch.online/android/get_messages.php";

            try {
                // Create JSON request body
                JSONObject requestBody = new JSONObject();
                requestBody.put("acceptingPetID", acceptingPetID);
                requestBody.put("acceptedPetID", acceptedPetID);

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, requestBody.toString());

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                OkHttpClient client = new OkHttpClient();
                try {
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        return parseChatMessages(response.body().string());
                    } else {
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ChatMessage> chatMessages) {
            if (chatMessages != null && !chatMessages.isEmpty()) {
                for (ChatMessage newMessage : chatMessages) {
                    // Check if the new message is not already in the messageList
                    boolean isNewMessage = true;
                    for (ChatMessage existingMessage : messageList) {
                        if (newMessage.getMessage().equals(existingMessage.getMessage())) {
                            isNewMessage = false;
                            break;
                        }
                    }

                    if (isNewMessage) {
                        messageList.add(newMessage);
                        adapter.notifyItemInserted(messageList.size() - 1);
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                }
            }
        }

        private List<ChatMessage> parseChatMessages(String responseData) {
            List<ChatMessage> chatMessages = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(responseData);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int messageId = jsonObject.getInt("id");
                    int senderId = jsonObject.getInt("sender");
                    int receiverId = jsonObject.getInt("receiver");
                    String message = jsonObject.getString("message");
                    String timestamp = jsonObject.getString("timestamp");

                    int acceptingPetIDInt = Integer.parseInt(acceptingPetID);

                    // Replace with your appropriate values for acceptingPetID and acceptedPetID
                    boolean isSentByCurrentUser = senderId == acceptingPetIDInt;
                    ChatMessage chatMessage = new ChatMessage(message, isSentByCurrentUser, timestamp);
                    chatMessages.add(chatMessage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Response", responseData);
            }

            return chatMessages;
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(messageList);
        recyclerView.setAdapter(adapter);
    }

    private void showMessageInChat(final String message, final boolean isSentByCurrentUser) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String timestamp = ""; // Provide the appropriate timestamp here
                ChatMessage chatMessage = new ChatMessage(message, isSentByCurrentUser, timestamp);
                messageList.add(chatMessage);
                adapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
            }
        });
    }


}
