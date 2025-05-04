package com.denissudak.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jooq.lambda.Unchecked;
import org.jooq.lambda.fi.util.function.CheckedSupplier;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JsonSerializer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private JsonSerializer() {
    }

    public static <T> String toJson(T data) {
        return Unchecked.supplier(() -> OBJECT_MAPPER.writeValueAsString(data)).get();
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        return Unchecked.supplier(() -> OBJECT_MAPPER.readValue(json, clazz)).get();
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        return isNotBlank(json) ? Unchecked.supplier((CheckedSupplier<List<T>>) () ->
                OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz))).get() : emptyList();
    }
}
