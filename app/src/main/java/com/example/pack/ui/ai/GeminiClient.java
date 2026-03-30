package com.example.pack.ui.ai;

//import androidx.test.espresso.web.util.concurrent.FutureCallback;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.GenerationConfig;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
//import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executors;

public class GeminiClient {

    public interface GeminiResponseListener {
        void onSuccess(String text);
        void onError(String error);
    }

    public void generate(String prompt, GeminiResponseListener listener) {
        GenerationConfig.Builder configBuilder = new GenerationConfig.Builder();
        configBuilder.temperature = 0.2f;

        GenerativeModel gm = new GenerativeModel(
                "gemini-3-flash-preview",
                "...........",
                configBuilder.build()//,
                //apiVersion:
        );
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder().addText(prompt).build();
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String text = result.getText();
                if (text != null) {
                    text = text.replace("```json", "").replace("```", "").trim();
                }
                listener.onSuccess(text);
            }

            @Override
            public void onFailure(Throwable t) {
                listener.onError(t.getMessage());
            }
        }, Executors.newSingleThreadExecutor());
    }
}
