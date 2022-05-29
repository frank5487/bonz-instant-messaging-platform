package en.upenn.bonz.im.service.impl;

import en.upenn.bonz.im.dao.MessageDAO;
import en.upenn.bonz.im.pojo.Message;
import en.upenn.bonz.im.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDAO messageDAO;

    @Override
    public List<Message> queryMessageList(Long fromId, Long toId, Integer page, Integer rows, Integer... order) {
        List<Message> list = messageDAO.findListByFromAndTo(fromId, toId, page, rows, order);
        for (Message message : list) {
            if (message.getStatus().intValue() == 1) {
                messageDAO.updateMessageState(message.getId(), 2);
            }
        }

        return list;
    }
}
