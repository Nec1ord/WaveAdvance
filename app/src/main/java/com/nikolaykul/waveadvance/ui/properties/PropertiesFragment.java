package com.nikolaykul.waveadvance.ui.properties;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikolaykul.waveadvance.R;
import com.nikolaykul.waveadvance.adapter.PropertiesRVAdapter;
import com.nikolaykul.waveadvance.databinding.FragmentPropertiesBinding;
import com.nikolaykul.waveadvance.di.component.ActivityComponent;
import com.nikolaykul.waveadvance.data.properties.Property;
import com.nikolaykul.waveadvance.ui.base.BaseFragment;

import java.util.List;

import javax.inject.Inject;

public class PropertiesFragment extends BaseFragment implements PropertiesMvpView {
    @Inject PropertiesPresenter mPresenter;
    private FragmentPropertiesBinding mBinding;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_properties, null, false);
        return mBinding.getRoot();
    }

    @Override protected void injectSelf(ActivityComponent component) {
        component.inject(this);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.initWithView(this);
    }

    @Override public void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    @Override public void showProperties(List<Property> properties) {
        initRecyclerView(mBinding.recycleView, properties);
    }

    private void initRecyclerView(RecyclerView recyclerView, List<Property> items) {
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new PropertiesRVAdapter(items));
    }

}
