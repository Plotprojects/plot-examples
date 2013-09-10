package com.plotprojects.coupons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.plotprojects.retail.android.FilterableNotification;

public class CouponNotificationOpenReceiver extends BroadcastReceiver {

    private static String LOG_TAG = "Plot Coupon Example/CouponNotificationOpenReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        FilterableNotification notification = intent.getParcelableExtra("notification");
        Log.d(LOG_TAG, "Opening notification: " + notification.toString());
        Intent openIntent = new Intent(context, ViewCouponActivity.class);
        openIntent.setAction(ViewCouponActivity.ACTION);
        openIntent.putExtras(intent.getExtras());
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(openIntent);
    }
}
