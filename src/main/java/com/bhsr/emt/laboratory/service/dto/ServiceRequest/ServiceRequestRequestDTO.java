package com.bhsr.emt.laboratory.service.dto.ServiceRequest;

import com.bhsr.emt.laboratory.domain.enumeration.ServiceRequestStatus;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestRequestDTO {

    private ServiceRequestStatus status;
    private String category;
    private String priority;
    private String[] diagnosticReportsIds;
    private String patientId;
}
