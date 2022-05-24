package en.upenn.bonz.im.dao.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import en.upenn.bonz.im.dao.MessageDAO;
import en.upenn.bonz.im.pojo.Message;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MessageDAOImpl implements MessageDAO {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Message> findListByFromAndTo(Long fromId, Long toId, Integer page, Integer rows) {

        // user A sends message to user B
        Criteria fromCriteria = new Criteria().andOperator(
                Criteria.where("from.id").is(fromId),
                Criteria.where("to.id").is(toId)
        );

        // user B sends message to user A
        Criteria toCriteria = new Criteria().andOperator(
                Criteria.where("to.id").is(fromId),
                Criteria.where("from.id").is(toId)
        );

        Criteria criteria = new Criteria().orOperator(fromCriteria, toCriteria);

        PageRequest pageRequest = PageRequest.of(page-1, rows, Sort.by(Sort.Direction.ASC, "sendDate"));

        Query query = new Query(criteria).with(pageRequest);

        //System.out.println(query);

        return mongoTemplate.find(query, Message.class);
    }

    @Override
    public Message findMessageById(String id) {
        return mongoTemplate.findById(new ObjectId(id), Message.class);
    }

    @Override
    public UpdateResult updateMessageState(ObjectId id, Integer status) {
        Query query = Query.query(Criteria.where("id").is(id));
        Update update = Update.update("status", status);
        if (status.intValue() == 1) {
            update.set("send_date", new Date());
        } else if (status.intValue() == 2) {
            update.set("read_date", new Date());
        }

        return mongoTemplate.updateFirst(query, update, Message.class);
    }

    @Override
    public Message saveMessage(Message message) {
        message.setSendDate(new Date());
        message.setStatus(1);
        message.setId(ObjectId.get());

        return mongoTemplate.save(message);
    }

    @Override
    public DeleteResult deleteMessage(String id) {
        Query query = Query.query(Criteria.where("id").is(new ObjectId(id)));

        return mongoTemplate.remove(query, Message.class);
    }
}
