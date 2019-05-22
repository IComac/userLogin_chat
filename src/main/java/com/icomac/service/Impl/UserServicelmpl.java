package com.icomac.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icomac.dao.UserDao;
import com.icomac.entity.User;
import com.icomac.service.UserService;

@Service
public class UserServicelmpl implements UserService{
	@Autowired
	private UserDao userDao;
	
	/* 
	 * 检验用户登录业务
	 * 
	 */
	public User checkLogin(String username, String password) {
		
		User user = userDao.findByUsername(username);
		if(user != null && user.getPassword().equals(password)){
		
			return user;
		}
		return null;
	}
	
	/*
	 * 检验注册用户业务
	 */
	public String checkRegist(String username, String password) {
		
		User user = userDao.findByUsername(username);
		if(user != null) {
			return "fail";
		}
		else{
			userDao.registerByUsernameAndPassword(username, password);
			return "success";
			}
	}
}
