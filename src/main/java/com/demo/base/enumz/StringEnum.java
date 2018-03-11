package com.demo.base.enumz;

/**
 * 列舉成員命名，請使用英文大寫形式
 */
public interface StringEnum {
	/**
	 * 列舉名稱,因會宣告成enum,此name()為原enum的方法
	 *
	 * 在此又定義name(),只是為了方便取
	 *
	 * @return
	 */
	String name();

	String getValue();
}
