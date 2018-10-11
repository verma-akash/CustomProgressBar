package com.gravity.customprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akash Verma on 20/04/18.
 */

public class CustomProgressBar extends FrameLayout {

    private int backgroundColor;
    private Context context;
    private LayoutInflater inflater;
    private View mView = null;
    private OnProgressLabelInteractionListener mListener;

    float textViewWidth = 0;

    private LinearLayout progressContainer;
    private LinearLayout formTextContainer;
    private List<TextView> formLabelsTextViewList = new ArrayList<>();
    float imageWidth = 0;

    List<? extends CustomProgressModel> formsModelList;
    List<ImageView> labelImageViewList = new ArrayList<>();
    List<TextView> labelTextViewList = new ArrayList<>();
    List<ProgressBar> progressBarList = new ArrayList<>();
    List<LinearLayout> progressDotsLayoutList = new ArrayList<>();

    int progressFinal = 0;
    int currentProgress = 0;
    int cumulativeSectionCount = 0;

    public CustomProgressBar(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, 0);
        backgroundColor = attributes.getColor(R.styleable.CustomProgressBar_cps_background, getResources().getColor(android.R.color.white));
        attributes.recycle();
        init(context);
    }

    public CustomProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, 0);
        backgroundColor = attributes.getColor(R.styleable.CustomProgressBar_cps_background, getResources().getColor(android.R.color.white));
        attributes.recycle();
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, 0);
        backgroundColor = attributes.getColor(R.styleable.CustomProgressBar_cps_background, getResources().getColor(android.R.color.white));
        attributes.recycle();
        init(context);
    }

    void init(Context context) {
        this.context = context;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        mView = inflater.inflate(R.layout.custom_progress_bar, this);
        progressContainer = mView.findViewById(R.id.progress_container);
        formTextContainer = mView.findViewById(R.id.form_text_container);
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

    public void addProgressForms(OnProgressLabelInteractionListener listener,
                                 List<? extends CustomProgressModel> formsModelList) {
        this.mListener = listener;
        this.formsModelList = formsModelList;
        inflateProgressViews();
    }

    private void inflateProgressViews() {
        for (int i = 0; i < formsModelList.size(); i++) {
            final int formIndex = i;
            final int cumulative = cumulativeSectionCount;

            View progressLabelView = inflater.inflate(R.layout.single_progress_label, null);
            progressContainer.addView(progressLabelView);

            RelativeLayout relativeLayout = (RelativeLayout) ((LinearLayout) progressLabelView).getChildAt(0);
            labelImageViewList.add((ImageView) relativeLayout.getChildAt(0));
            labelTextViewList.add((TextView) relativeLayout.getChildAt(1));

            progressLabelView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onProgressLabelClick(cumulative, formsModelList.get(formIndex).getLabelName());
                }
            });

            cumulativeSectionCount += formsModelList.get(formIndex).getSectionCount();

            View progressBarView = inflater.inflate(R.layout.single_progress_bar, null);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(param);
            linearLayout.addView(progressBarView);

            if (formsModelList.size() == 1 || (formsModelList.size() != 1 && i < formsModelList.size() - 1)) {
                progressContainer.addView(linearLayout);
            }

            RelativeLayout relativeLayout2 = (RelativeLayout) ((LinearLayout) progressBarView).getChildAt(0);
            progressDotsLayoutList.add((LinearLayout) relativeLayout2.getChildAt(0));
            progressBarList.add((ProgressBar) relativeLayout2.getChildAt(1));
        }

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

    private void setSectionLabels() {
        RelativeLayout.LayoutParams paramLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramLayout.setMargins(0, progressContainer.getHeight() + 6, 0, 0);
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

            if (i < formsModelList.size() - 1) {
                progressBarList.get(i).setMax(formsModelList.get(i).getSectionCount() * 100);
            }

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
                                if (formsModelList.size() == 1 || (formsModelList.size() != 1 && i < formsModelList.size() - 1)) {
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
        } else {
            int progressPrevious = 0;
            for (int i = 0; i < formsModelList.size(); i++) {
                if (currentProgress * 100 < progressPrevious + formsModelList.get(i).getSectionCount() * 100) {
                    labelTextViewList.get(i).setTextColor(getResources().getColor(R.color.white));
                    formLabelsTextViewList.get(i).setTextColor(getResources().getColor(R.color.white));
                    labelImageViewList.get(i).setImageResource(R.drawable.progress_label_selected);
                    break;
                }
                progressPrevious += formsModelList.get(i).getSectionCount() * 100;
            }
        }
    }

    public interface OnProgressLabelInteractionListener {
        void onProgressLabelClick(int formIndex, String classificationName);
    }
}
