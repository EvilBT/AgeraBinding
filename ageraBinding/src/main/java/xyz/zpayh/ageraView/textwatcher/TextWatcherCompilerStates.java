package xyz.zpayh.ageraView.textwatcher;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.widget.TextView;

import com.google.android.agera.Repository;

/**
 * 文 件 名: TextWatcherCompilerStates
 * 创 建 人: 陈志鹏
 * 创建日期: 2016/10/25 01:06
 * 邮   箱: ch_zh_p@qq.com
 * 修改时间:
 * 修改备注:
 */

public interface TextWatcherCompilerStates {

    interface RTextWatcher{

        @NonNull
        RFrequency<CharSequence> onChanged(@NonNull TextView textView);

        @NonNull
        RFrequency<CharSequence> beforeChanged(@NonNull TextView textView);

        @NonNull
        RFrequency<Editable> afterChanged(@NonNull TextView textView);
    }

    interface RFrequency<TVal>{

        @NonNull
        RConfig<TVal> onUpdatesPer(int millis);

        @NonNull
        RConfig<TVal> onUpdatesPerLoop();
    }

    interface RConfig<TVal>{

        @NonNull
        Repository<TVal> compile();
    }
}
