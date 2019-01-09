package com.example.nada.bakingapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nada.bakingapp.Models.Recipe;
import com.example.nada.bakingapp.R;

import static com.example.nada.bakingapp.Utils.Constants.RECIPE_KEY;

public class RecipeDetailsFragment extends Fragment {

    public final static String TAG = RecipeDetailsFragment.class.getName();

    //Vars
    private Recipe mRecipe;

    public static RecipeDetailsFragment newInstance(Recipe recipes) {
        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        Bundle recipeDetailsBundle = new Bundle();
        recipeDetailsBundle.putParcelable(RECIPE_KEY, recipes);
        recipeDetailsFragment.setArguments(recipeDetailsBundle);
        return recipeDetailsFragment;
    }

    public RecipeDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        mRecipe = (Recipe) getArguments().getParcelable(RECIPE_KEY);

        IngredientsFragment ingredientsFragment = IngredientsFragment.newInstance(mRecipe);
        MasterListFragment masterListFragment = MasterListFragment.newInstance(mRecipe);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.ingredients_fragment, ingredientsFragment)
                .commit();

        fragmentManager.beginTransaction()
                .replace(R.id.master_list_fragment, masterListFragment)
                .commit();
        return rootView;
    }
}
