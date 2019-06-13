package mehmetali.com.notdefterim;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NotesRecyclerView extends RecyclerView {

    List<View> emptyListKeepViews = Collections.emptyList();
    List<View> emptyListShowViews = Collections.emptyList();

    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            viewsStatus();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            viewsStatus();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            viewsStatus();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            viewsStatus();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            viewsStatus();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            viewsStatus();
        }
    };

    public void viewsStatus() {
        if (getAdapter() != null && !emptyListKeepViews.isEmpty() && !emptyListShowViews.isEmpty()) {

            if (getAdapter().getItemCount() == 0) {

                for (View view : emptyListKeepViews) {
                    view.setVisibility(View.GONE);
                }
                setVisibility(View.GONE);

                for (View view : emptyListShowViews) {
                    view.setVisibility(View.VISIBLE);
                }

            } else if (getAdapter().getItemCount() > 1) {

                for (View view : emptyListKeepViews) {
                    view.setVisibility(View.VISIBLE);
                }
                setVisibility(View.VISIBLE);

                for (View view : emptyListShowViews) {
                    view.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mObserver);
        }
        mObserver.onChanged();
    }

    public void emptyListKeep(View... views) {
        emptyListKeepViews = Arrays.asList(views);
    }

    public void emptyListShow(View... views) {

        emptyListShowViews = Arrays.asList(views);
    }

    public NotesRecyclerView(@NonNull Context context) {
        super(context);
    }

    public NotesRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NotesRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


}
