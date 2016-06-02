package com.nikolaykul.waveadvance.data;

import com.nikolaykul.waveadvance.item.PropertyItem;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MathManager {
    private static final Complex I_SQRT = Complex.I.sqrt();
    private double x0;
    private double y0;
    private double omega;
    private double mu;
    private double lambda;
    private double gamma;
    private double kappa;
    private double kappa1;
    private double k1;
    private Complex preSolvedPart;

    @Inject public MathManager() {
        initDefaultProperties();
        preSolvedPart = computePreSolvedPart();
    }

    public List<PropertyItem> getProperties() {
        ArrayList<PropertyItem> res = new ArrayList<>();
        res.add(new PropertyItem("x0", x0));
        res.add(new PropertyItem("y0", y0));
        res.add(new PropertyItem("omega", omega));
        res.add(new PropertyItem("mu", mu));
        res.add(new PropertyItem("lambda", lambda));
        res.add(new PropertyItem("gamma", gamma));
        res.add(new PropertyItem("kappa", kappa));
        res.add(new PropertyItem("kappa1", kappa1));
        res.add(new PropertyItem("k1", k1));
        return res;
    }

    public double u(double x, double y) {
        return coordinateFunction(x - x0, getLengthFromTheSource(x, y));
    }

    public double v(double x, double y) {
        return coordinateFunction(y - y0, getLengthFromTheSource(x, y));
    }

    private double coordinateFunction(double shift, double r) {
        final double scale = shift / r;
        final Complex arg1 = computeHankel(r * kappa1).multiply(kappa1);
        final Complex temp = I_SQRT.multiply(kappa1);
        final Complex arg2 = computeHankel(temp.multiply(r)).multiply(temp);
        final Complex result = arg1.subtract(arg2)
                .multiply(scale)
                .multiply(preSolvedPart);
        return result.abs();
    }

    private Complex computeHankel(double z) {
        return computeHankel(new Complex(z, 0.0));
    }

    private Complex computeHankel(Complex z) {
        final Complex arg1 = z
                .multiply(FastMath.PI)
                .reciprocal()
                .multiply(2.0)
                .sqrt();
        final Complex arg2 = z
                .subtract(3.0 / 4.0 * FastMath.PI)
                .exp();
        return arg1.multiply(arg2);
    }

    private double getLengthFromTheSource(double x, double y) {
        final double xSqr = (x - x0) * (x - x0);
        final double ySqr = (y - y0) * (y - y0);
        return FastMath.sqrt(xSqr + ySqr);
    }

    private void initDefaultProperties() {
        x0 = 20.11;
        y0 = 20.43;
        omega = 12.2;
        mu = 6.2;
        lambda = 13.21;
        gamma = (3 * lambda + 2 * mu) * 0.42;
        kappa = 23.2;
        k1 = omega / kappa;
        final double p = 0.21;
        final double k = p * (omega * omega);
        kappa1 = k / (lambda + 2 * mu);
    }

    private Complex computePreSolvedPart() {
        final Complex numerator = new Complex(0.0, gamma);
        final double temp = 4.0 * kappa * (lambda + 2.0 * mu);
        final Complex denominator = new Complex(kappa1 * kappa1, -(k1 * k1)).multiply(temp);
        return numerator.divide(denominator);
    }

}
