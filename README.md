# bonz-instant-messaging-system (optimized with RocketMQ)

## Design outline
Chat system is consisted of two necessary objects, 
User (data saved in the MySQL) and Message (data saved in the mongoDB).
Once created the objects with appropriate attributes, I choose WebSocket protocol
where once we created the connection between client and server, it will remain connection status.
Therefore, we don't need to set a time interval in the js file to refresh the application 
in order to achieve the instant messaging function. In the end, I optimized this chat system with RocketMQ.
Since if there are too many users chatting simultaneously, too many sessions will be saved in the server, 
which leads to a great pressure to server. In order to reduce the pressure on server, 
I adopt the RocketMQ as middleware to distribute sessions into different RocketMQ.

## POJO class
The attributes of POJO class will link with mongoDB automatically 
which is managed by Spring if it has the same class name with table, 
otherwise it should be added the annotation of @Document to assign the table

@Id: Assign ObjectId index  
@Index: Assign index to attribute  
@Field: Underscore to camel

* Message
  * id: ObjectId 
  * msg: String 
  * status: Integer // 1: unread, 2: read
  * sendDate: String
  * readDate: String
  * from: User
  * to: User
   
* User
  * id: Long
  * username: String
  
## Websocket protocol (ws://)
The classes managed by the spring
* MessageHandshakeInterceptor
  * extends HandshakeInterceptor
  * After connecting to the websocket protocol, interceptor would check if the connection is valid and read the uid 
* MessageHandler
  * Extends TextWebSocketHandler
  * Override methods to deal with the service logic and send messages
  * Implements RocketMQListener<>
  * Override methods to handle the messages which are registered in the topic and the same group
* WebSocketConfig
  * register MessageHandler and MessageHandshakeInterceptor into Spring container

## dao (Mapper)
* MessageDAO
  * Use mongoTemplate to query

## Project display

### User1 
![user1](https://github-zen-project.s3.amazonaws.com/bon-im-desc/Selina_1.jpeg "user1")

### User2 
![user2](https://github-zen-project.s3.amazonaws.com/bon-im-desc/Tom_1.jpeg "user2")

### User1 sends message
![user1](https://github-zen-project.s3.amazonaws.com/bon-im-desc/Selina_2.jpeg "user1")

### User2 sends message
![user2](https://github-zen-project.s3.amazonaws.com/bon-im-desc/Tom_2.jpeg "user2")
