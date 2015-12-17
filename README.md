# LearningSwagger
Learning how swagger works

### Projects to have locally for building:
- Note, this is using the snapshot version so there is a risk that something changes that will break the current setup. If you don't want that, check out the release tag from the projects below and compile with maven.<br>
	- swagger-codegen<br>
	- swagger-core<br>
	- swagger-samples<br>
	- swagger-ui<br>


### JARs requried to run the Generator:
* jmustache-1.9.jar<br>
* slf4j-api-1.7.12.jar<br>
* slf4j-ext-1.7.12.jar<br>
* slf4j-simple-1.7.12.jar<br>
* swagger-annotations-1.5.5-SNAPSHOT.jar<br>
* swagger-codegen-2.1.5-SNAPSHOT.jar<br>
* swagger-compat-spec-parser-1.0.12.jar<br>
* swagger-core-1.5.5-SNAPSHOT.jar<br>
* swagger-jaxrs-1.5.5-SNAPSHOT.jar<br>
* swagger-models-1.5.5-SNAPSHOT.jar<br>
* swagger-parser-1.0.12.jar<br>
<br>
* gson-2.3.jar<br>
* guava-15.0.jar<br>


### Mustache File Information:
- \*ApiServiceImpl.mustache - The Location of the actual logic for the RESTful entry points.<br>
- \*ApiService.mustache - abstract class that is extended by the *ApiServiceImpl.java class<br>
- \*ApiServiceFactory.mustache - returns the PersonsApiService.java (that extends ApiServiceImpl.java)<br>
- \*Api.mustache - the GET|POST method for Jersey.<br>
- model.mustache - This is any entry found under the "definitions" node in your input Swagger.json file<br> 
	- also contains get/set/hashcode/toString/equals as default methods

* Custom mustache files:<br>
	- DAO.mustache<br>
		- This will be used to create the stub entries for any x-daoSelectors entry found in the definitions section of the swagger input<br>

### TODO:
- add in the processProperty() function<br>
- add in the x-vendorExtension for shorts<br>
- add in the x-useDao for both operations and definitions<br>
	- entry/map/array for creating DAO class stubbing<br>



## Info below is from other readme file and will be changing / adding to the info above. 

			
### Eclipse Setup to run Generator:
Run -> Run Configurations -> new Java Application

	- Main Class: io.swagger.codegen.SwaggerCodegen
	
	- Arguments Tab
		generate
		-l com.homedepot.swagger.CustomGenerator 
		-i ./Swagger/CreateMS.1.0.json
		-o ./CustomMicroService
		-c SwaggerConfig/config.json
		-D debugSwagger=false
		-D debugModels=false
		-D debugSupportingFiles=false
		-t src/main/resources/DomainCodegen
		
	- Classpath Tab / User Entries:
		- swagger-codegen-cli
		

### Resource Files
The resource files are the layout of the generate files.<Br>

- api.mustache<Br>
	Jersey entry paths based on each of the operations found in the Swagger.json file.<Br>
	
- ApiResponseMEssage.mustache<Br>
	somesomething about returning from something that does something.<Br>
	
- apiService.mustache<Br>
	Abstract class for the ApiService class.<Br>
	
- apiServiceImpl.mustache<Br>
	The code that runs each of the Operation Path entries<Br>
	
- DAO.mustache<Br>
	in progress to create stubs for the DAO Calls that the services above will be calling.<Br>
	
- DAOMappingEnum.mustache<Br>
	Stub / setup for the DAOMapping ENUM.<Br>
	
### Swagger Files (/Swagger)<Br>
	CreateMS.1.0.json
		- Basic Setup that can be cloned to create a new entry.
		
### Swagger Config (/SwaggerConfig)
	Sample.config
		- Basic setup for the generator
		- systemProperties
			- these settings are what you would like to override in the default setup of the Generator. changing base package would be wise.
		
#### Notes:
	- new X-^ nodes that can be used on the module/properties object:
		- x-daoElement : Name of the column that is found in a DAO Selector.
		- x-db2column : name of the column that is in the database.
		- x-toShort : boolean that indiciates that a DTO property should be a short instead of an Integer
	- new X-^ nodes that can be used on the model object:
		- x-daoSelectors : In progress for setting up the DAO Template file
