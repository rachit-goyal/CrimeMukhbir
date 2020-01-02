package in.aaaonlineservice.crimemukhbir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

import static in.aaaonlineservice.crimemukhbir.MainActivity.MY_PREFS_NAME;


/**
 * Created by RACHIT on 6/1/2017.
 */

public class FcmMessaging extends FirebaseMessagingService {
    static int con=0;

    private Notificationutils notificationUtils;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        sendREgistrationToServer(s);
    }

        private void sendREgistrationToServer(final String recent_token) {
            String jsonstring="https://crimemukhbir.com/wp-json/apnwp/register?os_type=android&device_token="+recent_token.trim();
            StringRequest stringrequest=new StringRequest(Request.Method.GET, jsonstring, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            AppController.getInstance().addToRequestQueue(stringrequest);
        }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject json = new JSONObject(params);
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                boolean val=prefs.getBoolean("name",true);
                if (val) {

                    handleDataMessage(json);
                }
            } catch (Exception e) {
            }
        }
    }

    private void handleNotification(String message) {
        if (!Notificationutils.isAppIsInBackground(getApplicationContext())) {
            Intent pushNotification = new Intent(ConfigSync.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {

        try {

            String title = json.getString("title");
            String message = json.getString("message");
            //message=message.substring(0,20);





            if (Notificationutils.isAppIsInBackground(getApplicationContext())) {

                Intent resultIntent = new Intent(getApplicationContext(), NotiOpen.class);
                resultIntent.putExtra("title", title);
                    showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                con++;
                ShortcutBadger.applyCount(FcmMessaging.this,con);

            }
        } catch (JSONException e) {
        } catch (Exception e) {
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new Notificationutils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }


}
