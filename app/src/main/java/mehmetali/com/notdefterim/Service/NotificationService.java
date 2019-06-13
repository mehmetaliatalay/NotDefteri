package mehmetali.com.notdefterim.Service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mehmetali.com.notdefterim.ActivityMain;
import mehmetali.com.notdefterim.Data.NotlarProvider;
import mehmetali.com.notdefterim.Model.Notes;
import mehmetali.com.notdefterim.R;


public class NotificationService extends IntentService {

    static final Uri CONTENT_URI = NotlarProvider.CONTENT_URI;
    List<Notes> unCompletedNotes = new ArrayList<>();

    public NotificationService() {
        super("NotificationService");
        Log.d("fdsgfhdgfhg", "NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("fdsgfhdgfhg", "NotificationService onHandle");
        unCompletedNotes = getAllNotes();

        for (Notes notes : unCompletedNotes) {
            if (calculateDates(notes.getNotAddDate(), notes.getNotDate())) {

                notifyUser(notes.getNotContext());
            }
        }
    }

    private void notifyUser(String message) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Intent intent = new Intent(this, ActivityMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.ic_notifications_active)
                    .setContentTitle("NotDefterim")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, mBuilder.build());
        } else {

            Intent intent = new Intent(this, ActivityMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.ic_notifications_active)
                    .setContentTitle("NotDefterim")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(2, mBuilder.build());
        }


    }

    private boolean calculateDates(long notAddDate, long notDate) {
        long now = System.currentTimeMillis();

        if (now > notDate) {

            return false;
        } else {

            long remainingTime = (long) ((notDate - notAddDate) * 0.9);
            return now > (notAddDate + remainingTime);
        }
    }

    private List<Notes> getAllNotes() {
        List<Notes> unCompletedNotes = new ArrayList<>();
        String selection = "tamamlandi=?";
        String[] selectionArgs = {"0"};

        Cursor cursor = getContentResolver().query(CONTENT_URI, new String[]{"id", "noticerik", "notEklenmeTarih", "notTarih", "tamamlandi"}, selection, selectionArgs, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                unCompletedNotes.add(new Notes(cursor.getInt(cursor.getColumnIndex("id"))
                        , cursor.getString(cursor.getColumnIndex("noticerik"))
                        , cursor.getLong(cursor.getColumnIndex("notEklenmeTarih"))
                        , cursor.getLong(cursor.getColumnIndex("notTarih"))
                        , cursor.getInt(cursor.getColumnIndex("tamamlandi"))));

            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return unCompletedNotes;
    }

}
