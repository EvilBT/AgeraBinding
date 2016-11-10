package xyz.zpayh.ageraView.textwatcher;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 文 件 名: TextWatcherConfig
 * 创 建 人: 陈志鹏
 * 创建日期: 2016/10/25 01:39
 * 邮   箱: ch_zh_p@qq.com
 * 修改时间:
 * 修改备注:
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
        TextWatcherConfig.BEFORE_TEXT_CHANGED,
        TextWatcherConfig.ON_TEXT_CHANGED,
        TextWatcherConfig.AFTER_TEXT_CHANGED
})
public @interface TextWatcherConfig {

    int BEFORE_TEXT_CHANGED = 0;

    int ON_TEXT_CHANGED = 1;

    int AFTER_TEXT_CHANGED = 2;
}
