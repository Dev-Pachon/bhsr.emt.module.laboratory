package com.bhsr.emt.laboratory.service.dto.DiagnosticReportFormat;

import com.bhsr.emt.laboratory.service.dto.FieldFormat.FieldFormatResponseDTO;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticReportFormatResponseDTO {
    private String id;

    @NotNull
    private String name;

    @NotNull
    private Set<FieldFormatResponseDTO> fields;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
