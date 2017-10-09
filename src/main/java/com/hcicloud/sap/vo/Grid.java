package com.hcicloud.sap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Grid implements Serializable {
	private Long total = Long.valueOf(0L);
	private List rows = new ArrayList();
	private List aaData = new ArrayList();
	private long iTotalRecords;
	private long iTotalDisplayRecords;
	private List nodes=new ArrayList();
	private List links=new ArrayList();
	private String message;
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTotal() {
		return this.total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List getRows() {
		return this.rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public List getAaData() {
		return aaData;
	}

	public void setAaData(List aaData) {
		this.aaData = aaData;
	}

	public long getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(long iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public long getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public List getNodes() {
		return nodes;
	}

	public void setNodes(List nodes) {
		this.nodes = nodes;
	}

	public List getLinks() {
		return links;
	}

	public void setLinks(List links) {
		this.links = links;
	}
	
	
	
}
