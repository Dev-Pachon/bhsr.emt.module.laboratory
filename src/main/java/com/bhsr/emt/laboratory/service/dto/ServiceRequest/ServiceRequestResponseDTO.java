package com.bhsr.emt.laboratory.service.dto.ServiceRequest;

import com.bhsr.emt.laboratory.domain.Patient;
import com.bhsr.emt.laboratory.domain.User;
import com.bhsr.emt.laboratory.domain.enumeration.ServiceRequestStatus;
import com.bhsr.emt.laboratory.service.dto.DiagnosticReport.DiagnosticReportResponseLightDTO;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestResponseDTO {

    private String id;
    private Patient subject;
    private ServiceRequestStatus status;
    private String category;
    private String priority;
    private Set<DiagnosticReportResponseLightDTO> diagnosticReports;
    private Boolean doNotPerform;
    private Integer serviceId;
    private LocalDate createdAt;
    private User createdBy;
    private LocalDate updatedAt;
    private User updatedBy;
}
