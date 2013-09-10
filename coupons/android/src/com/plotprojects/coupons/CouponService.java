package com.plotprojects.coupons;

import android.location.Location;
import android.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CouponService {

    private static String LOG_TAG = "Plot Coupon Example/CouponService";
    private static String API_URL = "https://admin.plotprojects.com/api/v1/";
    private String accountId;

    public CouponService(String accountId, String privateKey) {
        this.accountId = accountId;
        this.privateKey = privateKey;
    }

    private String privateKey;

    public String createPlace(Location location) throws JSONException, IOException {
        HttpURLConnection urlConnection = null;
        try {
            // Create the payload
            byte[] body = new JSONObject()
                    .put("name", "Generated Place")
                    .put("location", new JSONObject()
                            .put("latitude", location.getLatitude())
                            .put("longitude", location.getLongitude())
                    ).toString().getBytes("UTF-8");
            // Create place through API
            URL url = new URL(API_URL + "place");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            String credentials =
                    Base64.encodeToString((accountId + ":" + privateKey).getBytes(), Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", "Basic " + credentials);
            OutputStream out = urlConnection.getOutputStream();
            out.write(body);
            int status = urlConnection.getResponseCode();
            if (status >= 200 && status < 300) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                byte[] readBuffer = new byte[1024];
                in.read(readBuffer);
                JSONObject response = new JSONObject(new String(readBuffer, "UTF-8"));
                return response.getString("result");
            } else {
                InputStream in = new BufferedInputStream(urlConnection.getErrorStream());
                byte[] readBuffer = new byte[1024];
                in.read(readBuffer);
                throw new IOException(new String(readBuffer));
            }
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
    }

    public String createNotification(String placeId, String message, String category) throws JSONException, IOException {
        HttpURLConnection urlConnection = null;
        try {
            // Create the payload
            byte[] body = new JSONObject()
                    .put("placeId", placeId)
                    .put("message", message)
                    .put("published", true)
                    .put("data", new JSONObject().put("category", category).toString())
                    .toString().getBytes("UTF-8");
            // Create notification through API
            URL url = new URL(API_URL + "notification");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            String credentials =
                    Base64.encodeToString((accountId + ":" + privateKey).getBytes(), Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", "Basic " + credentials);
            OutputStream out = urlConnection.getOutputStream();
            out.write(body);
            int status = urlConnection.getResponseCode();
            if (status >= 200 && status < 300) {
                InputStream in = urlConnection.getInputStream();
                byte[] readBuffer = new byte[1024];
                in.read(readBuffer);
                JSONObject response = new JSONObject(new String(readBuffer, "UTF-8"));
                return response.getString("result");
            } else {
                InputStream in = new BufferedInputStream(urlConnection.getErrorStream());
                byte[] readBuffer = new byte[1024];
                in.read(readBuffer);
                throw new IOException(new String(readBuffer));
            }
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
    }
}
