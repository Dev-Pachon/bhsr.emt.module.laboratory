package com.bhsr.emt.laboratory.service.dto.ServiceRequest;

import com.bhsr.emt.laboratory.domain.enumeration.ServiceRequestStatus;
import com.bhsr.emt.laboratory.service.dto.DiagnosticReportFormat.DiagnosticReportFormatRequestDTO;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestRequestDTO {

    private ServiceRequestStatus status;
    private String category;
    private String priority;
    private Set<DiagnosticReportFormatRequestDTO> diagnosticReportsFormats;
    private String subject;
}
