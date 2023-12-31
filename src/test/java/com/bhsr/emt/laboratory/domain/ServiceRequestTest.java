package com.bhsr.emt.laboratory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bhsr.emt.laboratory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceRequest.class);
        ServiceRequest serviceRequest1 = new ServiceRequest();
        serviceRequest1.setId("id1");
        ServiceRequest serviceRequest2 = new ServiceRequest();
        serviceRequest2.setId(serviceRequest1.getId());
        assertThat(serviceRequest1).isEqualTo(serviceRequest2);
        serviceRequest2.setId("id2");
        assertThat(serviceRequest1).isNotEqualTo(serviceRequest2);
        serviceRequest1.setId(null);
        assertThat(serviceRequest1).isNotEqualTo(serviceRequest2);
    }
}
