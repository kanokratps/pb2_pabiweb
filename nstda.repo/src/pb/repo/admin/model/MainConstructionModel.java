package pb.repo.admin.model;


public class MainConstructionModel {
	
	Long id;
	String construction;
	String constructionTh;
	String org;
	String orgTh;
	Integer sectionId;
	String costcenter;
	  
	private Long totalRowCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getConstruction() {
		return construction;
	}

	public void setConstruction(String construction) {
		this.construction = construction;
	}

	public String getConstructionTh() {
		return constructionTh;
	}

	public void setConstructionTh(String constructionTh) {
		this.constructionTh = constructionTh;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getOrgTh() {
		return orgTh;
	}

	public void setOrgTh(String orgTh) {
		this.orgTh = orgTh;
	}

	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

	public String getCostcenter() {
		return costcenter;
	}

	public void setCostcenter(String costcenter) {
		this.costcenter = costcenter;
	}

	public Long getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(Long totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

}
