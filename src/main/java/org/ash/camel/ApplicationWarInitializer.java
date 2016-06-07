package org.ash.camel;

import org.apache.camel.spring.boot.FatJarRouter;
import org.apache.camel.spring.boot.FatWarInitializer;

public class ApplicationWarInitializer extends FatWarInitializer {
    protected Class<? extends FatJarRouter> routerClass() {
        return Application.class;
    }
}
