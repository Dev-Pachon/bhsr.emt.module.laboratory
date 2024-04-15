package com.bhsr.emt.laboratory.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A IdentifierType.
 */
@Document(collection = "identifier_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdentifierType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @NotNull(message = "must not be null")
    @Field("name")
    private String name;
}
