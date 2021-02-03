package org.quarkus.samples.petclinic.system;

import io.quarkus.qute.i18n.Localized;
import io.quarkus.qute.i18n.Message;

@Localized("es")
public interface SpanishAppMessages extends AppMessages {
    
    @Override
    @Message("Buscar Propietario")
    String find_owner();

    @Override
    @Message("Apellido")
    String last_name();

    @Override
    @Message("Añadir Propietario")
    String add_owner();

    @Override
    @Message("Buscar Propietarios")
    String find_owners();

}
