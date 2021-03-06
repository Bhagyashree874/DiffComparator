WAES Coding For Selection Process
===

The goal of this assignment is to show your coding skills and what you value in software engineering. We value new ideas so next to the original requirement feel free to improve/add/extend. We evaluate the assignment depending on your role (Developer/Tester) and your level of seniority.

# The assignment

- Provide 2 http endpoints that accepts JSON base64 encoded binary data on both
endpoints
    - <host>/v1/diff/<ID>/left and <host>/v1/diff/<ID>/right
        - The provided data needs to be diff-ed and the results shall be available on a third end
point
    - <host>/v1/diff/<ID>
        - The results shall provide the following info in JSON format
            - If equal return that
            - If not of equal size just return that
            - If of same size provide insight in where the diffs are, actual diffs are not needed. 
                - So mainly offsets + length in the data

- Make assumptions in the implementation explicit, choices are good but need to be
communicated

# Stack & Requirements
*Eclipse
*Springboot
*Maven
*H2 database
*Tomcat
## Building and running the tests

* Compile and package the project

```bash
$ mvn clean package
```

## Building without running the tests

 You are required to execute the following command:

```bash
$ mvn clean package -DskipTests
```

# Running

Please see following command:

```bash
$ java -jar target/waes-assignment-diff-api-0.0.1-SNAPSHOT.jar
```
## Rest API

 As required, this API has three endpoints and they are according the assignment. 
 
 Postman can be used for testing the endpoints.


