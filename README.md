# REST Proxy Service

This proxy service can receive long-term running jobs via its REST endpoint and forward to internal services.
Submitted jobs progresses can be retrieved via an API endpoint to check whether they complete.

REST APIs are protected by user/password authentication and access tokens

## Build

#### Run in debug mode
```$xslt
mvn spring-boot:run
```

#### Build distribution
```$xslt
mvn package
```
Run the distribution 
```$xslt
java -jar target/integration-service-0.0.1-SNAPSHOT.jar
```

## APIs
#### GET /accesstoken
Create access token by login using basic authentication
- Request
```
curl -u user:password http://localhost:8080/accesstoken
```
The bootstrapped account name is 'user' with password is 'password'.

- Response:
```
{"accessToken":"vmghl0cd17dpdliknujks3s8al","userName":"user"}
```
Access tokens will expire after 10 minutes.

#### POST /submit?access_token=$token
Submit a binary file for processing

- Request
```
curl --form file=@./my-cv.doc http://localhost:8080/submit?access_token=vmghl0cd17dpdliknujks3s8al
```

- Response
```
{"processId":"b7f2bb75-e8ac-4fec-831b-0d092f9b2b16"}
```

#### GET /retrieve/$processId?access_token=$token

Query a processing task

- Request
```
curl localhost:8080/retrieve/b7f2bb75-e8ac-4fec-831b-0d092f9b2b16?access_token=vmghl0cd17dpdliknujks3s8al
```

- Response
```
{"id":"b7f2bb75-e8ac-4fec-831b-0d092f9b2b16","state":"COMPLETED","fileName":"my-cv.doc","result":"dummy result"}
```