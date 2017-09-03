package jkm.com.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private Context context;
    private ArrayList<IngredientModel> ingredientModels;

    IngredientAdapter(Context context, ArrayList<IngredientModel> ingredientModels) {
        this.context = context;
        this.ingredientModels = ingredientModels;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        IngredientModel ingredientModel = ingredientModels.get(position);

        String formattedQuantity = new DecimalFormat("#.#").format(ingredientModel.getQuantity());
        String ingredient = formattedQuantity + " " +
                ingredientModel.getMeasure() + " " +
                ingredientModel.getIngredient();

        holder.ingredientTextView.setText(ingredient);
    }

    @Override
    public int getItemCount() {
        if (ingredientModels == null) return 0;
        else return ingredientModels.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientTextView;

        IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientTextView = (TextView) itemView.findViewById(R.id.tv_ingredient);
            itemView.setTag(itemView);
        }
    }
}
