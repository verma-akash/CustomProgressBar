package com.gravity.customprogressbar;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akash Verma on 22/04/18.
 */

public class CustomProgressBarScrollable extends FrameLayout {

    private Context context;
    private LayoutInflater inflater;
    private View mView = null;

    private float textViewWidth = 0;

    private LinearLayout progressContainer;
    private LinearLayout formTextContainer;
    private HorizontalScrollView horizontalScrollView;
    private List<TextView> formLabelsTextViewList = new ArrayList<>();
    private float imageWidth = 0;

    private List<? extends CustomProgressModel> formsModelList;
    private List<ImageView> labelImageViewList = new ArrayList<>();
    private List<TextView> labelTextViewList = new ArrayList<>();
    private List<ProgressBar> progressBarList = new ArrayList<>();
    private List<LinearLayout> progressDotsLayoutList = new ArrayList<>();

    private int progressFinal = 0;
    private int currentProgress = 0;

    private String longestString;
    private int longestStringWidth;
    private float screenWidthDp;

    public CustomProgressBarScrollable(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomProgressBarScrollable(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CustomProgressBarScrollable(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomProgressBarScrollable(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    void init() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        mView = inflater.inflate(R.layout.custom_progress_bar_scrollable, this);
        progressContainer = mView.findViewById(R.id.progress_container);
        formTextContainer = mView.findViewById(R.id.form_text_container);
        horizontalScrollView = mView.findViewById(R.id.horizontal_scroll_view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private int getKnownDPI(int pixels) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, getContext().getResources().getDisplayMetrics());
    }

    private void setImageWidth() {
        imageWidth = labelImageViewList.get(0).getWidth();
    }

    public void addProgressForms(List<? extends CustomProgressModel> formsModelList) {
        this.formsModelList = formsModelList;
        longestString = formsModelList.get(0).getLabelName();
        inflateProgressViews();
    }

    private void inflateProgressViews() {

        for (int i = 1; i < formsModelList.size(); i++) {
            if (formsModelList.get(i).getLabelName().length() > formsModelList.get(i - 1).getLabelName().length())
                longestString = formsModelList.get(i).getLabelName();
        }

        calculateLongestStringWidth();
        calculateScreenWidth();

        for (int i = 0; i < formsModelList.size() - 1; i++) {

            View progressLabelView = inflater.inflate(R.layout.single_progress_label, null);
            View progressBarView = inflater.inflate(R.layout.single_progress_bar, null);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    getKnownDPI(longestStringWidth),
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(param);
            linearLayout.addView(progressBarView);

            progressContainer.addView(progressLabelView);
            progressContainer.addView(linearLayout);


            RelativeLayout relativeLayout = (RelativeLayout) ((LinearLayout) progressLabelView).getChildAt(0);
            labelImageViewList.add((ImageView) relativeLayout.getChildAt(0));
            labelTextViewList.add((TextView) relativeLayout.getChildAt(1));

            RelativeLayout relativeLayout2 = (RelativeLayout) ((LinearLayout) progressBarView).getChildAt(0);
            progressDotsLayoutList.add((LinearLayout) relativeLayout2.getChildAt(0));
            progressBarList.add((ProgressBar) relativeLayout2.getChildAt(1));
        }

        View progressLabelView = inflater.inflate(R.layout.single_progress_label, null);
        progressContainer.addView(progressLabelView);

        RelativeLayout relativeLayout = (RelativeLayout) ((LinearLayout) progressLabelView).getChildAt(0);
        labelImageViewList.add((ImageView) relativeLayout.getChildAt(0));
        labelTextViewList.add((TextView) relativeLayout.getChildAt(1));

        progressBarList.get(0).post(new Runnable() {
            @Override
            public void run() {
                setImageWidth();
                progressBarList.get(0).getWidth();
                progressContainer.post(new Runnable() {
                    @Override
                    public void run() {
                        setSectionLabels();
                        setCountTextLabel();
                        progressContainer.getX();
                    }
                });
            }
        });

        invalidate();
    }

    private void calculateScreenWidth() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
    }

    private void calculateLongestStringWidth() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        Rect result = new Rect();
        paint.getTextBounds(longestString, 0, longestString.length(), result);
        longestStringWidth = result.width();
    }

    private void setSectionLabels() {
        LinearLayout.LayoutParams paramLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramLayout.setMargins(0, 14, 0, 0);
        formTextContainer.setLayoutParams(paramLayout);
        formTextContainer.removeAllViews();
        for (int i = 0; i < formsModelList.size(); i++) {
            float marginLeft;

            TextView view = new TextView(getContext());

            if (i == 0) {
                marginLeft = progressContainer.getX() - progressBarList.get(0).getWidth() / 2;
                textViewWidth = ((RelativeLayout) ((LinearLayout) progressContainer.getChildAt(0)).getChildAt(0)).getChildAt(0).getWidth() + progressBarList.get(0).getWidth();
            } else {
                marginLeft = 0;
            }

            view.setTextColor(getResources().getColor(R.color.light_blue_unselected));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) textViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins((int) marginLeft, 0, 0, 0);
            view.setLayoutParams(params);
            view.setText(formsModelList.get(i).getLabelName());
            view.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
            view.setTypeface(view.getTypeface(), Typeface.BOLD);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            formTextContainer.addView(view);
            formLabelsTextViewList.add(view);

            if (i < formsModelList.size() - 1)
                progressBarList.get(i).setMax(formsModelList.get(i).getSectionCount() * 100);
        }

        setCountTextLabel();
        setProgressDotsView();
    }

