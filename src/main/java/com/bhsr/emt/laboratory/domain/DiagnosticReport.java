package com.bhsr.emt.laboratory.domain;

import com.bhsr.emt.laboratory.domain.enumeration.DiagnosticReportStatus;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A DiagnosticReport.
 */
@Document(collection = "diagnostic_report")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DiagnosticReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("status")
    private DiagnosticReportStatus status;

    @NotNull(message = "must not be null")
    @Field("created_at")
    private LocalDate createdAt;

    @NotNull(message = "must not be null")
    @Field("created_by")
    private String createdBy;

    @NotNull(message = "must not be null")
    @Field("updated_at")
    private LocalDate updatedAt;

    @NotNull(message = "must not be null")
    @Field("updated_by")
    private String updatedBy;

    @Field("deleted_at")
    private LocalDate deletedAt;

    @Field("subject")
    private Patient subject;

    @Field("format")
    private DiagnosticReportFormat format;

    @Field("basedOn")
    private ServiceRequest basedOn;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public DiagnosticReport id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DiagnosticReportStatus getStatus() {
        return this.status;
    }

    public DiagnosticReport status(DiagnosticReportStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DiagnosticReportStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public DiagnosticReport createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public DiagnosticReport createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public DiagnosticReport updatedAt(LocalDate updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public DiagnosticReport updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getDeletedAt() {
        return this.deletedAt;
    }

    public DiagnosticReport deletedAt(LocalDate deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(LocalDate deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Patient getSubject() {
        return this.subject;
    }

    public void setSubject(Patient patient) {
        this.subject = patient;
    }

    public DiagnosticReport subject(Patient patient) {
        this.setSubject(patient);
        return this;
    }

    public DiagnosticReportFormat getFormat() {
        return this.format;
    }

    public void setFormat(DiagnosticReportFormat diagnosticReportFormat) {
        this.format = diagnosticReportFormat;
    }

    public DiagnosticReport format(DiagnosticReportFormat diagnosticReportFormat) {
        this.setFormat(diagnosticReportFormat);
        return this;
    }

    public ServiceRequest getBasedOn() {
        return this.basedOn;
    }

    public void setBasedOn(ServiceRequest serviceRequest) {
        this.basedOn = serviceRequest;
    }

    public DiagnosticReport basedOn(ServiceRequest serviceRequest) {
        this.setBasedOn(serviceRequest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiagnosticReport)) {
            return false;
        }
        return id != null && id.equals(((DiagnosticReport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DiagnosticReport{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}
