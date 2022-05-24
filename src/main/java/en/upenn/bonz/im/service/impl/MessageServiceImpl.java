package en.upenn.bonz.im.service.impl;

import en.upenn.bonz.im.pojo.Message;
import en.upenn.bonz.im.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public List<Message> queryMessageList(Long fromId, Long toId, Integer page, Integer rows) {
        return null;
    }
}
