package com.chen.pojo.page;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("posts_takeoff")
public class Posts_Takeoff extends Posts_Temp{


    private String takeoffTime;
    private String takeoffReason;

}