    private void setCountTextLabel() {
        for (int i = 0; i < labelTextViewList.size(); i++) {
            labelTextViewList.get(i).setText("" + (i + 1));
        }
    }

    private void setProgressDotsView() {
        for (int i = 0; i < progressBarList.size(); i++) {
            int mTotalProgressBarWidth = progressBarList.get(i).getWidth();
            int dotsCount = mTotalProgressBarWidth / 13;
            for (int j = 0; j < dotsCount; j++) {
                View view = new View(getContext());
                view.setBackgroundColor(getResources().getColor(R.color.white));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(3, 3);
                params.setMargins(10, 0, 0, 0);
                view.setLayoutParams(params);
                progressDotsLayoutList.get(i).addView(view);
            }
        }
    }

    public void changeFormProgress(int progress) {
        progressFinal = progress;
        currentProgress = progress;

        progressBarList.get(0).post(new Runnable() {
            @Override
            public void run() {
                progressContainer.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < formsModelList.size(); i++) {

                            if (progressFinal == 0) {
                                resetOther(i);
                                break;
                            }

                            if (formsModelList.get(i).getSectionCount() - progressFinal > 0) {
                                if (i < formsModelList.size() - 1) {
                                    ProgressBarAnimation anim = new ProgressBarAnimation(progressBarList.get(i), progressBarList.get(i).getProgress(), progressFinal * 100);
                                    anim.setDuration(500);
                                    progressBarList.get(i).startAnimation(anim);
                                }
                                labelImageViewList.get(i).setImageResource(R.drawable.progress_label_unselected);
                                labelTextViewList.get(i).setVisibility(VISIBLE);
                                labelTextViewList.get(i).setTextColor(getResources().getColor(R.color.white));
                                formLabelsTextViewList.get(i).setTextColor(getResources().getColor(R.color.white));
                                progressFinal = 0;
                            } else {
                                progressFinal = Math.abs(formsModelList.get(i).getSectionCount() - progressFinal);
                                if (i < formsModelList.size() - 1) {
                                    ProgressBarAnimation anim = new ProgressBarAnimation(progressBarList.get(i), progressBarList.get(i).getProgress(), progressBarList.get(i).getMax());
                                    anim.setDuration(500);
                                    progressBarList.get(i).startAnimation(anim);
                                }
                                labelImageViewList.get(i).setImageResource(R.drawable.progress_label_completed);
                                labelTextViewList.get(i).setVisibility(GONE);
                                formLabelsTextViewList.get(i).setTextColor(getResources().getColor(R.color.white));
                            }
                        }

                        setProgressSelectedProgress();
                    }
                });
            }
        });
    }

    private void resetOther(int pos) {
        for (int i = pos; i < formsModelList.size(); i++) {
            if (i < formsModelList.size() - 1 && progressBarList.get(pos) != null) {
                ProgressBarAnimation anim = new ProgressBarAnimation(progressBarList.get(i), progressBarList.get(i).getProgress(), 0);
                anim.setDuration(500);
                progressBarList.get(i).startAnimation(anim);
            }

            labelImageViewList.get(i).setImageResource(R.drawable.progress_label_unselected);
            labelTextViewList.get(i).setVisibility(VISIBLE);
            labelTextViewList.get(i).setTextColor(getResources().getColor(R.color.text_label_unselected));
            formLabelsTextViewList.get(i).setTextColor(getResources().getColor(R.color.light_blue_unselected));

        }
    }

    private void setProgressSelectedProgress() {
        if (currentProgress == 0) {
            labelTextViewList.get(0).setTextColor(getResources().getColor(R.color.white));
            formLabelsTextViewList.get(0).setTextColor(getResources().getColor(R.color.white));
            labelImageViewList.get(0).setImageResource(R.drawable.progress_label_selected);
            horizontalScrollView.smoothScrollTo(0, 0);
        } else {
            int progressPrevious = 0;
            for (int i = 0; i < formsModelList.size(); i++) {
                if (currentProgress * 100 < progressPrevious + formsModelList.get(i).getSectionCount() * 100) {
                    labelTextViewList.get(i).setTextColor(getResources().getColor(R.color.white));
                    formLabelsTextViewList.get(i).setTextColor(getResources().getColor(R.color.white));
                    labelImageViewList.get(i).setImageResource(R.drawable.progress_label_selected);
                    setCenter(i);
                    break;
                }
                progressPrevious += formsModelList.get(i).getSectionCount() * 100;
            }
        }
    }

    private void setCenter(int index) {
        LinearLayout parent = (LinearLayout) ((LinearLayout) horizontalScrollView.getChildAt(0)).getChildAt(1);
        View view = parent.getChildAt(index);
        int scrollX = (view.getLeft() - ((int) (screenWidthDp)))
                + (view.getWidth() / 10);
        horizontalScrollView.smoothScrollTo(scrollX, 0);
    }
}