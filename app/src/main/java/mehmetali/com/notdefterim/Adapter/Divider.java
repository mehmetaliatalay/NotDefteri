package mehmetali.com.notdefterim.Adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import mehmetali.com.notdefterim.R;

public class Divider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mOrientation;


    public Divider(Context context, int orientation) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider);
        if (orientation != LinearLayoutManager.VERTICAL) {
            throw new IllegalArgumentException("This decoration can not be used");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawHorizontalDrawer(c, parent, state);
        }
    }

    private void drawHorizontalDrawer(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left, up, right, down;
        left = parent.getPaddingLeft();
        right = parent.getWidth() - parent.getPaddingRight();
        int numberOfElements = parent.getChildCount();

        for (int i = 0; i < numberOfElements; i++) {

            View viewAtTheMoment = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewAtTheMoment.getLayoutParams();
            up = viewAtTheMoment.getTop() - params.topMargin;
            down = up + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, up, right, down);
            mDivider.draw(c);
        }
    }
}
