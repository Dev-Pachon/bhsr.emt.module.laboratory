package com.bhsr.emt.laboratory.domain;

import com.bhsr.emt.laboratory.domain.enumeration.ServiceRequestStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ServiceRequest.
 */
@Document(collection = "service_request")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("status")
    private ServiceRequestStatus status;

    @NotNull(message = "must not be null")
    @Field("category")
    private String category;

    @NotNull(message = "must not be null")
    @Field("priority")
    private String priority;

    @NotNull(message = "must not be null")
    @Field("code")
    private UUID code;

    @Field("do_not_perform")
    private Boolean doNotPerform;

    @NotNull(message = "must not be null")
    @Field("service_id")
    private Integer serviceId;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ServiceRequest id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ServiceRequestStatus getStatus() {
        return this.status;
    }

    public ServiceRequest status(ServiceRequestStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ServiceRequestStatus status) {
        this.status = status;
    }

    public String getCategory() {
        return this.category;
    }

    public ServiceRequest category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return this.priority;
    }

    public ServiceRequest priority(String priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public UUID getCode() {
        return this.code;
    }

    public ServiceRequest code(UUID code) {
        this.setCode(code);
        return this;
    }

    public void setCode(UUID code) {
        this.code = code;
    }

    public Boolean getDoNotPerform() {
        return this.doNotPerform;
    }

    public ServiceRequest doNotPerform(Boolean doNotPerform) {
        this.setDoNotPerform(doNotPerform);
        return this;
    }

    public void setDoNotPerform(Boolean doNotPerform) {
        this.doNotPerform = doNotPerform;
    }

    public Integer getServiceId() {
        return this.serviceId;
    }

    public ServiceRequest serviceId(Integer serviceId) {
        this.setServiceId(serviceId);
        return this;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public ServiceRequest createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ServiceRequest createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public ServiceRequest updatedAt(LocalDate updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public ServiceRequest updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getDeletedAt() {
        return this.deletedAt;
    }

    public ServiceRequest deletedAt(LocalDate deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(LocalDate deletedAt) {
        this.deletedAt = deletedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceRequest)) {
            return false;
        }
        return id != null && id.equals(((ServiceRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceRequest{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", category='" + getCategory() + "'" +
            ", priority='" + getPriority() + "'" +
            ", code='" + getCode() + "'" +
            ", doNotPerform='" + getDoNotPerform() + "'" +
            ", serviceId=" + getServiceId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}
