package com.munatayev.timur.ibm.ebankingdemov3.Cloud;

import android.os.AsyncTask;

import com.google.api.client.util.Base64;
import com.google.gson.Gson;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.APIProperties;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.ConversationMessage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PostTask extends AsyncTask<ConversationMessage, Void, ConversationMessage> {

    @Override
    protected ConversationMessage doInBackground(ConversationMessage... params) {

        ConversationMessage messageResponse = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(APIProperties.getProperty("ServiceURI"));

        httppost.setHeader("Authorization","Basic "+Base64.encodeBase64String("user:passw00rd".getBytes()));
        httppost.setHeader("Content-Type", "application/json");

        try {

            String request = new Gson().toJson(params[0],ConversationMessage.class);

            StringEntity tmp = new StringEntity(request, "UTF-8");
            httppost.setEntity(tmp);

            HttpResponse httpResponse = httpclient.execute(httppost);
            HttpEntity entity = httpResponse.getEntity();

            InputStream inputStream = entity.getContent();

            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }

            JSONTokener jsonTokener = new JSONTokener(total.toString());
            JSONObject jsonObj = new JSONObject(jsonTokener);

            messageResponse = new Gson().fromJson(jsonObj.toString(),
                    ConversationMessage.class);

        } catch (Exception e) {
            System.out.print(e.toString());
        }

        return messageResponse;
    }
}