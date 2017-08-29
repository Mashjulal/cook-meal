package com.mashjulal.android.cookmeal.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mashjulal.android.cookmeal.R;
import com.mashjulal.android.cookmeal.database.CookMealLab;
import com.mashjulal.android.cookmeal.objects.Recipe;

import java.util.ArrayList;
import java.util.List;

import static com.mashjulal.android.cookmeal.Utils.formatTime;


public class RecipeCardRecyclerViewAdapter extends
        RecyclerView.Adapter<RecipeCardRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Recipe> mRecipes = new ArrayList<>();
    private static OnViewClickListener sOnViewClickListener;

    public RecipeCardRecyclerViewAdapter(Context context, List<Recipe> recipes) {
        mContext = context;
        mRecipes = recipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_recipe_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        if (recipe.getPreviewImagePath().isEmpty()) {
            holder.ivPreviewImage.setImageResource(R.drawable.no_photo);
        } else {
            holder.ivPreviewImage.setImageBitmap(BitmapFactory.decodeFile(recipe.getPreviewImagePath()));
        }
        holder.tvRecipeName.setText(recipe.getName());
        holder.view.setOnClickListener((v) -> sOnViewClickListener.onViewClick(position));
        holder.ibStart.setOnClickListener((v) -> sOnViewClickListener.onStartButtonClick(position));
        long cookTime = CookMealLab.getInstance(mContext).getCookTimeForRecipe(recipe.getId());
        if (cookTime != 0) {
            holder.tvCookTime.setText(formatTime(cookTime));
        } else {
            holder.tvCookTime.setText(null);
            holder.tvCookTime.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public void setOnViewClickListener(OnViewClickListener listener) {
        sOnViewClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView ivPreviewImage;
        TextView tvRecipeName;
        ImageButton ibStart;
        TextView tvCookTime;

        ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            ivPreviewImage = (ImageView) itemView.findViewById(R.id.iv_iRecipeCard_previewImage);
            tvRecipeName = (TextView) itemView.findViewById(R.id.tv_iRecipeCard_recipeName);
            ibStart = (ImageButton) itemView.findViewById(R.id.ib_iRecipeCard_start);
            tvCookTime = (TextView) itemView.findViewById(R.id.tv_iRecipeCard_cookTime);
        }
    }

    public interface OnViewClickListener {
        void onViewClick (int position);
        void onStartButtonClick (int position);
    }
}
