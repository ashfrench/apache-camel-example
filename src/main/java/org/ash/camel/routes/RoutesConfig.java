package org.ash.camel.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicyFactory;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfig {

    @Bean
    public CamelContext getCamelContext(){
        CamelContext camelContext = new DefaultCamelContext();
        camelContext.addRoutePolicyFactory(new MetricsRoutePolicyFactory());

        return camelContext;
    }
}
