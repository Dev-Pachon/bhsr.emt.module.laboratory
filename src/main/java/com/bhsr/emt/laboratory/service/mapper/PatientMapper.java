package com.bhsr.emt.laboratory.service.mapper;

import com.bhsr.emt.laboratory.domain.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class PatientMapper {

    Patient PatientFromString(String patientString) {
        return Patient.builder().id(patientString).build();
    }

    String PatientToString(Patient patient) {
        return patient.getId();
    }
}
