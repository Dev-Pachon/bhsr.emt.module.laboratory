package com.bhsr.emt.laboratory.domain;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    private String id;

    @NotNull(message = "must not be null")
    private String text;

    private String line;

    @NotNull(message = "must not be null")
    private String city;

    private String district;

    @NotNull
    private String state;

    @NotNull(message = "must not be null")
    private String country;
}
