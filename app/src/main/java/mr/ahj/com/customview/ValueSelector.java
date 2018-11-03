package mr.ahj.com.customview;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.os.Handler;

public class ValueSelector extends LinearLayout implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {

    View rootView;
    TextView valueText;
    View buttonPlus;
    View buttonMinus;

    int minValue = Integer.MIN_VALUE;
    int maxValue = Integer.MAX_VALUE;

    private boolean isPlusButtonPressed = false;
    private boolean isMinusButtonPressed = false;

    private static final int TIME_INTERVAL = 150;

    private Handler handler;

    public ValueSelector(Context context) {
        super(context);
        init(context);
    }

    public ValueSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        setSaveEnabled(true);

        rootView = inflate(context,R.layout.value_selector, this);
        valueText = (TextView) rootView.findViewById(R.id.value_text);
        buttonPlus = rootView.findViewById(R.id.btn_plus);
        buttonMinus = rootView.findViewById(R.id.btn_minus);

        handler = new Handler();

        buttonPlus.setOnClickListener(this);
        buttonMinus.setOnClickListener(this);

        buttonPlus.setOnLongClickListener(this);
        buttonMinus.setOnLongClickListener(this);

        buttonPlus.setOnTouchListener(this);
        buttonMinus.setOnTouchListener(this);
    }

    private void incrementValue() {
        int value = getValue();
        setValue(value + 1);
    }

    private void decrementValue() {
        int value = getValue();
        setValue(value - 1);
    }

    public int getValue() {
        String text = valueText.getText().toString();
        if (text.isEmpty()) {
            valueText.setText("0");
            return 0;
        }
        return Integer.valueOf(text);
    }

    public void setValue(int newValue) {
        if (newValue > maxValue) {
            valueText.setText(String.valueOf(maxValue));
        } else if (newValue < minValue) {
            valueText.setText(String.valueOf(minValue));
        } else {
            valueText.setText(String.valueOf(newValue));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonPlus.getId()) {
            incrementValue();
        } else if(v.getId() == buttonMinus.getId()) {
            decrementValue();
        }
    }
    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == buttonPlus.getId()) {
            isPlusButtonPressed = true;
            handler.postDelayed(new AutoIncrementer(), TIME_INTERVAL);
        }else if (v.getId() == buttonMinus.getId()) {
            isMinusButtonPressed = true;
            handler.postDelayed(new AutoDecrementer(), TIME_INTERVAL);
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            isPlusButtonPressed = false;
            isMinusButtonPressed = false;
        }
        return false;
    }

    private class AutoIncrementer implements Runnable {

        @Override
        public void run() {
            if (isPlusButtonPressed) {
                incrementValue();
                handler.postDelayed(this, TIME_INTERVAL);
            }
        }
    }

    private class AutoDecrementer implements Runnable {

        @Override
        public void run() {
            if (isMinusButtonPressed) {
                decrementValue();
                handler.postDelayed(this, TIME_INTERVAL);
            }
        }
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        ValueSelectorSavedState ss = new ValueSelectorSavedState(super.onSaveInstanceState());
        ss.currentValue = getValue();
        ss.maxValue = this.maxValue;
        ss.minValue = this.minValue;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        ValueSelectorSavedState ss = (ValueSelectorSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setValue(ss.currentValue);
        setMaxValue(ss.maxValue);
        setMaxValue(ss.minValue);
    }

    public static class ValueSelectorSavedState extends BaseSavedState {

        int currentValue;
        int maxValue;
        int minValue;

        public ValueSelectorSavedState(Parcel source) {
            super(source);
            currentValue = source.readInt();
            maxValue = source.readInt();
            minValue = source.readInt();
        }

        public ValueSelectorSavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentValue);
            out.writeInt(maxValue);
            out.writeInt(minValue);
        }

        public static final Parcelable.Creator<ValueSelectorSavedState> CREATOR =
                new Creator<ValueSelectorSavedState>() {
                    @Override
                    public ValueSelectorSavedState createFromParcel(Parcel source) {
                        return new ValueSelectorSavedState(source);
                    }

                    @Override
                    public ValueSelectorSavedState[] newArray(int size) {
                        return new ValueSelectorSavedState[size];
                    }
                };
    }
}
