# Description
A simple demo project which demonstrates, how to secure multiple microservices behind a custom gateway application

# Demo Parts
- oauth-server (9080)
- coded-gateway (9081)
- spring-gateway-custom-security (9082)
- person-service (9091)
- order-service (9092)


# Flow

## Start Services
```batch
$ startDemo.bat
```


## Call Gateway with missing Authorization Token
```bash
$ curl -v http://localhost:9090/personservice/persons/1
```

Result:
```
* Connected to localhost (127.0.0.1) port 9090 (#0)
> GET /service1/persons/2 HTTP/1.1
> User-Agent: curl/7.35.0
> Host: localhost:9090
> Accept: */*
>
< HTTP/1.1 401
< Set-Cookie: JSESSIONID=22F9DE66A84FA0A8952545C1FDA78F25; Path=/; HttpOnly
< WWW-Authenticate: Bearer
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Content-Length: 0
< Date: Sun, 08 Sep 2019 17:14:43 GMT
```

## Get Token
```bash
$ curl gateway:password@localhost:9080/oauth/token -d "grant_type=password&username=user1&password=password" | jq '.'
```

Result:

```json
{
  "jti": "66547dbd-5fa2-4439-ac0d-3851bea2c8b3",
  "scope": "read write",
  "expires_in": 599999999,
  "token_type": "bearer",
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MjE2Nzk2MzAwNSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjY2NTQ3ZGJkLTVmYTItNDQzOS1hYzBkLTM4NTFiZWEyYzhiMyIsImNsaWVudF9pZCI6ImdhdGV3YXkiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.U4KHYmfFuj8N8q6eeBl8bU9HCq-3rfhSqyj1r4Xhe6JvoIljRlVrtiLXyowlu5foin-c6hGjCPD4Lmc98UK3ShaOY7v6DZ1j1VZi9XM5GiNYomVCzFOgb1mcgoaGa1Q8FWtE0s1MgLauNXtXoDErQBpTjxsciwRHtVwfDsx5AiVx7t1hiJXPAYPFiba3U6-M00sZU8SaiXa2dkZJ7-YMJs4seCwLL8Nu8BbUlt3UxdM6YNIdL23jGu_Dk_pOlky183oYSzzcpcB6vf3Ji6uN41wm5aBPIpeFY5Ghi1Hf4vxy0x4RWNz8aLSL3fVNfxjTpMlI_w3seg6IpFPNi5O2KQ"
}
```


## Extract Token and call the gateway
```bash
$ TOKEN=$(curl gateway:password@localhost:9080/oauth/token -d "grant_type=password&username=user1&password=password" | jq '.access_token' | sed 's/"//g') 
$ curl -H "Authorization: Bearer $TOKEN" localhost:9090/personservice/persons/1
```

# Other Calls
```bash
$ TOKEN_USER1=$(curl gateway:password@localhost:9080/oauth/token -d "grant_type=password&username=user1&password=password" | jq '.access_token' | sed 's/"//g')
$ TOKEN_USER2=$(curl gateway:password@localhost:9080/oauth/token -d "grant_type=password&username=user2&password=password" | jq '.access_token' | sed 's/"//g')
$ TOKEN_ADMIN=$(curl gateway:password@localhost:9080/oauth/token -d "grant_type=password&username=admin&password=password" | jq '.access_token' | sed 's/"//g')

$ curl -H "Authorization: Bearer $TOKEN_USER1" localhost:9090/orderservice/orders/1 | jq . 
```


