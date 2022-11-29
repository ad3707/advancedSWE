# Questionnaire API

Our API is a question bank framework in which clients can create, update and delete their own questions (title, four choices and answers) as well as rank their end-users and get data analytics. Our swagger link: https://app.swaggerhub.com/apis-docs/VV2317/questionnaire-api/1.0.0#/. 

Our API uses H2 database to store question banks for each client. Every client's information is stored separately and cannot be accessed from another client. To use our API, the user needs to first boot up our Spring server by running `./mvnw spring-boot:run`. The user can then use curl commands to create, update or delete questions. Testing can be done by accessing our [Postman API](https://warped-comet-420882.postman.co/workspace/Questionnaire-API-Testing~8fba67a5-16c4-42c2-831b-9b0c3f9e6050/overview). To access our database, go to web broswer and enter `localhost:8080/h2-console`.

## Authentication and Authorization
A bearer token is needed in order to make CRUD commands to our API. Otherwise, 401 unauthorized errors will be received.

To retrieve a bearer token, use the following curl command.

```
curl --request POST \
  --url https://dev-lb0aibabfhuc6e6j.us.auth0.com/oauth/token \
  --header 'content-type: application/json' \
  --data '{"client_id":"LsVAxRmvrm8yxktqXOzdDWWn6mlAxd6P","client_secret":"DJeBImCv2Mi6Qbe3_m2mYPwAHSkuJO_YoXm_XlnWRg1B0myVdS4BPhO1BeaeCa3I","audience":"localhost:8080","grant_type":"client_credentials"}'
```

Use the bearer token provided in the response body and pass this in the request header when making CRUD commands.

Here is an example of what the curl command would look like:
```
curl --request GET \
  --url http://path_to_your_api/ \
  --header 'authorization: Bearer {BEARER_TOKEN}'
```

## SonarCloud

We have integrated static analysis with GitHub Workflows. Click the checkmark/'x' next to the most recent commit. Then click 'Details' for 'SonarCloud Code Analysis'. Scroll to bottom of page and click 'View more details on SonarCloud'.

This will give you information about the code coverage, code smells, bugs, and vulnerabilities 

**Currently we're at 88% coverage**

