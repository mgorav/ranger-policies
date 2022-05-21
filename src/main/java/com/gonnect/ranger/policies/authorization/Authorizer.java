package com.gonnect.ranger.policies.authorization;

import java.util.Set;

public interface Authorizer {

    void initialize();
    boolean authorize(String resourceName, String accessType, String user, Set<String> userGroups);
}
