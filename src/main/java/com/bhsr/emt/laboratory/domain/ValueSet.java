package com.bhsr.emt.laboratory.domain;

import com.bhsr.emt.laboratory.domain.enumeration.DataType;
import java.io.Serializable;
import java.util.Set;
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
public class ValueSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("name")
    private String name;

    @NotNull(message = "must not be null")
    @Field("description")
    private String description;

    @NotNull(message = "must not be null")
    @Field("dataType")
    private DataType dataType;

    @NotNull(message = "must not be null")
    @Field("constants")
    private Set<Constant> constants;
}
