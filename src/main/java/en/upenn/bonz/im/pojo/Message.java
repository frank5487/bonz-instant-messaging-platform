package en.upenn.bonz.im.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "message") // table's name
@Builder
public class Message {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String msg;
    /**
     * message status, 1: unread, 2: read
     */
    @Indexed
    private Integer status;
    @Field("send_date")
    @Indexed
    private Date sendDate;
    @Field("read_date")
    private Date readDate;
    @Indexed
    private User from;
    @Indexed
    private User to;

}
