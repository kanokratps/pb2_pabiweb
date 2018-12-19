package pb.repo.admin.model;


public class MainSectionModel {
	
	Long id;
	String description;
	String name;
	Integer divisionId;
	Integer sectorId;
	Integer subSectorId;
	Integer orgId;
	Integer costcenterId;
	  
	private Long totalRowCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}

	public Integer getSectorId() {
		return sectorId;
	}

	public void setSectorId(Integer sectorId) {
		this.sectorId = sectorId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	
	public Integer getSubSectorId() {
		return subSectorId;
	}

	public void setSubSectorId(Integer subSectorId) {
		this.subSectorId = subSectorId;
	}

	public Integer getCostcenterId() {
		return costcenterId;
	}

	public void setCostcenterId(Integer costcenterId) {
		this.costcenterId = costcenterId;
	}

	public Long getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(Long totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

}
