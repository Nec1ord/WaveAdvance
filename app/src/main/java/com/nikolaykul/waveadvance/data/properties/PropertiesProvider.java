package com.nikolaykul.waveadvance.data.properties;

import android.content.Context;

import com.nikolaykul.waveadvance.R;
import com.nikolaykul.waveadvance.di.scope.AppContext;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PropertiesProvider {
    private Property x0;
    private Property y0;
    private Property omega;
    private Property mu;
    private Property lambda;
    private Property gamma;
    private Property kappa;
    private Property kappa1;
    private Property k1;

    @Inject public PropertiesProvider(@AppContext Context context) {
        initDefaultProperties(context);
    }

    public List<Property> getAllProperties() {
        ArrayList<Property> res = new ArrayList<>();
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
        x0 = new Property(context.getString(R.string.x0), 20.11);
        y0 = new Property(context.getString(R.string.y0), 20.43);
        omega = new Property(context.getString(R.string.omega), 12.2);
        mu = new Property(context.getString(R.string.mu), 6.2);
        lambda = new Property(context.getString(R.string.lambda), 13.21);
        gamma = new Property(context.getString(R.string.gamma), (3 * lambda.getValue() + 2 * mu.getValue()) * 0.42);
        kappa = new Property(context.getString(R.string.kappa), 23.2);
        k1 = new Property(context.getString(R.string.k1), omega.getValue() / kappa.getValue());
        final double p = 0.21;
        final double k = p * (omega.getValue() * omega.getValue());
        kappa1 = new Property(context.getString(R.string.kappa1), k / (lambda.getValue() + 2 * mu.getValue()));
    }

}
