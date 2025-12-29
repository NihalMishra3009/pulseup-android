package com.example.martyapp;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class nayachabot_varvanta extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter chatAdapter;
    private List<Message> chatList;
    private EditText messageEditText;
    private ImageButton sendButton;
    private TextView welcomeTextView;

    private static final String BASE_URL = "https://openrouter.ai/api/v1/";
    private static final String API_KEY = "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";
    private GeminiApi geminiApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_ui);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0F3E4D")); // Replace with your hex color
        window.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        setDarkNavigationIcons(window);

        Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);

        // Enable Back Button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle Back Button Click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Go back to previous activity
            }
        });

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = findViewById(R.id.recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);
        welcomeTextView = findViewById(R.id.welcome_text);

        chatList = new ArrayList<>();
        chatAdapter = new MessageAdapter(chatList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getSecureOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        geminiApi = retrofit.create(GeminiApi.class);

        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                if (chatList.isEmpty()) {
                    welcomeTextView.setVisibility(View.GONE);
                }

                chatList.add(new Message(message, true));
                chatAdapter.notifyItemInserted(chatList.size() - 1);
                recyclerView.scrollToPosition(chatList.size() - 1);
                messageEditText.setText("");

                sendMessageToGemini(message);
            }
        });
    }

    private OkHttpClient getSecureOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .addHeader("Authorization", "Bearer " + API_KEY)
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(request);
                })
                .build();
    }

    private void sendMessageToGemini(String prompt) {
        List<GeminiRequest.Message> messages = new ArrayList<>();
        messages.add(new GeminiRequest.Message("user", prompt));

        GeminiRequest request = new GeminiRequest("openai/gpt-3.5-turbo", messages);

        geminiApi.generateContent("chat/completions", request).enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                String reply = "Sorry, no response.";
                if (response.isSuccessful() && response.body() != null) {
                    GeminiResponse resp = response.body();
                    Log.d("GeminiResponse", new Gson().toJson(resp));
                    if (!resp.getChoices().isEmpty()) {
                        reply = resp.getChoices().get(0).getMessage().getContent();
                    }
                } else {
                    Log.e("GeminiResponse", "Error: " + response.code());
                }

                chatList.add(new Message(reply, false));
                chatAdapter.notifyItemInserted(chatList.size() - 1);
                recyclerView.scrollToPosition(chatList.size() - 1);
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                Log.e("GeminiFailure", t.getMessage(), t);
                chatList.add(new Message("Failed to reach API", false));
                chatAdapter.notifyItemInserted(chatList.size() - 1);
                recyclerView.scrollToPosition(chatList.size() - 1);
            }
        });
    }
    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }
}
