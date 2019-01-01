package com.example.nada.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nada.bakingapp.Models.Ingredient;
import com.example.nada.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    public static final String TAG = IngredientsAdapter.class.getName();

    private Context mContext;
    private List<Ingredient> mIngredientsList;

    public IngredientsAdapter(Context context, List<Ingredient> ingredientsList) {
        mContext = context;
        mIngredientsList = ingredientsList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutIngredientItem = R.layout.item_ingredient;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutIngredientItem, viewGroup, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewHolder, int i) {
        ingredientViewHolder.bind(mIngredientsList.get(i));
    }

    @Override
    public int getItemCount() {
        return mIngredientsList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_itemIngredient_quantity)
        TextView textView_itemIngredient_quantity;
        @BindView(R.id.textView_itemIngredient_measure)
        TextView textView_itemIngredient_measure;
        @BindView(R.id.textView_itemIngredient_name)
        TextView textView_itemIngredient_name;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(Ingredient ingredient) {
            textView_itemIngredient_quantity.setText(String.valueOf(ingredient.getQuantity()));
            textView_itemIngredient_measure.setText(ingredient.getMeasure());
            textView_itemIngredient_name.setText(ingredient.getIngredient());
        }

    }
}
