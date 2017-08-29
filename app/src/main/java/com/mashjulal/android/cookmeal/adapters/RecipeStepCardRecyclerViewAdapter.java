package com.mashjulal.android.cookmeal.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mashjulal.android.cookmeal.R;
import com.mashjulal.android.cookmeal.objects.Step;
import com.mashjulal.android.cookmeal.objects.StepList;

import static com.mashjulal.android.cookmeal.Utils.formatTime;


public class RecipeStepCardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

     private Context mContext;
    private StepList mStepList;
    private boolean mIsEditable;

    private static OnButtonClickListener sOnButtonClickListener;

    public RecipeStepCardRecyclerViewAdapter(Context context, StepList stepList, boolean isEditable) {
        mContext = context;
        mStepList = stepList;
        mIsEditable = isEditable;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(viewType, parent, false);
        RecyclerView.ViewHolder vh = null;
        switch (viewType) {
            case R.layout.item_recipe_step_card:
                vh = new RecipeStepCardViewHolder(view);
                break;
            case R.layout.item_recipe_step_edit_card:
                vh = new RecipeStepEditCardViewHolder(view);
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Step recipeStep = mStepList.get(position);
        if (holder instanceof RecipeStepCardViewHolder) {
            initRecipeCard((RecipeStepCardViewHolder) holder, recipeStep);
        } else {
            initRecipeEditCard((RecipeStepEditCardViewHolder) holder, recipeStep, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (mIsEditable) ?
                R.layout.item_recipe_step_edit_card :
                R.layout.item_recipe_step_card;
    }

    @Override
    public int getItemCount() {
        return mStepList.size();
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        sOnButtonClickListener = listener;
    }

    private void initRecipeEditCard(final RecipeStepEditCardViewHolder holder, final Step recipeStep, final int position) {
        holder.tvStepNumber.setText(String.format(
                mContext.getString(R.string.title_step_number),
                position + 1));
        if (!recipeStep.getImagePath().isEmpty()) {
            holder.ibAddImage.setVisibility(View.GONE);
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.ivImage.setImageBitmap(BitmapFactory.decodeFile(recipeStep.getImagePath()));
        } else {
            holder.ibAddImage.setVisibility(View.VISIBLE);
            holder.ivImage.setImageBitmap(null);
            holder.ivImage.setVisibility(View.GONE);
        }

        holder.tilDescription.getEditText().setText(
                (!recipeStep.getDescription().isEmpty()) ?
                        recipeStep.getDescription() :
                        null);
        holder.tilDescription.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                recipeStep.setDescription(s.toString());
            }
        });

        if (recipeStep.getTime() != 0) {
            holder.ibAddTime.setVisibility(View.GONE);
            holder.llTime.setVisibility(View.VISIBLE);
            holder.llTime.setOnClickListener((v) -> sOnButtonClickListener.onAddTimeClick(position));
            holder.tvTime.setText(formatTime(recipeStep.getTime()));
        } else {
            holder.ibAddTime.setVisibility(View.VISIBLE);
            holder.llTime.setVisibility(View.GONE);
            holder.tvTime.setText(null);
        }
        holder.ibAddImage.setOnClickListener(v -> sOnButtonClickListener.onAddImageClick(position));
        holder.ibAddTime.setOnClickListener(v -> sOnButtonClickListener.onAddTimeClick(position));
    }

    private void initRecipeCard(RecipeStepCardViewHolder holder, Step recipeStep) {
        holder.tvStepNumber.setText(String.format(
                mContext.getString(R.string.title_step_number),
                recipeStep.getStepNumber()));
        if (!recipeStep.getImagePath().isEmpty()) {
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.ivImage.setImageBitmap(BitmapFactory.decodeFile(recipeStep.getImagePath()));
        } else {
            holder.ivImage.setImageBitmap(null);
            holder.ivImage.setVisibility(View.GONE);
        }
        holder.tvDescription.setText(recipeStep.getDescription());
        if (recipeStep.getTime() != 0) {
            holder.llTime.setVisibility(View.VISIBLE);
            holder.tvTime.setText(formatTime(recipeStep.getTime()));
        } else {
            holder.llTime.setVisibility(View.GONE);
            holder.tvTime.setText(null);
        }
    }

    public void setErrorOnEditText(RecyclerView.ViewHolder viewHolder, String message) {
        ((RecipeStepEditCardViewHolder) viewHolder).tilDescription.setError(message);
    }

    public void removeItem(int position) {
        mStepList.remove(position);
        notifyItemRangeRemoved(position, 1);
        notifyItemRangeChanged(position, mStepList.size());
    }

    public void addItem(Step step) {
        mStepList.add(step);
        notifyItemInserted(mStepList.size()-1);
    }

    private static class RecipeStepEditCardViewHolder extends RecyclerView.ViewHolder {

        TextView tvStepNumber;
        ImageView ivImage;
        TextInputLayout tilDescription;
        ImageButton ibAddImage;
        ImageButton ibAddTime;
        LinearLayout llTime;
        TextView tvTime;


        RecipeStepEditCardViewHolder(View itemView) {
            super(itemView);

            tvStepNumber = (TextView) itemView.findViewById(R.id.tv_iRecipeStepEditCard_stepNumber);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_iRecipeStepEditCard_image);
            tilDescription = (TextInputLayout) itemView.findViewById(R.id.til_iRecipeStepEditCard_description);
            ibAddTime = (ImageButton) itemView.findViewById(R.id.ib_iRecipeStepEditCard_addTime);
            ibAddImage = (ImageButton) itemView.findViewById(R.id.ib_iRecipeStepEditCard_addImage);
            llTime = (LinearLayout) itemView.findViewById(R.id.ll_iRecipeStepEditCard_time);
            tvTime = (TextView) itemView.findViewById(R.id.tv_iRecipeStepEditCard_time);
        }
    }

    private static class RecipeStepCardViewHolder extends RecyclerView.ViewHolder {

        TextView tvStepNumber;
        ImageView ivImage;
        TextView tvDescription;
        LinearLayout llTime;
        TextView tvTime;

        RecipeStepCardViewHolder(View itemView) {
            super(itemView);

            tvStepNumber = (TextView) itemView.findViewById(R.id.tv_iRecipeStepCard_stepNumber);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_iRecipeStepCard_image);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_iRecipeStepCard_description);
            llTime = (LinearLayout) itemView.findViewById(R.id.ll_iRecipeStepCard_time);
            tvTime = (TextView) itemView.findViewById(R.id.tv_iRecipeStepCard_time);
        }
    }

    public interface OnButtonClickListener {
        void onAddImageClick (int position);
        void onAddTimeClick (int position);
    }
}
