package xyz.zpayh.ageraView.click;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.agera.Observable;
import com.google.android.agera.Preconditions;
import com.google.android.agera.Updatable;

import java.util.Arrays;
import java.util.concurrent.Executor;

/**
 * Created by Administrator on 2016/10/24.
 */

public class ClickObservable implements Observable{
    private final Updatable[] NO_UPDATABLE = new Updatable[0];

    private final View view;

    private final int shortestClickWindowMillis;

    private long lastClickTimestamp = 0;

    private Updatable[] updatables;

    private int size;

    private final Executor mUpdateExecutor;

    private final View.OnClickListener clickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if (mUpdateExecutor != null){
                mUpdateExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        /*if (Looper.myLooper() == null) {
                            Looper.prepare();
                        }*/
                        dispatchUpdate();
                    }
                });
                return;
            }
            dispatchUpdate();
        }
    };

    public ClickObservable(@NonNull View view,@Nullable Executor updateExecutor, int millis) {
        this.view = view;
        this.shortestClickWindowMillis = millis;
        this.mUpdateExecutor = updateExecutor;
        this.updatables = NO_UPDATABLE;
    }

    @Override
    public void addUpdatable(@NonNull Updatable updatable) {
        Preconditions.checkNotNull(updatable);
        int indexToAdd = -1;
        for (int index = 0; index < updatables.length; index++) {
            if (updatables[index] == updatable){
                throw new IllegalStateException("Updatable already added, cannot add.");
            }
            if (updatables[index] == null){
                indexToAdd = index;
            }
        }
        if (indexToAdd == -1){
            indexToAdd = updatables.length;
            updatables = Arrays.copyOf(updatables,
                    indexToAdd < 2 ? 2 : indexToAdd*2);
        }
        updatables[indexToAdd] = updatable;
        size++;
        if (size == 1){
            view.setOnClickListener(clickListener);
        }
    }

    @Override
    public void removeUpdatable(@NonNull Updatable updatable) {
        for (int index = 0; index < updatables.length; index++) {
            if (updatables[index] == updatable){
                updatables[index] = null;
                size--;
                if (size == 0) {
                    view.setOnClickListener(null);
                }
                return;
            }
        }
        throw new IllegalStateException("Updatable not added, cannot remove.");
    }

    private synchronized void dispatchUpdate() {
        if (shortestClickWindowMillis > 0){
            final long elapsedTime = SystemClock.elapsedRealtime();
            final long timeFromLastClick = elapsedTime - lastClickTimestamp;
            if (timeFromLastClick > shortestClickWindowMillis){
                for (Updatable updatable : updatables) {
                    if (updatable != null){
                        updatable.update();
                    }
                }
                lastClickTimestamp = elapsedTime;
            }
            return;
        }
        for (Updatable updatable : updatables) {
            if (updatable != null){
                updatable.update();
            }
        }
    }
}
