package com.bhsr.emt.laboratory.service.mapper;

import com.bhsr.emt.laboratory.domain.FieldFormat;
import com.bhsr.emt.laboratory.service.dto.FieldFormat.FieldFormatRequestDTO;
import com.bhsr.emt.laboratory.service.dto.FieldFormat.FieldFormatResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FieldFormatMapper {
    FieldFormatResponseDTO FieldFormatToResponseDTO(FieldFormat fieldFormat);

    FieldFormat FieldFormatResponseDTOToFieldFormat(FieldFormatResponseDTO fieldFormatResponseDTO);
    FieldFormat FieldFormatRequestDTOToFieldFormat(FieldFormatRequestDTO fieldFormatRequestDTO);

    FieldFormatRequestDTO FieldFormatToRequestDTO(FieldFormat fieldFormat);
}
