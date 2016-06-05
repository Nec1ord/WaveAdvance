package com.nikolaykul.waveadvance.data;

import android.util.Pair;

import com.nikolaykul.waveadvance.data.properties.PropertiesProvider;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import timber.log.Timber;

@Singleton
public class MathManager {
    private static final Complex I_SQRT = Complex.I.sqrt();
    private PropertiesProvider mProvider;
    private Timer mTimer;

    @Inject public MathManager(PropertiesProvider provider) {
        mProvider = provider;
    }

    public void updateSourcePosition(double x, double y) {
        mProvider.updateSourcePosition(x, y);
    }

    public Observable<Pair<Double, Double>> updateCoordsByTime(double x, double y,
                                                               long period, double tDelta) {
        return Observable.create((Observable.OnSubscribe<Pair<Double, Double>>) subscriber -> {
            clearTimer();
            if (subscriber.isUnsubscribed()) {
                return;
            }

            final TimerTask task = new TimerTask() {
                private double t = 0.0;
                private double u = 0.0;
                private double v = 0.0;

                @Override public void run() {
                    update();
                    final double mult = FastMath.exp(mProvider.omega() * t);
                    final double xNew = u * mult;
                    final double yNew = v * mult;
                    Timber.d("TimerInfo:\nt=%f,\nmult=%f,\nx=%f,\ny=%f", t, mult, xNew, yNew);
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(new Pair<>(xNew, yNew));
                    }
                }

                private void update() {
                    if (0 == u && 0 == v) {
                        u = u(x, y);
                        v = v(x, y);
                    } else {
                        t += tDelta;
                    }
                }
            };
            mTimer = new Timer("Update coordinates timer");
            mTimer.schedule(task, 0L, period);
        });
    }

    public Observable<Pair<Double, Double>> updateCoords(double x, double y) {
        return Observable.create((Observable.OnSubscribe<Pair<Double, Double>>) subscriber -> {
            final double xNew = u(x, y);
            final double yNew = v(x, y);
            if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(new Pair<>(xNew, yNew));
                subscriber.onCompleted();
            }
        });
    }

    private double u(double x, double y) {
        return coordinateFunction(x - mProvider.x0(), getLengthFromTheSource(x, y));
    }

    private double v(double x, double y) {
        return coordinateFunction(y - mProvider.y0(), getLengthFromTheSource(x, y));
    }

    private double coordinateFunction(double shift, double r) {
        final double scale = shift / r;
        final Complex arg1 = computeHankel(r * mProvider.kappa1()).multiply(mProvider.kappa1());
        final Complex temp = I_SQRT.multiply(mProvider.kappa1());
        final Complex arg2 = computeHankel(temp.multiply(r)).multiply(temp);
        final Complex result = arg1.subtract(arg2)
                .multiply(scale)
                .multiply(computePreSolvedPart());
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

    private void clearTimer() {
        if (null != mTimer) {
            mTimer.purge();
            mTimer.cancel();
            mTimer = null;
        }
    }

}
