package com.bhsr.emt.laboratory.service.mapper;

import com.bhsr.emt.laboratory.domain.DiagnosticReport;
import com.bhsr.emt.laboratory.service.dto.DiagnosticReport.DiagnosticReportResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { DiagnosticReportFormatMapper.class, PatientMapper.class })
public interface DiagnosticReportMapper {
    DiagnosticReportResponseDTO DiagnosticReporToResponseDTO(DiagnosticReport diagnosticReportFormat);

    DiagnosticReport DiagnosticReportResponseDTOToDiagnosticReport(DiagnosticReportResponseDTO diagnosticReportResponseDTO);
}
