package com.example.newappointmentsystem.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)  // Automatically applies the converter wherever LocalDateTime is used
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, String> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Convert LocalDateTime to String for storage in the database
    @Override
    public String convertToDatabaseColumn(LocalDateTime localDateTime) {
        return (localDateTime == null) ? null : localDateTime.format(formatter);
    }

    // Convert String from the database back to LocalDateTime
    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : LocalDateTime.parse(dbData, formatter);
    }
}
