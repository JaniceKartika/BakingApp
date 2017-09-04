package jkm.com.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jkm.com.bakingapp.R;
import jkm.com.bakingapp.util.RecipeAssets;
import jkm.com.bakingapp.model.RecipeModel;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private Context context;
    private ArrayList<RecipeModel> recipeModels;
    private OnItemClickListener itemClickListener;

    public RecipeAdapter(Context context, ArrayList<RecipeModel> recipeModels, OnItemClickListener itemClickListener) {
        this.context = context;
        this.recipeModels = recipeModels;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        RecipeModel recipeModel = recipeModels.get(position);

        String recipeName = recipeModel.getName();
        holder.recipeNameTextView.setText(recipeName);

        String serving = context.getString(R.string.serving_prefix) +
                recipeModel.getServings() +
                context.getString(R.string.serving_suffix);
        holder.recipeServingTextView.setText(serving);

        String imagePath = recipeModel.getImage() == null ? "" : recipeModel.getImage();
        if (imagePath.isEmpty()) {
            holder.recipeImageView.setImageResource(RecipeAssets.getRecipeImageID(recipeName));
        } else {
            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.recipeImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (recipeModels == null) return 0;
        else return recipeModels.size();
    }

    public interface OnItemClickListener {
        void setOnItemClickListener(View view, int position);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_recipe_image)
        ImageView recipeImageView;
        @BindView(R.id.tv_recipe_name)
        TextView recipeNameTextView;
        @BindView(R.id.tv_recipe_serving)
        TextView recipeServingTextView;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.setOnItemClickListener(view, getAdapterPosition());
            }
        }
    }
}
