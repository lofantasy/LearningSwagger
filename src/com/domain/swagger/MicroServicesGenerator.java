/**
 * 
 */
package com.domain.swagger;

import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenModelFactory;
import io.swagger.codegen.CodegenModelType;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.CodegenResponse;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.JavaClientCodegen;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.domain.swagger.dto.DAODetailsDTO;
import com.domain.swagger.dto.MicroservicesModel;
import com.domain.swagger.dto.SwaggerJsonDTO;
import com.domain.swagger.properties.MicroservicesProperties;
import com.domain.swagger.utils.JSONParser;

/**
 * MicroServices generator based off the JaxRS Generator class from Swagger-Codegen
 */
public class MicroServicesGenerator extends JavaClientCodegen implements CodegenConfig {
	protected String title = "Micro Services";

	private JSONParser< DAODetailsDTO> daoDetailsParser = new JSONParser< DAODetailsDTO>();

	public MicroServicesGenerator() {
		super.processOpts();

		sourceFolder = "src/gen/java";
		invokerPackage = "com.domain.mm.rd.massnewsku.ms";
		artifactId = "domain-jaxrs-server";

		outputFolder = System.getProperty( "swagger.codegen.jaxrs.genfolder", "generated-code/domainMicroService");

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
		
		// Use a custom created Model/Properties class to update/extend the Swagger Model class:
		CodegenModelFactory.setTypeMapping( CodegenModelType.MODEL, MicroservicesModel.class);
		CodegenModelFactory.setTypeMapping( CodegenModelType.PROPERTY, MicroservicesProperties.class);

	}

	public CodegenType getTag() {
		return CodegenType.SERVER;
	}

	public String getName() {
		return "jaxrs";
	}

	public String getHelp() {
		return "Generates a Micro Services API Set.";
	}

	@Override
	public void processOpts() {
		super.processOpts();

		supportingFiles.clear();
		supportingFiles.add( new SupportingFile( "pom.mustache", "", "pom.xml"));
		supportingFiles.add( new SupportingFile( "README.mustache", "", "README.md"));
		supportingFiles.add( new SupportingFile( "ApiException.mustache", ( sourceFolder + '/' + apiPackage).replace( ".", "/"), "ApiException.java"));
		supportingFiles.add( new SupportingFile( "ApiOriginFilter.mustache", ( sourceFolder + '/' + apiPackage).replace( ".", "/"), "ApiOriginFilter.java"));
		supportingFiles.add( new SupportingFile( "ApiResponseMessage.mustache", ( sourceFolder + '/' + apiPackage).replace( ".", "/"), "ApiResponseMessage.java"));
		supportingFiles.add( new SupportingFile( "NotFoundException.mustache", ( sourceFolder + '/' + apiPackage).replace( ".", "/"), "NotFoundException.java"));
		supportingFiles.add( new SupportingFile( "StringUtil.mustache", (sourceFolder + '/' + invokerPackage).replace(".", "/"), "StringUtil.java"));
		supportingFiles.add( new SupportingFile( "web.mustache", ( "src/main/webapp/WEB-INF"), "web.xml"));
		

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
		if ( swagger != null) {
			if ( "/".equals( swagger.getBasePath())) {
				swagger.setBasePath( "");
			}
			if ( swagger.getPaths() != null) {
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
			}
		}
	}

	@Override
	public Map< String, Object> postProcessModels( Map< String, Object> objs) {
		@SuppressWarnings( "unchecked")
		List< Object> models = ( List< Object>) objs.get( "models");
		for ( Object _mo : models) {
			@SuppressWarnings( "unchecked")
			Map< String, Object> mo = ( Map< String, Object>) _mo;
			CodegenModel cm = ( CodegenModel) mo.get( "model");
			for ( CodegenProperty var : cm.vars) {
				// handle default value for enum, e.g. available => StatusEnum.available
				if ( var.isEnum && var.defaultValue != null && !"null".equals( var.defaultValue)) {
					var.defaultValue = var.datatypeWithEnum + "." + var.defaultValue;
				}
			}
		}
		return objs;
	}

	public Map< String, Object> postProcessOperations( Map< String, Object> objs) {
		@SuppressWarnings( "unchecked")
		Map< String, Object> operations = ( Map< String, Object>) objs.get( "operations");
		if ( operations != null) {
			@SuppressWarnings( "unchecked")
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
			
			// TODO: figure out why the imports are getting confused here. 
			
		}
		return objs;
	}

