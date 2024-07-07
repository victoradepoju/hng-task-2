package com.victor.hng_task2.dto.serializer;

import com.victor.hng_task2.dto.DataResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class DataResponseSerializer extends StdSerializer<DataResponse> {

    public DataResponseSerializer() {
        this(null);
    }

    public DataResponseSerializer(Class<DataResponse> t) {
        super(t);
    }

    @Override
    public void serialize(DataResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        if (value.accessToken() != null) {
            gen.writeStringField("accessToken", value.accessToken());
        }
        if (value.user() != null) {
            gen.writeObjectField("user", value.user());
        }
        if (value.userId() != null) {
            gen.writeStringField("userId", value.userId());
        }
        if (value.firstName() != null) {
            gen.writeStringField("firstName", value.firstName());
        }
        if (value.lastName() != null) {
            gen.writeStringField("lastName", value.lastName());
        }
        if (value.email() != null) {
            gen.writeStringField("email", value.email());
        }
        if (value.phone() != null && value.phone().isExplicitlySet()) {
            gen.writeStringField("phone", value.phone().getValue());
        }
        if (value.organisations() != null && !value.organisations().isEmpty()) {
            gen.writeObjectField("organisations", value.organisations());
        }
        if (value.orgId() != null) {
            gen.writeStringField("orgId", value.orgId());
        }
        if (value.name() != null) {
            gen.writeStringField("name", value.name());
        }
        if (value.description() != null && value.description().isExplicitlySet()) {
            gen.writeStringField("description", value.description().getValue());
        }

        gen.writeEndObject();
    }
}
