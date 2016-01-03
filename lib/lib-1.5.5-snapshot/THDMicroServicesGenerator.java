/**
 * 
 */
package com.homedepot.swagger;

import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.CodegenModelFactory;
import io.swagger.codegen.CodegenModelType;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.CodegenResponse;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.JavaClientCodegen;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.util.Json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.homedepot.swagger.dto.DAODetailsDTO;
import com.homedepot.swagger.dto.SwaggerJsonDTO;
import com.homedepot.swagger.model.DaoModel;
import com.homedepot.swagger.properties.THDProperties;
import com.homedepot.swagger.utils.JSONParser;

/**
 * 
 * 
 * @author SXH8009
 *
 */
public class THDMicroServicesGenerator extends JavaClientCodegen implements CodegenConfig {
	protected String title = "THD MicroServices";

	private JSONParser< DAODetailsDTO> daoDetailsParser = new JSONParser< DAODetailsDTO>();
	private THDDaoTemplateGenerator daoGen = new THDDaoTemplateGenerator();
	private String daoClassPath = null;
	
	private List< Object> modelImportList = new ArrayList< Object>();
	private Map< String, Object> modelImportsForDao = new HashMap< String, Object>();
	
	public THDMicroServicesGenerator() {
		super.processOpts();

		sourceFolder = "src/gen/java";
		invokerPackage = "io.swagger.api";
		artifactId = "swagger-jaxrs-server";

		outputFolder = System.getProperty( "swagger.codegen.jaxrs.genfolder", "generated-code/javaJaxRS");

		modelTemplateFiles.put( "model.mustache", ".java");

		apiTemplateFiles.put( "api.mustache", ".java");
		apiTemplateFiles.put( "apiService.mustache", ".java");
		apiTemplateFiles.put( "apiServiceImpl.mustache", ".java");
		apiTemplateFiles.put( "apiServiceFactory.mustache", ".java");

		embeddedTemplateDir = templateDir = "JavaJaxRS";
		
		apiPackage = System.getProperty( "swagger.codegen.jaxrs.apipackage", "io.swagger.api");
		modelPackage = System.getProperty( "swagger.codegen.jaxrs.modelpackage", "io.swagger.model");

		additionalProperties.put( CodegenConstants.INVOKER_PACKAGE, invokerPackage);
		additionalProperties.put( CodegenConstants.GROUP_ID, groupId);
		additionalProperties.put( CodegenConstants.ARTIFACT_ID, artifactId);
		additionalProperties.put( CodegenConstants.ARTIFACT_VERSION, artifactVersion);
		additionalProperties.put( "title", title);

	}

	@Override
	public void processOpts() {
		super.processOpts();

		supportingFiles.clear();

		supportingFiles.add( new SupportingFile( "README.mustache", "", "README.md"));
		supportingFiles.add( new SupportingFile( "ApiException.mustache", ( sourceFolder + '/' + apiPackage).replace( ".", "/"), "ApiException.java"));
		supportingFiles.add( new SupportingFile( "ApiOriginFilter.mustache", ( sourceFolder + '/' + apiPackage).replace( ".", "/"), "ApiOriginFilter.java"));
		supportingFiles.add( new SupportingFile( "ApiResponseMessage.mustache", ( sourceFolder + '/' + apiPackage).replace( ".", "/"), "ApiResponseMessage.java"));
		supportingFiles.add( new SupportingFile( "NotFoundException.mustache", ( sourceFolder + '/' + apiPackage).replace( ".", "/"), "NotFoundException.java"));
		supportingFiles.add( new SupportingFile( "web.mustache", ( "src/main/webapp/WEB-INF"), "web.xml"));

		// Use a custom created Model/Properties class to update/extend the Swagger Model class:
		CodegenModelFactory.setTypeMapping( CodegenModelType.MODEL, DaoModel.class);
		CodegenModelFactory.setTypeMapping( CodegenModelType.PROPERTY, THDProperties.class);

		
		daoClassPath = ( sourceFolder + '/' + apiPackage + "/dao").replace(".", "/");
	}

