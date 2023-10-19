package com.bhsr.emt.laboratory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bhsr.emt.laboratory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DiagnosticReportFormatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiagnosticReportFormat.class);
        DiagnosticReportFormat diagnosticReportFormat1 = new DiagnosticReportFormat();
        diagnosticReportFormat1.setId("id1");
        DiagnosticReportFormat diagnosticReportFormat2 = new DiagnosticReportFormat();
        diagnosticReportFormat2.setId(diagnosticReportFormat1.getId());
        assertThat(diagnosticReportFormat1).isEqualTo(diagnosticReportFormat2);
        diagnosticReportFormat2.setId("id2");
        assertThat(diagnosticReportFormat1).isNotEqualTo(diagnosticReportFormat2);
        diagnosticReportFormat1.setId(null);
        assertThat(diagnosticReportFormat1).isNotEqualTo(diagnosticReportFormat2);
    }
}
