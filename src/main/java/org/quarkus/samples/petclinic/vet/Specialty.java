package org.quarkus.samples.petclinic.vet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "specialties")
public class Specialty extends PanacheEntity {

    @Column(name = "name")
    public String name;

    @Override
    public String toString() {
        return "Specialty [name=" + name + "]";
    }

}
