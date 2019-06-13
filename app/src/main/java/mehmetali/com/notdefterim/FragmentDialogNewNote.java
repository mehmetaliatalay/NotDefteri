package mehmetali.com.notdefterim;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import org.greenrobot.eventbus.EventBus;


import mehmetali.com.notdefterim.Data.NotlarProvider;

public class FragmentDialogNewNote extends DialogFragment {

    static final Uri CONTENT_URI = NotlarProvider.CONTENT_URI;

    private ImageButton mBtnClose;
    private EditText mNoteContent;
    private MyDatePicker mDate;
    private Button mAddNote;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_new_note, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnClose = view.findViewById(R.id.btn_close);
        mNoteContent = view.findViewById(R.id.et_note);
        mDate = view.findViewById(R.id.dp_date);
        mAddNote = view.findViewById(R.id.btn_add_note);

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues values = new ContentValues();
                values.put("noticerik", mNoteContent.getText().toString());
                values.put("notEklenmeTarih",System.currentTimeMillis());
                values.put("notTarih", mDate.getTime());
                Uri uri = getActivity().getContentResolver().insert(CONTENT_URI, values);
                EventBus.getDefault().post(new DataEvent.UpdateList(1));
                dismiss();
            }
        });
    }
}
