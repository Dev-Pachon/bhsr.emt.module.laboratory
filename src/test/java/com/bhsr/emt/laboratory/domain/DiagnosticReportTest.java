package com.bhsr.emt.laboratory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bhsr.emt.laboratory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DiagnosticReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiagnosticReport.class);
        DiagnosticReport diagnosticReport1 = new DiagnosticReport();
        diagnosticReport1.setId("id1");
        DiagnosticReport diagnosticReport2 = new DiagnosticReport();
        diagnosticReport2.setId(diagnosticReport1.getId());
        assertThat(diagnosticReport1).isEqualTo(diagnosticReport2);
        diagnosticReport2.setId("id2");
        assertThat(diagnosticReport1).isNotEqualTo(diagnosticReport2);
        diagnosticReport1.setId(null);
        assertThat(diagnosticReport1).isNotEqualTo(diagnosticReport2);
    }
}
