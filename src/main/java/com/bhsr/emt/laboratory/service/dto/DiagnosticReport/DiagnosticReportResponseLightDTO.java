package com.bhsr.emt.laboratory.service.dto.DiagnosticReport;

import com.bhsr.emt.laboratory.domain.CustomField;
import com.bhsr.emt.laboratory.domain.User;
import com.bhsr.emt.laboratory.domain.enumeration.DiagnosticReportStatus;
import com.bhsr.emt.laboratory.service.dto.DiagnosticReportFormat.DiagnosticReportFormatResponseDTO;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticReportResponseLightDTO {

    private String id;

    private DiagnosticReportStatus status;

    private LocalDate createdAt;

    private User createdBy;

    private LocalDate updatedAt;

    private User updatedBy;

    private String format;
}