	@Override
	public void addOperationToGroup( String tag, String resourcePath, Operation operation, CodegenOperation co, Map< String, List< CodegenOperation>> operations) {
		String basePath = resourcePath;
		if ( basePath.startsWith( "/")) {
			basePath = basePath.substring( 1);
		}
		int pos = basePath.indexOf( "/");
		if ( pos > 0) {
			basePath = basePath.substring( 0, pos);
		}

		if ( basePath == "") {
			basePath = "default";
		}
		else {
			if ( co.path.startsWith( "/" + basePath)) {
				co.path = co.path.substring( ( "/" + basePath).length());
			}
			co.subresourceOperation = !co.path.isEmpty();
		}
		List< CodegenOperation> opList = operations.get( basePath);
		if ( opList == null) {
			opList = new ArrayList< CodegenOperation>();
			operations.put( basePath, opList);
		}
		opList.add( co);
		co.baseName = basePath;
	}

	@Override
	public void preprocessSwagger( Swagger swagger) {
		if ( "/".equals( swagger.getBasePath())) {
			swagger.setBasePath( "");
		}
		if ( swagger != null && swagger.getPaths() != null) {
			for ( String pathname : swagger.getPaths().keySet()) {
				Path path = swagger.getPath( pathname);
				if ( path.getOperations() != null) {
					for ( Operation operation : path.getOperations()) {
						if ( operation.getTags() != null) {
							List< Map< String, String>> tags = new ArrayList< Map< String, String>>();
							for ( String tag : operation.getTags()) {
								Map< String, String> value = new HashMap< String, String>();
								value.put( "tag", tag);
								value.put( "hasMore", "true");
								tags.add( value);
							}
							if ( tags.size() > 0) {
								tags.get( tags.size() - 1).remove( "hasMore");
							}
							if ( operation.getTags().size() > 0) {
								String tag = operation.getTags().get( 0);
								operation.setTags( Arrays.asList( tag));
							}
							operation.setVendorExtension( "x-tags", tags);
						}
					}
				}
			}

			if ( null != swagger.getVendorExtensions()) {
				// if you want to add more X-^ tags to the operation (api.mustache) you would add them here.
			}
		}
	}

	@Override
	public Map< String, Object> postProcessModels( Map< String, Object> objs) {
		objs = super.postProcessModels( objs);
		@SuppressWarnings( "unchecked")
		Map< String, Object> modelInfo = ( Map< String, Object>) ( ( List< Object>) objs.get( "models")).get( 0);
		processModel( modelInfo);

		return objs;
	}

	@Override
	public Map<String, Object> postProcessSupportingFileData(Map<String, Object> objs) {
		if ( objs.containsKey( "models")) {
			List<Map<String, String>> imports = new ArrayList<Map<String, String>>();
			
			List< Object> inputModels = (ArrayList<Object>)objs.get("models");
			
			for( Object mod : inputModels) {
				DaoModel dm = (DaoModel)((Map<String, Object>)mod).get("model");
				
				if ( dm.useDao) {
					dm.imports = new HashSet<String>();
					dm.imports.add( "com.homedepot.ta.aa.dao.exceptions.QueryException");
					dm.imports.add( "org.apache.log4j.Logger");
					dm.imports.add( "java.util.ArrayList");
					dm.imports.add( "java.util.List");
					dm.imports.add( "com.homedepot.mm.rd.massnewsku.ms.NotFoundException");
					dm.imports.add( "com.homedepot.ta.aa.dao.builder.DAO");
					
					Map<String, Object> models = new HashMap< String, Object>();
					models.put("package", modelPackage().replaceAll( "\\.model", ""));
					
					models.put("imports", imports);
					List<Object> modelsList = new ArrayList<Object>();
					
					
					modelsList.add( mod);
					models.put( "models", modelsList);
					daoGen.writeDaoStubForModel( this, daoClassPath, dm.classname, models);
				}
			}
		}
		
		return objs;
	}
	
