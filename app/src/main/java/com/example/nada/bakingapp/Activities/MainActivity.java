package com.example.nada.bakingapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.nada.bakingapp.Utils.Utils;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import com.example.nada.bakingapp.Adapters.RecipesAdapter;
import com.example.nada.bakingapp.Models.Recipe;
import com.example.nada.bakingapp.NetwokUtils.Retrofit;
import com.example.nada.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.nada.bakingapp.Utils.Constants.RECIPE_KEY;
import static com.example.nada.bakingapp.Utils.Constants.Recipes_URL;
import static com.example.nada.bakingapp.Utils.Utils.isConnectedToInternet;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.ListItemClickListener {

    public static final String TAG = MainActivity.class.getName();

    //Views
    @BindView(R.id.recipesLayout)
    CoordinatorLayout recipesLayout;
    @BindView(R.id.rv_recipes)
    RecyclerView mRecipesList;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    //Vars
    private RecipesAdapter mRecipesAdapter;
    private List<Recipe> tempRecipesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        tempRecipesList = new ArrayList<>();
        mRecipesAdapter = new RecipesAdapter(this, tempRecipesList, this);

        if (Utils.isTablet(getApplicationContext())) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            mRecipesList.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mRecipesList.setLayoutManager(linearLayoutManager);
        }

        mRecipesList.setHasFixedSize(false);
        mRecipesList.setAdapter(mRecipesAdapter);

        if (!isConnectedToInternet(this)) {
            showErrorSnackBar(getApplicationContext(), recipesLayout, getResources().getString(R.string.check_Internet_connection));
        } else {
            getRecipes();
        }

    }

    void getRecipes() {
        startAnim();
        Retrofit.getService(Recipes_URL).getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                stopAnim();
                tempRecipesList.addAll(response.body());
                mRecipesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                stopAnim();
                Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: InternetConnection");
            }
        });
    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
        Gson gson = new Gson();
        String recipe = gson.toJson(tempRecipesList.get(clickedItemIndex));
        startActivity(new Intent(MainActivity.this, RecipeDetailsActivity.class).putExtra(RECIPE_KEY, recipe));
    }


    void startAnim() {
        avi.show();
    }

    void stopAnim() {
        avi.hide();
    }

    public void showSuccessfulSnackBar(View view, int message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public void showErrorSnackBar(final Context context, View view, final String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isConnectedToInternet(context)) {
                            showSuccessfulSnackBar(view, R.string.internet_successfully_connected);
                            getRecipes();
                        } else {
                            showErrorSnackBar(context, recipesLayout, getResources().getString(R.string.check_Internet_connection));
                        }
                    }
                })
                .show();
    }

}
