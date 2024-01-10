package com.bhsr.emt.laboratory.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldFormat {

    private String name;

    private String dataType;

    private Boolean isRequired;

    private Boolean isSearchable;

    private String defaultValue;

    private String referenceValue;

    private String valueSet;

    private Integer order;
}
