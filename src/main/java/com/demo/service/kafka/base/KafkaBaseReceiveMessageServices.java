package com.demo.service.kafka.base;

import org.springframework.context.ApplicationContext;


public interface KafkaBaseReceiveMessageServices<T> {

	/**
	 * get receive loop disable status
	 * @return
	 */
	public boolean isDisable();
	
	/**
	 * set receive loop disable status
	 * @param disable
	 */
	public void setDisable(boolean disable);
	
	/**
	 * close receive shutdown
	 */
	public void shutdown();
	
	/***
	 * Set flow end callback, for receive
	 * @param ApplicationContext (sprint use)
	 */
	public void setAppContext(ApplicationContext appContext);

	/**
	 * receive processes
	 * @param receive listener
	 * @throws Exception
	 */
	public void messageReceiver(KafkaBaseReceiveListener<T> listener);
	
	/**
	 * create Topic
	 */
	public void createTopic();
	
	/**
	 * delete kafka Topic
	 */
	public void deleteTopic();
}
