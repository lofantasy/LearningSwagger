/**
 * 
 */
package com.domain.swagger.dto;

import io.swagger.codegen.CodegenModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lo
 *
 */
public class MicroservicesModel extends CodegenModel {

	public boolean useDao;
	public String daoName;
	public String dbName;
	
	public boolean debug = false;
	public List< String> jndi;
	public List< String> customJndi;
	
	public List< Object> readFunctions;
	public List< Object> insertFunctions;
	public List< Object> updateFunctions;
	public List< Object> deleteFunctions;
	
	public boolean isModelEnum = false;
	public Map< String, Object> allowableValues;
}
