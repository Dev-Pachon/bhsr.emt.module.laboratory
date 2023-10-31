package com.bhsr.emt.laboratory.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A DiagnosticReportFormat.
 */
@Document(collection = "diagnostic_report_format")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticReportFormat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    private String name;

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

    @Field("FieldFormat")
    private Set<FieldFormat> fieldFormats;
}
