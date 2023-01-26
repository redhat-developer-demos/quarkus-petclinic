package org.quarkus.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import org.quarkus.samples.petclinic.visit.Visit;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "pets")
public class Pet extends PanacheEntity {
    
	@Column(name = "name")
	@NotEmpty
    public String name;

	@Column(name = "birth_date")
	public LocalDate birthDate;

	@ManyToOne
	@JoinColumn(name = "type_id")
	public PetType type;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	public Owner owner;

	@Transient
	public Set<Visit> visits = new LinkedHashSet<>();

	public Pet attach() {
        return getEntityManager().merge(this);
    }

    protected Set<Visit> getVisitsInternal() {
		if (this.visits == null) {
			this.visits = new HashSet<>();
		}
		return this.visits;
	}

	protected void setVisitsInternal(Collection<Visit> visits) {
		this.visits = new LinkedHashSet<>(visits);
	}

	public List<Visit> getSortedVisits() {
		List<Visit> sortedVisits = new ArrayList<>(getVisitsInternal());
		Collections.sort(sortedVisits, new VisitComparator());
		return Collections.unmodifiableList(sortedVisits);
	}

	public void addVisit(Visit visit) {
		getVisitsInternal().add(visit);
		visit.petId = this.id;
    }
    
    public boolean isNew() {
		return this.id == null;
	}

	@Override
	public String toString() {
		return "Pet [birthDate=" + birthDate + ", name=" + name + ", type=" + type + "]";
	}

}
