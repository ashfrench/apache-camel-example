package org.ash.camel;

import org.eclipse.jetty.jaas.spi.AbstractLoginModule;
import org.eclipse.jetty.jaas.spi.UserInfo;
import org.eclipse.jetty.security.PropertyUserStore;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sample PropertyFileLoginModule.
 */
public class PropertyFileLoginModule extends AbstractLoginModule {
    public static final String DEFAULT_FILENAME = "realm.properties";

    private static final Logger LOG = LoggerFactory.getLogger(PropertyFileLoginModule.class);
    private static ConcurrentHashMap<String, PropertyUserStore> PROPERTY_USERSTORES = new ConcurrentHashMap<>();

    private int refreshInterval = 0;
    private String filename = null;

    @Override
    public void initialize(final Subject subject, final CallbackHandler callbackHandler,
                           final Map<String, ?> sharedState, final Map<String, ?> options) {
        super.initialize(subject, callbackHandler, sharedState, options);
        setupPropertyUserStore(options);
    }

    private void setupPropertyUserStore(final Map<String, ?> options) {
        parseConfig(options);

        if (PROPERTY_USERSTORES.get(filename) == null) {
            final PropertyUserStore propertyUserStore = new PropertyUserStore();
            propertyUserStore.setConfig(filename);
            propertyUserStore.setRefreshInterval(refreshInterval);

            final PropertyUserStore prev = PROPERTY_USERSTORES.putIfAbsent(filename, propertyUserStore);
            if (prev == null) {
                LOG.info("setupPropertyUserStore: Starting new PropertyUserStore. PropertiesFile: " + filename
                        + " refreshInterval: " + refreshInterval);

                try {
                    propertyUserStore.start();
                } catch (Exception e) {
                    LOG.warn("Exception while starting propertyUserStore: ", e);
                }
            }
        }
    }

    private void parseConfig(final Map<String, ?> options) {
        String tmp = (String) options.get("file");
        filename = (tmp == null ? DEFAULT_FILENAME : tmp);
        filename = System.getProperty("login.file", filename);
        tmp = (String) options.get("refreshInterval");
        refreshInterval = (tmp == null ? refreshInterval : Integer.parseInt(tmp));
    }

    @Override
    public UserInfo getUserInfo(final String userName) {
        final PropertyUserStore propertyUserStore = PROPERTY_USERSTORES.get(filename);
        if (propertyUserStore == null) {
            throw new IllegalStateException("PropertyUserStore should never be null here!");
        }

        LOG.trace("Checking PropertyUserStore " + filename + " for " + userName);
        final UserIdentity userIdentity = propertyUserStore.getUserIdentity(userName);
        if (userIdentity == null) {
            return null;
        }

        final Set<Principal> principals = userIdentity.getSubject().getPrincipals();
        final List<String> roles = new ArrayList<>();
        for (final Principal principal : principals) {
            roles.add(principal.getName());
        }

        final Credential credential = (Credential) userIdentity.getSubject().getPrivateCredentials().iterator().next();
        LOG.trace("Found: " + userName + " in PropertyUserStore " + filename);
        return new UserInfo(userName, credential, roles);
    }

}
