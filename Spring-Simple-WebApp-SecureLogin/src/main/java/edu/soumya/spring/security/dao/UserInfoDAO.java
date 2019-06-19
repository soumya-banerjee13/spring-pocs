package edu.soumya.spring.security.dao;

import java.util.List;

import edu.soumya.spring.security.model.UserInfo;
public interface UserInfoDAO {
     
    public UserInfo findUserInfo(String userName);
     
    // [USER,ADMIN,..]
    public List<String> getUserRoles(String userName);
     
}
