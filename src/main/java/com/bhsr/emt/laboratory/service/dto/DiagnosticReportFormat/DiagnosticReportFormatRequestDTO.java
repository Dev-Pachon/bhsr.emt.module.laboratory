package com.bhsr.emt.laboratory.service.dto.DiagnosticReportFormat;

import com.bhsr.emt.laboratory.service.dto.FieldFormat.FieldFormatRequestDTO;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticReportFormatRequestDTO {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private Set<FieldFormatRequestDTO> fieldFormats;
}
