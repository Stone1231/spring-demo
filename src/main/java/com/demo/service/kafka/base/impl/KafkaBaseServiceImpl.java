package com.demo.service.kafka.base.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import com.demo.model.Callback;
import com.demo.model.Feeback;
import com.demo.model.KafkaKey;
import com.demo.service.impl.ThreadService;
import com.demo.service.kafka.Impl.MessageKafkaSendServiceImpl;
import com.demo.service.kafka.base.KafkaBaseReceiveListener;
import com.demo.service.kafka.base.KafkaBaseService;
import com.demo.service.kafka.base.KafkaConsumerReceiveListener;
import com.demo.utils.StringUtil;

public class KafkaBaseServiceImpl<T> extends KafkaAdapterImpl implements KafkaBaseService<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaBaseServiceImpl.class);
	
	private final int defaultZeroPenRestCount = 3;
	
	private ThreadService threadService = new ThreadService();
	
	private boolean isDisable = false;
	private KafkaBaseReceiveListener<T> receiveListener = null;
	private Class<T> clazz;
	private ApplicationContext applicationContext;

	public void init(String zookeeper, String bootstrapList, String groupID, String topic, Class<T> inputClazz){
		super.init(zookeeper, bootstrapList, groupID, topic);
		clazz = inputClazz;
	}
	
	@Override
	/***
	 * Set flow end callback, for receive
	 * @param ApplicationContext (sprint use)
	 */
	public void setAppContext(ApplicationContext appContext){
		applicationContext = appContext;
	}

	private Callback callbackFunction;
	/***
	 * Set flow end callback , for send
	 * @param callback
	 */
	public void setCallback(Callback callback){
		callbackFunction = callback;
	}

	/***
	 * Set flow end callback
	 * @param ApplicationContext (sprint use)
	 * @param callback
	 */
	public void setCallback(ApplicationContext appContext, Callback callback) {
		applicationContext = appContext;
		callbackFunction = callback;
	}
	
	private ApplicationContext getAppContext() {
		return applicationContext;
	}

	private Callback getCallback() {
		return callbackFunction;
	}	

	@Override
	/**
	 * send message
	 * @param key
	 * @param message
	 * @return key String
	 * @throws Exception
	 */
	public String sendMessage(String key, T message) throws Exception{
        return super.sendMessage(convetKey(key), StringUtil.writeJSON(message));
	}
	
	@Override
	/**
	 * send message
	 * @param key
	 * @param message
	 * @param topic
	 * @return
	 * @throws Exception
	 */
	public String sendMessage(String key, T message, String topic) throws Exception{
        return super.sendMessage(convetKey(key), StringUtil.writeJSON(message), topic);
	}	
	
	@Override
	/**
	 * send message
	 * @param key
	 * @param message
	 * @param topics list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, T message, List<String> topics) throws Exception {
        return super.sendMessage(convetKey(key), StringUtil.writeJSON(message), topics);
	}

	@Override
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @param topic
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, List<T> messages) throws Exception {
		return super.sendMessages(convetKey(key), trensfromMessage(messages));
	}
	
	@Override
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, List<T> messages, String topic) throws Exception {
		return super.sendMessages(convetKey(key), trensfromMessage(messages), topic);
	}

	@Override
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @param topics list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, List<T> messages, List<String> topics)
			throws Exception {
		return super.sendMessages(convetKey(key), trensfromMessage(messages), topics);
	}
	
	@Override
	/**
	 * send message
	 * @param key
	 * @param message
	 * @param callback
	 * @return key String
	 * @throws Exception
	 */
	public String sendMessage(String key, T message, Callback callback) throws Exception {
        return super.sendMessage(convetKey(key, callback), StringUtil.writeJSON(message));
	}
	
	@Override
	/**
	 * send message
	 * @param key
	 * @param message
	 * @param topic
	 * @param callback
	 * @return
	 * @throws Exception
	 */
	public String sendMessage(String key, T message, String topic, Callback callback) throws Exception {
        return super.sendMessage(convetKey(key, callback), StringUtil.writeJSON(message), topic);
	}	
	
	@Override
	/**
	 * send message
	 * @param key
	 * @param message
	 * @param topics list
	 * @param callback
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, T message, List<String> topics, Callback callback)
			throws Exception {
        return super.sendMessage(convetKey(key, callback), StringUtil.writeJSON(message), topics);
	}

	@Override
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @param topic
	 * @param callback
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, List<T> messages, Callback callback) throws Exception {
		return super.sendMessages(convetKey(key, callback), trensfromMessage(messages));
	}
	
	@Override
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @param callback
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, List<T> messages, String topic, Callback callback) throws Exception {		
		return super.sendMessages(convetKey(key, callback), trensfromMessage(messages), topic);
	}

	@Override
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @param topics list
	 * @param callback
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, List<T> messages, List<String> topics, Callback callback)
			throws Exception {
		return super.sendMessages(convetKey(key, callback), trensfromMessage(messages), topics);
	}
	
	/**
	 * send message
	 * 
	 * @param key: KafkaKey
	 * @param message
	 * @param callback
	 * @return key
	 * @throws Exception
	 */
	private String sendMessage(KafkaKey key, T message) throws Exception {
        return super.sendMessage(StringUtil.writeJSON(key), StringUtil.writeJSON(message));
	}
	
	/**
	 * convet Key to KafkaKey
	 * @param key
	 * @return String
	 */
	private String convetKey(String key) {		
		return convetKey(key, getCallback());
	}
	
	/**
	 * convet Key to KafkaKey
	 * @param key
	 * @param callback
	 * @return String
	 */
	private String convetKey(String key, Callback callback) {		
		KafkaKey keys = new KafkaKey(key, callback);
		return StringUtil.writeJSON(keys);
	}

	/**
	 *  trensfrom List<T> to List<String>
	 * @param messages
	 * @return List<String>
	 */
	private List<String> trensfromMessage(List<T> messages) {
		List<String> strMessages = new ArrayList<>();
		for(T message:messages)
			strMessages.add(StringUtil.writeJSON(message));
		return strMessages;
	}
	
	@Override
	public boolean isDisable() {
		return isDisable;
	}

	@Override
	public void setDisable(boolean disable) {
		isDisable = disable;
	}

	@Override
	public void shutdown() {
		super.closeConnect();
		if (threadService != null) threadService.doStop();
	}		
	
	@Override
	public void messageReceiver(final KafkaBaseReceiveListener<T> listener) {
		threadService.doStart();
		threadService.sumbit(new Runnable() {
			@Override
			public void run() {
				while ((!isDisable())) {
					try {
						execute(listener);
					} catch (Exception ace) {
						LOGGER.error(
								"run error, sleep 5000ms", ace);
						sleep(5000);
					}
				}
			}
		});	
	}
	
	private void execute(KafkaBaseReceiveListener<T> listener){
		receiveListener = listener;
		receiveMessage();
	}
	
	private void receiveMessage(){
		
		int restCount = 0;
		while ((!isDisable())) {
			try {	
				int count = 
					super.receiveMessage(new ReceiveListener(this.getAppContext(), this.receiveListener, this.clazz));
				if (count == 0) restCount ++;
				if (restCount > defaultZeroPenRestCount) {
					restCount = 0;
					sleep(10000);
				}
			} catch (Exception ace) {
				LOGGER.error(
						"Connection Failed when retsuper.riving queue URL, sleep 5000ms",ace);
				sleep(5000);
			}
		}
	}	
	
	private void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			LOGGER.error("sleep", e);
		}
	}

	private class ReceiveListener implements KafkaConsumerReceiveListener {

		private KafkaBaseReceiveListener<T> kfListener = null;
		private ApplicationContext applContext = null;
		private Class<T> subClazz;
				
		private ReceiveListener(ApplicationContext inputAppContext, KafkaBaseReceiveListener<T> listener, Class<T> inputClazz){
			kfListener = listener;
			applContext = inputAppContext;
			subClazz = inputClazz;
		}
		
		@Override
		public Feeback receiveMessage(String key, String message) throws Exception {
			Exception ex= null;
			Feeback feedback = null;
			KafkaKey kafkaKey = null;
			T retMessage = null;
			
			try {
				retMessage = convertMessage(message);
				kafkaKey = convertKey(key);
				if (kfListener != null)
					feedback = kfListener.receiveMessage(kafkaKey.getKey(), retMessage);
				else
					feedback = new Feeback(Feeback.Status.success, "");
				
			} catch(Exception e) {
				ex = e;
				LOGGER.error("receiveMessage", e);
				feedback = new Feeback(Feeback.Status.reTry, e);
			} finally {
				
				//callBack
				try {
					callBack(kafkaKey, retMessage, ex, feedback);
				} catch(Exception e) {
					ex = e;
					LOGGER.error("callback error", e);
				}
				
				//reTry for reSend to 
				if (feedback != null && feedback.getStatus() == Feeback.Status.reTry){
					if(kafkaKey != null && retMessage != null) {
						sendMessage(kafkaKey, retMessage);
					} else {
						LOGGER.error("receiveMessage kafkaKey retMessage is null");
					}
				}
			}
			return feedback;
		}
		
		private KafkaKey convertKey(String key){
			
			KafkaKey rtnValue = null;
			try {
				if (key != null)
					rtnValue = StringUtil.readJSON(key, KafkaKey.class);				
			} catch (Exception e) {
				LOGGER.error(String.format("convert key error: %s %s", key, "\n"));
				
			}finally{
				if (rtnValue == null)
					rtnValue = new KafkaKey(key);
			}
			return rtnValue;
		}
		
		private T convertMessage(String message){
			
			T rtnValue = null;
			try {
				if (message != null)
					rtnValue = StringUtil.readJSON(message, subClazz);				
			} catch (Exception e) {
				LOGGER.error("Message convert<T> error %s %s", "\n", e);
			}
			
			return rtnValue;
		}
		
		private Object getAutowireObject(String beanName) {
			
			if (applContext == null) {
				return null;
			}
		    AutowireCapableBeanFactory autowire = applContext.getAutowireCapableBeanFactory();
		    if (autowire.containsBean(beanName))
		    	return autowire.getBean(beanName);
		    else
		    	return null;
		}
		
		private void callBack(KafkaKey key, T message, Exception exception, Feeback feeback) {
			
			if (key != null){
				if (key.getCallback() != null && feeback.getStatus() != Feeback.Status.reTry) {
					try {
						callBack(key.getKey(), message, exception, key.getCallback(), feeback);
					} catch (Exception e) {
						LOGGER.error("Call Back error %s", e);					
					}
				}
			}
		}

		private void callBack(String key, T message, Exception exception, Callback callBack, Feeback feeback) throws Exception {
		    String serviceName = callBack.getServiceName();
		    if (StringUtil.isNullOrEmpty(serviceName)) return;
		    
		    String beanName = callBack.getBeanName();		    
		    if (StringUtil.isNullOrEmpty(beanName)) return;

		    String methodName = callBack.getMethodName();		    
		    if (StringUtil.isNullOrEmpty(methodName)) return;
			
		    Class<?> c = Class.forName(serviceName);
		    Object newC = getAutowireObject(beanName);
		    if (newC == null) newC = c.newInstance();
		    
		    Method method = null;
		    Method[] methods = c.getDeclaredMethods();
		    for(int i=0; i<methods.length; i++){
		    	method = methods[i];
			    if(method.getName().equals(methodName)){
			    	//String Key, String message, String returnValue, Exception exception
			    	//Object result = 
			    	method.invoke(newC, key, message, feeback, exception);    //這邊應該怎麼寫才可以動態呼叫對應method
			    	return;
			    }
			}
		}		
	}
}