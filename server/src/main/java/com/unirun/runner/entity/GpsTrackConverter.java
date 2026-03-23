package com.unirun.runner.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Collections;
import java.util.List;

@Converter
public class GpsTrackConverter implements AttributeConverter<List<GpsPoint>, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public String convertToDatabaseColumn(List<GpsPoint> attribute) {
        if (attribute == null) {
            return "[]";
        }
        try {
            return MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize GPS track", e);
        }
    }

    @Override
    public List<GpsPoint> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return MAPPER.readValue(dbData, MAPPER.getTypeFactory().constructCollectionType(List.class, GpsPoint.class));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to deserialize GPS track", e);
        }
    }
}
