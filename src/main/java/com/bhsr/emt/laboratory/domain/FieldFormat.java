package com.bhsr.emt.laboratory.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldFormat {

    @Id
    private String id;

    private String name;

    private String dataType;

    private Boolean isRequired;

    private Boolean isSearchable;

    private String defaultValue;

    private String valueSet;

    private Integer order;
}
