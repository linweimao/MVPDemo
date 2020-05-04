package com.lwm.mvp.view;

public interface IUserView {
    void setUserName(String userName);
    void setPassword(String password);
    void error(String errormsg);
}