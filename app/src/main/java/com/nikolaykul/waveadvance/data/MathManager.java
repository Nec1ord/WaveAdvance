package com.nikolaykul.waveadvance.data;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MathManager {
    private static final Complex I_SQRT = Complex.I.sqrt();
    private PropertiesProvider mProvider;
    private Complex preSolvedPart;

    @Inject public MathManager(PropertiesProvider provider) {
        mProvider = provider;
        preSolvedPart = computePreSolvedPart();
    }

    public double u(double x, double y) {
        return coordinateFunction(x - mProvider.x0(), getLengthFromTheSource(x, y));
    }

    public double v(double x, double y) {
        return coordinateFunction(y - mProvider.y0(), getLengthFromTheSource(x, y));
    }

    private double coordinateFunction(double shift, double r) {
        final double scale = shift / r;
        final Complex arg1 = computeHankel(r * mProvider.kappa1()).multiply(mProvider.kappa1());
        final Complex temp = I_SQRT.multiply(mProvider.kappa1());
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
        final double xSqr = (x - mProvider.x0()) * (x - mProvider.x0());
        final double ySqr = (y - mProvider.y0()) * (y - mProvider.y0());
        return FastMath.sqrt(xSqr + ySqr);
    }

    private Complex computePreSolvedPart() {
        final Complex numerator = new Complex(0.0, mProvider.gamma());
        final double temp = 4.0 * mProvider.kappa() * (mProvider.lambda() + 2.0 * mProvider.mu());
        final Complex denominator = new Complex(
                mProvider.kappa1() * mProvider.kappa1(), -(mProvider.k1() * mProvider.k1()))
                .multiply(temp);
        return numerator.divide(denominator);
    }

}
