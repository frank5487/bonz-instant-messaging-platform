package en.upenn.bonz.im.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import en.upenn.bonz.im.dao.MessageDAO;
import en.upenn.bonz.im.pojo.Message;
import en.upenn.bonz.im.pojo.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class MessageHandler extends TextWebSocketHandler {

    @Autowired
    private MessageDAO messageDAO;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Map<Long, WebSocketSession> SESSION = new HashMap<>();

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

        WebSocketSession toSession = SESSION.get(toId);
        // check if user is online or not
        if (toSession != null && toSession.isOpen()) {
            // TODO the specfic data type should be correspond with the data type front end requires
            toSession.sendMessage(new TextMessage(MAPPER.writeValueAsString(message)));

            // update status as read
            messageDAO.updateMessageState(message.getId(), 2);
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long uid = (Long) session.getAttributes().get("uid");
        SESSION.remove(uid);
    }
}
