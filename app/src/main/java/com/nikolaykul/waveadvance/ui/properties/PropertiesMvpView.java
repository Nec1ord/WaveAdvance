package com.nikolaykul.waveadvance.ui.properties;

import com.nikolaykul.waveadvance.item.PropertyItem;
import com.nikolaykul.waveadvance.ui.base.MvpView;

import java.util.List;

public interface PropertiesMvpView extends MvpView {
    void showProperties(List<PropertyItem> properties);
}
