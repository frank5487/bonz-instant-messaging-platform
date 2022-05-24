package en.upenn.bonz.im.service;

import en.upenn.bonz.im.pojo.Message;

import java.util.List;

public interface MessageService {

    List<Message> queryMessageList(Long fromId, Long toId, Integer page, Integer rows);

}
