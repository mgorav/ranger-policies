package com.gonnect.ranger.policies.delegator;

import com.gonnect.ranger.policies.authorization.AuthMessage;
import com.gonnect.ranger.policies.authorization.Authorizer;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Set;

@RestController
public class CustomPolicyDelegator implements Authorizer {
    @Value("${com.gonnect.custom.authorizer}")
    private String customAuthorizer;
    @Autowired
    private WebClient client;

    public CustomPolicyDelegator() {
        initialize();
    }

    @Override
    public void initialize() {
        // Load your company policies APIs url from DB/configurations
    }


    @PostMapping("/authorize")
    @Override
    public boolean authorize(String resourceName, String accessType, String user, Set<String> userGroups) {


        return rangerAuthorize(new AuthMessage(resourceName, accessType, user, userGroups));
    }

    @PostMapping("/authorize")
    public boolean rangerAuthorize(@RequestBody AuthMessage authMessage) {
        if (client != null) {
            // POST
            Mono<Boolean> result = client
                    .post()
                    .uri(customAuthorizer)
                    .body(BodyInserters.fromValue(authMessage))
                    .retrieve()
                    .bodyToMono(Boolean.class);
            return result.block();
        }
        return false;
    }

    @PostMapping("/mock")
    public boolean mock(@RequestBody AuthMessage authMessage) {

        return false;
    }
}
