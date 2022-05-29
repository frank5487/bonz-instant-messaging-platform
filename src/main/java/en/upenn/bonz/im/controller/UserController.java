package en.upenn.bonz.im.controller;

import en.upenn.bonz.im.pojo.Message;
import en.upenn.bonz.im.pojo.User;
import en.upenn.bonz.im.pojo.UserData;
import en.upenn.bonz.im.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private MessageService messageService;

    /**
     * pull user list (mock)
     * @param fromId
     * @return
     */
    @GetMapping
    public List<Map<String, Object>> queryUserList(@RequestParam("fromId") Long fromId) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Map.Entry<Long, User> userEntry : UserData.USER_MAP.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", userEntry.getValue().getId());
            map.put("avatar", "https://bonz-house-project.s3.amazonaws.com/avatar/chat_avatar.jpg");

            map.put("from_user", fromId);
            map.put("info_type", null);
            map.put("to_user", map.get("id"));
            map.put("username", userEntry.getValue().getUsername());

            // TODO: get the last message
            // query: db.message.find().sort({send_date:-1}).limit(1)
            List<Message> messages = messageService.queryMessageList(fromId,
                    userEntry.getValue().getId(), 1, 1, -1);

            if (messages != null && !messages.isEmpty()) {
                Message message = messages.get(0);
                map.put("chat_msg", message.getMsg());
                map.put("chat_time", message.getSendDate().getTime());
            }
            result.add(map);
        }
        return result;
    }
}
