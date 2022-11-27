# Questionnaire API

Our API is a question bank framework in which clients can create, update and delete their own questions (title, four choices and answers) as well as rank their end-users and get data analytics. Our swagger link: https://app.swaggerhub.com/apis-docs/VV2317/questionnaire-api/1.0.0#/. 

Our API uses H2 database to store question banks for each client. Every client's information is stored separately and cannot be accessed from another client. To use our API, the user needs to first boot up our Spring server by running `./mvnw spring-boot:run`. The user can then use curl commands to create, update or delete questions. Testing can be done by accessing our [Postman API](https://warped-comet-420882.postman.co/workspace/Questionnaire-API-Testing~8fba67a5-16c4-42c2-831b-9b0c3f9e6050/overview). To access our database, go to web broswer and enter `localhost:8080/h2-console`.

## SonarQube

**Currently working with SonarCube locally (will integrate with CI GitHub Workflows soon)**

**Pull most recent version of GitHub repository code:** we have put sonarcube in gitignore because it is a really big file. You only need to download it once for now

((Download SonarQube:**
1. Download and install Java 11 on your system.
2. Download the SonarQube Community Edition zip file (located [here](https://www.sonarqube.org/success-download-community-edition/)).
3. Unzip the downloaded file and rename the folder ‘sonarqube’
4. Move this file inside your ‘advancedSWE’ folder (unable to push it to GitHub for some reason so need to do it this way)

**To start Sonarcube server (run in terminal from advancedSWE folder; will take some time to load):**
./sonarqube/bin/macosx-universal-64/sonar.sh console

**Go to home page:** http://localhost:9000/
	Username: admin
	Password: admin

You can change the password to whatever you like since this will be local to your computer

**Create a new project; follow the steps**
1. On the page with header 'How do you want to create your project?', select `Manually`.   
2. You will then be directed to a 'Create new project' page. Enter a project name of your choice with at least one non-digit. The project key will match 3. your project name. Click `set up` after you see green checkmarks on both input boxes.  
4. You will then be directed to your app's 'Overview'. On that page, click `Locally`.  
5. On the next page, you will be provided with a default token name. Click `Generate`, and then click `Continue`.  
6. Select `maven` and copy the code. Replace the first line with: `./mvnw test sonar:sonar \`

**Run Sonarcube in another terminal tab (can do directly from VSCode terminal):**
It should look something like this (but will vary when you do it with your account)
```
./mvnw test sonar:sonar \
  -Dsonar.projectKey=SWE-Questionnaire-API \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=sqp_c5c3a7e22937ee7d5eb02e7698fc99d383c859a1
```

Uses target folder to get unit test coverage report. Use “verify” instead of “test” for integration test

**See results:** http://localhost:9000/dashboard?id=SWE-Questionnaire-API

**Go to “Overall Code” tab**
- Bugs
- Vulnerabilities
- Security hotspots
- Technical debt (how much time it will take to fix errors)
- Code smells
- Code coverage

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

**Checkstyle Reports**:

We are using Checkstyle's sun_checks ruleset. Although our code is not fully compliant with the rules it defines as of right now, you can see present errors by going into the folder styleReports --> site --> checkstyle.html. To see the up-to-date checkstyle report, run ./mvnw site. A new folder called "target" should appear in your workspace folder. Go to target --> site --> checkstyle.html. Right click on checkstyle.html and "reveal in finder". Then click on the checkstyle.html from finder and it should launch a web page with the error report.

Our API uses the following external code in our codebase:  
[CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html). Our UserRepository and QuestionReposity extend this interface which supports storing and updating information of our users and questions with corresponding CRUD actions. 
[Java Persistence](https://docs.oracle.com/javaee/7/api/javax/persistence/package-summary.html) which manages object and relational mapping for our User and Question.  
[Springframework.web.bind.annotation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/package-summary.html) which binds requests to controllers and handler methods and binds request parameters to method arguments.  
[MockBean](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/mock/mockito/MockBean.html) and [Mockito](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/mock/mockito/package-summary.html) which mock objects for unit testing.
