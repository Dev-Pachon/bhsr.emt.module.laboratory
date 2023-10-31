package com.bhsr.emt.laboratory.service.dto.DiagnosticReportFormat;

import com.bhsr.emt.laboratory.service.dto.FieldFormat.FieldFormatResponseDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class DiagnosticReportFormatResponseDTO {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private Set<FieldFormatResponseDTO> fieldFormats;

    @NotNull
    private LocalDate createdAt;

    @NotNull
    private String createdBy;

    @NotNull
    private LocalDate updatedAt;

    @NotNull
    private String updatedBy;
}
