package en.upenn.bonz.im.controller;

import en.upenn.bonz.im.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private MessageService messageService;

    /**
     * pull user list
     * @param fromId
     * @return
     */
    @GetMapping
    public List<Map<String, Object>> queryUserList(@RequestParam("fromId") Long fromId) {
        return null;
    }
}
