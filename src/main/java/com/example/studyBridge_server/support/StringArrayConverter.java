package com.example.studyBridge_server.support;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Converter
public class StringArrayConverter implements AttributeConverter<Optional<List<String>>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(Optional<List<String>> attribute) {
        if (attribute != null) {
            return attribute.stream().map(String::valueOf).collect(Collectors.joining(SPLIT_CHAR));
        } else {
            return "";
        }
    }

    @Override
    public Optional<List<String>> convertToEntityAttribute(String dbData) {
        if (dbData.equals("")) {
            return Optional.of(new ArrayList<>());
        } else {
            return Optional.of(Arrays.stream(dbData.split(SPLIT_CHAR))
                    .map(String::toString)
                    .collect(Collectors.toList()));
        }
    }
}
