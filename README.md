# Read Me / Instructions how to run

####You need following inorder complet setup
1. Java 17
2. Maven
2. Gitbash ( Git Installation )
3. Docker 
4. Postman

#### Getting Started

Check out from Github with git clone below command

$git clone git@github.com:tmandala/bny_tics.git

Once checkout , run following to for build and package.

$ cd instructions-capture-service

$ mvn clean package
 
#Note: 99% of the times all test cases should be succeed, if any issues with test please run below

$ mvn clean package -DskipTests


####
Once Build is success you need to start follwoing two services
1. Kafka instance with below command
   
$docker-compose up -d

2. Trade Instruction caputure service with below command.
   
$java -jar target/instructions-capture-service-0.0.1-SNAPSHOT.jar

####
Once application is UP, either you can test API with Swagger-UI or Postman.
1. For Swagger UI please use this URL:

   http://localhost:8080/swagger-ui/index.html

3. For Postman please import colection from below folder
   
..\instructions-capture-service\src\test\resources

For Testing API , You can create single Trade Instructin with : http://localhost:8080/api/trades

Or Upload file with : http://localhost:8080/api/trades/upload

Once Trade instruciton is created , look for logs to find id and use the ID for Get API.
--example log below
2025-11-09T21:21:36.437-05:00  INFO 40468 --- [instructions-capture-service] [nio-8080-exec-6] c.e.instructions.service.TradeService    : processAndPublish  : CanonicalTrade[id=19b09bfb-b13d-4194-ab29-6dfc7b0202b7, maskedAccount=****1234, security=NVDA, type=B, amount=100000, timestamp=2025-08-04T21:15:33Z]






