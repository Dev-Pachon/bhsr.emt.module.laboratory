package com.bhsr.emt.laboratory.domain;

import com.bhsr.emt.laboratory.domain.enumeration.AdministrativeGender;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Patient.
 */
@Document(collection = "patient")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("active")
    private Boolean active;

    @NotNull(message = "must not be null")
    @Field("gender")
    private AdministrativeGender gender;

    @NotNull(message = "must not be null")
    @Field("birth_date")
    private LocalDate birthDate;

    @NotNull(message = "must not be null")
    @Field("address")
    private Address address;

    @NotNull(message = "must not be null")
    @Field("contact")
    private Contact contact;

    @NotNull(message = "must not be null")
    @Field("humanName")
    private HumanName humanName;
}
