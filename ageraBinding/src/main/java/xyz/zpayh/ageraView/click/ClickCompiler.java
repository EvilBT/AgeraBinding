package xyz.zpayh.ageraView.click;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.agera.Observable;
import com.google.android.agera.Preconditions;

import java.util.concurrent.Executor;

/**
 * Created by Administrator on 2016/10/24.
 */

public final class ClickCompiler implements
    ClickCompilerStates.RClickView,
    ClickCompilerStates.RFrequency,
    ClickCompilerStates.RFlow,
    ClickCompilerStates.RConfig{

    private static final ThreadLocal<ClickCompiler> compilers = new ThreadLocal<>();

    @NonNull
    public static ClickCompilerStates.RClickView compiler(){
        Preconditions.checkNotNull(Looper.myLooper());
        ClickCompiler compiler = compilers.get();
        if (compiler == null){
            compiler = new ClickCompiler();
        } else {
            compilers.set(null);
        }
        return compiler;
    }

    private static void recycle(@NonNull final ClickCompiler compiler){
        compilers.set(compiler);
    }

    private int frequency;

    private View view;

    private Executor executor;

    @NonNull
    @Override
    public ClickCompilerStates.RFrequency click(@NonNull View view) {
        this.view = Preconditions.checkNotNull(view);
        return this;
    }

    @NonNull
    @Override
    public Observable compile() {
        Observable observable = compileObservableAndReset();
        recycle(this);
        return observable;
    }

    private Observable compileObservableAndReset() {
        Observable observable = new ClickObservable(view,executor,frequency);
        view = null;
        frequency = 0;
        return observable;
    }

    @NonNull
    @Override
    public ClickCompilerStates.RConfig goTo(@NonNull Executor executor) {
        this.executor = Preconditions.checkNotNull(executor);
        return this;
    }

    @NonNull
    @Override
    public ClickCompilerStates.RFlow onUpdatesPer(int millis) {
        frequency = Math.max(0,millis);
        return this;
    }

    @NonNull
    @Override
    public ClickCompilerStates.RFlow onUpdatesPerLoop() {
        return onUpdatesPer(0);
    }
}