![PNG image](https://user-images.githubusercontent.com/113868845/204172978-a7536a1e-b1ea-4070-95aa-7d454c5faf3f.jpeg)

## How to Run Our Project

1. **Clone:** run `git clone https://github.com/ad3707/advancedSWE.git` in your terminal
2. **Install JDK 17:** Run `javac -version` in your terminal and confirm it returns `javac 17.*.*`. If the version is older, you can [use this link](https://www.oracle.com/java/technologies/downloads/#java17) to download and install the package compatible with your local machine.
3. **Install dependencies:** `./mvnw install`
4. **Run tests:** `./mvnw test`
5. **Run Clean (run before running test if error comes up):** `./mvnw clean`

## How to Test Our Project on Postman

1. Run Spring-Boot from terminal: `./mvnw spring-boot:run`
2. While it is running, go to Postman: https://warped-comet-420882.postman.co/workspace/Questionnaire-API-Testing~8fba67a5-16c4-42c2-831b-9b0c3f9e6050/collection/23969942-f4487392-6f96-4cf2-b49b-f86418ef93b8?ctx=documentation

## Endpoints to Test On Postman:

### Questions

**Add Question**

1. POST localhost:8080/questions 

3. Underneath, click "Body", "raw", and select "JSON".

3. In the top text-field, insert a question. For Example:

```
{
    "name": "What is 100+8?",
    "a": "108",
    "b": "102",
    "c": "303",
    "d": "20",
    "answer": "a"
}
```

4. Click "Send"
5. In the response text-field, click "Body", "Pretty", and select "JSON" from drop down options to see question just added.

Sample Output (the id will vary depending on how many questions already inputed):

```
{
    "id": 100,
    "name": "What is 100+8?",
    "a": "108",
    "b": "102",
    "c": "303",
    "d": "20",
    "answer": "a",
    "clientId": "0:0:0:0:0:0:0:1"
}
```

**Edit Question**

1. PUT localhost:8080/questions/100
3. Click "Body", "raw", and select "JSON" in the top text-field to edit question.

Example: Change above question to:

```
{
    "name": "What is 2+8?",
    "a": "1",
    "b": "10",
    "c": "33",
    "d": "20",
    "answer": "b"
}
```

4. Click "Send"
5. Above response field, click "Body", "Pretty", and select "JSON" from drop down options to see edited question.

Expected Repsonse:

```
{
    "id": 100,
    "name": "What is 2+8?",
    "a": "1",
    "b": "10",
    "c": "33",
    "d": "20",
    "answer": "b",
    "clientId": "0:0:0:0:0:0:0:1"
}
```

**View Question**

1. GET localhost:8080/questions/100
2. Click "Send"
3. Click "Body", "Pretty", and select "JSON" from drop down options to see the question requested.


Sample Output

```
{
    "id": 100,
    "name": "What is 2+8?",
    "a": "1",
    "b": "10",
    "c": "33",
    "d": "20",
    "answer": "b",
    "clientId": "0:0:0:0:0:0:0:1"
}
```

**Delete Question**

1. DELETE localhost:8080/questions/100
2. Click "Send"
3. Above response field, click "Body", "Pretty", and select "JSON" from drop down options to see deleted question.

Sample Output

```
{
    "id": 100,
    "name": "What is 2+8?",
    "a": "1",
    "b": "10",
    "c": "33",
    "d": "20",
    "answer": "b",
    "clientId": "0:0:0:0:0:0:0:1"
}
```

**View All Questions**

1. GET localhost:8080/questions
2. Click "Send"

Sample Output should list a all questions that have been posted and not yet deleted

### Users

**Create New User**

1. POST localhost:8080/users
2. Underneath, click "Body", "raw", and select "JSON".
3. In the top text-field, insert a user. For Example:

```
{
    "name": "Daniel",
    "attempted": "6",
    "correct": "5"
}
```

4. Click "Send"
5. In the response text-field, click "Body", "Pretty", and select "JSON" from drop down options to see user just added.

Expected Response:

```
{
    "id": 9,
    "name": "Daniel",
    "attempted": "6",
    "correct": "5"
    "clientId": "0:0:0:0:0:0:0:1",
    "percentCorrect": 0.8333333333333334
}
```

**Edit User**

1. PUT localhost:8080/users/8
3. Click "Body", "raw", and select "JSON" in the top text-field to edit user.

Example: Change above user to:

```
{
    "name": "Daniel Um",
    "attempted": "6",
    "correct": "5"
}
```

4. Click "Send"
5. Above response field, click "Body", "Pretty", and select "JSON" from drop down options to see edited user.

Expected Response:

```
{
    "id": 9,
    "name": "Daniel Um",
    "attempted": "6",
    "correct": "5",
    "clientId": "0:0:0:0:0:0:0:1",
    "percentCorrect": 0.8333333333333334
}
```

**View User**

1. GET localhost:8080/users/9
2. Click "Send"
3. Click "Body", "Pretty", and select "JSON" from drop down options to see the user requested.


Sample Output

```
{
    "id": 9,
    "name": "Daniel Um",
    "attempted": "6",
    "correct": "5",
    "clientId": "0:0:0:0:0:0:0:1",
    "percentCorrect": 0.8333333333333334
}
```

**Delete Question**

1. DELETE localhost:8080/users/9
2. Click "Send"
3. Above response field, click "Body", "Pretty", and select "JSON" from drop down options to see deleted user.

Sample Output

```
{
    "id": 9,
    "name": "Daniel Um",
    "attempted": "6",
    "correct": "5",
    "clientId": "0:0:0:0:0:0:0:1",
    "percentCorrect": 0.8333333333333334
}
```

**View All Users**

1. GET localhost:8080/users
2. Click "Send"

Sample Output should list a all users


**Answer a Question**

Put template: "/users/{userid}/answer/{questionid}"

1. PUT localhost:8080/users/9/answer/6
3. Click "Body", "raw", and select "JSON" in the top text-field to edit user.

Example: Send the answer (case-insensitive):

```
C
```

4. Click "Send"
5. Above response field, click "Body", "Pretty", and select "JSON" from drop down options to see edited user.

Expected Response:

```
{
    "id": 9,
    "name": "Daniel Um",
    "attempted": "7",
    "correct": "6",
    "clientId": "0:0:0:0:0:0:0:1",
    "percentCorrect": 0.8571428571428571
}
```

### Leaderboards

**Get Top K Users**

Template: "/leaderboard/{k}"

1. GET localhost:8080/leaderboard/2
2. Click "Send"

You should see a list of users who have the 2 highest scores (in this case)


### Our Client
Our client is a classroom platform that allows teachers to create exams for their students. They can add questions, delete questions, update questions, add students to their class, delete students, update students, and retrieve the top students in their class by using our API service. An example of some third party using our server for their own client is a company that hosts multiple trivia games such as geography, math, and history trivia where each category is their own client. 

**To run the client:**
1. Download this repo: https://github.com/ad3707/advancedSWEClient/tree/main
2. After downloading the repo, unzip it and open up the folder in VSCode.
3. Go to the index.html file under views and in the bottom right click Go Live.
4. If you do not see the "Go Live" button, then install this: https://marketplace.visualstudio.com/items?itemName=ritwickdey.LiveServer
5. Run the API service in another terminal. 
6. While the API service is running, the client is able to use the functionalities of our API. 

**To test submitting a question**
1. Go to dashboard
2. Click on "Create Quiz" on the top right
3. Fill out the form called "Add Question".
4. Click "Submit Question"
5. If you click "See Questions", you should see the question you created. 

**To test updating a question**
1. Go to dashboard
2. Click on "Create Quiz" on the top right
3. Fill out the form called "Update Question".
4. Click "Update"
5. If you click "See Questions", you should see updates you made to the specific question.  

**To test deleting a question**
1. Go to dashboard
2. Click on "Create Quiz" on the top right
3. Fill out the form called "Delete Question".
4. Click "Delete"
5. If you click "See Questions", the question you deleted, should not be there.

**To test adding a student**
1. Go to dashboard
2. Click on "Class Info" on the top right
3. Fill out the form called "Add Student".
4. Click "Add"
5. If you click "See All Students", you should see the student you added. 

**To test updating a student**
1. Go to dashboard
2. Click on "Class Info" on the top right
3. Fill out the form called "Update Student".
4. Click "Update"
5. If you click "See All Students", you should see updates you made to the specific student.  

**To test deleting a student**
1. Go to dashboard
2. Click on "Class Info" on the top right
3. Fill out the form called "Delete Student".
4. Click "Delete"
5. If you click "See All Students", the student you deletes, should not be there.

**To See the Leaderboard**
1. Go to dashboard
2. Click on "Class Info" on the top right
3. Fill out the form called "Leaderboard".
4. Click "View Leaderboard"
5. You should see the top k students in the leaderboard section. 


### Checkstyle Reports:

We are using Checkstyle's sun_checks ruleset. Although our code is not fully compliant with the rules it defines as of right now, you can see present errors by going into the folder styleReports --> site --> checkstyle.html. To see the up-to-date checkstyle report, run ./mvnw site. A new folder called "target" should appear in your workspace folder. Go to target --> site --> checkstyle.html. Right click on checkstyle.html and "reveal in finder". Then click on the checkstyle.html from finder and it should launch a web page with the error report.

Our API uses the following external code in our codebase:  
[CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html). Our UserRepository and QuestionReposity extend this interface which supports storing and updating information of our users and questions with corresponding CRUD actions. 
[Java Persistence](https://docs.oracle.com/javaee/7/api/javax/persistence/package-summary.html) which manages object and relational mapping for our User and Question.  
[Springframework.web.bind.annotation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/package-summary.html) which binds requests to controllers and handler methods and binds request parameters to method arguments.  
[MockBean](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/mock/mockito/MockBean.html) and [Mockito](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/mock/mockito/package-summary.html) which mock objects for unit testing.
