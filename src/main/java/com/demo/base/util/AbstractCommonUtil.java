package com.demo.base.util;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 抽象BaseUtil
 */
public abstract class AbstractCommonUtil implements CommonUtil {

	public AbstractCommonUtil() {
		//
	}

	/**
	 * To string.
	 *
	 * @return String
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
