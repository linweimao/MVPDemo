package com.lwm.mvp.model;

import android.util.Log;

import com.lwm.mvp.bean.User;
import com.lwm.mvp.presenter.UserPresenter;

import org.litepal.LitePal;

import java.util.List;

public class UserModel implements IUserModel {
    private static final String TAG = UserModel.class.getSimpleName();

    public UserModel() {
    }

    /**
     * 使用Litepal保存数据
     *
     * @return
     */
    @Override
    public void saveUserData(User user) {
        user.save();
        Log.d(TAG, "user: " + user);
    }

    /**
     * 通过 name从数据库读取数据
     */
    @Override
    public User readUserData(String name) {
        List<User> userList = LitePal.where("username = ?", name).find(User.class);
        if (userList.size() > 0)
            return userList.get(0);
        return null;
    }
}