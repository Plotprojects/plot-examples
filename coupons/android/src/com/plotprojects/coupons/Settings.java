package com.plotprojects.coupons;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    final static String SETTINGS_KEY = "settings";

    final static String FASTFOOD_PREFERENCE = "fastfood";
    final static String EVENTS_PREFERENCE = "events";
    final static String DINING_PREFERENCE = "dining";
    final static String FILM_PREFERENCE = "film";
    final static String ELECTRONICS_PREFERENCE = "electronics";
    final static String WELLNESS_PREFERENCE = "wellness";

    private SharedPreferences preferences;

    public Settings(Context context) {
        this.preferences = context.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE);
    }

    public boolean getBoolean(String key, boolean def) {
        return preferences.getBoolean(key, def);
    }

    public void setBoolean(final String key, final boolean value) {
        save(new SaveOperation() {
            @Override
            public void saveValue(SharedPreferences.Editor editor) {
                editor.putBoolean(key, value);
            }
        });
    }

    private interface SaveOperation {
        public void saveValue(SharedPreferences.Editor editor);
    }

    private void save(SaveOperation operation) {
        SharedPreferences.Editor editor = preferences.edit();
        operation.saveValue(editor);
        editor.commit();
    }
}
