package com.gonnect.ranger.policies.ranger;

import com.gonnect.ranger.policies.authorization.AuthMessage;
import com.gonnect.ranger.policies.authorization.Authorizer;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import org.apache.http.HttpHeaders;
import org.apache.ranger.plugin.audit.RangerDefaultAuditHandler;
import org.apache.ranger.plugin.service.RangerBasePlugin;
import org.apache.ranger.plugin.policyengine.RangerAccessResourceImpl;
import org.apache.ranger.plugin.policyengine.RangerAccessRequest;
import org.apache.ranger.plugin.policyengine.RangerAccessRequestImpl;
import org.apache.ranger.plugin.policyengine.RangerAccessResult;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class RangerAuthorizer implements Authorizer {
    private static volatile RangerBasePlugin rangerPlugin = null;
    private WebClient client = null;
    private String delegatorUrl = null;

    @Override
    public void initialize() {

        if (rangerPlugin == null) {
            synchronized (this.getClass()) {
                rangerPlugin = new RangerBasePlugin("rangercustomplugin", "rangercustomplugin");
                rangerPlugin.setResultProcessor(new RangerDefaultAuditHandler(rangerPlugin.getConfig()));

                rangerPlugin.init();
                doCreateWebClient();
            }
        }
    }

    @Override
    public boolean authorize(String resourceName, String accessType, String user, Set<String> userGroups) {

        if (client != null) {
            Mono<Boolean> result = client.post()
                    .uri(delegatorUrl)
                    .body(BodyInserters.fromValue(new AuthMessage(resourceName, accessType, user, userGroups)))
                    .retrieve()
                    .bodyToMono(Boolean.class);
            return result.block();
        }

        // Ranger flow
        RangerAccessResourceImpl resource = new RangerAccessResourceImpl();
        resource.setValue("path", resourceName); // "path" must be a value resource name in servicedef JSON

        RangerAccessRequest request = new RangerAccessRequestImpl(resource, accessType, user, userGroups, null);

        RangerAccessResult result = rangerPlugin.isAccessAllowed(request);

        return result != null && result.getIsAllowed();
    }

    // private
    private void doCreateWebClient() {
        Resource resource = new ClassPathResource("application.properties");
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            String delegatorUrl = props.getProperty("com.gonnect.policy.delegator.controller");

            client = WebClient.builder()
                    .baseUrl(delegatorUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", delegatorUrl))
                    .build();

        } catch (IOException e) {
            client = null;
            // log

        }
    }

}
