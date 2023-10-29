package com.bhsr.emt.laboratory.domain;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ValueSet.
 */
@Document(collection = "value_set")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ValueSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("name")
    private String name;

    @NotNull(message = "must not be null")
    @Field("constant")
    private List<Constant> constant;
}
