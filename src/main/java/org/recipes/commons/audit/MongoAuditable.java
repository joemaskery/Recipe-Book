package org.recipes.commons.audit;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
public abstract class MongoAuditable {

    @CreatedDate
    private LocalDateTime createdDate;
}
