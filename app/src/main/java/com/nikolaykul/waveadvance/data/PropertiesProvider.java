package com.nikolaykul.waveadvance.data;

import android.content.Context;

import com.nikolaykul.waveadvance.R;
import com.nikolaykul.waveadvance.di.scope.AppContext;
import com.nikolaykul.waveadvance.item.PropertyItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PropertiesProvider {
    private PropertyItem x0;
    private PropertyItem y0;
    private PropertyItem omega;
    private PropertyItem mu;
    private PropertyItem lambda;
    private PropertyItem gamma;
    private PropertyItem kappa;
    private PropertyItem kappa1;
    private PropertyItem k1;

    @Inject public PropertiesProvider(@AppContext Context context) {
        initDefaultProperties(context);
    }

    public List<PropertyItem> getAllProperties() {
        ArrayList<PropertyItem> res = new ArrayList<>();
        res.add(x0);
        res.add(y0);
        res.add(omega);
        res.add(mu);
        res.add(lambda);
        res.add(gamma);
        res.add(kappa);
        res.add(kappa1);
        res.add(k1);
        return res;
    }

    public double gamma() {
        return gamma.getValue();
    }

    public double k1() {
        return k1.getValue();
    }

    public double kappa1() {
        return kappa1.getValue();
    }

    public double kappa() {
        return kappa.getValue();
    }

    public double lambda() {
        return lambda.getValue();
    }

    public double mu() {
        return mu.getValue();
    }

    public double omega() {
        return omega.getValue();
    }

    public double x0() {
        return x0.getValue();
    }

    public double y0() {
        return y0.getValue();
    }

    private void initDefaultProperties(Context context) {
        x0 = new PropertyItem(context.getString(R.string.x0), 20.11);
        y0 = new PropertyItem(context.getString(R.string.y0), 20.43);
        omega = new PropertyItem(context.getString(R.string.omega), 12.2);
        mu = new PropertyItem(context.getString(R.string.mu), 6.2);
        lambda = new PropertyItem(context.getString(R.string.lambda), 13.21);
        gamma = new PropertyItem(context.getString(R.string.gamma), (3 * lambda.getValue() + 2 * mu.getValue()) * 0.42);
        kappa = new PropertyItem(context.getString(R.string.kappa), 23.2);
        k1 = new PropertyItem(context.getString(R.string.k1), omega.getValue() / kappa.getValue());
        final double p = 0.21;
        final double k = p * (omega.getValue() * omega.getValue());
        kappa1 = new PropertyItem(context.getString(R.string.kappa1), k / (lambda.getValue() + 2 * mu.getValue()));
    }

}
