package com.bhsr.emt.laboratory.service.dto.Constant;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConstantResponseDTO {

    @NotNull
    private String name;

    @NotNull
    private String value;

    @NotNull
    private String description;
}
