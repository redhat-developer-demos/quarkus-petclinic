package org.quarkus.samples.petclinic;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.quarkus.samples.petclinic.system.ErrorExceptionMapper.ERROR_HEADER;

import org.junit.jupiter.api.Test;
import org.quarkus.samples.petclinic.vet.VetResource;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(VetResource.class)
public class VetsResourceTest {

    @Test
    void vertPage() {
        when().get("vets.html").then().statusCode(200).header(ERROR_HEADER, is(nullValue()));
    }

}
