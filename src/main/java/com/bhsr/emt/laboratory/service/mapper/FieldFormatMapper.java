package com.bhsr.emt.laboratory.service.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import com.bhsr.emt.laboratory.domain.FieldFormat;
import com.bhsr.emt.laboratory.domain.ValueSet;
import com.bhsr.emt.laboratory.service.dto.FieldFormat.FieldFormatRequestDTO;
import com.bhsr.emt.laboratory.service.dto.FieldFormat.FieldFormatResponseDTO;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FieldFormatMapper {


    @Mapping(target = "valueSet", source = "valueSet.id")
    FieldFormatResponseDTO FieldFormatToResponseDTO(FieldFormat fieldFormat);
    
    @Mapping(target = "valueSet", source = "valueSet.id")
    FieldFormat FieldFormatResponseDTOToFieldFormat(FieldFormatResponseDTO fieldFormatResponseDTO);
    @Mapping(target = "valueSet", source = "valueSet.id")
    FieldFormat FieldFormatRequestDTOToFieldFormat(FieldFormatRequestDTO fieldFormatRequestDTO);

    @Mapping(target = "valueSet.id", source = "valueSet")
    FieldFormatRequestDTO FieldFormatToRequestDTO(FieldFormat fieldFormat);

    default ValueSet valueSetToId(String id) {
        return ValueSet.builder().id(id).build();
    }

    default String valueSetToId(ValueSet valueSet) {
        return valueSet.getId();
    }
}