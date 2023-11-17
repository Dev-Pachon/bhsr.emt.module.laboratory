package com.bhsr.emt.laboratory.domain;

import com.bhsr.emt.laboratory.domain.enumeration.ServiceRequestStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ServiceRequest.
 */
@Document(collection = "service_request")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

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
    @Field("diagnostic_reports_ids")
    private String[] diagnosticReportsIds;

    @Field("do_not_perform")
    private Boolean doNotPerform;

    @NotNull(message = "must not be null")
    @Field("service_id")
    private Integer serviceId;

    @Field("created_at")
    private LocalDate createdAt;

    @Field("created_by")
    private User createdBy;

    @Field("updated_at")
    private LocalDate updatedAt;

    @Field("updated_by")
    private User updatedBy;

    @Field("deleted_at")
    private LocalDate deletedAt;
}
