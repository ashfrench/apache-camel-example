package org.ash.camel.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder{

    @Autowired
    public MyRoute(CamelContext camelContext){
        super(camelContext);
    }

    @Override
    public void configure() throws Exception {
        from("timer:foo").setBody().constant("Ash Rules The World")
                .setHeader("Awesome Header").constant("My header")
                .to("log:bar");
    }
}
