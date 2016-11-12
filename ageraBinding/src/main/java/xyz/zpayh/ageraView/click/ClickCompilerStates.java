package xyz.zpayh.ageraView.click;

import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

import java.util.concurrent.Executor;

/**
 * Created by Administrator on 2016/10/24.
 */

public interface ClickCompilerStates {

    interface RClickView{

        @NonNull
        RFrequency click(@NonNull View view);
    }

    interface RFrequency extends RFlow{

        @NonNull
        RFlow onUpdatesPer(int millis);
    }

    interface RFlow extends RConfig{

        @NonNull
        RConfig goTo(@NonNull Executor executor);
    }

    interface RConfig{

        @NonNull
        Observable compile();

        void addUpdatable(@NonNull Updatable updatable);
    }
}
