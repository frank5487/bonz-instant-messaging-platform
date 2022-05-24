package en.upenn.bonz.im.dao;

import en.upenn.bonz.im.pojo.Message;
import en.upenn.bonz.im.pojo.User;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMessageDAO {

    @Autowired
    private MessageDAO messageDAO;

    @Test
    public void testSave() {
        Message message = Message.builder()
                .id(ObjectId.get())
                .msg("Hi!")
                .sendDate(new Date())
                .status(1)
                .from(new User(1001L, "Tom"))
                .to(new User(1002L,"Lisa"))
                .build();

        messageDAO.saveMessage(message);

        message = Message.builder()
                .id(ObjectId.get())
                .msg("How you doing")
                .sendDate(new Date())
                .status(1)
                .to(new User(1001L, "Tom"))
                .from(new User(1002L,"Lisa"))
                .build();

        messageDAO.saveMessage(message);

        message = Message.builder()
                .id(ObjectId.get())
                .msg("I'm learning how to use IM...")
                .sendDate(new Date())
                .status(1)
                .from(new User(1001L, "Tom"))
                .to(new User(1002L,"Lisa"))
                .build();

        messageDAO.saveMessage(message);

        message = Message.builder()
                .id(ObjectId.get())
                .msg("sounds great!")
                .sendDate(new Date())
                .status(1)
                .to(new User(1001L, "Tom"))
                .from(new User(1002L,"Lisa"))
                .build();

        messageDAO.saveMessage(message);

        System.out.println("ok");
    }

    @Test
    public void testQueryList() {
        List<Message> listByFromAndTo = messageDAO.findListByFromAndTo(1001L, 1002L, 1, 10);
//        List<Message> listByFromAndTo = messageDAO.findListByFromAndTo(1001L, 1002L, 2, 2);

        for (Message message : listByFromAndTo) {
            System.out.println(message.getMsg());
        }
    }
}
