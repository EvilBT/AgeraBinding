package xyz.zpayh.ageraView.textwatcher;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.google.android.agera.BaseObservable;
import com.google.android.agera.Observable;
import com.google.android.agera.Observables;
import com.google.android.agera.Repository;
import com.google.android.agera.Updatable;

import static com.google.android.agera.Preconditions.checkNotNull;

/**
 * 文 件 名: TextWatcherRepository
 * 创 建 人: 陈志鹏
 * 创建日期: 2016/10/25 01:28
 * 邮   箱: ch_zh_p@qq.com
 * 修改时间:
 * 修改备注:
 */

public final class TextWatcherRepository extends BaseObservable
    implements Repository, Updatable{

    @NonNull
    static Repository compiledRepository(
            @NonNull final TextView textView,
            final int frequency,
            @TextWatcherConfig final int textWatcherConfig){
        return new TextWatcherRepository(textView,frequency,textWatcherConfig);
    }

    private CharSequence charSequence;

    private Editable editable;

    private final Observable eventSource;

    @TextWatcherConfig
    private final int config;

    private TextWatcherRepository(@NonNull final TextView view,
                                 final int frequency,
                                 @TextWatcherConfig final int textWatcherConfig) {

        this.eventSource = Observables.perMillisecondObservable(frequency,
                new TextWatcherObservable(view,textWatcherConfig));

        this.config = textWatcherConfig;
    }

    @Override
    protected void observableActivated() {
        this.eventSource.addUpdatable(this);

    }

    @Override
    protected void observableDeactivated() {
        this.eventSource.removeUpdatable(this);
    }

    @NonNull
    @Override
    public Object get() {
        if (config == TextWatcherConfig.ON_TEXT_CHANGED ||
                config == TextWatcherConfig.BEFORE_TEXT_CHANGED){
            return charSequence;
        }
        if (config == TextWatcherConfig.AFTER_TEXT_CHANGED){
            return editable;
        }
        throw new IllegalStateException("异常错误");
    }

    @Override
    public void update() {
        dispatchUpdate();
    }

    private final class TextWatcherObservable extends BaseObservable{
        private final TextView textView;
        @TextWatcherConfig
        private final int config;

        private final TextWatcher textWatcher;

        TextWatcherObservable(@NonNull TextView textView, int config) {
            this.textView = checkNotNull(textView);
            this.config = config;

            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (TextWatcherObservable.this.config == TextWatcherConfig.BEFORE_TEXT_CHANGED){
                        TextWatcherRepository.this.charSequence = charSequence;
                        dispatchUpdate();
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (TextWatcherObservable.this.config == TextWatcherConfig.ON_TEXT_CHANGED){
                        TextWatcherRepository.this.charSequence = charSequence;
                        dispatchUpdate();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (TextWatcherObservable.this.config == TextWatcherConfig.AFTER_TEXT_CHANGED){
                        TextWatcherRepository.this.editable = editable;
                        dispatchUpdate();
                    }
                }
            };
        }

        @Override
        protected void observableActivated() {
            textView.addTextChangedListener(textWatcher);
        }

        @Override
        protected void observableDeactivated() {
            textView.removeTextChangedListener(textWatcher);
        }
    }
}
