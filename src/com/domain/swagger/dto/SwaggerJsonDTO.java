/**
 * 
 */
package com.domain.swagger.dto;

import com.google.gson.annotations.SerializedName;

/**
 * @author lo
 *
 */
public class SwaggerJsonDTO {

	private String type;
	private String format;
	private String description;
	@SerializedName( "x-dbColumn")
	private String dbColumn;
	@SerializedName( "x-daoElement")
	private String daoElement;
	@SerializedName( "x-toShort")
	private boolean isShort;

	public String getType() {
		return type;
	}

	public void setType( String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat( String format) {
		this.format = format;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( String description) {
		this.description = description;
	}

	public String getDbColumn() {
		return dbColumn;
	}

	public void setDbColumn( String dbColumn) {
		this.dbColumn = dbColumn;
	}

	public String getDaoElement() {
		return daoElement;
	}

	public void setDaoElement( String daoElement) {
		this.daoElement = daoElement;
	}

	public boolean isShort() {
		return isShort;
	}

	public void setShort( boolean isShort) {
		this.isShort = isShort;
	}

}