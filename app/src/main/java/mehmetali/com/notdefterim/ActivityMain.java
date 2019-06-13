package mehmetali.com.notdefterim;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import mehmetali.com.notdefterim.Adapter.AdapterNoteList;
import mehmetali.com.notdefterim.Adapter.Divider;
import mehmetali.com.notdefterim.Adapter.Filters;
import mehmetali.com.notdefterim.Adapter.SimpleTouchCallback;
import mehmetali.com.notdefterim.Data.NotlarProvider;
import mehmetali.com.notdefterim.Model.Notes;
import mehmetali.com.notdefterim.Service.NotificationService;

public class ActivityMain extends AppCompatActivity{

    static final Uri CONTENT_URI = NotlarProvider.CONTENT_URI;
    static final String ORDER_UNIMPORTANT = "SIRALAMA ONEMSIZ";
    static final String COMPLETED_UNIMPORTANT = "TAMAMLANMA ONEMSIZ";

    View emptyList;
    Toolbar mToolbar;
    Button mButtonNewNote;
    NotesRecyclerView mRecyclerView;
    AdapterNoteList mAdapterNoteList;
    List<Notes> allNotes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emptyList = findViewById(R.id.empty_list);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("NotDefterim");

        mButtonNewNote = findViewById(R.id.btn_add_note);
        mButtonNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteDialog();
            }
        });

        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.emptyListKeep(mToolbar);
        mRecyclerView.emptyListShow(emptyList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mAdapterNoteList = new AdapterNoteList(this, allNotes);
        mRecyclerView.setLayoutManager(manager);
        mAdapterNoteList.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapterNoteList);

        //swipe işlemi
        SimpleTouchCallback callback = new SimpleTouchCallback();
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);


        int filter = NotDefterimApp.getFilter(this);
        switch (filter) {

            case 0:
                updateList(ORDER_UNIMPORTANT, COMPLETED_UNIMPORTANT);
                break;
            case 1:
                updateList("notTarih DESC", COMPLETED_UNIMPORTANT);
                NotDefterimApp.filters(this, Filters.LONG_TIME);
                break;
            case 2:
                updateList("notTarih ASC", COMPLETED_UNIMPORTANT);
                NotDefterimApp.filters(this, Filters.SHORT_TIME);
                break;
            case 3:
                updateList(ORDER_UNIMPORTANT, "1");
                NotDefterimApp.filters(this, Filters.COMPLETED);
                break;
            case 4:
                updateList(ORDER_UNIMPORTANT, "0");
                NotDefterimApp.filters(this, Filters.NOT_COMPLETED);
                break;
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(ActivityMain.this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(ActivityMain.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, 3600000, pendingIntent);
        createNotificationChannel();
    }

    public void updateList(String order, String complete) {
        allNotes.clear();
        allNotes = getAllNotes(order, complete);
        mAdapterNoteList.update(allNotes);

    }

    private void addNoteDialog() {
        FragmentDialogNewNote dialogNewNote = new FragmentDialogNewNote();
        dialogNewNote.show(getSupportFragmentManager(), "DialogNewNote");
    }

    private void noteCompleteDialog(int position) {
        EventBus.getDefault().postSticky(new DataEvent.DialogFragmentComplete(position));
        FragmentDialogComplete complete = new FragmentDialogComplete();
        complete.show(getSupportFragmentManager(), "DialogComplete");

    }

    private List<Notes> getAllNotes(String order, String complete) {

        String orderQuery = order;
        String selection = "tamamlandi=?";
        String[] selectionArgs = {complete};


        if (order.equals(ORDER_UNIMPORTANT)) {
            orderQuery = null;
        }

        if (complete.equals(COMPLETED_UNIMPORTANT)) {
            selection = null;
            selectionArgs = null;

        }

        Cursor cursor = getContentResolver().query(CONTENT_URI, new String[]{"id", "noticerik", "notTarih", "tamamlandi"}, selection, selectionArgs, orderQuery);


        if (cursor != null) {
            while (cursor.moveToNext()) {
                allNotes.add(new Notes(cursor.getInt(cursor.getColumnIndex("id"))
                        , cursor.getString(cursor.getColumnIndex("noticerik"))
                        , cursor.getLong(cursor.getColumnIndex("notTarih"))
                        , cursor.getInt(cursor.getColumnIndex("tamamlandi"))));


            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return allNotes;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_new_note:
                addNoteDialog();
                break;

            case R.id.menu_no_filter:
                updateList(ORDER_UNIMPORTANT, COMPLETED_UNIMPORTANT);
                NotDefterimApp.filters(this, Filters.NOFILTER);
            case R.id.menu_long_note:
                updateList("notTarih DESC", COMPLETED_UNIMPORTANT);
                NotDefterimApp.filters(this, Filters.LONG_TIME);
                break;
            case R.id.menu_short_note:
                updateList("notTarih ASC", COMPLETED_UNIMPORTANT);
                NotDefterimApp.filters(this, Filters.SHORT_TIME);
                break;
            case R.id.menu_completed_note:
                updateList(ORDER_UNIMPORTANT, "1");
                NotDefterimApp.filters(this, Filters.COMPLETED);
                break;
            case R.id.menu_not_completed_note:
                updateList(ORDER_UNIMPORTANT, "0");
                NotDefterimApp.filters(this, Filters.NOT_COMPLETED);
                break;
            default:
                NotDefterimApp.filters(this, Filters.NOFILTER);
                return false;

        }
        return true;
    }


    @Subscribe
    public void onDialogComplete(DataEvent.DialogComplete event) {
        noteCompleteDialog(event.getPosition());
    }

    @Subscribe
    public void onNotEkleDialog(DataEvent.NotEkleDialog event) {
        if (event.getTrigger() == 1) {
            addNoteDialog();
        }
    }

    @Subscribe
    public void updateDataList(DataEvent.UpdateList event) {
        if (event.getTrigger() == 1) {
            updateList(ORDER_UNIMPORTANT, COMPLETED_UNIMPORTANT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        mRecyclerView.setAdapter(null);
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
