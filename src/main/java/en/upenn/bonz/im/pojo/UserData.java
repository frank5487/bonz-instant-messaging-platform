package en.upenn.bonz.im.pojo;

import java.util.HashMap;
import java.util.Map;

public class UserData {

    public static final Map<Long,User> USER_MAP = new HashMap<>();

    static {
        USER_MAP.put(1001L, User.builder().id(1001L).username("Tom").build());
        USER_MAP.put(1002L, User.builder().id(1002L).username("Lisa").build());
        USER_MAP.put(1003L, User.builder().id(1003L).username("Selina").build());
        USER_MAP.put(1004L, User.builder().id(1004L).username("Jack").build());
        USER_MAP.put(1005L, User.builder().id(1005L).username("Hana").build());
    }
}
