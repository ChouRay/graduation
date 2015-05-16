package com.izlei.shlibrary.data.entity.mapper;

import com.izlei.shlibrary.data.entity.UserEntity;
import com.izlei.shlibrary.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouzili on 2015/5/7.
 */
public class UserEntityDataMapper {

    public UserEntity transform(User user) {
        UserEntity userEntity = null;
        if (user != null) {
            userEntity = new UserEntity();
            userEntity.setUsername(user.getUsername());
            userEntity.setPassword(user.getPassword());
            userEntity.setEmail(user.getEmail());
        }
        return userEntity;
    }

    public User transform(UserEntity userEntity) {
        User user = null;
        if (userEntity != null) {
            try {
                user = new User();
                user.setUsername(userEntity.getUsername());
                user.setEmail(userEntity.getEmail());
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public List<User> transform(List<UserEntity> userEntities) {
        List<User> users = new ArrayList<>();
        User user;
        if (userEntities != null) {
            for (UserEntity userEntity : userEntities) {
                user = this.transform(userEntity);
                if (user != null) {
                    users.add(user);
                }
            }
        }
        return users;
    }

}
