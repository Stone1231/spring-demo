package com.demo;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import com.demo.base.BaseTest;
import com.demo.dao.mysql.MessageMysqlDao;
import com.demo.model.Message;
import com.demo.service.MessageService;
import com.demo.service.impl.MessageServiceImpl;
import com.demo.utils.StringUtil;

@RunWith(PowerMockRunner.class)  //PowerMock才能模擬private method
@PrepareForTest(MessageServiceImpl.class)
public class MocksTest extends BaseTest {

	@Mock
	private MessageService messageService;
	
	@InjectMocks
	private MessageServiceImpl messageServiceImpl = PowerMockito.spy(new MessageServiceImpl());
	
	@Mock
	private MessageMysqlDao messageMysqlDao;
	
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
	public void mockTest(){
		
		Message mockMessage = Mockito.mock(Message.class);
		Mockito.when(mockMessage.getBody()).thenReturn("xxxxx");
		
		assertTrue(mockMessage.getBody().equals("xxxxx"));
	}
    
    @Test
	public void mock2Test(){
		
    	List<Message> messages = Arrays.asList(
    			new Message(),
    			new Message()
    			);
    	
    	messages.get(0).setBody("message 1");
    	messages.get(1).setBody("message 2");
		
    	String type = "type";
    	
    	Mockito.when(messageService.getTypeCache(type)).thenReturn(messages);
    	List<Message> mockMessages = messageService.getTypeCache(type);//一定要輸入一樣的type
    	
    	System.out.println(mockMessages.size());
    	System.out.println(mockMessages.get(0).getBody());
    	System.out.println(mockMessages.get(1).getBody());
    	System.out.println("====");
    	
    	Mockito.when(messageService.getTypeCache(Mockito.anyString())).thenReturn(messages);
    	mockMessages = messageService.getTypeCache("perikdkowjjww");//隨便輸入
    	
    	System.out.println(mockMessages.size());
    	System.out.println(mockMessages.get(0).getBody());
    	System.out.println(mockMessages.get(1).getBody());
    	System.out.println("====");
	}
   
    
    @Test
    public void whiteBoxTest(){
    	//WhiteBox的目的就是跳過面向對象語言的封裝性，
    	//允許test case直接操作類的私有成員、私有方法、甚至通過私有構造函數創建實例。
    	try {
			String msg = Whitebox.invokeMethod(messageServiceImpl, "privateMsg", "Hello! whiteBox test");
			System.out.println(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    @Test
	public void powerMockPublicTest() throws Exception {

    	List<Message> messages = Arrays.asList(
    			new Message(),
    			new Message()
    			);
    	
    	messages.get(0).setBody("message 1");
    	messages.get(1).setBody("message 2");
    	
    	PowerMockito.doReturn(messages).when(
		messageServiceImpl, 
		"getTypeCache", 
		Mockito.anyString() //設定可以輸入任何字串
		);

    	List<Message> mockMessages = messageServiceImpl.getTypeCache("perikdkowjjww");//隨便輸入
    	
    	System.out.println(mockMessages.size());
    	System.out.println(mockMessages.get(0).getBody());
    	System.out.println(mockMessages.get(1).getBody());
    	System.out.println("====");
    }
    
    @Test
	public void powerMockPublic2Test() throws Exception{

		
		Message message = new Message();
		message.setBody("hello");
		
		PowerMockito.doReturn(message).when(
				messageServiceImpl, 
				"convertMsg", 
				Mockito.any(Message.class) //設定可以輸入任何字串
				);
		
		Message resMessage = messageServiceImpl.convertMsg(message);		
		System.out.println(StringUtil.writeJSON(resMessage));
    }
    
    @Test
	public void powerMockPrivateTest() {
    	
    	//相關的Mockito一開始就要設好, 後面再設會出錯
//    	Message message = Mockito.mock(Message.class);
//    	Mockito.when(message.getType()).thenReturn("");
//    	Mockito.when(message.getBody()).thenReturn("");
    	
    	Message message = new Message();
    	message.setBody("hello");
    	message.setType("");
    	
    	List<Message> messages = Mockito.mock(ArrayList.class);//new ArrayList<Message>();
    	
    	Mockito.when(
    			messages.get(Mockito.anyInt())
    			).thenReturn(message);
    	
    	Mockito.when(
    			messageMysqlDao.getbyType(Mockito.anyString())
    			).thenReturn(messages);
//    	
    	
    	Mockito.when(
    			messageMysqlDao.update(Mockito.any(Message.class))
    			).thenReturn(1);

		try {
			PowerMockito.doReturn("nothing").when(
					messageServiceImpl, 
					"privateMsg", 
					"error"//Mockito.anyString()
					);
			
			Message resMessage = messageServiceImpl.convertMsg(message);
			System.out.println(StringUtil.writeJSON(resMessage));
			
	    	message.setBody("error");//故意出錯
	    	resMessage = messageServiceImpl.convertMsg(message);
	    	System.out.println(StringUtil.writeJSON(resMessage));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertTrue(false);
		}
    	
		
	}
}
