package com.fidflop.baketime.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fidflop.baketime.R;
import com.fidflop.baketime.data.Recipe;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    Context context;

    public RecipeListAdapter(List<Recipe> recipes, Context context) {
        this.recipes = recipes;
        this.context = context;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View recipeCardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_card, parent, false);
        RecipeViewHolder rvh = new RecipeViewHolder(recipeCardView);
        return rvh;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, final int position) {
        holder.txtRecipeName.setText(recipes.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String name = recipes.get(position).getName();
                //Toast.makeText(context, name + " is selected", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra("recipe", recipes.get(position));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView txtRecipeName;

        public RecipeViewHolder(View view) {
            super(view);
            txtRecipeName = view.findViewById(R.id.idRecipeName);
        }
    }
}