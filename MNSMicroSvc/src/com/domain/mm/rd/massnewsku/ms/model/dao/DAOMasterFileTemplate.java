package com.domain.mm.rd.massnewsku.ms;



import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;



/**
* Data from the MMD_FILE_TMPL Table
**/
@ApiModel(description = "Data from the MMD_FILE_TMPL Table")
@javax.annotation.Generated(value = "", date = "")
public class MasterFileTemplate  {

	public Integer ownerApplicationId = null;
	public String columnHeaderText = null;
	public String dataTypeIndicator = null;
	public short minimumCharacterCount = -1;
	public short maximumCharacterCount = -1;
	public String fieldName = null;
	public String dataText = null;
	public MasterFileTemplateDataText dataTextDetails = null;


}


