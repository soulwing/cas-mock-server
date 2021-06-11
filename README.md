cas-mock-server
===============

This is a simple mock up of a CAS server suitable for testing applications
that depend on CAS without performing any real authentication.

## Authenticating with the Mock CAS

This mock CAS server has no configured users and allows any credentials in which username = password. As an example, entering a username of "john" and password of "john" will successfully authenticate the user. 


## Running with Docker

Docker Hub is configured to perform automated builds for all code pushes to this repo. The image is available at [soulwing/cas-mock-server](https://hub.docker.com/r/soulwing/cas-mock-server).

```
docker container run -dp 8080:8080 soulwing/cas-mock-server
```

### Building

A Dockerfile is provided to make it easy to run the mock server. The Docker image includes a Tomcat 8 server and deploys the mock server at the server root. To build, simply build using Docker:

```
docker image build -t cas-mock-server .
```

### Running the Container

Once built, you can start the Docker container using the following command:

```
docker container run -dp 8080:8080 cas-mock-server
```

Once running, you can open to http://localhost:8080/cas/login?service=http://localhost and see the CAS protocol in action. Obviously, you'll want to connect a real service, but it'll at least show it working.


## Attribute Release

Most CAS 3.0 providers have the ability to release attributes that further 
describe the authenticated user. This mock CAS server comes with a couple of
built-in attribute data providers.

### JSON Attributes Provider

The simplest attributes provider you can use is the _JSON attributes provider_.
This provider reads a JSON object from a URL that describes the attributes to
include for a user.

Here's an example.

```json

{
  "DEFAULT": {
    "attributes": {
      "affiliation": "EMPLOYEE",
      "groupMembership": "valid-user"
    }
  },
  "fletcher": {
    "inherit": "DEFAULT",
    "attributes": {
      "uid": 1,
      "displayName": "Jordan Fletcher",
      "groupMembership": [
          "admin",
          "power-user"
      ]
    }
  }
}
```

The JSON object contains keys which correspond to user login names, and each
user object contains an _attributes_ object whose keys and values are the 
attributes to be released for the user. In order to avoid repetition in the
file, you can define a user object (here the _DEFAULT_ object) that contains
attributes to be inherited by other users. The _inherit_ attribute of a user
specifies the login name of a user whose attributes are to be inherited by that
user.

To use the _JSON attributes provider_ simply specify the ATTRIBUTES_JSON_URL
environment variable as a URL pointing to JSON object to be loaded. Any URL
supported by Java's `HttpURLConnection` is supported.

### JDBC Attributes Provider

The _JDBC attributes provider_ reads attributes from an SQL database using JDBC.
The provider assumes that attributes for a user can be read as a query result 
row using the user's login name as a query parameter. Additionally, it supports 
the ability to read values for a single multi-valued attribute 
(e.g. group memberships), as rows of a single column value using the user's
login name as a query parameter.

You configure the JDBC attributes provider by specifying several different 
environment variables.

* ATTRIBUTES_JDBC_URL - full JDBC URL for the database
* ATTRIBUTES_JDBC_DRIVER - (optional) fully-qualified class name of the JDBC
  driver; this is needed mostly for Tomcat which does not automatically register
  JDBC drivers
* ATTRIBUTES_JDBC_USERNAME - (optional) database username
* ATTRIBUTES_JDBC_PASSWORD - (optional) database password
* ATTRIBUTES_JDBC_USER_QUERY - SQL query that will return the attributes using a
  single parameter placeholder (?) that will be filled using the user's login
  name
* ATTRIBUTES_JDBC_USER_COLUMNS - comma-separated list of attribute names that 
  correspond to positional columns in the result
* ATTRIBUTES_JDBC_GROUP_QUERY - (optional) SQL query that will return values
  for a single multi-valued attribute such as group memberships, using a single
  parameter placeholder (?) that will be filled using the user's login name
* ATTRIBUTES_JDBC_GROUP_COLUMNS - comma-separated list of attribute names that 
  correspond to positional columns in the group query result.
  
> **IMPORTANT**:
> In order to use the JDBC attribute provider, you must also arrange to include
> the JDBC driver in the classpath for the mock CAS server. If you're using
> PostgreSQL or MySQL as a database, reasonably current versions of those
> drivers are bundled with the CAS server. If you need another driver, and  
> you're using the Mock CAS server container image that is based on Tomcat,
> you can create a custom container image based on the image for
> mock CAS server that copies your JDBC driver into Tomcat's `lib/` directory.

For example, suppose your database has an `app_user` table that describes 
each user.

```postgresql
CREATE TABLE app_user (
  id BIGINT PRIMARY KEY, 
  name VARCHAR(255) UNIQUE NOT NULL, 
  display_name VARCHAR(255), 
  affiliation VARCHAR(255)
)
``` 

Let's suppose that the columns of this table correspond to CAS attributes 
_uid_, _loginName_, _displayName_, and _affiliation_.

Using the example table above, the query for the ATTRIBUTES_JDBC_USER_QUERY
environment variable could be written as follows.

```sql
SELECT uid, name, display_name, affiliation
FROM app_user
WHERE name = ?
```

The value for the ATTRIBUTES_JDBC_USER_COLUMNS variable for this example would be
`uid,loginName,displayName,affiliation`.

Suppose you also want to include a single multi-valued attribute representing 
group memberships for the user and that group memberships are represented in 
two tables. In this example, this first table defines the available groups.

```sql
CREATE TABLE app_group (
  id BIGINT PRIMARY KEY, 
  name VARCHAR(255) UNIQUE NOT NULL
)
```

And this table defines the members of the groups.

```sql
CREATE TABLE app_group_member (
  group_id BIGINT NOT NULL REFERENCES app_group (id), 
  user_id BIGINT NOT NULL REFERENCES app_user (id))
```

We can include a multi-valued _groupMembership_ attribute by specifying the
ATTRIBUTES_JDBC_GROUP_QUERY variable as a query as follows.

```sql
SELECT g.name FROM app_group g
INNER JOIN app_group_member gm ON gm.group_id = g.id
INNER JOIN app_user u ON gm.user_id = u.id 
WHERE u.name = ?
```

Additionally, specify the ATTRIBUTES_JDBC_GROUP_COLUMNS variable with the value
`groupMembership`.

### Using a custom attribute provider

If none of the built-in attribute providers are suitable for your needs, you
can provide your own. Attribute providers are located using the Java 
`ServiceLoader` mechanism. You can create your own by writing a provider class
that implements the `AttributesServiceProvider` interface and placing it into
a JAR file with a service provider specification in `META-INF/services`. See
the [ServiceLoader](https://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html) 
documentation for details on creating a service provider.

After creating your provider, you must arrange to make it available on the 
classpath of the mock CAS server. Assuming you are using the mock CAS server
variant that runs on top of Tomcat, you can do this by creating a custom
container image based on `cas-mock-server` image and copying the provider's JAR
file into Tomcat's `lib/` directory.
