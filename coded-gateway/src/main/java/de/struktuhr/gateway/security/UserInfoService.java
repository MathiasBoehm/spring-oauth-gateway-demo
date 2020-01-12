package de.struktuhr.gateway.security;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserInfoService {

    private Map<String, UserInfo> data;

    public UserInfoService() {
        initData();
    }

    private void initData() {
        data = new LinkedHashMap<>();

        data.put("user1", new UserInfo("user1", "org_1"));
        data.put("user2", new UserInfo("user2", "org_2"));
        data.put("admin", new UserInfo("admin", "org_admin"));
    }

    public UserInfo getUserInfo(String userId) {
        return data.get(userId);
    }
}
