cas-mock-server
===============

This is a simple mock up of a CAS server suitable for testing applications
that depend on CAS without performing any real authentication.

## Authenticating with the Mock CAS

This mock CAS server has no configured users and allows any credentials in which username = password. As an example, entering a username of "john" and password of "john" will successfully authenticate the user. 


## Running with Docker

A Dockerfile is provided to make it easy to run the mock server. The Docker image includes a Tomcat 8 server and deploys the mock server at the server root.

### Building

To build, simply build using Docker:

```
docker image build -t cas-mock-server .
```

### Running the Container

Once built, you can start the Docker container using the following command:

```
docker container run -dp 8080:8080 cas-mock-server
```

Once running, you can open to http://localhost:8080/cas/login?service=http://localhost and see the CAS protocol in action. Obviously, you'll want to connect a real service, but it'll at least show it working.
