package com.example.newappointmentsystem.converters;

import com.example.newappointmentsystem.models.Appointment.Status;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, String> {

    @Override
    public String convertToDatabaseColumn(Status status) {
        if (status == null) {
            return null;
        }
        // Convert enum to lowercase string for the database
        return status.name().toLowerCase();
    }

    @Override
    public Status convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        // Convert database string to uppercase enum constant
        return Status.valueOf(dbData.toUpperCase());
    }
}
