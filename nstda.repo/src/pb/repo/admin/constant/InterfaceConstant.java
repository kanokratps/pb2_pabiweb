package pb.repo.admin.constant;

import java.util.LinkedHashMap;
import java.util.Map;

public class InterfaceConstant {
	public static final Map<String, Object> BUDGET_TYPE = new LinkedHashMap<String, Object>();
	
    static {
    	BUDGET_TYPE.put(MainBudgetSrcConstant.TYPE_UNIT, "unit_base");
    	BUDGET_TYPE.put(MainBudgetSrcConstant.TYPE_PROJECT, "project_base");
    	BUDGET_TYPE.put(MainBudgetSrcConstant.TYPE_ASSET, "invest_asset");
    	BUDGET_TYPE.put(MainBudgetSrcConstant.TYPE_CONSTRUCTION, "invest_construction");
    }
}
