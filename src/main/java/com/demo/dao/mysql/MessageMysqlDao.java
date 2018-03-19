package com.demo.dao.mysql;

import java.util.List;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.demo.model.Message;

public interface MessageMysqlDao extends CrudRepository<Message, Long> {

    List<Message> findByType(String type);
    
    List<Message> findByMsgIdAndType(String msgId, String type);

    Long countByType(String type);

    @Transactional
    Long deleteByType(String type);

    //@Modifying
    @Transactional
    List<Message> removeByType(String type);
}