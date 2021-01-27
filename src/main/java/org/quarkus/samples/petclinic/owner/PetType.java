package org.quarkus.samples.petclinic.owner;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.qute.TemplateExtension;

@Entity
@Table(name = "types")
public class PetType extends PanacheEntity {

	@Column(name = "name")
	public String name;

	public static PetType parse(String text) {
		Collection<PetType> findPetTypes = PetType.listAll();
		for (PetType type : findPetTypes) {
			if (type.name.equals(text)) {
				return type;
			}
		}
		throw new IllegalArgumentException("type not found: " + text);
	}

}

@TemplateExtension
class Formatter {
	static String stringify(PetType petType) {
		return petType.name;
	}

}
