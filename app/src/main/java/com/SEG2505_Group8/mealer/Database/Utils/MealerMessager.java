package com.SEG2505_Group8.mealer.Database.Utils;

import android.content.Context;
import android.os.AsyncTask;

import com.google.auth.oauth2.GoogleCredentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Inspired from https://stackoverflow.com/questions/66919302/how-can-i-send-notification-to-specific-user-using-the-users-token-in-firebase
 * Updated to follow Firebase's v1 auth endpoints and message implementation
 */
public class MealerMessager {

    private static final String[] SCOPES = { "https://www.googleapis.com/auth/firebase.messaging" };

    private final Context context;

    public MealerMessager(Context context) {
        this.context = context;
    }

    public void notifyClientOrder(String orderId, String clientMessagingToken) {
        Map<String, String> data = new HashMap<>();
        data.put("orderId", orderId);
        data.put("type", "orderStatus");
        sendMessage(clientMessagingToken, "Order Status Updated", "Your order has been updated", data);
    }

    private void sendMessage(final String recipient, final String title, final String body, final Map<String, String> dataMap)
    {
        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("body", body);
        notificationMap.put("title", title);

        Map<String, Object> rootMap = new HashMap<>();
        Map<String, Object> messageMap = new HashMap<>();

        messageMap.put("notification", notificationMap);
        messageMap.put("token", recipient);
        if (dataMap != null)
            messageMap.put("data", dataMap);

        rootMap.put("message", messageMap);

        new SendFCM().setFcm(rootMap).execute();
    }

    class SendFCM extends AsyncTask<String, String, String> {

        private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/v1/projects/seg-mealer/messages:send";
        private Map<String, Object> fcm;

        SendFCM setFcm(Map<String, Object> fcm) {
            this.fcm = fcm;
            return this;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, new JSONObject(fcm).toString());

                /**
                 *
                 */

                int identifier = context.getResources().getIdentifier("serviceaccount", "raw", context.getPackageName());

                // Service account credentials not provided
                if (identifier == 0) {
                    System.out.println("Service Account Credentials not provided! Notification Sending disabled.");
                    return "";
                }

                // Create OAuth Access Token using Service Account credentials
                GoogleCredentials creds = GoogleCredentials.fromStream(context.getResources().openRawResource(identifier)).createScoped(Arrays.asList(SCOPES));
                creds.refresh();
                creds.refreshAccessToken();

                String token = creds.getAccessToken().getTokenValue();

                Request request = new Request.Builder()
                        .url(FCM_MESSAGE_URL)
                        .post(body)
                        .addHeader("Authorization","Bearer " + token)
                        .build();
                Response response = new OkHttpClient().newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultJson = new JSONObject(result);
                int success, failure;
                success = resultJson.getInt("success");
                failure = resultJson.getInt("failure");
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

}