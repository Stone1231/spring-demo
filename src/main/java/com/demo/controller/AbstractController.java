package com.demo.controller;

import org.springframework.beans.factory.annotation.Value;

import com.demo.utils.StringUtil;

public abstract class AbstractController {

	@Value("${system.env}")
	protected String environment;

	//-------------------------------------------------------------------------
	
	/**
	 * 是否開放使用debug模式
	 * 
	 * @return true:放行, false:關閉
	 */
	protected boolean isAllowDebugMode() {
		if (StringUtil.isNullOrEmpty(environment))
			return false;

		return environment.matches("(?i)pre[-]?dev|(?i)dev|(?i)pre[-]?prd");
	}

}
