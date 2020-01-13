# 1 Description
A simple demo project which demonstrates, how to secure multiple microservices behind a custom gateway application

# 2 Demo Parts
- oauth-server (9080)
- coded-gateway (9081)
- spring-gateway-custom-security (9082)
- spring-gateway-spring-security (9083)
- person-service (9091)
- order-service (9092)


# 3 Spring Cloud Gateway with Custom Security Filter
## 3.1 - Start Services (person-service, order-service, spring-gateway-custom-security)
```batch
$ scripts\start-custom-security-demo.bat
```
## 3.2 - Call Employee Service without a token
```bash
$ curl http://localhost:9082/personservice/persons/1
```   
## 3.3 - Call Order Service without token
```bash
$ curl http://localhost:9082/orderservice/orders/1
```
Result:
```bash
```
## 3.4 - Call Order Service with token
```bash
$ export USER_TOKEN_1=eyJraWQiOiI0NzExIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJ1c2VyMSIsImlzcyI6Imh0dHBzOlwvXC9kZW1vLmNvbSIsInVzZXJ0eXBlIjoiY3VzdG9tZXIiLCJleHAiOjE1Nzg5MjEwMzYsImN1c3RvbWVyIjoiMTIzNDU2Nzg5MCJ9.GbiAnEUmK94ui5fjkpLGxIE63JrA7uFAQw6HCXZt54TXM0EcK_GjEIogkg8zr20Uj9sMg9y2Dp0THRCdMmbK4sqJe5Lni7MH3VAD_2abdc6NjbdfZ6LHVtoubOAhvje_ShAIC6T5pNP4DIp3kZilzXFrdY7agT9NJeynifFih2r-TEWXkcX2ceisNxbBfVxAEdAF1j8J-tRSJuZBZjBIt4RcD3J9hc7v17m3xA4WTZA_wF7_4vz5Nl8mmJpBDJyeG7lBsicCRScfEQ0fly-XX4YyGDPRNgbOfi3ovrIKFAG1rb1VGJ2S9on6yG7v1e0hpyWce_w4RtEO5aN8qfQ3HQ
$ curl --header "X-CUSTOM-TOKEN: $USER_TOKEN_1" http://localhost:9082/orderservice/orders/1

$ export USER_TOKEN_2=eyJraWQiOiI0NzExIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJ1c2VyMiIsImlzcyI6Imh0dHBzOlwvXC9kZW1vLmNvbSIsInVzZXJ0eXBlIjoiZW1wbG95ZWUiLCJleHAiOjE1Nzg5MjMwNjR9.dS7x-SD1VGG1Z_clq3kPcmJkXpEW3q-zDLyGsQSvf_2ORqPsm5CZEOLqjc7nkWfU8x37bx2ITx2ghTYZyM2BufcBN05SMRxhGWnPuKRlAehES7Wfd6fqmH6EfrsQu-FjvqyvxYHGdgzrq_0sqQd-sseipIqozLov1g9XaEq6ib_ndXjg5WVZ7zVEOXAo1L2Ar-nlI0ogzhjIOXHn1izUM0jGX0NxcazLA2wtSSodh1FFfyumqeK3AqY_X0am2qchzW9uSf3M9m2a01D26xtnXY0eKcpszMmT60qt_5mi4BaVXp1_sLIMb1Ne-4WyTsYPOvdEK7jjITjJEsN_k3qhLA
$ curl --header "X-CUSTOM-TOKEN: $USER_TOKEN_2" http://localhost:9082/orderservice/orders/1
```
## 3.5 - Call Order Service and try to inject a private header
```bash
$ curl --v -header "X-USER-INFO: BadGuy" http://localhost:9082/orderservice/orders/1 | jq .
```
Result:
```bash
> GET /orderservice/orders/1 HTTP/1.1
> User-Agent: curl/7.35.0
> Host: localhost:9082
> Accept: */*
> X-USER-INFO: BadGuy
>
< HTTP/1.1 500 Internal Server Error
< Content-Type: application/json;charset=UTF-8
< Content-Length: 189
<
* Connection #0 to host localhost left intact
{
  "message": "Malicious Client is trying to submit X-USER-INFO header",
  "error": "Internal Server Error",
  "status": 500,
  "path": "/orderservice/orders/1",
  "timestamp": "2020-01-13T13:27:48.191+0000"
}
```
## 3.6 - Example Custom JWT Token
Header:
```json
{
  "kid": "4711",
  "typ": "JWT",
  "alg": "RS256"
}
```
Payload:
```json
{
  "sub": "user2",
  "iss": "https://demo.com",
  "usertype": "employee",
  "exp": 1578923261,
  "customer": "1234567890"
}
```


# 4 Spring Cloud Gateway using Spring Security
## 4.1 - Start Services (person-service, order-service, oauth-server, spring-gateway-spring-security)
```batch
$ scripts\start-spring-security-demo.bat
```
## 4.2 - Call Gateway with missing Authorization Token
```bash
$ curl -v http://localhost:9083/orderservice/orders/1
```
Result:
```bash
...HTTP/1.1 401 Unauthorized
```
## 5.3 - Extract Token and call the gateway
```bash
$ TOKEN=$(curl gateway:password@localhost:9080/oauth/token -d "grant_type=password&username=user1&password=password" | jq '.access_token' | sed 's/"//g') 
$ curl -H "Authorization: Bearer $TOKEN" localhost:9083/orderservice/orders/1 | jq .
```

# 5 Custom Coded Gateway
## 5.1 - Start Services (person-service, order-service, oauth-server, coded-gateway)
```batch
$ scripts\start-coded-gateway-demo.bat
```
## 5.2 - Call Gateway with missing Authorization Token
```bash
$ curl -v http://localhost:9081/personservice/persons/1
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
## 5.3 - Get Token
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
## 5.4 - Extract Token and call the gateway
```bash
$ TOKEN=$(curl gateway:password@localhost:9080/oauth/token -d "grant_type=password&username=user1&password=password" | jq '.access_token' | sed 's/"//g') 
$ curl -H "Authorization: Bearer $TOKEN" localhost:9090/personservice/persons/1
```

## 5.5 - Other Calls
```bash
$ TOKEN_USER1=$(curl gateway:password@localhost:9080/oauth/token -d "grant_type=password&username=user1&password=password" | jq '.access_token' | sed 's/"//g')
$ TOKEN_USER2=$(curl gateway:password@localhost:9080/oauth/token -d "grant_type=password&username=user2&password=password" | jq '.access_token' | sed 's/"//g')
$ TOKEN_ADMIN=$(curl gateway:password@localhost:9080/oauth/token -d "grant_type=password&username=admin&password=password" | jq '.access_token' | sed 's/"//g')

$ curl -H "Authorization: Bearer $TOKEN_USER1" localhost:9090/orderservice/orders/1 | jq . 
```


