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
}
