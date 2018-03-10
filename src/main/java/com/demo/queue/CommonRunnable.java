package com.demo.queue;


public interface CommonRunnable extends Runnable
{
	/**
	 * ��
	 * 
	 * @return
	 */
	boolean isDisable();
	

	void setDisable(boolean disable);

	/**
	 * ��
	 * 
	 * @return
	 */
	boolean isLogEnable();

	void setLogEnable(boolean logEnable);

	/**
	 * �銵�
	 */
	void execute();
	
}
