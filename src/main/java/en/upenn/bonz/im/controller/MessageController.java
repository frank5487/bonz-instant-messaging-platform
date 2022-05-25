package en.upenn.bonz.im.controller;

import en.upenn.bonz.im.pojo.Message;
import en.upenn.bonz.im.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@CrossOrigin
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * pull message list
     * @param fromId
     * @param toId
     * @param page
     * @param rows
     * @return
     */
    @GetMapping
    public List<Message> queryMessageList(@RequestParam("fromId") Long fromId,
                                          @RequestParam("toId") Long toId,
                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "rows", defaultValue = "15") Integer rows) {
        return messageService.queryMessageList(fromId, toId, page,rows);
    }
}
