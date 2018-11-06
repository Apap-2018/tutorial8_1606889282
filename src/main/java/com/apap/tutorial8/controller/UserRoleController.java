package com.apap.tutorial8.controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.apap.tutorial8.model.PasswordModel;
import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;

	@RequestMapping( value = "/addUser", method = RequestMethod.POST)
	private ModelAndView addUserSubmit(@ModelAttribute UserRoleModel user,RedirectAttributes redirect) {
		String message="";
		
		if(this.validatePass(user.getPassword())) {
			userService.addUser(user);
			message=null; 
		}
		else {
			message="password tidak sesuai ketentuan";
		}
		ModelAndView redir = new ModelAndView("redirect:/");
		redirect.addFlashAttribute("msg",message);
		return redir;
		
	}
	
	@RequestMapping( value = "/update")
	private String updatePassword() {
		return "update-password";
	}
	
	@RequestMapping(value="/submitPass",method=RequestMethod.POST)
	private ModelAndView submitPassword(@ModelAttribute PasswordModel pass, Model model, RedirectAttributes redir) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		//ngambil username yg lagi login
		UserRoleModel user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		String message= "";
		if(pass.getKonfirmPassword().equals(pass.getPasswordBaru())) {
			if(passwordEncoder.matches(pass.getPasswordLama(), user.getPassword())) {
				if(validatePass(pass.getPasswordBaru())) {
					userService.changePassword(user, pass.getPasswordBaru());
					message = "password berhasil diubah";
				}
				else {
					message = "password tidak sesuai ketentuan";
					
				}
			}
			else {
				message = "password lama anda salah";
			}
		}
		else {
			message = "password baru anda tidak cocok";
		}
		
		ModelAndView modelAndView = new ModelAndView("redirect:/user/update");
		redir.addFlashAttribute("msg",message);
		return modelAndView;
	}
	
	public boolean validatePass(String pass) {
		if(pass.length()>=8 && Pattern.compile("[0-9]").matcher(pass).find() && Pattern.compile("[a-zA-Z]").matcher(pass).find()) {
			return true;
		}
		else {
			return false;
		}
	}
}
