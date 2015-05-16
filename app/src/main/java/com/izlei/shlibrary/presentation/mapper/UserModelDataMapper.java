package com.izlei.shlibrary.presentation.mapper;

import com.izlei.shlibrary.domain.User;
import com.izlei.shlibrary.presentation.model.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouzili on 2015/5/7.
 */
public class UserModelDataMapper {
    public UserModel transform(User user) {
        UserModel userModel = null;
        if (user != null) {
            userModel = new UserModel();
            userModel.setUsername(user.getUsername());
            userModel.setEmail(user.getEmail());
        }
        return userModel;
    }

    public List<UserModel> transform(List<User> users) {
        UserModel userModel;
        List<UserModel>  userModels = new ArrayList<>();
        for (User user : users) {
            userModel = transform(user);
            if (userModel != null) {
                userModels.add(userModel);
            }
        }
        return userModels;
    }

    public User transform(UserModel userModel) {
        User user = null;
        if (userModel != null) {
            user = new User();
            user.setUsername(userModel.getUsername());
            user.setPassword(userModel.getPassword());
            user.setEmail(userModel.getEmail());
        }
        return user;
    }

}
