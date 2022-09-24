# About The Project
This Java project is a sample tool renting system called Tool Renter. The goal behind this application is to allow users that adopt this software to rent the available tools based on their tool code (`CHNS`, `LADW`, `JAKD`, `JAKR`). This was created to satisfy a company's interview requirement.

### Built With
* [Java](https://openjdk.java.net/projects/jdk/17/)
* [Maven](https://maven.apache.org/)

## Getting Started
This project is managed using Maven and can be built by running the following commands:

```
mvn clean install
```

or by executing the below command to skip the JUnit 5 tests.

```
mvn clean install -DskipTests
```

## How to Use
After executing the commands in the `Getting Started` section, the built/packaged jar file can be found in the generate `target` directory. The java application can be started by simply running the following command:

```
java -jar .\ToolRenter-1.0.0.jar
```

When the application loads, there will be a menu displayed as below. Enter `1` to begin a transaction to check out a tool for rent and option `2` to exit. 

![image](https://user-images.githubusercontent.com/16766291/155455657-8173bdb2-8e2a-4e8c-974d-daf398cc7f53.png)

The checkout menu, when entered, will look like the following. Here, four pieces of information are required in order to rent a tool: the tool code, number of rental days, discount perent, and the checkout date.

![image](https://user-images.githubusercontent.com/16766291/155456008-5f1850df-3374-47d6-b988-9d8161060610.png)

After entering all of the requested data, press enter to finalize and display the rental agreement.

![image](https://user-images.githubusercontent.com/16766291/155456120-e57174d8-1301-446d-9396-5c0076c42bce.png)


## Error Handling
If the ToolRenter application encounters any errors, the application will cancel the current rental transaction and revert to the main menu showing the error like below.

![image](https://user-images.githubusercontent.com/16766291/155456349-e1f9a7d7-39cf-425d-a96e-5342bc982e97.png)

