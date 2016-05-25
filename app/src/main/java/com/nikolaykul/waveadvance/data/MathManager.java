package com.nikolaykul.waveadvance.data;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

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

    public MathManager() {
        initDefaultProperties();
        preSolvedPart = computePreSolvedPart();
    }

    public double u(double x, double y) {
        return coordinateFunction(x - x0, getLength(x, y));
    }

    public double v(double x, double y) {
        return coordinateFunction(y - y0, getLength(x, y));
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
                .subtract((3.0 * FastMath.PI) / 4.0)
                .exp();
        return arg1.multiply(arg2);
    }

    private double getLength(double x, double y) {
        final double xSqr = (x - x0) * (x - x0);
        final double ySqr = (y - y0) * (y - y0);
        return FastMath.sqrt(xSqr + ySqr);
    }

    private void initDefaultProperties() {
        final double p = 1.21;
        final double k = p * (omega * omega);
        x0 = 20.11;
        y0 = 20.43;
        omega = 12.2;
        mu = 86.2;
        lambda = 123.21;
        gamma = (3 * lambda + 2 * mu) * 0.42;
        kappa = 23.2;
        kappa1 = k / (lambda + 2 * mu);
        k1 = omega / kappa;
    }

    private Complex computePreSolvedPart() {
        final Complex numerator = new Complex(0.0, gamma);
        final double temp = 4.0 * kappa * (lambda + 2.0 * mu);
        final Complex denominator = new Complex(kappa1 * kappa1, -(k1 * k1)).multiply(temp);
        return numerator.divide(denominator);
    }

}
