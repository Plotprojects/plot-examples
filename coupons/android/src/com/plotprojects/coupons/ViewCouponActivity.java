package com.plotprojects.coupons;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.plotprojects.retail.android.FilterableNotification;
import org.json.JSONObject;

public class ViewCouponActivity extends Activity {

    public static final String ACTION = "com.plotprojects.retail.android.example.OPEN_NOTIFICATION";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get notification from the intent
        FilterableNotification notification = getIntent().getParcelableExtra("notification");
        try {
            String category = new JSONObject(notification.getData()).getString("category");
            setTitle("Plot Coupon Example: " + category);
        } catch (Exception e) {
            return;
        }
        setContentView(R.layout.coupon);
        ((TextView)findViewById(R.id.messageTextView)).setText(notification.getMessage());
    }
}