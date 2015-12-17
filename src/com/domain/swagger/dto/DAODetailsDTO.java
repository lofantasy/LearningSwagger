/**
 * 
 */
package com.domain.swagger.dto;

import com.google.gson.annotations.SerializedName;

/**
 * @author shadowwa
 *
 */
public class DAODetailsDTO {
	@SerializedName( "x-daoDataSource")
	public String dataSource;
	
	@SerializedName("x-daoSelectors")
	public DAOSelectorsDTO daoSelectors;

}
