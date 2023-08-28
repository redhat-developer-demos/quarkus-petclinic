package org.quarkus.samples.petclinic.login;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User  {
@Id
    public String email;
    public String password;


}