	@SuppressWarnings( "unchecked")
	@Override
	public Map< String, Object> postProcessOperations( Map< String, Object> objs) {
		System.out.println("NAO DIS");
		Map< String, Object> operations = ( Map< String, Object>) objs.get( "operations");
		if ( operations != null) {
			List< CodegenOperation> ops = ( List< CodegenOperation>) operations.get( "operation");
			for ( CodegenOperation operation : ops) {
				List< CodegenResponse> responses = operation.responses;
				if ( responses != null) {
					for ( CodegenResponse resp : responses) {
						if ( "0".equals( resp.code)) {
							resp.code = "200";
						}
					}
				}
				if ( operation.returnType == null) {
					operation.returnType = "Void";
				}
				else if ( operation.returnType.startsWith( "List")) {
					String rt = operation.returnType;
					int end = rt.lastIndexOf( ">");
					if ( end > 0) {
						operation.returnType = rt.substring( "List<".length(), end).trim();
						operation.returnContainer = "List";
					}
				}
				else if ( operation.returnType.startsWith( "Map")) {
					String rt = operation.returnType;
					int end = rt.lastIndexOf( ">");
					if ( end > 0) {
						operation.returnType = rt.substring( "Map<".length(), end).split( ",")[1].trim();
						operation.returnContainer = "Map";
					}
				}
				else if ( operation.returnType.startsWith( "Set")) {
					String rt = operation.returnType;
					int end = rt.lastIndexOf( ">");
					if ( end > 0) {
						operation.returnType = rt.substring( "Set<".length(), end).trim();
						operation.returnContainer = "Set";
					}
				}
				
				if ( operation.operationId.endsWith( "Get")) { operation.operationId = "get" + capitalize( operation.operationId.replaceAll("Get$", "")); }
			}
		}
		
		if (null != objs.get("imports")) {
			
			List< Object> oo = ( List<Object>)objs.get("imports");
			
			// newIp.put( "import", "omg");
			
			// TODO: when doing the models part. start pooling the imports that would be required for these API Interfaces. 
			// ^ Also, only put it in when doing the Impl. no reason to load it for the Api. 
			
			System.out.println( modelImportList.toString());
			
			
			oo.addAll( modelImportList);
		}
		
		return objs;
	}
	public static String capitalize(String str) {
	      int strLen;
	      if (str == null || (strLen = str.length()) == 0) {
	          return str;
	      }
	      return new StringBuffer(strLen)
	          .append(Character.toTitleCase(str.charAt(0)))
	          .append(str.substring(1))
	          .toString();
	  }
	@Override
	public String apiFileFolder() {
		return outputFolder + "/" + sourceFolder + "/" + apiPackage().replace( '.', '/');
	}

	@Override
	public String apiFilename( String templateName, String tag) {
		String result = super.apiFilename( templateName, tag);

		if ( templateName.endsWith( "Impl.mustache")) {
			String output = System.getProperty( "swagger.codegen.jaxrs.impl.source");
			if ( null != output) {
				int ix = result.lastIndexOf( '/');
				result = result.substring( 0, ix) + "/impl" + result.substring( ix, result.length() - 5) + "ServiceImpl.java";
			}
		}
		else if ( templateName.endsWith( "Factory.mustache")) {
			int ix = result.lastIndexOf( '/');
			result = result.substring( 0, ix) + "/factories" + result.substring( ix, result.length() - 5) + "ServiceFactory.java";
		}
		else if ( templateName.endsWith( "Service.mustache")) {
			int ix = result.lastIndexOf( '/');
			result = result.substring( 0, ix) + "/services" + result.substring( ix, result.length() - 5) + "Service.java";
		}
		else if ( templateName.endsWith( "api.mustache")) {
			String output = System.getProperty( "swagger.codegen.jaxrs.api.source");
			if ( null != output) {
				int ix = result.lastIndexOf( '/');
				result = result.substring( 0, ix) + "/api" + result.substring( ix, result.length() - 5) + ".java";
			}
		}
		else if ( templateName.equals( "DAO.mustache")) {
			int ix = result.lastIndexOf( '/');
			result = result.substring( 0, ix) + "/dao" + result.substring( ix, result.length() - 11) + "DAO.java";
		}
		return result;
	}
	
	// Custom functions not based in Swagger:

	private void processModel( Map< String, Object> modelInfo) {
		DaoModel model = ( DaoModel) modelInfo.get( "model");

		for ( CodegenProperty property : model.vars) {
			processProperty( ( THDProperties) property);

			if ( property.isEnum && property.defaultValue != null && !"null".equals( property.defaultValue)) {
				property.defaultValue = property.datatypeWithEnum + "." + property.defaultValue;
			}
		}
		
		if ( model.modelJson.contains( "x-useDao")) {
			System.out.println("~~~ " + Json.pretty( model.modelJson));
			DAODetailsDTO daoEnt = daoDetailsParser.createObject( model.modelJson, DAODetailsDTO.class);
			
			System.out.println("\tDaoEnt: " + daoEnt.getDataSource());
			System.out.println("\tDAOSelectors:");
			System.out.println("\t\tInsert:");
			System.out.println("\t\t\tJNDI: " + daoEnt.getDaoSelectors().getJndi());
			System.out.println("\t\t\tCustom: " + daoEnt.getDaoSelectors().getCustomJndi());
			System.out.println("\t\t\t" + daoEnt.getDaoSelectors().getInsertSelector());
			
			// model.daoName =
			model.useDao = true;
			model.jndi = Arrays.asList( ""); // buildJndiEnum( model, daoEnt.getDaoSelectors().getJndi()); // daoEnt.getDaoSelectors().getJndi();
			model.insertFunctions = buildDaoSelectors( daoEnt);

			buildJndiEnum( model, daoEnt.getDaoSelectors().getJndi());
			
			modelImportsForDao = new HashMap< String, Object>();
			modelImportsForDao.put( "import", daoPackage() + "." + model.classname + "DAO");
			
			modelImportList.add( modelImportsForDao);
		}
		
	}
	
