package de.struktuhr.gateway.security;

import lombok.Data;

@Data
public class UserInfo {

    private final String userId;

    private final String organizationId;
}
