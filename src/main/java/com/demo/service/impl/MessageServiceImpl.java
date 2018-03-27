package com.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.dao.mysql.MessageMysqlDao;
import com.demo.model.Message;
import com.demo.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageMysqlDao messageMysqlDao;
	
	@Override
	public List<Message> getbyType(String type) {
		// TODO Auto-generated method stub
		return messageMysqlDao.getbyType(type);
	}

}
