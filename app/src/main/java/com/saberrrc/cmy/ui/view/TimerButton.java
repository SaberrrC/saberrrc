package com.saberrrc.cmy.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.saberrrc.cmy.common.utils.StrUtils;

public class TimerButton extends AppCompatTextView {

    //config*****begin
    private static final int WHAT_BY_TIMER_BTN = 0x0001;
    private static final int CLOCK_TIME = 60;//
    //config*****end

    private int count = CLOCK_TIME;
    private String defalutTips = "获取验证码";


    public TimerButton(Context context) {
        super(context);
    }

    public TimerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void removeCallbacksAndMessages() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_BY_TIMER_BTN:
                    if (count <= 0) {
                        resetTimer();
                        setClickable(true);
                        return;
                    }
                    setText(count + "s");
                    count--;
                    handler.postDelayed(timerRunnable, 1000);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(WHAT_BY_TIMER_BTN);
        }
    };

    /**
     * start this timer
     */
    public void startTimer() {
        handler.sendEmptyMessage(WHAT_BY_TIMER_BTN);
        setClickable(false);
        setEnabled(false);
//        setBackgroundResource(R.drawable.shape_get_code_after_click);//开始倒计时 按钮外边款改变
//        setBackgroundResource(R.drawable.shape_get_code);//开始倒计时 按钮外边款改变
//        setTextColor(getResources().getColor(R.color.gray_999999));
    }

    /**
     * stop this timer
     */
    public void stopTimer() {
        handler.removeCallbacks(timerRunnable);
        handler.removeMessages(WHAT_BY_TIMER_BTN);
        setClickable(true);
        setEnabled(true);
//        setBackgroundResource(R.drawable.shape_get_code);//结束倒计时 按钮外边款改变
//        setTextColor(getResources().getColor(R.color.light_blue));
    }

    /**
     * reset this timer
     */
    public void resetTimer() {
        count = CLOCK_TIME;
        handler.removeCallbacks(timerRunnable);
        handler.removeMessages(WHAT_BY_TIMER_BTN);
        setText(StrUtils.isEmpty(defalutTips) ? "" : defalutTips);
        setClickable(true);
        setEnabled(true);
//        setBackgroundResource(R.drawable.shape_get_code);//重置倒计时 按钮外边款改变
//        setTextColor(getResources().getColor(R.color.light_blue));
    }

    /**
     * set the defalut tips 2 btn name
     *
     * @param defalutTips
     */
    public void setDefalutTips(String defalutTips) {
        this.defalutTips = defalutTips;
    }
}
