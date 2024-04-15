package com.bhsr.emt.laboratory.domain;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomField {

    @NotNull
    private String key;

    @NotNull
    private String value;
}
