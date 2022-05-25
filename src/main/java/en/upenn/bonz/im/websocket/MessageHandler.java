package en.upenn.bonz.im.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import en.upenn.bonz.im.dao.MessageDAO;
import en.upenn.bonz.im.pojo.Message;
import en.upenn.bonz.im.pojo.UserData;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RocketMQMessageListener(
        topic = "bonz-im-send-message-topic",
        selectorExpression = "SEND_MSG",
        consumerGroup = "bonz-im-group",
        messageModel = MessageModel.BROADCASTING
)
public class MessageHandler extends TextWebSocketHandler implements RocketMQListener<String> {

    @Autowired
    private MessageDAO messageDAO;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Map<Long, WebSocketSession> SESSION = new HashMap<>();

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long uid = (Long) session.getAttributes().get("uid");

        SESSION.put(uid, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        Long uid = (Long) session.getAttributes().get("uid");

        JsonNode jsonNode = MAPPER.readTree(textMessage.getPayload());
        Long toId = jsonNode.get("toId").asLong();
        String msg = jsonNode.get("msg").asText();

        Message message = Message.builder().from(UserData.USER_MAP.get(uid))
                .to(UserData.USER_MAP.get(toId))
                .msg(msg)
                .build();

        // save the message to mongodb, status would be 1 by default (unread status)
        message = messageDAO.saveMessage(message);

        String msgJson = MAPPER.writeValueAsString(message);

        WebSocketSession toSession = SESSION.get(toId);
        // check if user is online or not
        if (toSession != null && toSession.isOpen()) {
            // TODO the specfic data type should be correspond with the data type front end requires
            toSession.sendMessage(new TextMessage(msgJson));

            // update status as read
            messageDAO.updateMessageState(message.getId(), 2);
        } else {
            // this user may be offline or in the other cluster node of RocketMQ server
            // requirement: add tag in order to conveniently filter the messages for consumer
            rocketMQTemplate.convertAndSend("bonz-im-send-message-topic:SEND_MSG", msgJson);
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long uid = (Long) session.getAttributes().get("uid");
        SESSION.remove(uid);
    }

    @Override
    public void onMessage(String msg) {
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            Long toId = jsonNode.get("to").get("id").longValue();

            WebSocketSession toSession = SESSION.get(toId);
            // check if user is online or not
            if (toSession != null && toSession.isOpen()) {
                // TODO the specfic data type should be correspond with the data type front end requires
                toSession.sendMessage(new TextMessage(msg));

                // update status as read
                messageDAO.updateMessageState(new ObjectId(jsonNode.get("id").asText()), 2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