	private List< Object> buildDaoSelectors( DAODetailsDTO daoEnt) {
		List< Object> rst = new ArrayList< Object>();
		
		for( Entry< String, Object> ent : daoEnt.getDaoSelectors().getInsertSelector().entrySet()) {
			Map< String, Object> func = new HashMap< String, Object>();
			func.put( "function", ((Map<String, Object>)ent.getValue()).get( "dao"));
			func.put( "selector", ((Map<String, Object>)ent.getValue()).get( "selector"));
			func.put( "debug", ((Map<String, Object>)ent.getValue()).get( "debug"));
			
			
			
			List< Object> input = ( List< Object>) ((Map<String, Object>)ent.getValue()).get( "params");
			List< Object> parametersList = new ArrayList< Object>();
			System.out.println( "OMG: " + input);
			
			if ( null != input) {
				for( Object o : input) {
					Map< String, Object> in = (Map< String, Object>)o;
					System.out.println( "~~ " + o);
					
					for( Entry< String, Object> keySetI : in.entrySet()) {
						parametersList.add( new HashMap< String, Object>() { { put("key", keySetI.getKey()); put("value", keySetI.getValue()); }});

					}
					
				}

				func.put( "parameters", parametersList);
			}
			rst.add( func);
		}
		
		return rst;
	}
	
	private void buildJndiEnum(DaoModel model, List< String> list ) {
		// see JavaClientCodegen.postProcessModels for a sudo smoother approach to doing enums.  
		
		/**
		 * "allowableValues" : {
		 * 	"values" : [ one, two, three ]
		 * }
		 */
		
		Map< String, Object> allowableValues = new HashMap< String, Object>();
		allowableValues.put( "values", list);
		
		
		Map< String, Object> keyValueMapping;
		List< Object> allowableList = new ArrayList< Object>();
		
		for( String s : list) { 
			keyValueMapping = new HashMap< String, Object>();
			
			String name = s.replaceAll( "\\.", "_").replaceAll( "_$", "");
			String value = s;
			
			keyValueMapping.put( "name", name);
			keyValueMapping.put( "value", value);
			
			allowableList.add( keyValueMapping);
		}
		
		allowableValues.put( "enumVars", allowableList);
		
		// for( String li : list) { String nLi = li.replaceAll("\\.", "_"); newList.add( nLi); }
		
//		keyValueMapping = new HashMap< String, Object>();
//		keyValueMapping.put( "values", newList);
//		allowableList.add( keyValueMapping);
		
		model.isModelEnum = true;
//		model.allowableValues = allowableList;
		
		model.allowableValues = allowableValues;
	}

	private void processProperty( THDProperties property) {
		List< String> params = new ArrayList< String>();
		if ( property.required != null) {
			params.add( "required=" + property.required);
		}
		
		// if ( property.jsonSchema.contains("x-")) : check for any X-^ variables.
		String jsonString = property.jsonSchema;
		JSONParser< SwaggerJsonDTO> jParser = new JSONParser< SwaggerJsonDTO>();
		System.out.println("JsonString: " + jsonString);
		SwaggerJsonDTO dto = jParser.createObject( jsonString, SwaggerJsonDTO.class);
		
		if ( dto.isShort()) {
			property.isShort = true;
			property.datatype = "short";
			property.datatypeWithEnum = "short";
			property.defaultValue = "-1";
		}
		
		if ( null != dto.getDaoElement()) {	property.daoElement = dto.getDaoElement(); }
		if ( null != dto.getDbColumn()) { property.dbColumn = dto.getDbColumn(); }
		
	}
	
	private String daoPackage() {
		return (modelPackage().replaceAll("\\.model$", "\\.dao"));
	}
}
