package com.lwm.mvp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lwm.mvp.bean.User;
import com.lwm.mvp.presenter.UserPresenter;
import com.lwm.mvp.view.IUserView;

/*
    Model: 数据层，负责与网络层和数据库层的逻辑交互。
    View: UI层，显示数据, 并向Presenter报告用户行为。
    Presenter: 从Model拿数据，应用到UI层，管理UI的状态，响应用户的行为。
 */

// Activity 在项目中是一个全局的控制者，负责创建 view 以及 presenter 实例，并将二者联系起来。
public class MainActivity extends AppCompatActivity implements IUserView, View.OnClickListener {

    private EditText mUserEditText, mPassEditText, mReadEditText;
    private Button btnBaocun, btnDuqu, btnClear;
    private UserPresenter mUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserEditText = (EditText) findViewById(R.id.muser_editText);
        mPassEditText = (EditText) findViewById(R.id.mpass_editText);
        mReadEditText = (EditText) findViewById(R.id.chaxun_editText);
        btnBaocun = (Button) findViewById(R.id.btn_baocun);
        btnDuqu = (Button) findViewById(R.id.btn_duqu);
        btnClear = (Button) findViewById(R.id.btn_clear);
        mUserPresenter = new UserPresenter(this);
        btnBaocun.setOnClickListener(this);
        btnDuqu.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    @Override
    public void setUserName(String userName) {
        mUserEditText.setText(userName);
    }

    @Override
    public void setPassword(String password) {
        mPassEditText.setText(password);
    }

    @Override
    public void error(String errormsg) {
        Toast.makeText(this, errormsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_baocun:
                save();
                break;
            case R.id.btn_duqu:
                read();
                break;
            case R.id.btn_clear:
                clear();
                break;
        }
    }

    //保存
    public void save() {
        User user = new User();
        user.setUsername(mUserEditText.getText().toString());
        user.setPassword(mPassEditText.getText().toString());
        mUserPresenter.saveUser(user);
    }

    //清空 edittext
    public void clear() {
        mUserEditText.setText("");
        mPassEditText.setText("");
    }

    //读数据
    public void read() {
        mUserPresenter.readUser(mReadEditText.getText().toString());
    }
}
