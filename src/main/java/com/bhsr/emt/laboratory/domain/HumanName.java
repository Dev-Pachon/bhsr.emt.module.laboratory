package com.bhsr.emt.laboratory.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HumanName {

    private String text;
    private String given;
    private String family;
}
