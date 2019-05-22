package com.icomac.service;

import com.icomac.entity.User;

public interface UserService {
	//检验用户登录
	User checkLogin(String username,String password);
	
	//检验注册用户
	String checkRegist(String username,String password);
}
