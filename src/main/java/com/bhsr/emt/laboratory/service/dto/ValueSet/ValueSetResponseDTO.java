package com.bhsr.emt.laboratory.service.dto.ValueSet;

import com.bhsr.emt.laboratory.domain.Constant;
import com.bhsr.emt.laboratory.domain.enumeration.DataType;
import com.bhsr.emt.laboratory.service.dto.Constant.ConstantResponseDTO;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueSetResponseDTO {

    private String id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private String description;

    @NotNull(message = "must not be null")
    private DataType dataType;

    @NotNull(message = "must not be null")
    private Set<ConstantResponseDTO> constants;
}
