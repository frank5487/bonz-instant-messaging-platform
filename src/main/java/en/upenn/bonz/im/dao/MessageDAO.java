package en.upenn.bonz.im.dao;

import en.upenn.bonz.im.pojo.Message;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;

import java.util.List;

public interface MessageDAO {

    /**
     * query chat history from two users
     *
     * @param fromId
     * @param toId
     * @param page
     * @param rows
     * @return
     */
    List<Message> findListByFromAndTo(Long fromId, Long toId, Integer page, Integer
            rows);

    /**
     * query message by id
     *
     * @param id
     * @return
     */
    Message findMessageById(String id);

    /**
     * update message status
     *
     * @param id
     * @param status
     * @return
     */
    UpdateResult updateMessageState(ObjectId id, Integer status);

    /**
     * save new message
     *
     * @param message
     * @return
     */
    Message saveMessage(Message message);

    /**
     * delete messages by id
     *
     * @param id
     * @return
     */
    DeleteResult deleteMessage(String id);

}
