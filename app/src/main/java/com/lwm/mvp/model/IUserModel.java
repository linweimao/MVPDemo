package com.lwm.mvp.model;

import com.lwm.mvp.bean.User;

public interface IUserModel {
    void saveUserData(User user);

    User readUserData(String name);
}
