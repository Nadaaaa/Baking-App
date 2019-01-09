package com.example.nada.bakingapp.Widgets;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.nada.bakingapp.Activities.MainActivity;
import com.example.nada.bakingapp.Models.Ingredient;
import com.example.nada.bakingapp.R;

import java.util.List;



public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AppWidgetListView(this.getApplicationContext(), MainActivity.getDataFromSharedPrefs(getApplicationContext()).getIngredients());
    }
}

class AppWidgetListView implements RemoteViewsService.RemoteViewsFactory {

    private List<Ingredient> dataList;
    private Context context;

    public AppWidgetListView(Context applicationContext, List<Ingredient> dataList) {
        this.context = applicationContext;
        this.dataList = dataList;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item_ingredient);

        String ingredientName = dataList.get(position).getQuantity() + " "
                + dataList.get(position).getMeasure() + " "
                + dataList.get(position).getIngredient();

        views.setTextViewText(R.id.widget_ingredientName, ingredientName);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {

        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

