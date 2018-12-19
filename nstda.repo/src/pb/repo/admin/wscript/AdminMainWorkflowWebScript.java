package pb.repo.admin.wscript;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.alfresco.httpclient.Response;
import org.alfresco.repo.web.scripts.workflow.WorkflowModelBuilder;
import org.alfresco.repo.workflow.WorkflowModel;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.cmr.workflow.WorkflowService;
import org.alfresco.service.cmr.workflow.WorkflowTask;
import org.alfresco.service.cmr.workflow.WorkflowTaskQuery;
import org.alfresco.service.cmr.workflow.WorkflowTaskQuery.OrderBy;
import org.alfresco.service.cmr.workflow.WorkflowTaskState;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.ISO8601DateFormat;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.ScriptRemote;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonDateTimeUtil;
import pb.common.util.CommonUtil;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminWorkflowService;
import pb.repo.admin.service.MainWorkflowService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.HttpMethod;
import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class AdminMainWorkflowWebScript {
	
	private static Logger log = Logger.getLogger(AdminMainWorkflowWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/admin/main/workflow";
	
    public static final String PARAM_AUTHORITY = "authority";
    public static final String PARAM_STATE = "state";
    public static final String PARAM_PRIORITY = "priority";
    public static final String PARAM_DUE_BEFORE = "dueBefore";
    public static final String PARAM_DUE_AFTER = "dueAfter";
    public static final String PARAM_PROPERTIES = "properties";
    public static final String PARAM_POOLED_TASKS = "pooledTasks";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_EXCLUDE = "exclude";
    public static final String PARAM_OTHER = "other";
    
    public static final String PARAM_MAX_ITEMS = "maxItems";
    public static final String PARAM_SKIP_COUNT = "skipCount";
    
    public static final int DEFAULT_MAX_ITEMS = -1;
    
    public static final int DEFAULT_SKIP_COUNT = 0;
    
    private WorkflowTaskDueAscComparator taskComparator = new WorkflowTaskDueAscComparator();
    
	@Autowired
	AuthenticationService authenticationService;

	@Autowired
	NamespaceService namespaceService;
	
	@Autowired
	NodeService nodeService;
	
	@Autowired
	WorkflowService workflowService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	DictionaryService dictionaryService;
	
	@Autowired
	AdminWorkflowService adminWorkflowService;
	
	@Autowired
	MainWorkflowService mainWorkflowService;
	
	@Autowired
	AdminMasterService masterService;
	
	  /**
	   * Handles the "list" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
	   * and request handling objects.
	   * 
	   * @param t : type
	   * @param s : searchTerm
	   * @param m : My Task
	   * @param e : employee
	   * @param response
	   * @throws Exception
	   */
	  @Uri(URI_PREFIX+"/list")
	  public void handleList(@RequestParam(required=true) final String e
			  			   , @RequestParam(required=false) final String et
			  			   , @RequestParam(required=false) final String t
			  			   , @RequestParam(required=false) final String s
			 			   , @RequestParam(required=false) final String sort
			  			   , @RequestParam(required=false) final Integer start
			  			   , @RequestParam(required=false) final Integer limit
			  			   , final WebScriptResponse response)  throws Exception {
	    
		  	log.info("e:"+e+", et:"+et+", t:"+t+", s:"+s);
		  
			String json = null;
			
			try {
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				
				Map<String,Object> params = new HashMap<String,Object>();
				if(e!=null && !e.equals("")) {
					params.put("e", e);
				}
				params.put("t", t);
				if (et!=null) {
					if (et.equals("1")) {
						params.put("m", "1");
					} else 
					if (et.equals("2")) {
						params.put("a", "1");
					}
				}
				
				params.put("orderBy", "docid");
				
				if (s != null && !s.equals("")) {
		    		String[] terms = s.split(" ");
		        	
		    		params.put("terms", terms);
				}
				params.put("start", start);
				params.put("limit", limit);
				
				// Order By
				StringBuffer orderBy = new StringBuffer();
				StringBuffer oriOrderBy = new StringBuffer();
				
				if (sort!=null) {
					JSONArray sortArr = new JSONArray(sort);
					for(int i=0; i<sortArr.length(); i++) {
						if (oriOrderBy.length()>0) {
							oriOrderBy.append(",");
						}
						JSONObject sortObj = sortArr.getJSONObject(i);
						oriOrderBy.append(sortObj.getString("property"));
						oriOrderBy.append(" ");
						oriOrderBy.append(sortObj.getString("direction"));
					}
				} else {
					MainMasterModel orderByModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ADMIN_WF_ORDER_BY,false);
					oriOrderBy.append(orderByModel.getFlag1());
				}
				
				String[] orders = oriOrderBy.toString().split(",");
				for(int i=0; i<orders.length; i++) {
					if (orderBy.length()>0) {
						orderBy.append(",");
					}
					
					String[] os = orders[i].trim().split(" ");
					
					String f = os[0];
					if (f.equals("wfstatus")) {
						f = "wf_status";
					} else
					if (f.equals("no")) {
						f = "docid";
					} else
					if (f.equals("desc")) {
						f = "description_";
					} else
					if (f.equals("assignee")) {
						f = "assignee_";
					} else
					if (f.equals("id")) {
						f = "activititask";
					} else
					if (f.equals("wfid")) {
						f = "activitiworkflow";
					}
					
					orderBy.append(f);
					orderBy.append((f.indexOf("_time")<0 
								 && f.indexOf("_date")<0) 
								 && !f.equalsIgnoreCase("reqdate")
								 && !f.equalsIgnoreCase("order_field") 
								 && !f.equalsIgnoreCase("total") 
								 ? " collate \"C\" " : " "
					);
					if (os.length>1) {
						orderBy.append(os[os.length-1]);
					}
				}
				
				log.info("orderBy:"+orderBy.toString());
				params.put("orderBy", orderBy.toString());
				
				List<String> oldWfId = new ArrayList<String>();
				List<Map<String,Object>> dbList = adminWorkflowService.list(params);
				for(Map<String,Object> dbMap : dbList) {
					
					if (oldWfId.indexOf(dbMap.get("activitiworkflow"))<0) {
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("wfid", dbMap.get("activitiworkflow"));
						map.put("id", dbMap.get("activititask"));
						map.put("no", dbMap.get("docid"));
						map.put("desc", dbMap.get("description_"));
						map.put("assignee", dbMap.get("assignee_"));
						map.put("requester", dbMap.get("requester"));
						map.put("preparer", dbMap.get("preparer"));
						map.put("reqdate", CommonDateTimeUtil.convertToGridDateTime((Timestamp)dbMap.get("reqdate")));
						map.put("remark", dbMap.get("remark"));
						map.put("status", dbMap.get("action") + " ("+dbMap.get("status")+")");
						
						map.put("totalrowcount", dbMap.get("totalRowCount"));
						list.add(map);
						
						oldWfId.add((String)dbMap.get("activitiworkflow"));
					}
				}
				
				json = CommonUtil.jsonSuccess(list);
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
			}
	    
	  }
	  
	  /**
	   * Handles the "list" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
	   * and request handling objects.
	   * 
	   * @param t : type
	   * @param s : searchTerm
	   * @param m : My Task
	   * @param e : employee
	   * @param response
	   * @throws Exception
	   */
//	  @Uri(URI_PREFIX+"/list")
	  public void handleLList(@RequestParam(required=true) final String e
			  			   , @RequestParam(required=false) final String m
			  			   , @RequestParam(required=false) final String t
			  			   , @RequestParam(required=false) final String s
			  			   , @RequestParam(required=false) final Integer start
			  			   , @RequestParam(required=false) final Integer limit
			  			   , final WebScriptResponse response)  throws Exception {
	    
		  	log.info("e:"+e);
		  	log.info("m:"+m);
		  	log.info("t:"+t);
		  	log.info("s:"+s);
		  
			String json = null;
			
			String properties = "bpm_description";
			String pooledTasks = null;
			String name = null;
			String exclude = "wcmwf:*";
			Integer maxItems = null;
			Integer skipCount = null;

			try {
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				
				Map<String,Object> params = new HashMap<String,Object>();
				if(e!=null && !e.equals("")) {
					params.put("e", e);
				}
				params.put("t", t);
				params.put("m", m);
				
				params.put("orderBy", "no");
				
				if (s != null && !s.equals("")) {
		    		String[] terms = s.split(" ");
		        	
		    		params.put("terms", terms);
				}
				
				List<Map<String,Object>> dbList = adminWorkflowService.list(params);
				for(Map<String,Object> dbMap : dbList) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id", dbMap.get("id"));
					map.put("no", dbMap.get("no"));
					map.put("desc", dbMap.get("desc"));
					map.put("requester", dbMap.get("requester"));
					map.put("preparer", dbMap.get("preparer"));
					map.put("reqdate", CommonDateTimeUtil.convertToGridDateTime((Timestamp)dbMap.get("reqdate")));
					map.put("remark", dbMap.get("remark"));
					map.put("status", dbMap.get("status"));
					
					list.add(map);
				}
				
				if (m!=null && m.equals("true")) {
					List<Map<String,Object>> wfList = wfList(e, null, null, null, null, properties, null, null, exclude, maxItems, skipCount);
					
					List<Map<String,Object>> newList = new ArrayList<Map<String,Object>>();

					for(Map<String,Object> map : list) {
						for(Map<String,Object> wfMap:wfList) {
							if (wfMap.get("id").equals(map.get("id"))) {
								newList.add(map);
								break;
							}
						}
					}
					
					list = newList;
				}
				
				json = CommonUtil.jsonSuccess(list);
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
			}
	    
	  }
	  
		private List<Map<String,Object>> wfList(@RequestParam(required=false) String authority,
				   @RequestParam(required=false) final String state,
				   @RequestParam(required=false) final String priority,
				   @RequestParam(required=false) final String dueBefore,
				   @RequestParam(required=false) final String dueAfter,
				   @RequestParam final String properties,
				   @RequestParam(required=false) final String pooledTasks,
				   @RequestParam(required=false) final String name,
				   @RequestParam(required=false) final String exclude,
				   @RequestParam(required=false) Integer maxItems,
				   @RequestParam(required=false) Integer skipCount
				   ) throws Exception {

			List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
			
				WorkflowModelBuilder modelBuilder = new WorkflowModelBuilder(namespaceService, nodeService, authenticationService, personService, workflowService, dictionaryService);
				
				if (maxItems==null) {
					maxItems = DEFAULT_MAX_ITEMS;
				}
				if (skipCount==null) {
					skipCount = DEFAULT_SKIP_COUNT;
				}
			 
			 Map<String, Object> filters = new HashMap<String, Object>(5);
			
//			 if (authority == null)
//			 {
//			     // ALF-11036 fix, if authority argument is omitted the tasks for the current user should be returned.
//			     authority = authenticationService.getCurrentUserName();
//			 }
			 
			 // state is also not included into filters list, for the same reason
			 WorkflowTaskState taskState = getState(state);
			 
			 // determine if pooledTasks should be included, when appropriate i.e. when an authority is supplied
			 Boolean pooledTasksOnly = getPooledTasks(pooledTasks);
			 
			 // get list of properties to include in the response
			 List<String> propertyList = getProperties(properties);
			 
			 // get filter param values
			 // Doy+ {
			 filters.put(PARAM_NAME, name);
			 // Doy+ }
			 filters.put(PARAM_PRIORITY, priority);
			 processDateFilter(dueBefore, "dueBefore", filters);
			 processDateFilter(dueAfter, "dueAfter", filters);
			 
			 if (exclude != null && exclude.length() > 0)
			 {
			     filters.put(PARAM_EXCLUDE, new ExcludeFilter(exclude));
			 }
			 
			 List<WorkflowTask> allTasks;
			
			 // default task state to IN_PROGRESS if not supplied
			 if (taskState == null)
			 {
			     taskState = WorkflowTaskState.IN_PROGRESS;
			 }
			 
			 // no workflow instance id is present so get all tasks
			 if (authority != null)
			 {
			     List<WorkflowTask> tasks = workflowService.getAssignedTasks(authority, taskState, true);
			     List<WorkflowTask> pooledTaskList = workflowService.getPooledTasks(authority, true);
			     if (pooledTasksOnly != null)
			     {
			         if (pooledTasksOnly.booleanValue())
			         {
			             // only return pooled tasks the user can claim
			             allTasks = new ArrayList<WorkflowTask>(pooledTaskList.size());
			             allTasks.addAll(pooledTaskList);
			         }
			         else
			         {
			             // only return tasks assigned to the user
			             allTasks = new ArrayList<WorkflowTask>(tasks.size());
			             allTasks.addAll(tasks);
			         }
			     }
			     else
			     {
			         // include both assigned and unassigned tasks
			         allTasks = new ArrayList<WorkflowTask>(tasks.size() + pooledTaskList.size());
			         allTasks.addAll(tasks);
			         allTasks.addAll(pooledTaskList);
			     }
			     
			     // sort tasks by due date
			     Collections.sort(allTasks, taskComparator);
			 }
			 else
			 {
			     // authority was not provided -> return all active tasks in the system
			     WorkflowTaskQuery taskQuery = new WorkflowTaskQuery();
			     taskQuery.setTaskState(taskState);
			     taskQuery.setActive(null);
			     taskQuery.setOrderBy(new OrderBy[] { OrderBy.TaskDue_Asc });
			     allTasks = workflowService.queryTasks(taskQuery);
			 }
			 
			 int totalCount = 0;
			 
//			 for(String p : propertyList) {
//				 log.info("p:"+p);
//			 }
			 
			 // Filter results
			 WorkflowTask task = null;
			 for(int i=0; i<allTasks.size(); i++)
			 {
			 	task = allTasks.get(i);
			 	
//			 	log.info("id:"+task.getId());
//			 	log.info("name:"+task.getName());
//			 	log.info("desc:"+task.getDescription());
			 	
			 	if (matches(task, filters, null, null))
			     {
			 		// Total-count needs to be based on matching tasks only, so we can't just use allTasks.size() for this
			     	totalCount++;
			     	if(totalCount > skipCount && (maxItems < 0 || maxItems > list.size()))
			     	{
			     		// Only build the actual detail if it's in the range of items we need. This will
			     		// drastically improve performance over paging after building the model
			     		list.add(buildSimple(task, propertyList));
			     	}
			     }
			 }
			 
			return list;
	  }
		
		private Map<String,Object> buildSimple(WorkflowTask task, List<String> propertyList) throws Exception {
			Map<String,Object> map = new HashMap<String,Object>();
			
			Map<QName, Serializable> props = task.getProperties();
			for(Entry<QName, Serializable> e:props.entrySet()) {
				log.info(" - "+e.getKey().getPrefixString()+","+e.getKey()+":"+e.getValue());
				
				if (e.getKey().getLocalName().equals("id")) {
					map.put("no", e.getValue());
				} else 
				if (e.getKey().getLocalName().equals("workflowStatus")) {
					map.put("status", e.getValue());
				} else 
				if (e.getKey().getLocalName().equals("objective")) {
					map.put("remark", e.getValue());
				}
			}
			
			map.put("id", task.getId());
			map.put("desc", task.getDescription());
			
			map.put("requester", task.getId());
			map.put("preparer", task.getId());
			map.put("reqdate", task.getId());
			
			return map;
		}		
		
	    /**
	     * Retrieves the list of property names to include in the response.
	     * 
	     * @param req The WebScript request
	     * @return List of property names
	     */
	    private List<String> getProperties(String propertiesStr)
	    {
	        if (propertiesStr != null)
	        {
	            return Arrays.asList(propertiesStr.split(","));
	        }
	        return null;
	    }
	    
	    /**
	     * Retrieves the pooledTasks parameter.
	     * 
	     * @param req The WebScript request
	     * @return null if not present, Boolean object otherwise
	     */
	    private Boolean getPooledTasks(String includePooledTasks)
	    {
	        Boolean result = null;
	        if (includePooledTasks != null)
	        {
	            result = Boolean.valueOf(includePooledTasks);
	        }
	        
	        return result;
	    }
	    
	    /**
	     * Gets the specified {@link WorkflowTaskState}, null if not requested
	     * 
	     * @param req
	     * @return
	     */
	    private WorkflowTaskState getState(String stateName)
	    {
	        if (stateName != null)
	        {
	            try
	            {
	                return WorkflowTaskState.valueOf(stateName.toUpperCase());
	            }
	            catch (IllegalArgumentException e)
	            {
	                String msg = "Unrecognised State parameter: " + stateName;
	                throw new WebScriptException(401, msg);
	            }
	        }
	        
	        return null;
	    }

	    
		/**
	     * Determine if the given task should be included in the response.
	     * 
	     * @param task The task to check
	     * @param filters The list of filters the task must match to be included
	     * @return true if the task matches and should therefore be returned
	     */
	    private boolean matches(WorkflowTask task, Map<String, Object> filters, Map<String,Object> model, List<String> other)
	    {
	        // by default we assume that workflow task should be included
	        boolean result = true;

	        for (String key : filters.keySet())
	        {
	            Object filterValue = filters.get(key);

	            // skip null filters (null value means that filter was not specified)
	            if (filterValue != null)
	            {
	                if (key.equals(PARAM_EXCLUDE))
	                {
	                    ExcludeFilter excludeFilter = (ExcludeFilter)filterValue;
	                    String type = task.getDefinition().getMetadata().getName().toPrefixString(this.namespaceService);
	                    if (excludeFilter.isMatch(type))
	                    {
	                        result = false;
	                        break;
	                    }
	                }
	                else if (key.equals(PARAM_DUE_BEFORE))
	                {
	                    Date dueDate = (Date)task.getProperties().get(WorkflowModel.PROP_DUE_DATE);

	                    if (!isDateMatchForFilter(dueDate, filterValue, true))
	                    {
	                        result = false;
	                        break;
	                    }
	                }
	                else if (key.equals(PARAM_DUE_AFTER))
	                {
	                    Date dueDate = (Date)task.getProperties().get(WorkflowModel.PROP_DUE_DATE);

	                    if (!isDateMatchForFilter(dueDate, filterValue, false))
	                    {
	                        result = false;
	                        break;
	                    }
	                }
	                else if (key.equals(PARAM_PRIORITY))
	                {
	                    if (!filterValue.equals(task.getProperties().get(WorkflowModel.PROP_PRIORITY).toString()))
	                    {
	                        result = false;
	                        break;
	                    }
	                }
	                // Doy+ {
	                else if (key.equals(PARAM_NAME))
	                {
//	                	for(QName k : task.getProperties().keySet()) {
//	                    	System.out.println("Task.prop : "+k.toString());
//	                	}
	                	System.out.println("Task Desc : "+task.getProperties().get(WorkflowModel.PROP_DESCRIPTION));
	                    if (task.getProperties().get(WorkflowModel.PROP_DESCRIPTION).toString().indexOf(filterValue.toString()) < 0)
	                    {
	                        result = false;
	                        break;
	                    }
	                }
	                // Doy+ }
	                else {
//	                	log.info("other");
	                	if (other!=null) {
	                		String[] fv = (String[])filterValue;
	                		boolean rr = true;
	            			for(String f : fv) {
	                    		boolean r = false;
	            				for(String p : other) {
//	            					log.info("p:"+p+":"+model.get(p));
		                			if(model.get(p)!=null && model.get(p).toString().toLowerCase().indexOf(f) >= 0) {
//			                			log.info("found");
		            					r = true;
		            					break;
		            				}
	                			}
	            				if(!r) {
	            					rr = false;
	            					break;
	            				}
	                		}
//	                		log.info("rr:"+rr);
	                		result = rr;
	                		if (!rr) {
	                			break;
	                		}
	                		
//		                	Map<QName,Serializable> props = task.getProperties();
//		                	for(String p : other) {
//		                		log.info("p1:"+p);
//		                		boolean r = false;
//		                		p = p.replace('_', ':');
//		                		log.info("p2:"+p);
//		                		for(Entry<QName, Serializable> e:props.entrySet()) {
//		                			if(p.equals(e.getKey().getPrefixString())) {
//			                			log.info("prefixString:"+e.getKey().getPrefixString());
//			                			log.info("filterValue:"+filterValue);
//			                			log.info("e.getValue:"+e.getValue());
//		                				if(e.getValue()!=null && e.getValue().toString().indexOf(filterValue.toString()) >= 0) {
//				                			log.info("found");
//		                					r = true;
//		                				}
//		                				break;
//		                			}
//		                		}
	//
//		                		log.info("r:"+r);
//		                		result = r;
//		                		if (r) {
//		                			break;
//		                		}
//		                	}
	                	}
	                }
	                
	            }
	        }

	        return result;
	    }
	    
	    /**
	     * Processes the given date filter parameter from the provided webscript request.
	     * 
	     * If the parameter is present but set to an empty string or to "null" the
	     * date is added to the given filters Map as "", if the parameter
	     * contains an ISO8601 date it's added as a Date object to the filters.
	     * 
	     * @param req The WebScript request
	     * @param paramName The name of the parameter to look for
	     * @param filters Map of filters to add the date to
	     */
	    protected void processDateFilter(String dateParam, String paramName, Map<String, Object> filters)
	    {
	        // TODO: support other keywords i.e. today, tomorrow
	        
	        if (dateParam != null)
	        {
	            Object date = "";
	            
	            if (!"".equals(dateParam) && dateParam!=null)
	            {
	                date = getDateParameter(dateParam);
	            }
	            
	            filters.put(paramName, date);
	        }
	    }
	    
	    protected Date getDateParameter(String dateString)
	    {
	        if (dateString != null)
	        {
	            try
	            {
	                return ISO8601DateFormat.parse(dateString.replaceAll(" ", "+"));
	            }
	            catch (Exception e)
	            {
	                String msg = "Invalid date value: " + dateString;
	                throw new WebScriptException(401, msg);
	            }
	        }
	        return null;
	    }
	    
	    protected int getIntParameter(WebScriptRequest req, String paramName, int defaultValue)
	    {
	        String paramString = req.getParameter(paramName);
	        
	        if (paramString != null)
	        {
	            try
	            {
	                int param = Integer.valueOf(paramString);
	                
	                if (param > 0)
	                {
	                    return param;
	                }
	            }
	            catch (NumberFormatException e) 
	            {
	                throw new WebScriptException(401, e.getMessage());
	            }
	        }
	        
	        return defaultValue;
	    }
	    
	    /**
	     * Helper class to check for excluded items.
	     */
	    public class ExcludeFilter
	    {
	        private static final String WILDCARD = "*";
	        
	        private List<String> exactFilters;
	        private List<String> wilcardFilters;
	        private boolean containsWildcards = false;
	        
	        /**
	         * Creates a new ExcludeFilter
	         * 
	         * @param filters Comma separated list of filters which can optionally
	         *        contain wildcards
	         */
	        public ExcludeFilter(String filters)
	        {
	            // tokenize the filters
	            String[] filterArray = StringUtils.tokenizeToStringArray(filters, ",");
	            
	            // create a list of exact filters and wildcard filters
	            this.exactFilters = new ArrayList<String>(filterArray.length);
	            this.wilcardFilters = new ArrayList<String>(filterArray.length);
	            
	            for (String filter : filterArray)
	            {
	                if (filter.endsWith(WILDCARD))
	                {
	                    // at least one wildcard is present
	                    this.containsWildcards = true;
	                    
	                    // add the filter without the wildcard
	                    this.wilcardFilters.add(filter.substring(0, 
	                                (filter.length()-WILDCARD.length())));
	                }
	                else
	                {
	                    // add the exact filter
	                    this.exactFilters.add(filter);
	                }
	            }
	        }
	        
	        /**
	         * Determines whether the given item matches one of
	         * the filters.
	         * 
	         * @param item The item to check
	         * @return true if the item matches one of the filters
	         */
	        public boolean isMatch(String item)
	        {
	            // see whether there is an exact match
	            boolean match = this.exactFilters.contains(item);
	            
	            // if there wasn't an exact match and wildcards are present
	            if (item != null && !match && this.containsWildcards)
	            {
	                for (String wildcardFilter : this.wilcardFilters)
	                {
	                    if (item.startsWith(wildcardFilter))
	                    {
	                        match = true;
	                        break;
	                    }
	                }
	            }
	            
	            return match;
	        }
	    }
	    
	    /**
	     * Comparator to sort workflow tasks by due date in ascending order.
	     * { Doy+
	     * date1 = o1  // ASC
	     * date1 = o2  // DESC
	     * } Doy+
	     */
	    class WorkflowTaskDueAscComparator implements Comparator<WorkflowTask>
	    {
	        @Override
	        public int compare(WorkflowTask o1, WorkflowTask o2)
	        {
	            Date date1 = (Date)o1.getProperties().get(WorkflowModel.PROP_START_DATE);
	            Date date2 = (Date)o2.getProperties().get(WorkflowModel.PROP_START_DATE);
	            
	            long time1 = date1 == null ? Long.MAX_VALUE : date1.getTime();
	            long time2 = date2 == null ? Long.MAX_VALUE : date2.getTime();
	            
	            long result = time1 - time2;
	            
	            return (result > 0) ? 1 : (result < 0 ? -1 : 0);
	        }
	        
	    }
	    
	    class WorkflowTaskDueDescComparator implements Comparator<WorkflowTask>
	    {
	        @Override
	        public int compare(WorkflowTask o1, WorkflowTask o2)
	        {
	            Date date1 = (Date)o2.getProperties().get(WorkflowModel.PROP_START_DATE);
	            Date date2 = (Date)o1.getProperties().get(WorkflowModel.PROP_START_DATE);
	            
	            long time1 = date1 == null ? Long.MAX_VALUE : date1.getTime();
	            long time2 = date2 == null ? Long.MAX_VALUE : date2.getTime();
	            
	            long result = time1 - time2;
	            
	            return (result > 0) ? 1 : (result < 0 ? -1 : 0);
	        }
	        
	    }
	    
	    /**
	     * Determines whether the given date is a match for the given filter value.
	     * 
	     * @param date The date to check against
	     * @param filterValue The value of the filter, either an empty String or a Date object
	     * @param dateBeforeFilter true to test the date is before the filterValue, 
	     *        false to test the date is after the filterValue
	     * @return true if the date is a match for the filterValue
	     */
	    protected boolean isDateMatchForFilter(Date date, Object filterValue, boolean dateBeforeFilter)
	    {
	        boolean match = true;
	        
	        if (filterValue.equals(""))
	        {
	            if (date != null)
	            {
	                match = false;
	            }
	        }
	        else
	        {
	            if (date == null)
	            {
	                match = false;
	            }
	            else
	            {
	                if (dateBeforeFilter)
	                {
	                    if (date.getTime() >= ((Date)filterValue).getTime())
	                    {
	                        match = false;
	                    }
	                }
	                else
	                {
	                    if (date.getTime() <= ((Date)filterValue).getTime())
	                    {
	                        match = false;
	                    }
	                }
	            }
	        }
	        
	        return match;
	    }	    
	  
	  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/cancel")
	  public void handleCancel(@RequestParam(required=true) final String i
			  			   , final WebScriptResponse response)  throws Exception {
	    
		  	log.info("cancel(i:"+i+")");
		  	
			String json = null;
			
			try {
				JSONArray jsArr = new JSONArray(i);
				
				adminWorkflowService.cancel(jsArr);
				
				json = CommonUtil.jsonSuccess();
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
			}
	    
	  }

	  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/assign")
	  public void handleAssign(@RequestParam(required=true) final String i
 			   			   , @RequestParam(required=false) final String s
			  			   , @RequestParam(required=false) final String a
			  			   , @RequestParam(required=false) final String r
			  			   , final WebScriptResponse response)  throws Exception {

		  	log.info("assign(s:"+s+", a:"+a+", r:"+r+", i:"+i+")");
		  
			String json = null;
			
			try {
				JSONArray jsArr = new JSONArray(i);
				
				String result = adminWorkflowService.reassign(jsArr, s, a, r);
				
				if (result==null || result.equals("")) {
					json = CommonUtil.jsonSuccess();
				} else {
					json = CommonUtil.jsonFail(result);
				}
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
			}
	    
	  }

	  
	  @Uri(URI_PREFIX+"/assignee/list")
	  public void handleAssigneeList(@RequestParam(required=true) final String id
			  			   , final WebScriptResponse response)  throws Exception {
	    
		  	log.info("/assignee/list:id:"+id);
		  
			String json = null;
			
			try {
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("wkId", id);
				params.put("orderBy", "id");
				
				List<Map<String,Object>> dbList = mainWorkflowService.listAssignee(params);
				for(Map<String,Object> dbMap : dbList) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id", dbMap.get("id"));
					map.put("src", dbMap.get("src_user"));
					map.put("dest", dbMap.get("dest_user"));
					map.put("assignedTime", CommonDateTimeUtil.convertToGridDateTime((Timestamp)dbMap.get("created_time")));
					map.put("active", dbMap.get("active"));
					
					map.put("totalrowcount", dbMap.get("totalRowCount"));
					list.add(map);
				}
				
				json = CommonUtil.jsonSuccess(list);
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
			}
	    
	  }
	  
	  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/assignee/active")
	  public void handleAssigneeActive(@RequestParam(required=true) final String id
			  			   , @RequestParam(required=true) final String a
			  			   , final WebScriptResponse response)  throws Exception {
	    
		  	log.info("/assignee/active:id:"+id);
		  
			String json = null;
			
			try {
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("id", Long.parseLong(id));
				params.put("active", a.equals("1") ? "0" : "1");
				
				mainWorkflowService.updateAssignee(params);
				
				json = CommonUtil.jsonSuccessEmptyList();
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
			}
	  }

	  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/fixDoc")
	  public void handleFixDoc(@RequestParam(required=true) final String i
			  			   , final WebScriptRequest request
			  			   , final WebScriptResponse response)  throws Exception {
	    
		  	log.info("fixDoc(i:"+i+")");
		  	
			String json = null;
			
			try {
				JSONArray jsArr = new JSONArray(i);
				
				adminWorkflowService.fixDoc(jsArr, request);
				
				json = CommonUtil.jsonSuccess();
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
			}
	    
	  }

	  
}
