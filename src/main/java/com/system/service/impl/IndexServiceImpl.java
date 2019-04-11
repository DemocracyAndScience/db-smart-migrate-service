package com.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.config.UsersConfig;
import com.system.entity.User;
import com.system.service.IndexService;
import com.system.utils.MD5Utils;
import com.system.utils.constants.SystemConstants;

@Service
public class IndexServiceImpl implements IndexService {

	@Autowired
	private UsersConfig usersConfig;
	
	@Autowired
	private HttpSession session ;

	@Override
	public void toIndex(User user) {

		boolean usernameIsExsits = false;
		boolean passwordIsRight = false;
		List<Map<String, String>> users = usersConfig.getUsers();
		outterLoop: for (Map<String, String> map : users) {
			Set<Entry<String, String>> entrySet = map.entrySet();
			for (Entry<String, String> entry : entrySet) {
				usernameIsExsits = entry.getKey().equals(user.getUsername());
				if (usernameIsExsits) {
					passwordIsRight = entry.getValue().equals(MD5Utils.MD5(user.getPassword()));
					if (passwordIsRight) {
						session.setAttribute(SystemConstants.LOGIN_KEY, user);
					}
					break outterLoop;
				}
			}
		}
		if(!usernameIsExsits) {
			throw new RuntimeException("用户名不存在！");
		}
		if(!passwordIsRight) {
			throw new RuntimeException("密码不正确！");
		}
		
	}

}
