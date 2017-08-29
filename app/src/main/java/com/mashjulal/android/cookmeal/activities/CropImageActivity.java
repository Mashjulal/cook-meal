package com.mashjulal.android.cookmeal.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mashjulal.android.cookmeal.R;

public class CropImageActivity extends AppCompatActivity {

    private static final String TAG = CropImageActivity.class.getSimpleName();

    private View mViewCrop;
    private RelativeLayout.LayoutParams mLayoutParams;
    private ImageView mIVImage;
    private float dx;
    private float dy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.rv_aCropImage_layout);
        mViewCrop = findViewById(R.id.view_aCropImage_crop);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        mLayoutParams = new RelativeLayout.LayoutParams(
                width, (int) (height / 2.5f));
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mViewCrop.setLayoutParams(mLayoutParams);
        mIVImage = (ImageView) findViewById(R.id.iv_aCropImage_image);
        mIVImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx =  event.getX();
                        dy =  event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float x =  event.getX();
                        float y =  event.getY();
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mIVImage.getLayoutParams();
                        float left = lp.leftMargin + (x - dx);
                        float top = lp.topMargin + (y - dy);
                        lp.leftMargin = (int)left;
                        lp.topMargin = (int)top;
                        mIVImage.setLayoutParams(lp);
                        break;
                }
                return true;
            }
        });
        Bitmap d = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);
        mIVImage.setImageBitmap(scaled);
    }
}
