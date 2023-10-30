package com.bhsr.emt.laboratory.service.dto.DiagnosticReportFormat;

import com.bhsr.emt.laboratory.service.dto.FieldFormat.FieldFormatRequestDTO;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticReportFormatRequestDTO {
    @NotNull
    private String name;

    @NotNull
    private Set<FieldFormatRequestDTO> fields;
}
