package com.example.nada.bakingapp.Activities;


import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.nada.bakingapp.Fragments.RecipeDetailsFragment;
import com.example.nada.bakingapp.Fragments.VideoDetailsFragment;
import com.example.nada.bakingapp.Fragments.VideosNavigationFragment;
import com.example.nada.bakingapp.Models.Recipe;
import com.example.nada.bakingapp.R;

import com.example.nada.bakingapp.Utils.Utils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.nada.bakingapp.Utils.Constants.RECIPE_KEY;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static final String TAG = RecipeDetailsActivity.class.getName();

    //Views
    @BindView(R.id.iv_backArrow)
    ImageView iv_backArrow;

    //Vars
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ButterKnife.bind(this);

        Gson gson = new Gson();
        mRecipe = gson.fromJson(getIntent().getStringExtra(RECIPE_KEY), Recipe.class);

        RecipeDetailsFragment recipeDetailsFragment = RecipeDetailsFragment.newInstance(mRecipe);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (!Utils.isTablet(this)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_details_fragment, recipeDetailsFragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.tablet_recipe_details_fragment, recipeDetailsFragment)
                    .commit();

            VideosNavigationFragment videosNavigationFragment = VideosNavigationFragment.newInstance(mRecipe, 0);

            fragmentManager.beginTransaction()
                    .replace(R.id.tablet_video_details_fragment, videosNavigationFragment)
                    .commit();
        }

    }

    @OnClick(R.id.iv_backArrow)
    public void onClickBackArrow() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }
}
