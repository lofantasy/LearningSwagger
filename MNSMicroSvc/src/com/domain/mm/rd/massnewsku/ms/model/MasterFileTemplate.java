package com.domain.mm.rd.massnewsku.ms.model;

import com.domain.mm.rd.massnewsku.ms.model.MasterFileTemplateDataText;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;



/**
 * Data from the MMD_FILE_TMPL Table
 **/
@ApiModel(description = "Data from the MMD_FILE_TMPL Table")
@javax.annotation.Generated(value = "class com.domain.swagger.MicroServicesGenerator", date = "2015-12-17T19:43:39.139-05:00")
public class MasterFileTemplate  {
  
  private Integer ownerApplicationId = null;
  private String columnHeaderText = null;
  private String dataTypeIndicator = null;
  private short minimumCharacterCount = -1;
  private short maximumCharacterCount = -1;
  private String fieldName = null;
  private String dataText = null;
  private MasterFileTemplateDataText dataTextDetails = null;

  
  /**
   * Application ID of this Template.
   **/
  @ApiModelProperty(value = "Application ID of this Template.")
  @JsonProperty("ownerApplicationId")
  public Integer getOwnerApplicationId() {
    return ownerApplicationId;
  }
  public void setOwnerApplicationId(Integer ownerApplicationId) {
    this.ownerApplicationId = ownerApplicationId;
  }

  
  /**
   * Name of the expected Header in the template
   **/
  @ApiModelProperty(value = "Name of the expected Header in the template")
  @JsonProperty("columnHeaderText")
  public String getColumnHeaderText() {
    return columnHeaderText;
  }
  public void setColumnHeaderText(String columnHeaderText) {
    this.columnHeaderText = columnHeaderText;
  }

  
  /**
   * Expected data type of the column
   **/
  @ApiModelProperty(value = "Expected data type of the column")
  @JsonProperty("dataTypeIndicator")
  public String getDataTypeIndicator() {
    return dataTypeIndicator;
  }
  public void setDataTypeIndicator(String dataTypeIndicator) {
    this.dataTypeIndicator = dataTypeIndicator;
  }

  
  /**
   * Minimun amount of characters allowed
   **/
  @ApiModelProperty(value = "Minimun amount of characters allowed")
  @JsonProperty("minimumCharacterCount")
  public short getMinimumCharacterCount() {
    return minimumCharacterCount;
  }
  public void setMinimumCharacterCount(short minimumCharacterCount) {
    this.minimumCharacterCount = minimumCharacterCount;
  }

  
  /**
   * Maximun amount of characters allowed
   **/
  @ApiModelProperty(value = "Maximun amount of characters allowed")
  @JsonProperty("maximumCharacterCount")
  public short getMaximumCharacterCount() {
    return maximumCharacterCount;
  }
  public void setMaximumCharacterCount(short maximumCharacterCount) {
    this.maximumCharacterCount = maximumCharacterCount;
  }

  
  /**
   * Not sure, another team made this.
   **/
  @ApiModelProperty(value = "Not sure, another team made this.")
  @JsonProperty("fieldName")
  public String getFieldName() {
    return fieldName;
  }
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  
  /**
   * JSON String of the row details
   **/
  @ApiModelProperty(value = "JSON String of the row details")
  @JsonProperty("dataText")
  public String getDataText() {
    return dataText;
  }
  public void setDataText(String dataText) {
    this.dataText = dataText;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("dataTextDetails")
  public MasterFileTemplateDataText getDataTextDetails() {
    return dataTextDetails;
  }
  public void setDataTextDetails(MasterFileTemplateDataText dataTextDetails) {
    this.dataTextDetails = dataTextDetails;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MasterFileTemplate masterFileTemplate = (MasterFileTemplate) o;
    return Objects.equals(ownerApplicationId, masterFileTemplate.ownerApplicationId) &&
        Objects.equals(columnHeaderText, masterFileTemplate.columnHeaderText) &&
        Objects.equals(dataTypeIndicator, masterFileTemplate.dataTypeIndicator) &&
        Objects.equals(minimumCharacterCount, masterFileTemplate.minimumCharacterCount) &&
        Objects.equals(maximumCharacterCount, masterFileTemplate.maximumCharacterCount) &&
        Objects.equals(fieldName, masterFileTemplate.fieldName) &&
        Objects.equals(dataText, masterFileTemplate.dataText) &&
        Objects.equals(dataTextDetails, masterFileTemplate.dataTextDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ownerApplicationId, columnHeaderText, dataTypeIndicator, minimumCharacterCount, maximumCharacterCount, fieldName, dataText, dataTextDetails);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class MasterFileTemplate {\n");
    
    sb.append("  ownerApplicationId: ").append(ownerApplicationId).append("\n");
    sb.append("  columnHeaderText: ").append(columnHeaderText).append("\n");
    sb.append("  dataTypeIndicator: ").append(dataTypeIndicator).append("\n");
    sb.append("  minimumCharacterCount: ").append(minimumCharacterCount).append("\n");
    sb.append("  maximumCharacterCount: ").append(maximumCharacterCount).append("\n");
    sb.append("  fieldName: ").append(fieldName).append("\n");
    sb.append("  dataText: ").append(dataText).append("\n");
    sb.append("  dataTextDetails: ").append(dataTextDetails).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}


