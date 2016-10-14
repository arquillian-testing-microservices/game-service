package org.arquillian.microservices.gameservice;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import org.apache.http.entity.ContentType;
import org.arquillian.microservices.gameservice.boundary.AgeCheckerGateway;
import org.arquillian.microservices.gameservice.entity.Pegi;
import org.arquillian.pact.consumer.spi.Pact;
import org.arquillian.pact.consumer.spi.PactVerification;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.arquillian.microservices.gameservice.entity.Pegi.PEGI_16;
import static org.arquillian.microservices.gameservice.entity.Pegi.PEGI_18;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class AgeCheckerConsumer {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
                .addClass(AgeCheckerGateway.class)
                .addClass(Pegi.class)
                .addAsLibraries(
                        Maven.resolver()
                                .resolve("org.apache.deltaspike.core:deltaspike-core-impl:1.7.1")
                                .withTransitivity()
                                .as(JavaArchive.class)
                )
                .addAsWebInfResource(
                        new StringAsset("agechecker.url=http://localhost:9090"),
                        "classes/META-INF/apache-deltaspike.properties"
                );

        return webArchive;
    }

    @Inject
    AgeCheckerGateway ageCheckerGateway;

    @Pact(provider = "age-checker", consumer = "game-service")
    public PactFragment createFragmentWithValidAge(PactDslWithProvider builder) {

        return builder
                .uponReceiving("User older than PEGI description")
                    .path("/checker/")
                    .method("POST")
                    .body(toJson(
                            "{                  " +
                            "  'age': 18,       " +
                            "  'pegi': 'PEGI_16'" +
                            "}")
                    )
                .willRespondWith()
                    .status(200)
                    .body("OK", ContentType.TEXT_PLAIN)

            .toFragment();

    }

    @Pact(provider = "age-checker", consumer = "game-service")
    public PactFragment createFragmentWithInvalidAge(PactDslWithProvider builder) {

        return builder
                .uponReceiving("User younger than PEGI description")
                 .path("/checker/")
                 .method("POST")
                 .body(toJson(
                 "{                  " +
                 "  'age': 16,       " +
                 "  'pegi': 'PEGI_18'" +
                 "}")
                 )
                 .willRespondWith()
                 .status(200)
                 .body("NO_OK", ContentType.TEXT_PLAIN)
                .toFragment();

    }

    @Test
    @PactVerification(value = "age-checker", fragment = "createFragmentWithValidAge")
    public void should_give_access_in_case_of_valid_user_according_its_age() {
        assertThat(ageCheckerGateway.canViewGameInformation(18, PEGI_16), is(true));
    }

    @Test
    @PactVerification(value = "age-checker", fragment = "createFragmentWithInvalidAge")
    public void should_forbid_access_in_case_of_invalid_user_according_its_age() {
        assertThat(ageCheckerGateway.canViewGameInformation(16, PEGI_18), is(false));
    }

    public static String toJson(String jsonString) {
        StringBuilder builder = new StringBuilder();
        boolean single_context = false;
        for (int i = 0; i < jsonString.length(); i++) {
            char ch = jsonString.charAt(i);
            if (ch == '\\') {
                i = i + 1;
                if (i < jsonString.length()) {
                    ch = jsonString.charAt(i);
                    if (!(single_context && ch == '\'')) {
                        // unescape ' inside single quotes
                        builder.append('\\');
                    }
                }
            } else if (ch == '\'') {
                // Turn ' into ", for proper JSON string
                ch = '"';
                single_context = !single_context;
            }
            builder.append(ch);
        }

        return builder.toString();
    }

}
