package com.siberhus.ngai.example.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.siberhus.ngai.model.AbstractAuditableModel;

@Entity
@Table(name="PRODUCTS")
public class Product extends AbstractAuditableModel<Long, User>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="SKU_CODE")
	private String skuCode;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="PRICE")
	private BigDecimal price;
	
	@Column(name="DESCRIPTION")
	private String description;

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
