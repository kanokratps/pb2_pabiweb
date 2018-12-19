package pb.repo.admin.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.apache.ibatis.session.SqlSession;

import pb.repo.admin.model.MainWorkflowHistoryModel;
import pb.repo.admin.model.MainWorkflowNextActorModel;
import pb.repo.admin.model.SubModuleModel;


public interface SubModuleService {
	
	public List<Map<String, Object>> listWorkflowPath(String id, String lang);
	
	public void update(SqlSession session, SubModuleModel model) throws Exception;
	public Object get(String id, String lang);
	public Object getForWfPath(String id, String lang);
	public String getWorkflowName() throws Exception;
	public String getWorkflowDescription(SubModuleModel paramModel) throws Exception;
	public String getWorkflowDescriptionEn(SubModuleModel paramModel) throws Exception;
	
	public Map<String, Object> convertToMap(SubModuleModel model);
	public String getSubModuleType();
	
	public void setWorkflowParameters(Map<QName, Serializable> parameters, SubModuleModel model, List<NodeRef> docList, List<NodeRef> attachDocList);
	
	public String getActionCaption(String action, String lang);
	
	public List<MainWorkflowNextActorModel> listNextActor(SubModuleModel model);
	public List<String> listRelatedUser(SubModuleModel model);
	
	public String getFirstComment(SubModuleModel model);
	public String getFirstStatus();
	
	public String getNextActionInfo(Object obj, String lang);
	
    public String getModelUri();
    public String getWfUri();
    public String getModelPrefix();
    public String getDocDesc();
    public MainWorkflowHistoryModel getReqByWorkflowHistory(SubModuleModel model);
    public MainWorkflowHistoryModel getAppByWorkflowHistory(SubModuleModel model);
    public List<String> getSpecialUserForAddPermission(SubModuleModel model);
    public List<String> getSpecialGroupForAddPermission();
    
	public QName getPropNextReviewers();
	public QName getPropDescEn();
	
//	public Map<String, String> getBossMap(String docType, String type, String costCenter, String reqUser, Double amount);
//	public Map<String, String> getBossMap(String docType, SubModuleModel model) throws Exception;
	
	public Boolean addPermissionToAttached();
	
	public void setFirstTaskAssignee(Map<QName, Serializable> parameters, SubModuleModel model);
	
	public void prepareModelForWfDesc(SubModuleModel model, String lang);
	
	public Map<String,Object> getWorkflowPathParamters(SubModuleModel model);
	public Double getWorkflowPathParamterTotalForProject(SubModuleModel model);
	public Double getWorkflowPathParamterTotalForSection(SubModuleModel model);
	
	public String getMessage(String code);
}
