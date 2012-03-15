package com.chinaece.gaia.types;

import java.io.Serializable;
import java.util.ArrayList;

public class BossTrackingType  implements GaiaType,Serializable{
	
	private static final long serialVersionUID = -5772724268794155583L;
	
	private String id;
	
	private String feedbackdepart;
	
	private String dateenter;
	
	private String item;
	
	private String datedone;
	
	private String depart;
	
	private String describe;
	
	private String nextor;
	
	private String identify;
	
	private String modality;
	
	private String person;
	
	private String date;
	
	private String isdo;
	
	private String secretary;
	
	private String leadview;
	
	private String num;
	
	private String title;
	
	private ArrayList<Child> childs = new ArrayList<BossTrackingType.Child>();
	
	public ArrayList<Child> getChilds() {
		return childs;
	}

	public void setChilds(ArrayList<Child> childs) {
		this.childs = childs;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFeedbackdepart() {
		return feedbackdepart;
	}

	public void setFeedbackdepart(String feedbackdepart) {
		this.feedbackdepart = feedbackdepart;
	}

	public String getDateenter() {
		return dateenter;
	}

	public void setDateenter(String dateenter) {
		this.dateenter = dateenter;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getDatedone() {
		return datedone;
	}

	public void setDatedone(String datedone) {
		this.datedone = datedone;
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getNextor() {
		return nextor;
	}

	public void setNextor(String nextor) {
		this.nextor = nextor;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIsdo() {
		return isdo;
	}

	public void setIsdo(String isdo) {
		this.isdo = isdo;
	}

	public String getSecretary() {
		return secretary;
	}

	public void setSecretary(String secretary) {
		this.secretary = secretary;
	}

	public String getLeadview() {
		return leadview;
	}

	public void setLeadview(String leadview) {
		this.leadview = leadview;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	
	public void addChild(String id,String follow){
		Child child = new Child();
		child.id = id;
		child.follow = follow;
		childs.add(child);
	}

	public class Child implements Serializable{
		private static final long serialVersionUID = 1586688295051425789L;

		private String id;
		 
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getFollow() {
			return follow;
		}

		public void setFollow(String follow) {
			this.follow = follow;
		}

		private String follow;
	}
	
	

}
