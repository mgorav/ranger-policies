package com.gonnect.ranger.policies.authorization;

import java.util.HashSet;
import java.util.Set;

public class AuthMessage {
    String resourceName;
    String accessType;
    String user;
    Set<String> userGroups;

    public AuthMessage(String resourceName, String accessType, String user, Set<String> userGroups) {
        this.resourceName = resourceName;
        this.accessType = accessType;
        this.user = user;
        if (userGroups != null) {
            this.userGroups = userGroups;
        } else {
            this.userGroups = new HashSet<>();
        }
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Set<String> getUserGroups() {
        return userGroups;
    }

    public void addUserGroup(String userGroup) {
        userGroups.add(userGroup);
    }

    public void setUserGroups(Set<String> userGroups) {
        this.userGroups = userGroups;
    }
}
