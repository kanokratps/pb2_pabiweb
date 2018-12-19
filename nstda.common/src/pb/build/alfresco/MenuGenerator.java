package pb.build.alfresco;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class MenuGenerator {
	
	public void genMenu(String propFile, String destPath) {
		try {
			
			Properties props = new Properties();
			InputStream ins = new FileInputStream(propFile);
			if (ins != null) {
				props.load(ins);
				
//				String[] menus = props.getProperty("menu.order").split(",");
				
				Path outputPath = Paths.get(destPath + File.separator + "share-header.get.js");
				try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
				
					prefixModule(writer);
					
//					for(String menu : menus) {
//						
//						String key = "module."+menu;
//						String value = props.getProperty(key);
//						
//						if (value != null && Boolean.parseBoolean(value)) {
//							if (key.equals("module.admin")) {
//								adminModule(writer);
//							}
//							else
//							if (key.equals("module.pcm")) {
//								pcmModule(writer);
//							}
//							else
//							if (key.equals("module.exp")) {
//								expModule(writer);
//							}
//							if (key.equals("module.hr")) {
//								hrModule(writer);
//							}
//						} // if (value)
//					} // for

					suffixModule(writer);
					
					writer.flush();
					writer.close();
				
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			else {
				throw new FileNotFoundException("propertyfile : '" + propFile + "' not found!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void prefixModule(Writer writer) throws Exception {
	    writer.write("var headerBar = widgetUtils.findObject(model.jsonModel, \"id\", \"HEADER_APP_MENU_BAR\");");
	    writer.write("if (headerBar != null)");
	    writer.write("{");
	}

	private void suffixModule(Writer writer) throws Exception {
	    /////////////////////
		
		writer.write("headerBar.config.widgets.splice(1, 0, {");
		writer.write(	"id: \"HEADER_PB_BUDGET\",");
		writer.write(	"name: \"alfresco/header/AppMenu\",");
		writer.write(	"config: {");
		writer.write(		"id: \"HEADER_PB_BUDGET\",");
		writer.write(		"label: \"header.menu.pb-budget.label\",");
		writer.write(		"widgets: []");   
//		writer.write(			"name: \"alfresco/menus/AlfMenuGroup\",");
//		writer.write(			"config: {");
//		writer.write(				"widgets: []");
//		writer.write(			"}");
//		writer.write(		"}]");
		writer.write(	"}");   
		writer.write("});");
		
		writer.write("headerBar.config.widgets.splice(1, 0, {");
		writer.write(	"id: \"HEADER_PB\",");
		writer.write(	"name: \"alfresco/header/AppMenu\",");
		writer.write(	"config: {");
		writer.write(		"id: \"HEADER_PB\",");
		writer.write(		"label: \"header.menu.pb.label\",");
		writer.write(		"widgets: []");   
//		writer.write(			"name: \"alfresco/menus/AlfMenuGroup\",");
//		writer.write(			"config: {");
//		writer.write(				"widgets: []");
//		writer.write(			"}");
//		writer.write(		"}]");
		writer.write(	"}");   
		writer.write("});");
		
//		writer.write("headerBar.config.widgets.splice(1, 0, {");
//		writer.write(	"id: \"HEADER_PB\",");
//		writer.write(	"name: \"alfresco/header/AlfMenuBarPopup\",");
//		writer.write(	"config: {");
//		writer.write(		"label: \"header.menu.pb.label\",");
//		writer.write(		"widgets: [{");   
//		writer.write(			"name: \"alfresco/menus/AlfMenuGroup\",");
//		writer.write(			"config: {");
//		writer.write(				"widgets: []");
//		writer.write(			"}");
//		writer.write(		"}]");
//		writer.write(	"}");
//		writer.write("});");
		
		////////////////////
		
	    writer.write("var inspos = 1;");
	    
	    writer.write("if (!user.isAdmin) {");
	    writer.write("var result = remote.call(\"/pb/admin/menu/allow\");");
	    
	    writer.write("if (result.status.code == status.STATUS_OK) {");
	    	writer.write("var a = jsonUtils.toObject(result.text);");
	    	
	    	writer.write("if (!a.home) {");
//			writer.write("logger.log(\"--- delete home\");");
			writer.write("widgetUtils.deleteObjectFromArray(model.jsonModel, \"id\", \"HEADER_HOME\");");
			writer.write("inspos = 0;");
			writer.write("}");
			
	    	writer.write("if (!a.myFiles) {");
//			writer.write("logger.log(\"--- delete myFiles\");");
			writer.write("widgetUtils.deleteObjectFromArray(model.jsonModel, \"id\", \"HEADER_MY_FILES\");");
	    	writer.write("}");
	    	
	    	writer.write("if (!a.sharedFiles) {");
//			writer.write("logger.log(\"--- delete sharedFiles\");");
			writer.write("widgetUtils.deleteObjectFromArray(model.jsonModel, \"id\", \"HEADER_SHARED_FILES\");");
			writer.write("}");
	    	
	    	writer.write("if (!a.sites) {");
//			writer.write("logger.log(\"--- delete sites\");");
			writer.write("widgetUtils.deleteObjectFromArray(model.jsonModel, \"id\", \"HEADER_SITES_MENU\");");
			writer.write("}");
			
	    	writer.write("if (!a.tasks) {");
//			writer.write("logger.log(\"--- delete tasks\");");
			writer.write("widgetUtils.deleteObjectFromArray(model.jsonModel, \"id\", \"HEADER_TASKS\");");
			writer.write("}");
			
	    	writer.write("if (!a.people) {");
//			writer.write("logger.log(\"--- delete people\");");
			writer.write("widgetUtils.deleteObjectFromArray(model.jsonModel, \"id\", \"HEADER_PEOPLE\");");
			writer.write("}");
		
	    	writer.write("if (!a.repository) {");
//			writer.write("logger.log(\"--- delete repository\");");
			writer.write("widgetUtils.deleteObjectFromArray(model.jsonModel, \"id\", \"HEADER_REPOSITORY\");");
			writer.write("}");
	    writer.write("}");
	    
	    writer.write("}");
	    
	    writer.write("}");

	}	
	private void _prefixModule(Writer writer) throws Exception {
	    writer.write("var headerBar = widgetUtils.findObject(model.jsonModel, \"id\", \"HEADER_APP_MENU_BAR\");");
	    writer.write("if (headerBar != null)");
	    writer.write("{");
	    writer.write("var widgets = [];");
	}

	private void _suffixModule(Writer writer) throws Exception {
		writer.write("headerBar.config.widgets.splice(1, 0, {");
		writer.write(	"id: \"HEADER_PB\",");
		writer.write(	"name: \"alfresco/header/AlfMenuBarPopup\",");
		writer.write(	"config: {");
		writer.write(		"label: \"header.menu.pb.label\",");
		writer.write(		"widgets: [{");   
		writer.write(			"name: \"alfresco/menus/AlfMenuGroup\",");
		writer.write(			"config: {");
		writer.write(				"widgets: widgets");
		writer.write(			"}");
		writer.write(		"}]");
		writer.write(	"}");
		writer.write("});");
		
		writer.write("if(!user.isAdmin){");
		
	    writer.write("function findAndRemoveIn(obj, arrContext, arrIdx, id)");
	    writer.write("{");
	    writer.write(	"var idx, max, key;");
	    writer.write(	"if (obj !== undefined && obj !== null)");    
	    writer.write(	"{");
	    writer.write(		"if (Object.prototype.toString.apply(obj) === \"[object Object]\")");
	    writer.write(		"{");
	    writer.write(			"if (obj.hasOwnProperty(\"id\") && obj.id === id)");
	    writer.write(			"{");
	    writer.write(				"if (arrContext !== null && arrIdx !== null)");
	    writer.write(				"{ arrContext.splice(arrIdx, 1); }");
	    writer.write(				"else");
	    writer.write(				"{ logger.debug(\"Unexpected match outside of array structure: \" + jsonUtils.toJSONString(obj)); }");
	    writer.write(			"}");
	    writer.write(			"else");
	    writer.write(			"{");
	    writer.write(				"for (key in obj)");
	    writer.write(				"{");
	    writer.write(					"if (obj.hasOwnProperty(key))");
	    writer.write(					"{ findAndRemoveIn(obj[key], null, null, id); }");
	    writer.write(				"}");
	    writer.write(			"}");
	    writer.write(		"}");
	    writer.write(		"else if (Object.prototype.toString.apply(obj) === \"[object Array]\")");
	    writer.write(		"{");
	    writer.write(			"for (idx = 0, max = obj.length; idx < max; idx++)");
	    writer.write(			"{ findAndRemoveIn(obj[idx], obj, idx, id); }");
	    writer.write(		"}");
	    writer.write(	"}");
	    writer.write("}");
	    
	    writer.write("var widget, widgetsToRemove = [ \"HEADER_SHARED_FILES\", \"HEADER_MY_FILES\", \"HEADER_PEOPLE\", \"HEADER_TASKS\", \"HEADER_REPOSITORY\",");
	    writer.write(								"\"HEADER_SEARCH\", \"HEADER_SITES_MENU\" ], idx, max;");
	                                     
	    writer.write("for (idx = 0, max = widgetsToRemove.length; idx < max; idx++)");
	    writer.write(	"findAndRemoveIn(model.jsonModel.widgets, null, null, widgetsToRemove[idx]);");

		writer.write("}"); // if is not admin
	    
	    writer.write("}"); // last line
	}
	
	private void adminModule(Writer writer) throws Exception {
		System.out.println("+ admin");
	    writer.write("if (user.isAdmin) {");
	    writer.write(	"widgets.push({");
	    writer.write(		"name: \"alfresco/header/AlfMenuItem\",");
	    writer.write(		"config:{");
		writer.write(			"id: \"HEADER_PB_ADMIN\",");
		writer.write(			"label: \"header.menu.pb-admin.label\",");
		writer.write(			"iconClass: \"alf-pb-admin-icon\",");
		writer.write(			"targetUrl: \"admin\"");
	    writer.write(		"}");
		writer.write(	"});");
		writer.write("}");
	}

	private void pcmModule(Writer writer) throws Exception {
		System.out.println("+ pcm");
		writer.write("widgets.push({");
		
	    
		writer.write(	"id: \"HEADER_PB_PCM\",");
		writer.write(	"name: \"alfresco/header/AlfCascadingMenu\",");
		writer.write(	"config: {");
		writer.write(		"label: \"header.menu.pb-pcm.label\",");
		writer.write(		"iconClass: \"alf-pb-pcm-icon\",");
		writer.write(		"widgets: [{");   
		writer.write(			"name: \"alfresco/menus/AlfMenuGroup\",");
		writer.write(			"config: {");
		writer.write(				"widgets: [{");
		
		writer.write(	"name: \"alfresco/menus/AlfMenuItem\",");
		writer.write(	"config:{");
		writer.write(		"id: \"HEADER_PB_PCM_REQ\",");
		writer.write(		"label: \"header.menu.pb-pcm-req.label\",");
		writer.write(		"iconClass: \"alf-pb-pcm-icon\",");
		writer.write(		"targetUrl: \"pcm-req\"");
		writer.write(	"}");
		 
		writer.write(				"},{");
		
		writer.write(	"name: \"alfresco/menus/AlfMenuItem\",");
		writer.write(	"config:{");
		writer.write(		"id: \"HEADER_PB_PCM_ORD\",");
		writer.write(		"label: \"header.menu.pb-pcm-ord.label\",");
		writer.write(		"iconClass: \"alf-pb-pcm-icon\",");
		writer.write(		"targetUrl: \"pcm-ord\"");
		writer.write(	"}");
		
		writer.write(				"},{");
		
		writer.write(	"name: \"alfresco/menus/AlfMenuItem\",");
		writer.write(	"config:{");
		writer.write(		"id: \"HEADER_PB_PCM_USE\",");
		writer.write(		"label: \"header.menu.pb-pcm-use.label\",");
		writer.write(		"iconClass: \"alf-pb-pcm-icon\",");
		writer.write(		"targetUrl: \"https://pabi2o-test.intra.nstda.or.th/stock_request\",");
		writer.write(		"targetUrlType: \"FULL_PATH\",");
		writer.write(		"targetUrlLocation: \"NEW\"");
		writer.write(	"}");
		
		
		writer.write(				"}]");
		writer.write(			"}");
		writer.write(		"}]");
		writer.write(	"}");
		
		writer.write("});");
	}

	private void expModule(Writer writer) throws Exception {
		System.out.println("+ exp");
		writer.write("widgets.push({");
		
		writer.write(	"id: \"HEADER_PB_EXP\",");
		writer.write(	"name: \"alfresco/header/AlfCascadingMenu\",");
		writer.write(	"config: {");
		writer.write(		"label: \"header.menu.pb-exp.label\",");
		writer.write(		"iconClass: \"alf-pb-exp-icon\",");
		writer.write(		"widgets: [{");   
		writer.write(			"name: \"alfresco/menus/AlfMenuGroup\",");
		writer.write(			"config: {");
		writer.write(				"widgets: [{");
		
		writer.write(	"name: \"alfresco/menus/AlfMenuItem\",");
		writer.write(	"config:{");
		writer.write(		"id: \"HEADER_PB_EXP_USE\",");
		writer.write(		"label: \"header.menu.pb-exp-use.label\",");
		writer.write(		"iconClass: \"alf-pb-exp-icon\",");
		writer.write(		"targetUrl: \"exp-use\"");
		writer.write(	"}");
		
		writer.write(				"},{");
		
		writer.write(	"name: \"alfresco/menus/AlfMenuItem\",");
		writer.write(	"config:{");
		writer.write(		"id: \"HEADER_PB_EXP_BRW\",");
		writer.write(		"label: \"header.menu.pb-exp-brw.label\",");
		writer.write(		"iconClass: \"alf-pb-exp-icon\",");
		writer.write(		"targetUrl: \"exp-brw\"");
		writer.write(	"}");
		
		writer.write(				"}]");
		writer.write(			"}");
		writer.write(		"}]");
		writer.write(	"}");
		
		writer.write("});");
	}

	private void hrModule(Writer writer) throws Exception {
		System.out.println("+ hr");
		writer.write("widgets.push({");
		
		writer.write(	"id: \"HEADER_PB_HR\",");
		writer.write(	"name: \"alfresco/header/AlfCascadingMenu\",");
		writer.write(	"config: {");
		writer.write(		"label: \"header.menu.pb-hr.label\",");
		writer.write(		"iconClass: \"alf-pb-hr-icon\",");
		writer.write(		"widgets: [{");   
		writer.write(			"name: \"alfresco/menus/AlfMenuGroup\",");
		writer.write(			"config: {");
		writer.write(				"widgets: [{");
		
		writer.write(	"name: \"alfresco/menus/AlfMenuItem\",");
		writer.write(	"config:{");
		writer.write(		"id: \"HEADER_PB_HR_SAL\",");
		writer.write(		"label: \"header.menu.pb-hr-sal.label\",");
		writer.write(		"iconClass: \"alf-pb-hr-icon\",");
		writer.write(		"targetUrl: \"hr-sal\"");
		writer.write(	"}");
		
		writer.write(				"}]");
		writer.write(			"}");
		writer.write(		"}]");
		writer.write(	"}");
		
		writer.write("});");
	}
	
	public static void main(String[] args) {
//		
//		URL location = MenuGenerator.class.getProtectionDomain().getCodeSource().getLocation();
//        System.out.println(location.getFile());
        
		MenuGenerator mg = new MenuGenerator();

		String propFile = args[0];
		String destPath = args[1];
		
		mg.genMenu(propFile, destPath);
	}

}
