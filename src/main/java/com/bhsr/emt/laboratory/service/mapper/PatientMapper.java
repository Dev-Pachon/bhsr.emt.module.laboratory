package com.bhsr.emt.laboratory.service.mapper;

import com.bhsr.emt.laboratory.domain.HumanName;
import com.bhsr.emt.laboratory.domain.Identifier;
import com.bhsr.emt.laboratory.domain.IdentifierType;
import com.bhsr.emt.laboratory.domain.Patient;
import com.bhsr.emt.laboratory.domain.enumeration.AdministrativeGender;
import com.bhsr.emt.laboratory.service.dto.Patient.PatientServiceResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    default Patient PatientFromString(String patientString) {
        return Patient.builder().id(patientString).build();
    }

    default String PatientToString(Patient patient) {
        return patient.getId();
    }

    default Patient PatientServiceToPatient(PatientServiceResponse patientServiceResponse) {
        if (patientServiceResponse == null) {
            return null;
        }

        Patient.PatientBuilder patientResponse = Patient.builder();
        HumanName.HumanNameBuilder humanName = HumanName.builder();
        Identifier.IdentifierBuilder identifier = Identifier.builder();
        AtomicReference<String> Name = new AtomicReference<>("");
        AtomicReference<String> lastName = new AtomicReference<>("");
        AtomicReference<String> secondLastName = new AtomicReference<>("");
        AtomicReference<String> middleName = new AtomicReference<>("");

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        patientServiceResponse
            .getFieldsList()
            .forEach(field -> {
                switch (field.getName()) {
                    case "idType":
                        identifier.type(IdentifierType.builder().id(UUID.randomUUID()).name(field.getValue()).build());
                        break;
                    case "idNumber":
                        identifier.value(field.getValue());
                        break;
                    case "birthDate":
                        patientResponse.birthDate(LocalDate.parse(field.getValue(), inputFormatter));
                        break;
                    case "gender":
                        patientResponse.gender(AdministrativeGender.valueOf(field.getValue()));
                        break;
                    case "Name":
                        Name.set(field.getValue());
                        break;
                    case "lastName":
                        lastName.set(field.getValue());
                        break;
                    case "middleName":
                        middleName.set(field.getValue());
                        break;
                    case "secondLastName":
                        secondLastName.set(field.getValue());
                        break;
                }
            });

        patientResponse.name(
            humanName
                .given(String.join(" ", Name.get(), middleName.get()))
                .family(String.join(" ", lastName.get(), secondLastName.get()))
                .text(String.join(" ", Name.get(), middleName.get(), lastName.get(), secondLastName.get()))
                .build()
        );

        patientResponse.identifier(identifier.build());
        patientResponse.active(true);
        patientResponse.id(patientServiceResponse.getId());
        return patientResponse.build();
    }
}
