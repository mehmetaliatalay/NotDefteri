package mehmetali.com.notdefterim.Adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import mehmetali.com.notdefterim.Data.NotlarProvider;
import mehmetali.com.notdefterim.DataEvent;
import mehmetali.com.notdefterim.Model.Notes;
import mehmetali.com.notdefterim.NotDefterimApp;
import mehmetali.com.notdefterim.R;

public class AdapterNoteList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int ITEM = 0;
    private static final int NO_FILTER = 1;
    private static final int FOOTER = 2;
    private static final int ADD_FOOTER = 1;
    private static final int ADD_EMPTY_FILTER = 1;

    private LayoutInflater mInfilator;
    private List<Notes> allNotes;
    private ContentResolver resolver;
    private Context context;
    private int mfilter;


    public AdapterNoteList(Context context, List<Notes> list) {
        this.context = context;
        resolver = context.getContentResolver();
        mInfilator = LayoutInflater.from(context);
        allNotes = list;

    }


    //Herbir satırın oluşmasını sağlıyor.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == ITEM) {
            View view = mInfilator.inflate(R.layout.note_row, viewGroup, false);
            return new NoteHolder(view);
        } else if (i == NO_FILTER) {
            View view = mInfilator.inflate(R.layout.empty_filter, viewGroup, false);
            return new NoFilterHolder(view);
        } else {
            View view = mInfilator.inflate(R.layout.footer, viewGroup, false);
            return new FooterHolder(view);
        }

    }


    //Verilerin atanması işlemi.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof NoteHolder) {
            NoteHolder noteHolder = (NoteHolder) holder;
            noteHolder.mTextNote.setText(allNotes.get(i).getNotContext());
            noteHolder.setBackgroundColor(allNotes.get(i).getIsDone());
            noteHolder.setDate(allNotes.get(i).getNotDate());
        }

    }


    @Override
    public long getItemId(int position) {
        if (position < allNotes.size()) {
            return allNotes.get(position).getId();
        }
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemViewType(int position) {
        if (!allNotes.isEmpty()) {
            if (position < allNotes.size()) {
                return ITEM;
            } else {
                return FOOTER;
            }
        } else {
            if (mfilter == Filters.COMPLETED || mfilter == Filters.NOT_COMPLETED) {
                if (position == 0) {
                    return NO_FILTER;
                } else {
                    return FOOTER;
                }
            } else {
                return ITEM;
            }
        }
    }


    //Listenin eleman sayısı.
    @Override
    public int getItemCount() {
        if (!allNotes.isEmpty()) {
            return allNotes.size() + ADD_FOOTER;
        } else {
            if (mfilter == Filters.SHORT_TIME || mfilter == Filters.LONG_TIME || mfilter == Filters.NOFILTER) {
                return 0;
            } else {
                return ADD_FOOTER + ADD_EMPTY_FILTER;
            }
        }
    }

    public void update(List<Notes> list) {
        this.allNotes = list;
        mfilter = NotDefterimApp.getFilter(context);
        notifyDataSetChanged();
    }

    @Subscribe
    public void onDialogFragmentDatabase(DataEvent.DialogFragmentDatabase event) {

        int position = event.getPosition();

        if (position < allNotes.size()) {
            Notes note = allNotes.get(position);
            String noteID = String.valueOf(note.getId());
            ContentValues values = new ContentValues();
            values.put("tamamlandi", 1);
            int numberOfDeletedRows = resolver.update(NotlarProvider.CONTENT_URI, values, "id=?", new String[]{noteID});

            if (numberOfDeletedRows != 0) {
                note.setIsDone(1);
                allNotes.set(position, note);
                Log.i("TAMAMLANAN ID", noteID);
                update(allNotes);
            }
        }
    }


    @Subscribe
    public void onSwipe(DataEvent.SwappedNot event) {
        if (event.getPosition() < allNotes.size()) {
            Notes note = allNotes.get(event.getPosition());
            String idToDelete = String.valueOf(note.getId());
            int numberOfDeletedRows = resolver.delete(NotlarProvider.CONTENT_URI, "id = ?", new String[]{idToDelete});
            if (numberOfDeletedRows != 0) {
                allNotes.remove(note);

                if (allNotes.isEmpty() && (mfilter == Filters.COMPLETED || mfilter == Filters.NOT_COMPLETED)) {
                    NotDefterimApp.filters(context, Filters.NOFILTER);
                    EventBus.getDefault().post(new DataEvent.UpdateList(1));
                }
            }
            update(allNotes);
        }

    }


    //Layout öğelerinin bulunması ve veri atanması.onBindViewHolder ile senkronize çalışır.
    public class NoteHolder extends RecyclerView.ViewHolder {

        TextView mTextNote;
        TextView mTextDate;
        View mItemView;

        NoteHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            mTextNote = itemView.findViewById(R.id.tv_context);
            mTextDate = itemView.findViewById(R.id.tv_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new DataEvent.DialogComplete(getAdapterPosition()));
                }
            });

        }

        private void setBackgroundColor(int i) {
            Drawable bgColor;
            if (i == 0) {
                bgColor = ContextCompat.getDrawable(context, R.color.item_not_completed);
            } else {
                bgColor = ContextCompat.getDrawable(context, R.color.item_completed);
            }
            itemView.setBackground(bgColor);
        }

        private void setDate(long date) {
            mTextDate.setText(DateUtils.getRelativeTimeSpanString(date, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS, 0));
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder {

        Button mFooterButton;

        FooterHolder(@NonNull View itemView) {
            super(itemView);

            mFooterButton = itemView.findViewById(R.id.btn_footer);

            mFooterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new DataEvent.NotEkleDialog(1));
                }
            });
        }
    }

    public class NoFilterHolder extends RecyclerView.ViewHolder {

        public NoFilterHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromRecyclerView(recyclerView);

    }
}
