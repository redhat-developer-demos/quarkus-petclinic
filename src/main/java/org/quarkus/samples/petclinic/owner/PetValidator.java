package org.quarkus.samples.petclinic.owner;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PetValidator implements ConstraintValidator<ValidPet, Pet> {

    @Override
    public boolean isValid(Pet value, ConstraintValidatorContext context) {

        String name = value.name;

        if (name == null || name.length() == 0) {
            context.buildConstraintViolationWithTemplate("name {required}");
            return false;
        }

        if (value.isNew() && value.type == null) {
            context.buildConstraintViolationWithTemplate("type {required}");
            return false;
        }

        if (value.birthDate== null) {
            context.buildConstraintViolationWithTemplate("birthDate {required}");
        }

        return true;
    }

}
