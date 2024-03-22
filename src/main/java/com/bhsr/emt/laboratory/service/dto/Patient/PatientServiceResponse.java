package com.bhsr.emt.laboratory.service.dto.Patient;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientServiceResponse {

    private String id;

    private String created;

    private String role;

    private List<PatientField> fieldsList;
}
