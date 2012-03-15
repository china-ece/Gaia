package com.chinaece.gaia.types;

import java.util.ArrayList;
import java.util.Collection;

import com.chinaece.gaia.types.documentitem.BranchType;
import com.chinaece.gaia.types.documentitem.ItemType;

 public class DocumentType implements GaiaType{
	 
	protected int version;
	 
	protected ArrayList<ItemType> items;
	
	protected boolean editable;
	
	protected ArrayList<BranchType> flowPath;
	
	protected boolean submitable;
	
	protected boolean choiceflag;
	
	protected String currNodeid;
	
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	public String getCurrNodeid() {
		return currNodeid;
	}

	public void setCurrNodeid(String currNodeid) {
		this.currNodeid = currNodeid;
	}

	public boolean isChoiceflag() {
		return choiceflag;
	}

	public void setChoiceflag(boolean choiceflag) {
		this.choiceflag = choiceflag;
	}

	public void setItems(ArrayList<ItemType> items) {
		this.items = items;
	}

	public boolean isSubmitable() {
		return submitable;
	}

	public void setSubmitable(boolean submitable) {
		this.submitable = submitable;
	}

	public ArrayList<ItemType> getItems() {
		return items;
	}

	public void setItems(Collection<ItemType> collection) {
		this.items = (ArrayList<ItemType>) collection;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public ArrayList<BranchType> getFlowPath() {
		return flowPath;
	}

	public void setFlowPath(ArrayList<BranchType> flowPath) {
		this.flowPath = flowPath;
	}
}
