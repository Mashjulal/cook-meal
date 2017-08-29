package com.mashjulal.android.cookmeal.fragments;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mashjulal.android.cookmeal.R;
import com.mashjulal.android.cookmeal.objects.Step;

import static com.mashjulal.android.cookmeal.Utils.formatTime;


public class StepFragment extends Fragment {

    private static final String TAG = StepFragment.class.getSimpleName();

    private static final String ARG_STEP = "step";

    private Step mStep;
    private ProgressBar mPBTimerProgress;
    private ImageButton mIBStartPause;
    private TextView mTVTime;
    private boolean mIsGoing = false;
    private long mTime = 0;
    private long mTotalTime = 0;
    private Handler mHandler = new Handler();
    private Context mContext;
    private OnFinishTimerListener mOnFinishTimerListener;

    public StepFragment() {
        // Required empty public constructor
    }

    public static StepFragment newInstance(Step step) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = (Step) getArguments().getSerializable(ARG_STEP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        View v = inflater.inflate(R.layout.fragment_step, container, false);
        ((TextView) v.findViewById(R.id.tv_frStep_stepNumber)).setText(String.format(
                mContext.getString(R.string.title_step_number),
                mStep.getStepNumber()));
        ((TextView) v.findViewById(R.id.tv_frStep_description)).setText(mStep.getDescription());
        String imagePath = mStep.getImagePath();
        ImageView ivImage = ((ImageView) v.findViewById(R.id.iv_frStep_image));
        if (!imagePath.equals("")) {
            ivImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        } else {
            ivImage.setImageBitmap(null);
            ivImage.setVisibility(View.GONE);
        }
        mPBTimerProgress = (ProgressBar) v.findViewById(R.id.pb_frStep_timerProgress);
        mIBStartPause = (ImageButton) v.findViewById(R.id.btn_frStep_startPauseTimer);
        mIBStartPause.setOnClickListener(v1 -> start());
        if (mStep.getTime() != 0) {
            mTVTime = (TextView) v.findViewById(R.id.tv_frStep_time);
            mTotalTime = mStep.getTime();
            mTime = mTotalTime;
            updateProgressBar();
            updateTimeTextView();
        } else {
            (v.findViewById(R.id.rv_frStep_timer)).setVisibility(View.GONE);
        }
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible() && !isVisibleToUser) {
            mIsGoing = false;
            if (mStep.getTime() != 0) {
                mTotalTime = mStep.getTime();
                mTime = mTotalTime;
                refreshButtonState();
                updateProgressBar();
                updateTimeTextView();
            }
        }
    }

    public void setOnFinishTimerListener(OnFinishTimerListener listener) {
        mOnFinishTimerListener = listener;
    }

    private void pause() {
        mIsGoing = false;
        refreshButtonState();
        mIBStartPause.setOnClickListener((v) -> start());
    }

    private void start() {
        mIsGoing = true;
        refreshButtonState();
        mIBStartPause.setOnClickListener((v) -> pause());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!mIsGoing)
                    return;
                mTime -= 1000;
                updateProgressBar();
                updateTimeTextView();
                if (mTime == 0) {
                    playAlarm();
                    pause();
                    mOnFinishTimerListener.onTimerFinish();
                } else {
                    mHandler.postDelayed(this, 1000);
                }
            }
        });
    }

    private void updateTimeTextView() {
        mTVTime.setText(formatTime(mTime));
    }

    private void updateProgressBar() {
        mPBTimerProgress.setProgress((int) (mTime * 100 / mTotalTime));
    }

    private void refreshButtonState() {
        mIBStartPause.setImageResource((mIsGoing) ?
                R.drawable.ic_pause_yellow :
                R.drawable.ic_play_yellow);
    }

    private void playAlarm() {
        // TODO: change ringtone
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(mContext, notification);
        r.play();
    }

    public interface OnFinishTimerListener {
        void onTimerFinish();
    }
}
