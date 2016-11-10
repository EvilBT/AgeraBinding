package xyz.zpayh.ageraView.textwatcher;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.android.agera.Preconditions;
import com.google.android.agera.Repository;

import static xyz.zpayh.ageraView.textwatcher.TextWatcherConfig.AFTER_TEXT_CHANGED;
import static xyz.zpayh.ageraView.textwatcher.TextWatcherConfig.BEFORE_TEXT_CHANGED;
import static xyz.zpayh.ageraView.textwatcher.TextWatcherConfig.ON_TEXT_CHANGED;

/**
 * 文 件 名: TextWatcherCompiler
 * 创 建 人: 陈志鹏
 * 创建日期: 2016/10/25 01:15
 * 邮   箱: ch_zh_p@qq.com
 * 修改时间:
 * 修改备注:
 */
@SuppressWarnings({"unchecked, rawtypes"})
public final class TextWatcherCompiler implements
    TextWatcherCompilerStates.RTextWatcher,
    TextWatcherCompilerStates.RConfig,
    TextWatcherCompilerStates.RFrequency{

    private static final ThreadLocal<TextWatcherCompiler> compilers = new ThreadLocal<>();

    @NonNull
    public static TextWatcherCompilerStates.RTextWatcher compiler(){
        Preconditions.checkNotNull(Looper.myLooper());
        TextWatcherCompiler compiler = compilers.get();
        if (compiler == null){
            compiler = new TextWatcherCompiler();
        } else {
            compilers.set(null);
        }
        return compiler;
    }

    private static void recycle(@NonNull final TextWatcherCompiler compiler){
        compilers.set(compiler);
    }

    private int frequency;

    private TextView textView;

    @TextWatcherConfig
    private int whenWatcher = AFTER_TEXT_CHANGED;

    @NonNull
    @Override
    public Repository compile() {
        Repository repository = compileObservableAndReset();
        recycle(this);
        return repository;
    }

    private Repository compileObservableAndReset() {
        Repository repository = TextWatcherRepository.compiledRepository(textView,frequency,whenWatcher);
        textView = null;
        frequency = 0;
        whenWatcher = AFTER_TEXT_CHANGED;
        return repository;
    }

    @NonNull
    @Override
    public TextWatcherCompiler onUpdatesPer(int millis) {
        frequency = Math.max(0,millis);
        return this;
    }

    @NonNull
    @Override
    public TextWatcherCompiler onUpdatesPerLoop() {
        return onUpdatesPer(0);
    }

    @NonNull
    @Override
    public TextWatcherCompiler onChanged(@NonNull TextView textView) {
        this.textView = Preconditions.checkNotNull(textView);
        this.whenWatcher = ON_TEXT_CHANGED;
        return this;
    }

    @NonNull
    @Override
    public TextWatcherCompiler beforeChanged(@NonNull TextView textView) {
        this.textView = Preconditions.checkNotNull(textView);
        this.whenWatcher = BEFORE_TEXT_CHANGED;
        return this;
    }

    @NonNull
    @Override
    public TextWatcherCompiler afterChanged(@NonNull TextView textView) {
        this.whenWatcher = AFTER_TEXT_CHANGED;
        this.textView = Preconditions.checkNotNull(textView);
        return this;
    }
}
