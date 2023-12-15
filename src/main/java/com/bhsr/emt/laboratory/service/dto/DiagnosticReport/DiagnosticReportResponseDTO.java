package com.bhsr.emt.laboratory.service.dto.DiagnosticReport;

import com.bhsr.emt.laboratory.domain.CustomField;
import com.bhsr.emt.laboratory.domain.DiagnosticReportFormat;
import com.bhsr.emt.laboratory.domain.Patient;
import com.bhsr.emt.laboratory.domain.User;
import com.bhsr.emt.laboratory.domain.enumeration.DiagnosticReportStatus;
import com.bhsr.emt.laboratory.service.dto.DiagnosticReportFormat.DiagnosticReportFormatResponseDTO;
import com.bhsr.emt.laboratory.service.dto.ServiceRequest.ServiceRequestResponseDTO;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticReportResponseDTO {

    private String id;

    private DiagnosticReportStatus status;

    private LocalDate createdAt;

    private User createdBy;

    private LocalDate updatedAt;

    private User updatedBy;

    private LocalDate deletedAt;

    private Patient subject;

    private DiagnosticReportFormatResponseDTO format;

    private CustomField[] fields;

    private ServiceRequestResponseDTO basedOn;
}
