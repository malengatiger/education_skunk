package com.boha.skunk.util;

import com.boha.skunk.data.ExamRow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class ExamRowConverter implements AttributeConverter<List<ExamRow>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<ExamRow> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ExamRow> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<ExamRow>>() {});
        } catch (JsonProcessingException e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return null;
        }
    }
}