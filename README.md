# Questionnaire API
------------------------------------------------------------------------------------------------------
Requirements:

Write a README.md that describes in simple terms how to build, run and test your server. 

The README.md should also describe all the operational entry points to your server or link to some external documentation, e.g., SwaggerLinks to an external site..  As noted above, this might be a subset of your proposed API.  This is your "API documentation".  If certain entry points must be called in certain orders or never called in certain orders, make sure to say so.

**Checkstyle Reports**:

We are using Checkstyle's sun_checks ruleset. Although our code is not fully compliant with the rules it defines as of right now, you can see present errors by going into the folder styleReports --> site --> checkstyle.html.

If any third-party code is included in your codebase, also document exactly which code this is, where it resides in your repository, and where you got it from (e.g., download url). 
------------------------------------------------------------------------------------------------------

Our API is a question bank framework in which clients can create, update and delete their own questions (title, four choices and answers) as well as rank their end-users and get data analytics. Our swagger link: https://app.swaggerhub.com/apis-docs/VV2317/questionnaire-api/1.0.0#/. 

Our API uses H2 database to store question banks for each client. Every client's information is stored separately and cannot be accessed from another client. To use our API, the user needs to first boot up our Spring server by running `./mvnw spring-boot:run`. The user can then use curl commands to create, update or delete questions. Testing can be done by accessing our [Postman API](https://warped-comet-420882.postman.co/workspace/Questionnaire-API-Testing~8fba67a5-16c4-42c2-831b-9b0c3f9e6050/overview). To access our database, go to web broswer and enter `localhost:8080/h2-console`.

Our API uses the following external code in our codebase:  
[CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html). Our UserRepository and QuestionReposity extend this interface which supports storing and updating information of our users and questions with corresponding CRUD actions. 
[Java Persistence](https://docs.oracle.com/javaee/7/api/javax/persistence/package-summary.html) which manages object and relational mapping for our User and Question.  
[Springframework.web.bind.annotation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/package-summary.html) which binds requests to controllers and handler methods and binds request parameters to method arguments.  
[MockBean](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/mock/mockito/MockBean.html) and [Mockito](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/mock/mockito/package-summary.html) which mock objects for unit testing.
