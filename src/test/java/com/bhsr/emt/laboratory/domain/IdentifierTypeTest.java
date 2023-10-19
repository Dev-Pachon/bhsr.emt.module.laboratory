package com.bhsr.emt.laboratory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bhsr.emt.laboratory.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class IdentifierTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdentifierType.class);
        IdentifierType identifierType1 = new IdentifierType();
        identifierType1.setId(UUID.randomUUID());
        IdentifierType identifierType2 = new IdentifierType();
        identifierType2.setId(identifierType1.getId());
        assertThat(identifierType1).isEqualTo(identifierType2);
        identifierType2.setId(UUID.randomUUID());
        assertThat(identifierType1).isNotEqualTo(identifierType2);
        identifierType1.setId(null);
        assertThat(identifierType1).isNotEqualTo(identifierType2);
    }
}
