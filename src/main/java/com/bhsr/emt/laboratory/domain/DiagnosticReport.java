package com.bhsr.emt.laboratory.domain;

import com.bhsr.emt.laboratory.domain.enumeration.DiagnosticReportStatus;
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
 * A DiagnosticReport.
 */
@Document(collection = "diagnostic_report")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticReport implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private User createdBy;

    @NotNull(message = "must not be null")
    @Field("updated_at")
    private LocalDate updatedAt;

    @NotNull(message = "must not be null")
    @Field("updated_by")
    private User updatedBy;

    @Field("deleted_at")
    private LocalDate deletedAt;

    @NotNull(message = "must not be null")
    @Field("subject")
    private String subject;

    @Field("format")
    private DiagnosticReportFormat format;

    @Field("fields")
    private CustomField[] fields;

    @Field("basedOn")
    private ServiceRequest basedOn;
}
