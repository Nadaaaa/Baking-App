package com.example.nada.bakingapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nada.bakingapp.Models.Recipe;
import com.example.nada.bakingapp.Models.Step;
import com.example.nada.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.nada.bakingapp.Utils.Constants.RECIPE_KEY;
import static com.example.nada.bakingapp.Utils.Constants.VIDEO_KEY;

public class VideosNavigationFragment extends Fragment {

    public static final String TAG = VideosNavigationFragment.class.getName();

    //Views
    @BindView(R.id.imageView_previousVideo)
    ImageView imageView_previousVideo;
    @BindView(R.id.imageView_nextVideo)
    ImageView imageView_nextVideo;
    @BindView(R.id.textView_stepNumber)
    TextView textView_stepNumber;
    @BindView(R.id.textView_totalSteps)
    TextView textView_totalSteps;
    @BindView(R.id.layout_steps)
    LinearLayout layout_steps;

    // Vars
    private Recipe mRecipe;
    private static int clickedItemIndex;
    private int videosListLength;
    private FragmentManager fragmentManager;
    public static VideosNavigationFragment instance;
    private VideoDetailsFragment videoDetailsFragment;

    public VideosNavigationFragment() {

    }


    public static VideosNavigationFragment newInstance(Recipe recipe, int clickedItemIndex) {
        VideosNavigationFragment videosNavigationFragment = new VideosNavigationFragment();
        Bundle videoBundle = new Bundle();
        videoBundle.putParcelable(RECIPE_KEY, recipe);
        videoBundle.putInt(VIDEO_KEY, clickedItemIndex);
        videosNavigationFragment.setArguments(videoBundle);
        return videosNavigationFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_videos_navigation, container, false);
        instance = this;
        ButterKnife.bind(this, rootView);

        fragmentManager = getChildFragmentManager();

        mRecipe = getArguments().getParcelable(RECIPE_KEY);
        clickedItemIndex = getArguments().getInt(VIDEO_KEY);
        videosListLength = mRecipe.getSteps().size();

        if (clickedItemIndex == 0) {
            layout_steps.setVisibility(View.GONE);
            imageView_previousVideo.setFocusable(false);
            imageView_previousVideo.setImageResource(R.drawable.ic_navigate_before_dimmed);
        } else if (clickedItemIndex == videosListLength - 1) {
            layout_steps.setVisibility(View.VISIBLE);
            imageView_nextVideo.setFocusable(false);
            imageView_nextVideo.setImageResource(R.drawable.ic_navigate_next_dimmed);
        }
        Step currentVideo = mRecipe.getSteps().get(clickedItemIndex);

        textView_stepNumber.setText(String.valueOf(currentVideo.getId()));
        textView_totalSteps.setText(String.valueOf(videosListLength - 1));

        if(VideoDetailsFragment.getInstance()==null)
            videoDetailsFragment = VideoDetailsFragment.newInstance(currentVideo);
        else
            videoDetailsFragment = VideoDetailsFragment.getInstance();

        fragmentManager.beginTransaction()
                .replace(R.id.video_details_frameLayout, videoDetailsFragment)
                .commit();
        return rootView;
    }



    @OnClick(R.id.imageView_nextVideo)
    public void onClickImageViewNextVideo() {
        layout_steps.setVisibility(View.VISIBLE);
        int nextVideoIndex = clickedItemIndex + 1;
        if (nextVideoIndex == 1) {
            imageView_previousVideo.setImageResource(R.drawable.ic_navigate_before);
        }
        if (nextVideoIndex > videosListLength - 1) {
            imageView_nextVideo.setFocusable(false);
            imageView_nextVideo.setImageResource(R.drawable.ic_navigate_next_dimmed);
        } else {
            if (nextVideoIndex == videosListLength - 1) {
                imageView_nextVideo.setImageResource(R.drawable.ic_navigate_next_dimmed);
            }
            clickedItemIndex++;

            Step nextVideo = mRecipe.getSteps().get(clickedItemIndex);
            textView_stepNumber.setText(String.valueOf(nextVideo.getId()));

            VideoDetailsFragment videoDetailsFragment = VideoDetailsFragment.newInstance(nextVideo);

            fragmentManager.beginTransaction()
                    .replace(R.id.video_details_frameLayout, videoDetailsFragment)
                    .commit();
        }
    }

    @OnClick(R.id.imageView_previousVideo)
    public void onClickImageViewPreviousVideo() {
        layout_steps.setVisibility(View.VISIBLE);
        int previousVideoIndex = clickedItemIndex - 1;
        if (previousVideoIndex == videosListLength - 2) {
            imageView_nextVideo.setImageResource(R.drawable.ic_navigate_next);
        }
        if (previousVideoIndex < 0) {
            imageView_previousVideo.setFocusable(false);
            imageView_previousVideo.setImageResource(R.drawable.ic_navigate_before_dimmed);
        } else {
            if (previousVideoIndex == 0) {
                layout_steps.setVisibility(View.GONE);
                imageView_previousVideo.setImageResource(R.drawable.ic_navigate_before_dimmed);
            }
            clickedItemIndex--;

            Step previousVideo = mRecipe.getSteps().get(clickedItemIndex);
            textView_stepNumber.setText(String.valueOf(previousVideo.getId()));

            VideoDetailsFragment videoDetailsFragment = VideoDetailsFragment.newInstance(previousVideo);

            fragmentManager.beginTransaction()
                    .replace(R.id.video_details_frameLayout, videoDetailsFragment)
                    .commit();
        }
    }

    public static VideosNavigationFragment getInstance() {
        return instance;
    }
}
