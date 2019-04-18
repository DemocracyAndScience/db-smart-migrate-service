package com.system.controller;

import com.system.config.DatabasesNames;
import com.system.entity.User;
import com.system.service.IndexService;
import com.system.utils.Result;
import com.system.utils.componet.DBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class IndexContoller {

	@Autowired
	private DatabasesNames databasesNames;
	@Autowired
	private DBUtils dBUtils;
	@Autowired
	private IndexService indexService;

	@RequestMapping(value = "/")
	public String home() {
		return "index";
	}

	@ResponseBody
	@RequestMapping(value = "/toIndex" , method=RequestMethod.POST)
    public Result toIndex(@Valid User user  ){
		try {
			indexService.toIndex(user);
		}catch(Exception e) {
			return Result.fail(e.getMessage());
		}
		return Result.success("");
    }

	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}

	@ResponseBody
	@RequestMapping(value = "/databasesNames")
	public List<String> databasesNames() {
		return databasesNames.getNames();
	}

	@ResponseBody
	@RequestMapping(value = "/currentDatabaseName")
	public String currentDatabaseName(HttpSession session) {
		String tableSchema = dBUtils.getTableSchema(session);
		return tableSchema;
	}

}
