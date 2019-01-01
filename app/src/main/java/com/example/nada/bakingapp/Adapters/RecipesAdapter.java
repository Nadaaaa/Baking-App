package com.example.nada.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nada.bakingapp.Models.Recipe;
import com.example.nada.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    public static final String TAG = RecipesAdapter.class.getName();

    private Context mContext;
    private List<Recipe> mRecipesList;
    private final ListItemClickListener mClickListener;

    public interface ListItemClickListener {
        void onListItemClicked(int clickedItemIndex);
    }

    public RecipesAdapter(Context context, List<Recipe> recipesList, ListItemClickListener listener) {
        mContext = context;
        mRecipesList = recipesList;
        mClickListener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutRecipeItem = R.layout.item_recipe;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutRecipeItem, viewGroup, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int i) {
        recipeViewHolder.bind(mRecipesList.get(i));
    }

    @Override
    public int getItemCount() {
        return mRecipesList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageView_recipe)
        ImageView mRecipeImage;
        @BindView(R.id.textView_recipeName)
        TextView mRecipeName;
        @BindView(R.id.textView_recipeServingsTimes)
        TextView mRecipeServingsTimes;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bind(Recipe recipe) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.baking);

            Glide.with(mContext).load(recipe.getImage())
                    .thumbnail(0.5f).apply(requestOptions)
                    .into(mRecipeImage);

            mRecipeName.setText(recipe.getName());
            mRecipeServingsTimes.setText(String.valueOf(recipe.getServings()));
        }

        @Override
        public void onClick(View view) {
            mClickListener.onListItemClicked(getAdapterPosition());
        }
    }

}
