package com.nikolaykul.waveadvance.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikolaykul.waveadvance.R;
import com.nikolaykul.waveadvance.data.properties.Property;
import com.nikolaykul.waveadvance.databinding.ItemPropertyBinding;

import java.util.List;

public class PropertiesRVAdapter extends
        RecyclerView.Adapter<PropertiesRVAdapter.PropertiesViewHolder> {
    private List<Property> mItems;

    public PropertiesRVAdapter(List<Property> items) {
        mItems = items;
    }

    @Override
    public PropertiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ItemPropertyBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_property, parent, false);
        return new PropertiesViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(PropertiesViewHolder holder, int position) {
        holder.setItem(mItems.get(position));
    }

    @Override public int getItemCount() {
        return mItems.size();
    }

    protected class PropertiesViewHolder extends RecyclerView.ViewHolder {
        private ItemPropertyBinding mBinding;

        public PropertiesViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        private void setItem(Property item) {
            mBinding.setItem(item);
            mBinding.etValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override public void afterTextChanged(Editable s) {
                    final String str = s.toString().replaceAll("\\D+", "");
                    if (null == str || str.isEmpty()) return;
                    final double value = Double.parseDouble(str);
                    item.setValue(value);
                }
            });
        }

    }
}
