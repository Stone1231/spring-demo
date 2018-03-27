package com.demo.dao.mysql;

import java.util.List;

//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.demo.model.Message;


public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findByType(String type);
    
    List<Message> findByMsgIdAndType(String msgId, String type);

    Long countByType(String type);

    @Transactional
    Long deleteByType(String type);

    //@Modifying
    @Transactional
    List<Message> removeByType(String type);
    
//    @Query(
//    		value = "select * from message where log_date between 1 and :log_date", 
//    		nativeQuery = true)
    List<Message> customQuery(@Param("log_date") long logDate);
}