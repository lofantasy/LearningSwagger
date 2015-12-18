/**
 * 
 */
package com.domain.swagger;

import java.io.Reader;
import java.util.Map;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import io.swagger.codegen.AbstractGenerator;
import io.swagger.codegen.CodegenConfig;

/**
 * @author shadowwa
 *
 */
public class DaoModelGenerator extends AbstractGenerator {
	public void writeDaoStubForModel( CodegenConfig config, String path, String name, Map<String, Object> models) {
		try {
			String suffix = "DAO";
			String filename = path + "/" + suffix + name + ".java";
			
			String templateFile = getFullTemplateFile(config, "daoModel.mustache");
            String template = readTemplate(templateFile);
            Template tmpl = Mustache.compiler()
                    .withLoader(new Mustache.TemplateLoader() {
                        @Override
                        public Reader getTemplate(String name) {
                            return getTemplateReader( getFullTemplateFile(config, name + ".mustache"));
                        }
                    })
                    .defaultValue("")
                    .compile(template);
            writeToFile(config.outputFolder() + "/" + filename, tmpl.execute(models));

			System.out.println( "Hello: " + filename);
		}
		catch( Exception e) {
			e.printStackTrace();
			System.out.println("Failed!");
		}
	}
}
