package com.bhsr.emt.laboratory.service.dto.FieldFormat;

import com.bhsr.emt.laboratory.domain.ValueSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldFormatRequestDTO {

    private String name;

    private String dataType;

    private Boolean isRequired;

    private Boolean isSearchable;

    private String defaultValue;

    private String valueSet;

    private Integer order;
}
