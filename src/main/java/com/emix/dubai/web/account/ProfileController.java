package com.emix.dubai.web.account;

import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.emix.dubai.entity.User;
import com.emix.dubai.service.account.AccountService;
import com.emix.dubai.service.account.ShiroDbRealm.ShiroUser;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 用户修改自己资料的Controller.
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/account/settings/profile")
public class ProfileController {

	@Autowired
	private AccountService accountService;

	@RequestMapping(method = RequestMethod.GET)
	public String profileForm(Model model) {
		Long id = getCurrentUserId();
		model.addAttribute("user", accountService.getUser(id));
		return "account/profile";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String updateProfile(@Valid @ModelAttribute("preloadUser") User user, RedirectAttributes redirectAttributes) {
		accountService.updateUser(user);
		updateCurrentUserName(user.getNiceName());
        redirectAttributes.addFlashAttribute("message", "个人资料成功保存。");
        return "redirect:/account/settings/profile";
	}

	@ModelAttribute("preloadUser")
	public User getUser(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return accountService.getUser(id);
		}
		return null;
	}

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

	/**
	 * 更新Shiro中当前用户的用户名.
	 */
	private void updateCurrentUserName(String userName) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		user.name = userName;
	}
}
