package org.quarkus.samples.petclinic.owner;

import java.time.LocalDate;

import javax.ws.rs.ext.ParamConverter;

public class LocalDateConverter implements ParamConverter<LocalDate> {

    @Override
    public LocalDate fromString(String value) {
        if ("".equals(value.trim()))
            return null;
        return LocalDate.parse(value);
    }

    @Override
    public String toString(LocalDate value) {
        if (value == null)
            return null;
        return value.toString();
    }

}
