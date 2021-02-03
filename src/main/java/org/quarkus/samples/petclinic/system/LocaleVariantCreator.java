package org.quarkus.samples.petclinic.system;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.ws.rs.core.MediaType;

import io.quarkus.qute.Variant;

public class LocaleVariantCreator {
    
    public static Variant locale(Locale l) {
        return new Variant(Locale.getDefault(), MediaType.TEXT_PLAIN, StandardCharsets.UTF_8.name());
    }

}
