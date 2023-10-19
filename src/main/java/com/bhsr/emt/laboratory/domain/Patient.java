package com.bhsr.emt.laboratory.domain;

import com.bhsr.emt.laboratory.domain.enumeration.AdministrativeGender;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Patient.
 */
@Document(collection = "patient")
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Patient id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Patient active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public AdministrativeGender getGender() {
        return this.gender;
    }

    public Patient gender(AdministrativeGender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(AdministrativeGender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Patient birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            ", gender='" + getGender() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            "}";
    }
}
