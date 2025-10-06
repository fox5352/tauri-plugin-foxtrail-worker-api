package com.plugin.foxtrailworker;

import android.content.Context;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.ApplicationInfo;

import java.util.Random;


public class Notification {
    private Context context;
    private Random random = new Random();

    public Notification(Context context) {
        this.context = context;
    }

    public boolean checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public void requestNotificationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
        }
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Get app name from package manager
            String appName;
            try {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo ai = context.getApplicationInfo();
                appName = (String) pm.getApplicationLabel(ai);
            } catch (Exception e) {
                appName = "App"; // fallback
            }

            NotificationChannel channel = new NotificationChannel(
                    "default_channel",
                    appName + " Updates",  // This will show your actual app name
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("Notifications from " + appName);

            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);
        }
    }

    public void sendNotification(String title, String message) {
        try {
            // CHECK PERMISSION FIRST
            if (!checkNotificationPermission()) {
                Log.e("Notification", "No notification permission");
                return;
            }

            clearAllNotifications();

            Intent intent = context.getPackageManager()
                    .getLaunchIntentForPackage(context.getPackageName());


            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // --- START OF ICON LOOKUP AND FALLBACK ---
            int smallIconId = 0;
            try {
                // 1. Try to find the custom icon ('ic_notification') in your plugin's resources
                smallIconId = context.getResources().getIdentifier(
                        "ic_notification", "drawable", context.getPackageName());

            } catch (Exception e) {
                // Log the error but proceed to fallback
                Log.e("Notification", "Error finding custom icon: " + e.getMessage());
            }

            // 2. If the custom icon is not found (ID is 0), use a reliable system icon
            if (smallIconId == 0) {
                smallIconId = android.R.drawable.ic_dialog_info; // A standard, safe system icon
                Log.w("Notification", "Custom icon 'ic_notification' not found. Falling back to system icon.");
            }
            // --- END OF ICON LOOKUP AND FALLBACK ---

            int notificationId = random.nextInt(1000);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default_channel")
                    // Use the determined icon ID (custom or system fallback)
                    .setSmallIcon(smallIconId)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(false)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat.from(context).notify(notificationId, builder.build());
            Log.d("Notification", "Notification sent - " + title);

        } catch (SecurityException e) {
            Log.e("Notification", "SecurityException - No permission: " + e.getMessage());
        } catch (Exception e) {
            Log.e("Notification", "Notification failed: " + e.getMessage());
        }
    }

    public void clearAllNotifications() {
        NotificationManagerCompat.from(context).cancelAll();
    }

    public void clearNotificationById(int notificationId) {
        NotificationManagerCompat.from(context).cancel(notificationId);
    }
}
