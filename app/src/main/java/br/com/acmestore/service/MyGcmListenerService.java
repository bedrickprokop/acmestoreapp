package br.com.acmestore.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

import br.com.acmestore.Constants;
import br.com.acmestore.R;
import br.com.acmestore.data.entity.MessageQueue;
import br.com.acmestore.data.entity.User;
import br.com.acmestore.product.productdetail.ProductDetailActivity;

public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        String message = bundle.getString("message");
        MessageQueue messageQueue = new Gson().fromJson(message, MessageQueue.class);

        sendNotification(messageQueue);
    }

    private void sendNotification(MessageQueue messageQueue) {
        Intent intent = new Intent(this, ProductDetailActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.INTENT_KEY_FROMVIEW, messageQueue.getFromView());
        intent.putExtra(Constants.INTENT_KEY_PRODUCTID, messageQueue.getProductId());
        intent.putExtra(Constants.INTENT_KEY_USER, messageQueue.getUser());
        intent.putExtra(Constants.INTENT_KEY_FROMNOTIFICATION, messageQueue.getFromNotification());

        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //Build a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("ACME Store")
                .setContentText(messageQueue.getMessage())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
        //0 = ID da notificação
    }
}
