package com.icomac.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.icomac.entity.User;
import com.icomac.service.UserService;

@Controller
@RequestMapping("/user")

//这里用了@SessionAttributes，可以直接把model中的user(也就key)放入其中
//这样保证了session中存在user这个对象
@SessionAttributes("user")
public class UserController {
	
	@Autowired
	private UserService userServivce;
	
	//正常访问login页面
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	
	//正常访问regist页面
	@RequestMapping("/regist")
	public String regist(){
		return "regist";
	}
	
	//表单提交过来的路径
	@RequestMapping("/checkLogin")
	public String checkLogin(HttpServletRequest request, HttpServletResponse response, User user,Model model){
		//调用service方法
		user = userServivce.checkLogin(user.getUsername(), user.getPassword());
		//若有user则添加到model里并且跳转到成功页面
		if(user != null){
			model.addAttribute("user",user);
			HttpSession session = request.getSession();
			System.out.println(user.getUsername()+"登录");
			session.setAttribute("SESSION_USERNAME", user.getUsername());
			return "websocket";
		}
		return "fail";
	}

	//注册表单提交的路径
	@RequestMapping("/checkRegist")
	public String checkRegist(User user) {
		String result = userServivce.checkRegist(user.getUsername(), user.getPassword());
		if(result == "fail") {
			return "registfail";
		}
		return "registsuccess";
	}
	
	//测试超链接跳转到另一个页面是否可以取到session值
	@RequestMapping("/anotherpage")
	public String hrefpage(){
		
		return "anotherpage";
	}
	
	//注销方法
	@RequestMapping("/outLogin")
	public String outLogin(HttpSession session){
		//通过session.invalidata()方法来注销当前的session
		session.invalidate();
		return "login";
	}
	
	//测试
	@RequestMapping("/test")
	public String test(){
		return "test";
	}
}
