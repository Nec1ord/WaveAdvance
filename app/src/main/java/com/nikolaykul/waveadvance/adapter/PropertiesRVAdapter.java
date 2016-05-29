package com.nikolaykul.waveadvance.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;

import com.nikolaykul.waveadvance.databinding.DrawerItemBinding;
import com.nikolaykul.waveadvance.item.PropertyItem;

import java.util.List;

public class PropertiesRVAdapter extends
        RecyclerView.Adapter<PropertiesRVAdapter.PropertiesViewHolder> {
    private List<PropertyItem> mItems;

    public PropertiesRVAdapter(List<PropertyItem> items) {
        mItems = items;
    }

    @Override
    public PropertiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PropertiesViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(PropertiesViewHolder holder, int position) {
        final PropertyItem item = mItems.get(position);
        holder.mBinding.setItem(item);
        holder.mBinding.etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override public void afterTextChanged(Editable s) {
                final double value = Double.parseDouble(s.toString());
                item.setValue(value);
            }
        });
    }

    @Override public int getItemCount() {
        return mItems.size();
    }

    protected class PropertiesViewHolder extends RecyclerView.ViewHolder {
        private DrawerItemBinding mBinding;

        public PropertiesViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

    }
}
