##  Boiler Plate Spring
This is the ShopCart Project

## Changes you need to make
### 1. pom.xml
	File for put maven dependencies/plugins. You need to adapt for your project.

### 2. application.yml
	File for setting properties. You need to adapt for your project.

	  #####
	  # while there is no dedicated host for your application or for ShopCart, you need to change the
	  # context-path on the application.yml file
	  #####
	server:
	  servlet:
	    context-path: /ShopCart
	  context-path: /ShopCart
	    #context-path: /BoilerPlateSpring
	  #context-path: /BoilerPlateSpring

### 3. logback-spring.xml
	File for logs configuration. You need to adapt for your project.

### 4. sonar-project.properties
	File for sonar configuration. You need to adapt for your project.

## Adding external datasource in your project
### 1. Go to file application.yml and search the property 'datasources.datasourceList'
#### Respecting the hierarchy, put your datasource:
	# FOR LOCAL PROFILE
    -
      username: datasourceUsername
      password: datasourcePassword
      url: datasourceUrl
      driver-class-name: datasourceDriver-class-name
      jndi-name: datasourceJndiName

	# FOR OTHERS PROFILES
    -
      jndi-name: datasourceJndiName

### Requirements
- You will need Java 1.8 or greater installed on your system.
- Choose a recent version of your IDE (Netbeans, Intellij or Eclipse).  
- Install the Lombok in your computer https://projectlombok.org/download

### Download Project
Get the code by either cloning this repository using git
```bash
git clone https://gitlab.gruposalvadorcaetano.pt/b2b/shopcart/be.git
```
or

downloading source code as a zip archive.

Once downloaded, open the terminal in the project directory or use your prefered IDE:

#### -> For Eclipse IDE, use the smart import (import->Git->Projects from Git(with smart import)

### Run Project and Build

# Build the application
```bash
mvnw clean install 
or 
mvn clean install
```
## TIP:
```
If after maven updtate project and maven clean install, you face the issue of not having a specific jar
 - go to C:\users\<user>\.m2\repository and see if the .jar file is not there
 - if the jar is missing, put the dependency in pom.xml
```
# Run application in local mode
```bash
	mvnw spring-boot:run -Dspring-boot.run.profiles=local 
	or 
	mvn spring-boot:run -Dspring-boot.run.profiles=local

eclipse
Run > Run Configurations > Environment > Add
	spring.profiles.active = local
```

# Debug application in local mode

	
	```eclipse
		Debug > Debug Configurations >	Remote Java Application > Create New with desired port (9000)
		on the class "/src/main/java/com/gsc/boilerplatespring/BoilerPlateSpringApplication.java" -> "debug as" -> "java application"
	
	```bash
		mvnw spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9000"
		or 
		mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9000"
	
		When "Listening for transport dt_socket at address: 9000" appears: Start the created <Debug Configuration>	
	
```

# @ECLIPSE
- Run maven generate sources
- Right Click on Project > Build Path > Configure Build Path > Sources
	- Add folder target/generated-sources/annotations/org.hibernate.jpamodelgen

### Project Configuration

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Doc OpenAPI](https://springdoc.org)


### Releases

None


### ENV
	LOCAL	
		http://localhost:8080/ShopCart/swagger-ui/index.html
DEV
		https://devshopcart.rigorcg.pt/fe/
		https://devshopcart.rigorcg.pt/ShopCart/swagger-ui.html

	STAGING
		https://demoshopcart.rigorcg.pt/fe/
		https://demoshopcart.rigorcg.pt/ShopCart/swagger-ui.html
		

### Deploy 
The standard build generates 2 war’s. The one with ‘.war’ contains an embedded Jetty server.
The one with ‘.war.original’ does not contain the embedded Jetty server and is the one that should be used to deploy to the various environments.

