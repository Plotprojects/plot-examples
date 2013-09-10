package com.plotprojects.coupons;

import android.util.Log;
import com.plotprojects.retail.android.FilterableNotification;
import com.plotprojects.retail.android.NotificationFilterReceiver;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryNotificationFilterReceiver extends NotificationFilterReceiver  {

    private static final String LOG_TAG = "Plot Coupon Example/CategoryNotificationFilterReceiver";

    @Override
    public List<FilterableNotification> filterNotifications(List<FilterableNotification> filterableNotifications) {
        // Get the users preferences
        Settings settings = new Settings(getApplicationContext());
        List<FilterableNotification> filteredNotifications = new ArrayList<FilterableNotification>(filterableNotifications.size());
        for (FilterableNotification notification : filterableNotifications) {
            try {
                String category = new JSONObject(notification.getData()).getString("category").toLowerCase();
                if (settings.getBoolean(category, false)) {
                    Log.d(LOG_TAG, "Category active: " + category);
                    filteredNotifications.add(notification);
                } else {
                    Log.d(LOG_TAG, "Category not active: " + category);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Malformed notification: " + notification.getData(), e);
            }
        }
        return filteredNotifications;
    }
}
