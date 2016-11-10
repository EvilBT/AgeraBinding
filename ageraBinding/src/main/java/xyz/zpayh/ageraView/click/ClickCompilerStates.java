package xyz.zpayh.ageraView.click;

import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.agera.Observable;

import java.util.concurrent.Executor;

/**
 * Created by Administrator on 2016/10/24.
 */

public interface ClickCompilerStates {

    interface RClickView{

        @NonNull
        RFrequency click(@NonNull View view);
    }

    interface RFrequency{

        @NonNull
        RFlow onUpdatesPer(int millis);

        @NonNull
        RFlow onUpdatesPerLoop();
    }

    interface RFlow extends RConfig{

        @NonNull
        RConfig goTo(@NonNull Executor executor);
    }

    interface RConfig{

        @NonNull
        Observable compile();
    }
}