	public Map< String, Object> postProcessSupportingFileData( Map< String, Object> objs) {
		if ( objs.containsKey( "models")) {
			List< Map< String, String>> imports = new ArrayList< Map<String,String>>();
			
			List< Object> inputModels = (ArrayList<Object>)objs.get("models");
			
			for( Object mod : inputModels) {
				MicroservicesModel model = ( MicroservicesModel) ((Map<String, Object>)mod).get( "model");
				
				if ( model.useDao) {
					model.imports = new HashSet<String>();
					model.imports.add( "com.domain.ta.aa.dao.exceptions.QueryException");
					model.imports.add( "org.apache.log4j.Logger");
					model.imports.add( "java.util.ArrayList");
					model.imports.add( "java.util.List");
					
					// TODO: need to update this to better handle the imports. 
					/*
					Map<String, Object> models = new HashMap< String, Object>();
					models.put("package", modelPackage().replaceAll( "\\.model", ""));
					
					models.put("imports", imports);
					List<Object> modelsList = new ArrayList<Object>();
					
					
					modelsList.add( mod);
					models.put( "models", modelsList);
					daoGen.writeDaoStubForModel( this, daoClassPath, dm.classname, models);
	*/
				}
			}
		}
		
		return objs;
	}
	
	@Override
	public String toApiName( String name) {
		if ( name.length() == 0) { return "DefaultApi"; }
		name = sanitizeName( name);
		return camelize( name) + "Api";
	}

	@Override
	public String apiFilename( String templateName, String tag) {

		String result = super.apiFilename( templateName, tag);

		if ( templateName.endsWith( "Impl.mustache")) {
			int ix = result.lastIndexOf( '/');
			result = result.substring( 0, ix) + "/impl" + result.substring( ix, result.length() - 5) + "ServiceImpl.java";

			String output = System.getProperty( "swagger.codegen.jaxrs.impl.source");
			if ( output == null) {
				output = "src" + File.separator + "main" + File.separator + "java";
			}
			
			result = result.replace( apiFileFolder(), implFileFolder( output));
		}
		else if ( templateName.endsWith( "Factory.mustache")) {
			int ix = result.lastIndexOf( '/');
			result = result.substring( 0, ix) + "/factories" + result.substring( ix, result.length() - 5) + "ServiceFactory.java";

			String output = System.getProperty( "swagger.codegen.jaxrs.impl.source");
			if ( output != null) {
				result = result.replace( apiFileFolder(), implFileFolder( output));
			}
		}
		else if ( templateName.endsWith( "Service.mustache")) {
			int ix = result.lastIndexOf( '/');
			result = result.substring( 0, ix) + "/services" + result.substring( ix, result.length() - 5) + "Service.java";
		}
		else if ( templateName.endsWith( "api.mustache")) {
			String output = System.getProperty( "swagger.codegen.jaxrs.api.source");
			if ( null != output) {
				// result = output;
				int ix = result.lastIndexOf( '/');
				result = result.substring( 0, ix) + "/api" + result.substring( ix, result.length() - 5) + ".java";
			}
		}


		return result;
	}

	private String implFileFolder( String output) {
		return outputFolder + "/src/" + apiPackage().replace( '.', '/');
	}

	public boolean shouldOverwrite( String filename) {
		return super.shouldOverwrite( filename) && !filename.endsWith( "ServiceImpl.java") && !filename.endsWith( "ServiceFactory.java");
	}
	
	
	private void processModel( Map< String, Object> modelInfo) {
		MicroservicesModel model = ( MicroservicesModel) modelInfo.get( "model");
		
		for ( CodegenProperty property : model.vars) {
			processProperty( ( MicroservicesProperties) property);

			if ( property.isEnum && property.defaultValue != null && !"null".equals( property.defaultValue)) {
				property.defaultValue = property.datatypeWithEnum + "." + property.defaultValue;
			}
		}
		
		// TODO if model.modenJson.contains( "x-useDao") -- create a Actual DTO of this object (use SwaggerJsonDTO). 
		// DAODetailsDTO = daoEnt = daoDetailsParser.createObject( model.modelJson, DAODetailsDTO.class);
		
		// buildJndiEnum
	}
	
	
	/**
	 * Parse the properties jsonString to build the X-^ Variable for setting short and dao names. 
	 * @param property
	 */
	private void processProperty( MicroservicesProperties property) {
		
		String jsonString = property.jsonSchema;
		JSONParser< SwaggerJsonDTO> jp = new JSONParser< SwaggerJsonDTO>();
		SwaggerJsonDTO dto = jp.createObject( jsonString, SwaggerJsonDTO.class);
		
		if ( dto.isShort()) {
			property.isShort = true;
			property.datatype = "short";
			property.datatypeWithEnum = "short";
			property.defaultValue = "-1";
		}
		
		if ( null != dto.getDaoElement()) {	property.daoElement = dto.getDaoElement(); }
	}
	
	private List< Object> buildDaoSelectors( DAODetailsDTO daoEnt) {
		// TODO:
		
		return null;
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
}
