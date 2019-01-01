package com.example.nada.bakingapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nada.bakingapp.Adapters.IngredientsAdapter;
import com.example.nada.bakingapp.Models.Ingredient;
import com.example.nada.bakingapp.Models.Recipe;
import com.example.nada.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.nada.bakingapp.Utils.Constants.RECIPE_KEY;

public class IngredientsFragment extends Fragment {

    public static final String TAG = IngredientsFragment.class.getName();

    //Views
    @BindView(R.id.rv_ingredients)
    RecyclerView mIngredientList;

    //Vars
    private Recipe mRecipe;
    private List<Ingredient> tempIngredientsList;
    private IngredientsAdapter mIngredientsAdapter;

    public IngredientsFragment() {

    }

    public static IngredientsFragment newInstance(Recipe recipes) {
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        Bundle ingredientBundle = new Bundle();
        ingredientBundle.putSerializable(RECIPE_KEY, recipes);
        ingredientsFragment.setArguments(ingredientBundle);
        return ingredientsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        ButterKnife.bind(this, rootView);

        mRecipe = (Recipe) getArguments().getSerializable(RECIPE_KEY);

        tempIngredientsList = new ArrayList<>();
        tempIngredientsList.addAll(mRecipe.getIngredients());
        mIngredientsAdapter = new IngredientsAdapter(getContext(), tempIngredientsList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1,
                GridLayoutManager.HORIZONTAL, false);
        mIngredientList.setLayoutManager(gridLayoutManager);
        mIngredientList.setAdapter(mIngredientsAdapter);

        return rootView;
    }

}
