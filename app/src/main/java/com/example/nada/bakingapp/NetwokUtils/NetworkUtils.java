package com.example.nada.bakingapp.NetwokUtils;

import com.example.nada.bakingapp.Models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkUtils {

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
