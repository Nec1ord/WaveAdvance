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

@Singleton
public class MathManager {
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
                private Complex u;
                private Complex v;

                @Override public void run() {
                    update();
                    final Complex exp = new Complex(0, -mProvider.omega() * t).exp();
                    final double xNew = u.multiply(exp).getReal();
                    final double yNew = v.multiply(exp).getReal();
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(new Pair<>(xNew, yNew));
                    } else {
                        this.cancel();
                    }
                }

                private void update() {
                    if (null == u && null == v) {
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
            final double xNew = u(x, y).getReal();
            final double yNew = v(x, y).getReal();
            if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(new Pair<>(xNew, yNew));
                subscriber.onCompleted();
            }
        });
    }

    private Complex u(double x, double y) {
        return coordinateFunction(x - mProvider.x0(), getLengthFromTheSource(x, y));
    }

    private Complex v(double x, double y) {
        return coordinateFunction(y - mProvider.y0(), getLengthFromTheSource(x, y));
    }

    private Complex coordinateFunction(double shift, double r) {
        final double kappa1Sqr = mProvider.kappa1() * mProvider.kappa1();
        final double k1Sqr = mProvider.k1() * mProvider.k1();
        final Complex iSqrtK1 = Complex.I.sqrt().multiply(mProvider.k1());
        // fts
        final Complex fstArg1 = new Complex(0.0, mProvider.gamma() * shift);
        final Complex fstArg2 = new Complex(kappa1Sqr, -k1Sqr)
                .multiply(r * 4 * mProvider.kappa() * (mProvider.lambda() + 2 * mProvider.mu()));
        final Complex fst = fstArg1
                .divide(fstArg2);
        // snd
        final Complex sndArg1 = computeHankel(r * mProvider.kappa1())
                .multiply(mProvider.kappa1());
        final Complex sndArg2 = computeHankel(iSqrtK1.multiply(r))
                .multiply(iSqrtK1);
        final Complex snd = sndArg1.subtract(sndArg2);
        // answer
        return fst.multiply(snd);
    }

    private Complex computeHankel(double z) {
        return computeHankel(new Complex(z, 0.0));
    }

    private Complex computeHankel(Complex z) {
        // fst
        final Complex fstArg1 = new Complex(2.0, 0.0);
        final Complex fstArg2 = z.multiply(FastMath.PI);
        final Complex fst = fstArg1
                .divide(fstArg2)
                .sqrt();
        // snd
        final Complex sndArg1 = z.subtract(3.0 / 4.0 * FastMath.PI);
        final Complex sndArg2 = new Complex(-sndArg1.getImaginary(), sndArg1.getReal()); // i*z
        final Complex snd = sndArg2.exp();
        // answer
        return fst.multiply(snd);
    }

    private double getLengthFromTheSource(double x, double y) {
        final double xSqr = (x - mProvider.x0()) * (x - mProvider.x0());
        final double ySqr = (y - mProvider.y0()) * (y - mProvider.y0());
        return FastMath.sqrt(xSqr + ySqr);
    }

    private void clearTimer() {
        if (null != mTimer) {
            mTimer.purge();
            mTimer.cancel();
            mTimer = null;
        }
    }

}
