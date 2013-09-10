package com.plotprojects.coupons;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.plotprojects.retail.android.Plot;
import com.plotprojects.retail.android.PlotConfiguration;

public class MainActivity extends Activity
{
    final static Object lock = new Object();
    final static String LOG_TAG = "Plot Coupon Example/MainActivity";
    final static String PLOT_ACCOUNT_ID = "<my account id>";
    final static String PLOT_PUBLIC_KEY = "<my public key>";
    final static String PLOT_PRIVATE_KEY = "<my private key>";

    private static PlotConfiguration plotConfiguration = new PlotConfiguration(PLOT_PUBLIC_KEY);

    private CouponService couponService;
    private Settings settings;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Plot.init(getApplicationContext(), plotConfiguration);
        couponService = new CouponService(PLOT_ACCOUNT_ID, PLOT_PRIVATE_KEY);
        settings = new Settings(getApplicationContext());
        setTitle("Plot Coupon Example");
        setContentView(R.layout.main);
        // Create tabs
        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("categories").setIndicator("Categories").setContent(R.id.categories_tab));
        tabHost.addTab(tabHost.newTabSpec("coupons").setIndicator("Coupons").setContent(R.id.coupons_tab));
        // Restore state
        restoreState();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveState();
    }

    public void createCoupon(View button) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Saving");
        progress.setMessage("Wait while saving...");
        progress.show();

        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                Location loc = getLocation();
                if (loc == null) {

                    return "Unable to get current location";
                }
                if (params[0].trim().isEmpty()) {
                    return "No message entered";
                }
                Coupon coupon = new Coupon(loc, params[0], params[1]);
                try {
                    String placeId = couponService.createPlace(coupon.getLocation());
                    String notificationId = couponService.createNotification(placeId, coupon.getMessage(), coupon.getCategory());
                    return "Created coupon notification with id " + notificationId;

                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error storing coupon", e);
                    return "Error storing coupon";
                }
            }

            protected void onPostExecute(String result) {
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
                progress.hide();
            }
        };
        String message = ((EditText)findViewById(R.id.message)).getText().toString();
        String category = ((Spinner)findViewById(R.id.cat)).getSelectedItem().toString();
        task.execute(message, category);
    }

    private void restoreState() {
        Log.d(LOG_TAG, "restoreState");
        ((ToggleButton)findViewById(R.id.fastFoodButton)).setChecked(settings.getBoolean(Settings.FASTFOOD_PREFERENCE, false));
        ((ToggleButton)findViewById(R.id.filmButton)).setChecked(settings.getBoolean(Settings.FILM_PREFERENCE, false));
        ((ToggleButton)findViewById(R.id.electronicsButton)).setChecked(settings.getBoolean(Settings.ELECTRONICS_PREFERENCE, false));
        ((ToggleButton)findViewById(R.id.wellnessButton)).setChecked(settings.getBoolean(Settings.WELLNESS_PREFERENCE, false));
        ((ToggleButton)findViewById(R.id.diningButton)).setChecked(settings.getBoolean(Settings.DINING_PREFERENCE, false));
        ((ToggleButton)findViewById(R.id.eventsButton)).setChecked(settings.getBoolean(Settings.EVENTS_PREFERENCE, false));
    }

    private void saveState() {
        Log.d(LOG_TAG, "saveState");
        settings.setBoolean(Settings.FASTFOOD_PREFERENCE, ((ToggleButton) findViewById(R.id.fastFoodButton)).isChecked());
        settings.setBoolean(Settings.FILM_PREFERENCE, ((ToggleButton) findViewById(R.id.filmButton)).isChecked());
        settings.setBoolean(Settings.ELECTRONICS_PREFERENCE, ((ToggleButton) findViewById(R.id.electronicsButton)).isChecked());
        settings.setBoolean(Settings.WELLNESS_PREFERENCE, ((ToggleButton) findViewById(R.id.wellnessButton)).isChecked());
        settings.setBoolean(Settings.DINING_PREFERENCE, ((ToggleButton) findViewById(R.id.diningButton)).isChecked());
        settings.setBoolean(Settings.EVENTS_PREFERENCE, ((ToggleButton) findViewById(R.id.eventsButton)).isChecked());
    }

    private Location getLocation() {
        synchronized(lock) {
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Location networkLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (networkLocation != null) return networkLocation;
            return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
    }

}
