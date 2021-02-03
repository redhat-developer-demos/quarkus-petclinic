package org.quarkus.samples.petclinic.system;

import io.quarkus.qute.i18n.Message;
import io.quarkus.qute.i18n.MessageBundle;

@MessageBundle
public interface AppMessages {
    
    @Message("Find Owner")
    String find_owner();

    @Message("Last Name")
    String last_name();

    @Message("Add Owner")
    String add_owner();

    @Message("Find Owners")
    String find_owners();
}
