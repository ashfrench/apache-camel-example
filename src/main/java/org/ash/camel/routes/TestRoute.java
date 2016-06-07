package org.ash.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TestRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer://foo?period=5000&daemon=false")
                .setBody().constant("ASH WAS HERE!")
                .log(">>> ${body}")
                .to("mock:result");
    }
}
