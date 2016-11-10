package xyz.zpayh.ageraView;

import xyz.zpayh.ageraView.click.ClickCompiler;
import xyz.zpayh.ageraView.click.ClickCompilerStates;
import xyz.zpayh.ageraView.textwatcher.TextWatcherCompiler;
import xyz.zpayh.ageraView.textwatcher.TextWatcherCompilerStates;

/**
 * 文 件 名: AgeraViews
 * 创 建 人: 陈志鹏
 * 创建日期: 2016/10/25 01:15
 * 邮   箱: ch_zh_p@qq.com
 * 修改时间:
 * 修改备注:
 */

public final class AgeraViews {

    public static ClickCompilerStates.RClickView compilerClick(){
        return ClickCompiler.compiler();
    }

    public static TextWatcherCompilerStates.RTextWatcher compilerTextWatcher(){
        return TextWatcherCompiler.compiler();
    }
}
