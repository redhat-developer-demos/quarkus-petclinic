package org.quarkus.samples.petclinic.owner;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.qute.TemplateExtension;

@Entity
@Table(name = "types")
public class PetType extends PanacheEntity {

    @Column(name = "name")
    public String name;

    public static PetType parse(String text) {
        return PetType.<PetType> listAll().stream().filter(type -> type.name.equals(text)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("type not found: " + text));
    }

}

@TemplateExtension
class Formatter {

    static String stringify(PetType petType) {
        return petType.name;
    }

    private Formatter() {
    }

}
