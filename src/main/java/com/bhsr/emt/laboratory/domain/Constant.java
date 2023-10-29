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
public class Constant {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private String value;

    @NotNull
    private String description;
}
