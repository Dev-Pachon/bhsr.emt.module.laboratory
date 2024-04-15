package com.bhsr.emt.laboratory.service.mapper;

import com.bhsr.emt.laboratory.domain.DiagnosticReportFormat;
import com.bhsr.emt.laboratory.service.dto.DiagnosticReportFormat.DiagnosticReportFormatRequestDTO;
import com.bhsr.emt.laboratory.service.dto.DiagnosticReportFormat.DiagnosticReportFormatResponseDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link DiagnosticReportFormat} and its DTO called {@link DiagnosticReportFormatRequestDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */

@Mapper(componentModel = "spring", uses = { FieldFormatMapper.class })
public interface DiagnosticReportFormatMapper {
    DiagnosticReportFormatResponseDTO DiagnosticReportFormatToResponseDTO(DiagnosticReportFormat diagnosticReportFormat);

    DiagnosticReportFormat DiagnosticReportFormatResponseDTOToDiagnosticReportFormat(
        DiagnosticReportFormatResponseDTO diagnosticReportFormatResponseDTO
    );

    DiagnosticReportFormat DiagnosticReportFormatRequestDTOToDiagnosticReportFormat(
        DiagnosticReportFormatRequestDTO diagnosticReportFormatRequestDTO
    );

    DiagnosticReportFormatRequestDTO DiagnosticReportFormatToRequestDTO(DiagnosticReportFormat diagnosticReportFormat);
}
