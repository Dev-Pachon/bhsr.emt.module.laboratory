package com.bhsr.emt.laboratory.service.mapper;


import org.mapstruct.Mapper;

import com.bhsr.emt.laboratory.domain.DiagnosticReportFormat;
import com.bhsr.emt.laboratory.service.dto.DiagnosticReportFormat.DiagnosticReportFormatRequestDTO;
import com.bhsr.emt.laboratory.service.dto.DiagnosticReportFormat.DiagnosticReportFormatResponseDTO;

/**
 * Mapper for the entity {@link DiagnosticReportFormat} which uses {} and its DTO called {@link DiagnosticReportFormatRequestDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */



@Mapper(componentModel = "spring", uses = {FieldFormatMapper.class})
public interface DiagnosticReportFormatRequestMapper {
    
    DiagnosticReportFormatResponseDTO DiagnosticReportFormatToResponseDTO(DiagnosticReportFormat diagnosticReportFormat);

    DiagnosticReportFormat DiagnosticReportFormatResponseDTOToDiagnosticReportFormat(DiagnosticReportFormatResponseDTO diagnosticReportFormatResponseDTO);

    DiagnosticReportFormat DiagnosticReportFormatRequestDTOToDiagnosticReportFormat(DiagnosticReportFormatRequestDTO diagnosticReportFormatRequestDTO);

    DiagnosticReportFormatRequestDTO DiagnosticReportFormatToRequestDTO(DiagnosticReportFormat diagnosticReportFormat);
}
