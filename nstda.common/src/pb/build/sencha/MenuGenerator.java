package pb.build.sencha;

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

import pb.common.constant.CommonConstant;

public class MenuGenerator {
	
	public void genMenu(String propFile, String destPath) {
		try {
			
			Properties props = new Properties();
			InputStream ins = new FileInputStream(propFile);
			if (ins != null) {
				props.load(ins);
				
				String[] menus = props.getProperty("menu.order").split(",");
				
				Path outputPath = Paths.get(destPath + File.separator + CommonConstant.MODULE_PROP_FILE);
				try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
				
					writer.write("menu.order="+props.getProperty("menu.order")+"\n");
					
					for(String menu : menus) {
						
						String key = "module."+menu;
						String value = props.getProperty(key);
						
						if (value != null && Boolean.parseBoolean(value)) {
							if (key.equals("module.admin")) {
								adminModule(writer);
							}
							else
							if (key.equals("module.pcm")) {
								pcmModule(writer);
							}
							else
							if (key.equals("module.exp")) {
								expModule(writer);
							}
							else
							if (key.equals("module.hr")) {
								hrModule(writer);
							}
						} // if (value)
					} // for

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
	
	private void adminModule(Writer writer) throws Exception {
		System.out.println("+ admin");
	    writer.write("admin=1\n");
	}

	private void pcmModule(Writer writer) throws Exception {
		System.out.println("+ pcm");
	    writer.write("pcm=1\n");
	}

	private void expModule(Writer writer) throws Exception {
		System.out.println("+ exp");
	    writer.write("exp=1\n");
	}

	private void hrModule(Writer writer) throws Exception {
		System.out.println("+ hr");
	    writer.write("hr=1\n");
	}
	
	public static void main(String[] args) {
		
//		URL location = MenuGenerator.class.getProtectionDomain().getCodeSource().getLocation();
//        System.out.println(location.getFile());
        
		MenuGenerator mg = new MenuGenerator();

		String propFile = args[0];
		String destPath = args[1];
		
		mg.genMenu(propFile, destPath);
	}

}
