package mehmetali.com.notdefterim.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import org.greenrobot.eventbus.EventBus;

import mehmetali.com.notdefterim.DataEvent;

public class SimpleTouchCallback extends ItemTouchHelper.Callback {


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == 0) {
            return makeMovementFlags(0, ItemTouchHelper.END);
        } else {
            return makeMovementFlags(0, 0);
        }

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        EventBus.getDefault().post(new DataEvent.SwappedNot(viewHolder.getAdapterPosition()));

    }
}
