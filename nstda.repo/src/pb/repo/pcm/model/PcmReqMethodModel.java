package pb.repo.pcm.model;


public class PcmReqMethodModel {
	
	Long id;
	String obj;
	String method;
	String docType;
	String cond1;
	String cond2;
	String priceFrom;
	String priceTo;
	
	private Long totalRowCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObj() {
		return obj;
	}

	public void setObj(String obj) {
		this.obj = obj;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getCond1() {
		return cond1;
	}

	public void setCond1(String cond1) {
		this.cond1 = cond1;
	}

	public String getCond2() {
		return cond2;
	}

	public void setCond2(String cond2) {
		this.cond2 = cond2;
	}

	public String getPriceFrom() {
		return priceFrom;
	}

	public void setPriceFrom(String priceFrom) {
		this.priceFrom = priceFrom;
	}

	public String getPriceTo() {
		return priceTo;
	}

	public void setPriceTo(String priceTo) {
		this.priceTo = priceTo;
	}

	public Long getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(Long totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

}
