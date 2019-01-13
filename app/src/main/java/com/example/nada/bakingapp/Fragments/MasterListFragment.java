package com.example.nada.bakingapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nada.bakingapp.Activities.RecipeDetailsActivity;
import com.example.nada.bakingapp.Adapters.VideosAdapter;
import com.example.nada.bakingapp.Models.Recipe;
import com.example.nada.bakingapp.Models.Step;
import com.example.nada.bakingapp.R;
import com.example.nada.bakingapp.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.nada.bakingapp.Utils.Constants.RECIPE_KEY;

public class MasterListFragment extends Fragment implements VideosAdapter.ListItemClickListener {

    public final static String TAG = MasterListFragment.class.getName();

    //Views
    @BindView(R.id.rv_videos)
    RecyclerView mVideosList;

    //Vars
    private Recipe mRecipe;
    private List<Step> tempStepsList;
    private VideosAdapter mVideosAdapter;

    public static MasterListFragment newInstance(Recipe recipes) {
        MasterListFragment masterListFragment = new MasterListFragment();
        Bundle masterListBundle = new Bundle();
        masterListBundle.putParcelable(RECIPE_KEY, recipes);
        masterListFragment.setArguments(masterListBundle);
        return masterListFragment;
    }

    public MasterListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        ButterKnife.bind(this, rootView);

        mRecipe = (Recipe) getArguments().getParcelable(RECIPE_KEY);

        tempStepsList = new ArrayList<>();
        tempStepsList.addAll(mRecipe.getSteps());
        mVideosAdapter = new VideosAdapter(getContext(), tempStepsList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mVideosList.setLayoutManager(linearLayoutManager);
        mVideosList.setAdapter(mVideosAdapter);

        return rootView;
    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        if (!Utils.isTablet(getContext())) {
            VideosNavigationFragment videosNavigationFragment = VideosNavigationFragment.newInstance(mRecipe, clickedItemIndex);
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_details_fragment, videosNavigationFragment)
                    .addToBackStack(RecipeDetailsActivity.TAG)
                    .commit();
        } else {
            VideosNavigationFragment videosNavigationFragment = VideosNavigationFragment.newInstance(mRecipe, clickedItemIndex);

            fragmentManager.beginTransaction()
                    .replace(R.id.tablet_video_details_fragment, videosNavigationFragment)
                    .commit();
        }
    }

}
