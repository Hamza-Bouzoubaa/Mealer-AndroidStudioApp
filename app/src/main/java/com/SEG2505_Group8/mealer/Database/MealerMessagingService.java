package com.SEG2505_Group8.mealer.Database;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MealerMessagingService extends FirebaseMessagingService {

    private static final String orderStatusType = "orderStatus";

    public void onNewToken(@NonNull String token) {
        Services.getDatabaseClient().updateUserToken();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {

            Map<String, String> data = remoteMessage.getData();

            String type = data.get("type");

            if (type != null && type.equals(orderStatusType)) {
                showOrderNotification(data.get("orderId"));
            }
        }
    }

    public void showOrderNotification(String orderId) {

        Services.getDatabaseClient().getOrder(orderId, order -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "orders");
            builder.setSmallIcon(R.drawable.mealerlogo);
            builder.setAutoCancel(true);
            builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
            builder.setOnlyAlertOnce(true);
            builder.setContentIntent(pendingIntent);
            builder.setContentTitle("Mealer Order Status");
            builder.setContentText(order.getStatus().toString());

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("orders", "mealer", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);

            notificationManager.notify(0, builder.build());
        });
    }
}
