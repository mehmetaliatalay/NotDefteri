package mehmetali.com.notdefterim;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyDatePicker extends LinearLayout implements View.OnTouchListener {

    private TextView mTextDay;
    private TextView mTextMonth;
    private TextView mTextYear;
    Calendar mCalendar;
    SimpleDateFormat mFormatter;

    public MyDatePicker(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.date_view, this);
        mCalendar = Calendar.getInstance();
        mFormatter = new SimpleDateFormat("MMM");

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextDay = this.findViewById(R.id.tv_date_day);
        mTextMonth = this.findViewById(R.id.tv_date_month);
        mTextYear = this.findViewById(R.id.tv_date_year);

        mTextDay.setOnTouchListener(this);
        mTextMonth.setOnTouchListener(this);
        mTextYear.setOnTouchListener(this);


        int day = mCalendar.get(Calendar.DATE);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);

        update(day, month, year, 0, 0, 0);

    }


    private void update(int day, int month, int year, int hour, int minute, int second) {
        mTextDay.setText(String.valueOf(day));
        mTextMonth.setText(mFormatter.format(mCalendar.getTime()));
        mTextYear.setText(String.valueOf(year));

    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public MyDatePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyDatePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public long getTime() {
        return mCalendar.getTimeInMillis();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {

            case R.id.tv_date_day:
                processEventFor(mTextDay, event);
                break;
            case R.id.tv_date_month:
                processEventFor(mTextMonth, event);
                break;
            case R.id.tv_date_year:
                processEventFor(mTextYear, event);
                break;
        }
        return false;
    }

    private void processEventFor(TextView textView, MotionEvent event) {

        Drawable[] drawables = textView.getCompoundDrawables();
        if (isThereUpDrawable(drawables) && isThereDownDrawable(drawables)) {

            Rect upRect = drawables[1].getBounds();
            Rect downRect = drawables[3].getBounds();

            float x = event.getX();
            float y = event.getY();

            if (upDrawableClicked(textView, upRect, x, y)) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    increase(textView.getId());
                   // changeBackGroundColor(textView, 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                   //changeBackGroundColorBack(textView);
                }
            } else if (downDrawableClicked(textView, downRect, x, y)) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    decrease(textView.getId());
                    //changeBackGroundColor(textView, 2);
                }else if (event.getAction() == MotionEvent.ACTION_UP) {
                   // changeBackGroundColorBack(textView);
                }
            }
        }
    }

    private void changeBackGroundColorBack(TextView textView) {

        textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal, 0, R.drawable.down_normal);
    }

    private void changeBackGroundColor(TextView textView, int i) {
        if (i == 1) {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal_pressed, 0, R.drawable.down_normal);
        } else if (i == 2) {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal, 0, R.drawable.down_normal_pressed);
        }
    }

    private void decrease(int id) {
        switch (id) {
            case R.id.tv_date_day:
                mCalendar.add(Calendar.DATE, -1);
                break;
            case R.id.tv_date_month:
                mCalendar.add(Calendar.MONTH, -1);
                break;
            case R.id.tv_date_year:
                mCalendar.add(Calendar.YEAR, -1);
                break;
        }
        update(mCalendar.get(Calendar.DATE), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.YEAR), 0, 0, 0);
    }

    private void increase(int id) {

        switch (id) {
            case R.id.tv_date_day:
                mCalendar.add(Calendar.DATE, 1);
                break;
            case R.id.tv_date_month:
                mCalendar.add(Calendar.MONTH, 1);
                break;
            case R.id.tv_date_year:
                mCalendar.add(Calendar.YEAR, 1);
                break;
        }
        update(mCalendar.get(Calendar.DATE), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.YEAR), 0, 0, 0);
    }

    private boolean upDrawableClicked(TextView textView, Rect upRect, float x, float y) {

        int xMin = textView.getPaddingLeft();
        int xMax = textView.getWidth() - textView.getPaddingRight();

        int yMin = textView.getPaddingTop();
        int yMax = textView.getPaddingTop() + upRect.height();

        return x > xMin && x < xMax && y > yMin && y < yMax;

    }

    private boolean downDrawableClicked(TextView textView, Rect downRect, float x, float y) {

        int xMin = textView.getPaddingLeft();
        int xMax = textView.getWidth() - textView.getPaddingRight();

        int yMax = textView.getHeight() - textView.getPaddingBottom();
        int yMin = yMax - downRect.height();


        return x > xMin && x < xMax && y > yMin && y < yMax;
    }

    private boolean isThereUpDrawable(Drawable[] drawables) {
        return drawables[1] != null;
    }

    private boolean isThereDownDrawable(Drawable[] drawables) {
        return drawables[3] != null;
    }


}
