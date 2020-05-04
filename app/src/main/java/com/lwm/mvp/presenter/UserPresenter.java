package com.lwm.mvp.presenter;

import android.util.Log;

import com.lwm.mvp.bean.User;
import com.lwm.mvp.model.IUserModel;
import com.lwm.mvp.model.UserModel;
import com.lwm.mvp.view.IUserView;


public class UserPresenter {

    private static final String TAG = UserPresenter.class.getSimpleName();

    private IUserView mIUserView;
    private IUserModel mIUserModel;

    public UserPresenter(IUserView mIUserView) {

        this.mIUserView = mIUserView;
        mIUserModel = new UserModel();
    }

    /**
     * 数据保存
     *
     * @param user
     */
    public void saveUser(User user) {
        mIUserModel.saveUserData(user);
    }

    /**
     * 读取数据
     *
     * @param name
     */
    public void readUser(String name) {
        User user = mIUserModel.readUserData(name);
        Log.d(TAG, "getread: " + user);
        if (user != null) {
            mIUserView.setUserName(user.getUsername());
            mIUserView.setPassword(user.getPassword());
        } else {
            mIUserView.error("没有找到");
        }
    }
}