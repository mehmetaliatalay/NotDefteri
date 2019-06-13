package mehmetali.com.notdefterim;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class NotDefterimApp extends Application {


    public static void filters(Context context, int filter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("filter", filter);
        editor.apply();
    }

    public static int getFilter(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("filter", 0);
    }

}
