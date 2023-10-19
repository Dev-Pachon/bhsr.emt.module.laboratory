package com.bhsr.emt.laboratory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bhsr.emt.laboratory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValueSetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValueSet.class);
        ValueSet valueSet1 = new ValueSet();
        valueSet1.setId("id1");
        ValueSet valueSet2 = new ValueSet();
        valueSet2.setId(valueSet1.getId());
        assertThat(valueSet1).isEqualTo(valueSet2);
        valueSet2.setId("id2");
        assertThat(valueSet1).isNotEqualTo(valueSet2);
        valueSet1.setId(null);
        assertThat(valueSet1).isNotEqualTo(valueSet2);
    }
}
