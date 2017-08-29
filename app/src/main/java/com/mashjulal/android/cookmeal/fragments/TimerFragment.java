package com.mashjulal.android.cookmeal.fragments;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.mashjulal.android.cookmeal.R;

import static com.mashjulal.android.cookmeal.Utils.formatTime;
import static com.mashjulal.android.cookmeal.Utils.getTime;


public class TimerFragment extends Fragment {

    private static final String TAG = TimerFragment.class.getSimpleName();

    private Context mContext;
    private ProgressBar mPBProgress;
    private TextView tvTime;
    private Button btnStart;
    private Button btnStop;
    private Button btnReset;
    private Button btnSetTime;

    private long mTime = 0;
    private long mTotalTime = 0;
    private boolean isGoing = false;

    private Handler mHandler = new Handler();

    public TimerFragment() {
        // Required empty public constructor
    }

    public static TimerFragment newInstance() {
        return new TimerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        mContext = container.getContext();
        mPBProgress = (ProgressBar) view.findViewById(R.id.pb_frTimer_progress);
        tvTime = (TextView) view.findViewById(R.id.tv_frTimer_time);
        btnStart = (Button) view.findViewById(R.id.btn_frTimer_start);
        btnStop = (Button) view.findViewById(R.id.btn_frTimer_stop);
        btnReset = (Button) view.findViewById(R.id.btn_frTimer_reset);
        btnSetTime = (Button) view.findViewById(R.id.btn_frTimer_setTime);

        btnSetTime.setOnClickListener((v) -> setTime());
        btnStart.setOnClickListener((v) -> start());
        btnStop.setOnClickListener((v) -> stop());
        btnReset.setOnClickListener((v) -> reset());

        refreshTimer();
        return view;
    }

    private void reset() {
        stop();
        mTime = 0;
        mTotalTime = 0;
        refreshTimer();
        mPBProgress.setMax(100);
        mPBProgress.setProgress(100);
    }

    private void refreshTimer() {
        updateTimeTextView();
        refreshButtonsState();
    }

    private void stop() {
        isGoing = false;
        refreshButtonsState();
    }

    private void start() {
        if (mTotalTime == 0)
            return;

        isGoing = true;
        refreshButtonsState();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isGoing)
                    return;

                mTime -= 1000;
                updateProgressBar();
                updateTimeTextView();

                if (mTime == 0) {
                    playAlarm();
                    reset();
                } else {
                    mHandler.postDelayed(this, 1000);
                }
            }
        });
    }

    private void setTime() {
        int[] time = getTime(mTime);
        new MyTimePickerDialog(
                mContext,
                (view, hourOfDay, minute, seconds) -> {
                    long tm = (seconds + minute * 60 + hourOfDay * 3600) * 1000;
                    if (tm == 0)
                        return;
                    mTotalTime = tm;
                    mTime = mTotalTime;
                    updateTimeTextView();
                    updateProgressBar();
                }, time[0], time[1], time[2], true).show();
    }

    private void updateTimeTextView() {
        tvTime.setText(formatTime(mTime));
    }

    private void updateProgressBar() {
        mPBProgress.setProgress((int) (mTime * 100 / mTotalTime));
    }

    private void refreshButtonsState() {
        btnStop.setVisibility((isGoing) ? View.VISIBLE : View.GONE);
        btnStart.setVisibility((isGoing) ? View.GONE : View.VISIBLE);
        btnSetTime.setVisibility((mTime != 0) ? View.GONE : View.VISIBLE);

        if (mTime > 0) {
            btnReset.setVisibility(View.VISIBLE);
            btnReset.setEnabled(!isGoing);
        } else {
            btnReset.setVisibility(View.GONE);
        }
    }

    private void playAlarm() {
        // TODO: change ringtone
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(mContext, notification);
        r.play();
    }
}
