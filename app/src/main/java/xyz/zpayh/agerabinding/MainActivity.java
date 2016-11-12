package xyz.zpayh.agerabinding;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.agera.Observable;
import com.google.android.agera.Repository;
import com.google.android.agera.Updatable;

import xyz.zpayh.ageraView.AgeraViews;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;

    private TextView mTextView;

    private FloatingActionButton fab;

    Observable mClickObservable;

    Updatable mClickUpdatable = new Updatable() {
        @Override
        public void update() {
            Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

    Repository<Editable> mTextChangedRepository;

    Updatable mTextChangedUpdatable = new Updatable() {
        @Override
        public void update() {
            mTextView.setText(mTextChangedRepository.get());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        mClickObservable = AgeraViews.click(fab)
                //.onUpdatesPer(2000)//可指定点击间隔，防止点击过快引起的多次响应
                //.goTo(Executors.newSingleThreadExecutor())//可指定Updatable在其他线程执行
                .compile();//生成Observable

        mEditText = (EditText) findViewById(R.id.et_input);
        mTextView = (TextView) findViewById(R.id.text);

        mTextChangedRepository = AgeraViews.compilerTextWatcher()
                .afterChanged(mEditText)
                //.onUpdatesPer(2000)//可设置刷新频率
                .compile();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //一般注册完要在对应的生命周期解除注册
        mTextChangedRepository.addUpdatable(mTextChangedUpdatable);
        //同一个Updatable只能注册一次，多次注册会报错
        //mTextChangedRepository.addUpdatable(mTextChangedUpdatable);
        mClickObservable.addUpdatable(mClickUpdatable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //一般注册完要在对应的生命周期解除注册
        mTextChangedRepository.removeUpdatable(mTextChangedUpdatable);
        //解除一个没有订阅事件的Updatable会报错
        //mTextChangedRepository.removeUpdatable(mTextChangedUpdatable);
        mClickObservable.removeUpdatable(mClickUpdatable);
    }
}
