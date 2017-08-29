package com.mashjulal.android.cookmeal.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mashjulal.android.cookmeal.R;
import com.mashjulal.android.cookmeal.objects.Ingredient;
import com.mashjulal.android.cookmeal.objects.IngredientList;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class IngredientItemRecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private IngredientList mIngredientList;
    // TODO: change type to type code
    private static List<String> sQuantityTypes;
    private boolean mIsEditable;

    public IngredientItemRecyclerViewAdapter(Context context, IngredientList ingredientList, boolean isEditable) {
        mContext = context;
        mIngredientList = ingredientList;
        sQuantityTypes = Arrays.asList(mContext.getResources()
                .getStringArray(R.array.entries_spinner_edit_ingredients));
        mIsEditable = isEditable;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(viewType, parent, false);
        RecyclerView.ViewHolder vh = null;
        switch (viewType) {
            case R.layout.item_ingredient:
                vh = new IngredientItemViewHolder(view);
                break;
            case R.layout.item_edit_ingredient:
                vh = new IngredientEditItemViewHolder(view);
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Ingredient ingredient = mIngredientList.getIngredient(position);
        if (holder instanceof IngredientItemViewHolder) {
            initIngredientItem((IngredientItemViewHolder) holder, ingredient);
        } else {
            initIngredientEditItem((IngredientEditItemViewHolder) holder, ingredient);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (mIsEditable) ?
                R.layout.item_edit_ingredient :
                R.layout.item_ingredient;
    }

    private void initIngredientEditItem(
            final IngredientEditItemViewHolder holder, Ingredient ingredient) {
        holder.etName.setText(ingredient.getName());
        holder.spinnerQuantityTypes.setSelection(sQuantityTypes.indexOf(ingredient.getType()));
        holder.etQuantity.setText(String.format(Locale.getDefault(), "%d", ingredient.getValue()));

        holder.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = s.toString();
                ingredient.setName(name);
            }
        });
        holder.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String sNumber = s.toString();
                int quantity = (!sNumber.isEmpty()) ? Integer.parseInt(sNumber) : 0;
                ingredient.setValue(quantity);
            }
        });
        holder.spinnerQuantityTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = sQuantityTypes.get(position);
                ingredient.setType(type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initIngredientItem(IngredientItemViewHolder holder, Ingredient ingredient) {
        holder.tvName.setText(ingredient.getName());
        holder.tvQuantity.setText(String.format(Locale.getDefault(),
                "%d %s",
                ingredient.getValue(),
                ingredient.getType()));
    }

    public void setErrorOnEditText(RecyclerView.ViewHolder viewHolder, String message) {
        ((IngredientEditItemViewHolder) viewHolder).etName.setError(message);
    }

    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }

    private static class IngredientItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvQuantity;

        IngredientItemViewHolder(View itemView) {
            super(itemView);

            tvName =
                    (TextView) itemView.findViewById(R.id.tv_iIngredient_name);
            tvQuantity =
                    (TextView) itemView.findViewById(R.id.tv_iIngredient_quantityAndType);
        }
    }

    private static class IngredientEditItemViewHolder extends RecyclerView.ViewHolder {

        EditText etName;
        Spinner spinnerQuantityTypes;
        EditText etQuantity;

        IngredientEditItemViewHolder(View itemView) {
            super(itemView);

            etName =
                    (EditText) itemView.findViewById(R.id.et_iEditIngredient_name);
            spinnerQuantityTypes =
                    (Spinner) itemView.findViewById(R.id.spinner_iEditIngredient_quantityTypes);
            etQuantity =
                    (EditText) itemView.findViewById(R.id.et_iEditIngredient_quantity);
        }
    }
}
