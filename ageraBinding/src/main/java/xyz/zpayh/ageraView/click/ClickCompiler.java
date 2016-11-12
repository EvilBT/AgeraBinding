package xyz.zpayh.ageraView.click;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

import java.util.concurrent.Executor;

import static com.google.android.agera.Preconditions.checkNotNull;
import static com.google.android.agera.Preconditions.checkState;

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
        checkState(Looper.myLooper() != null, "Can only be compiler on a Looper thread");
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

    private ClickCompiler(){
        this.view = null;
        this.frequency = 0;
        this.executor = null;
    }

    private int frequency;

    private View view;

    private Executor executor;

    @NonNull
    @Override
    public ClickCompiler click(@NonNull View view) {
        this.view = checkNotNull(view);
        return this;
    }

    @NonNull
    @Override
    public ClickCompiler goTo(@NonNull Executor executor) {
        this.executor = checkNotNull(executor);
        return this;
    }

    @NonNull
    @Override
    public ClickCompiler onUpdatesPer(int millis) {
        frequency = Math.max(0,millis);
        return this;
    }

    @NonNull
    @Override
    public Observable compile() {
        Observable observable = compileObservableAndReset();
        recycle(this);
        return observable;
    }

    @Override
    public void addUpdatable(@NonNull Updatable updatable) {
        Observable observable = compile();
        observable.addUpdatable(updatable);
    }

    private Observable compileObservableAndReset() {
        Observable observable = new ClickObservable(view,executor,frequency);
        view = null;
        frequency = 0;
        return observable;
    }
}
