package org.quarkus.samples.petclinic.visit;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.FormParam;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "visits")
public class Visit extends PanacheEntity {
    
	@Column(name = "visit_date")
	@FormParam("date")
	public LocalDate date;

	@NotEmpty
	@Column(name = "description")
	@FormParam("description")
	public String description;

	@Column(name = "pet_id")
	public Long petId;

	public static Collection<Visit> findByPetId(Long petId) {
        return Visit.list("petId", petId);
    }
}
