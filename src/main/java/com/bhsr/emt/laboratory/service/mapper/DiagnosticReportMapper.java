package com.bhsr.emt.laboratory.service.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { DiagnosticReportFormatMapper.class })
public interface DiagnosticReportMapper {
    //    DiagnosticReportResponseDTO DiagnosticReporToResponseDTO(DiagnosticReport diagnosticReportFormat);
    //
    //    DiagnosticReport DiagnosticReportResponseDTOToDiagnosticReport(DiagnosticReportResponseDTO diagnosticReportResponseDTO);
}
