package com.deepakTraders.generalstore.services;

import com.deepakTraders.generalstore.exceptions.UserException;
import com.deepakTraders.generalstore.models.User;

public interface UserService {

    public User findUserById(Long userId) throws UserException;

    public User findUserProfileByJwt(String jwt) throws UserException;

}
