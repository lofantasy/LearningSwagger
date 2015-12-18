package com.domain.swagger.dto;

import io.swagger.codegen.CodegenModel;

import java.util.List;
import java.util.Map;

public class DaoModel extends CodegenModel {
	public boolean useDao;
	public String daoName;
	public String dbName;
	
	public boolean debug = false;
	public List< String> jndi;
	public List< String> customJndi;
	public List< Object> insertFunctions;
	
	
	public boolean isModelEnum = false;
	public Map< String, Object> allowableValues;
}
