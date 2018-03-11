package com.demo.service;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.demo.model.MqttRequest;

import com.demo.service.MqttPubService;
import com.demo.utils.DateUtil;
import com.demo.utils.JsonUtil;
import com.demo.model.Message;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class MqttPubServiceImplTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Autowired
	private MqttPubService service;

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void publishMsg() throws Exception {
		Message msg = new Message();
		msg.setType("chat");
		msg.setCategory("plain text");

		String topic = "chat/test";
		msg.setSender("ab2e120e86214f2d890425d441ce4869");
		msg.setReceiver("888bcd8203c8491e9437871215e5744d");

		msg.setChatID(DateUtil.getUTCTimeInOpenFireFormat());
		msg.setLogDate(DateUtil.getUTCTime());
		msg.setBody("Hello~");

		publishSync(JsonUtil.writeJSON(msg), topic);
		

		while (true) {

		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void publishMsgs() throws InterruptedException {
		// publishQueue();
		Message msg = new Message();
		msg.setType("chat");
		msg.setCategory("plain text");
		// msg.setBody("see you");

		Integer i = 0;

		// String topic = "topic1/test";
		String topic = "chat/test";
		msg.setSender("ab2e120e86214f2d890425d441ce4869");
		msg.setReceiver("888bcd8203c8491e9437871215e5744d");

		while (i < 20000) {
			msg.setChatID(i.toString());
			msg.setLogDate(DateUtil.getUTCTime());
			msg.setBody("Hello~" + i++);
			// i++;
			publishSync(JsonUtil.writeJSON(msg), topic);
			// publishPaho(new Gson().toJson(msg), topic);
		}

		// Thread.sleep(2000);
		// while (i < 2000) {
		// //publishQueue("hi" + ++i);
		// publish("hello" + ++i);
		// }

		while (true) {

		}

	}

	private void publish(String msg, String topic) {
		MqttRequest request = new MqttRequest();
		request.setTopic(topic);
		request.setMessage(msg);
		request.setQos(1);
		request.setRetain(false);
		service.sendMessage(request);
	}

	private void publishSync(String msg, String topic) {
		MqttRequest request = new MqttRequest();
		request.setTopic(topic);
		request.setMessage(msg);
		request.setQos(1);
		request.setRetain(false);
		service.sendMessageSync(request);
	}
}
