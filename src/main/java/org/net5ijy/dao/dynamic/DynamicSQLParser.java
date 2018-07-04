package org.net5ijy.dao.dynamic;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;

/**
 * 动态SQL解析工具。<br />
 * <br />
 * 
 * @author 创建人：xuguofeng
 * @version 创建于：2018年6月15日 上午11:38:37
 */
public class DynamicSQLParser {

	private static Logger log = LoggerFactory.getLogger(DynamicSQLParser.class);

	private Configuration freeMarkerConfig = new Configuration(new Version(
			"2.3.0"));

	/**
	 * SqlID—>freemarker Template对应关系
	 */
	private Map<String, Template> sqlTemplates = new HashMap<String, Template>();

	private Map<String, String> sqlMap = new HashMap<String, String>();

	/**
	 * 静态单例
	 */
	private static DynamicSQLParser instance = new DynamicSQLParser();

	private DynamicSQLParser() {
		Document document = getDocument("dynamic-sql.xml");
		initAllTemplates(document);
	}

	/**
	 * 获取单例对象。<br />
	 * <br />
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月15日 上午11:40:23
	 * @return
	 */
	public static DynamicSQLParser getDynamicSQLParser() {
		return instance;
	}

	/**
	 * 获取sql.xml配置文件dom对象。<br />
	 * <br />
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月15日 上午11:40:33
	 * @return
	 */
	private Document getDocument(String path) {
		try {
			SAXReader reader = new SAXReader();
			reader.setValidation(false);
			reader.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-external-dtd",
					false);
			Document document = reader.read(DynamicSQLParser.class
					.getClassLoader().getResourceAsStream(path));
			return document;
		} catch (SAXException e) {
			log.error("加载sql.xml文件失败", e);
			throw new RuntimeException("加载sql.xml文件失败", e);
		} catch (DocumentException e) {
			log.error("加载sql.xml文件失败", e);
			throw new RuntimeException("加载sql.xml文件失败", e);
		}
	}

	/**
	 * 加载SQL模板。<br />
	 * <br />
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月15日 下午7:20:17
	 * @param doc
	 */
	@SuppressWarnings("unchecked")
	private void initAllTemplates(Document doc) {
		Element root = doc.getRootElement();
		List<Element> files = root.elements("file");
		for (Element file : files) {
			String path = file.attributeValue("path");
			Document d = this.getDocument(path);
			initOneFileTemplates(d);
		}
	}

	/**
	 * 加载一个SQL模板。<br />
	 * <br />
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月15日 上午11:50:24
	 * @param doc
	 */
	@SuppressWarnings("unchecked")
	private void initOneFileTemplates(Document doc) {
		Element root = doc.getRootElement();
		List<Element> sqls = root.elements("sql");
		StringTemplateLoader loader = new StringTemplateLoader();
		for (Element sql : sqls) {
			String id = sql.attributeValue("id");
			String sqlTxt = sql.getTextTrim();
			this.sqlMap.put(id, sqlTxt);
			loader.putTemplate(id, sqlTxt);
			this.freeMarkerConfig.setTemplateLoader(loader);
			try {
				Template template = this.freeMarkerConfig.getTemplate(id);
				if (log.isDebugEnabled()) {
					log.debug("加载SQL模板[" + id + ", " + sqlTxt + "]");
				}
				this.sqlTemplates.put(id, template);
			} catch (TemplateNotFoundException e) {
				log.error("SQL模板未找到", e);
			} catch (MalformedTemplateNameException e) {
				log.error("SQL模板名称格式不正确", e);
			} catch (ParseException e) {
				log.error("SQL模板解析出错", e);
			} catch (IOException e) {
				log.error("SQL模板解析出错", e);
			}
		}
	}

	/**
	 * 根据sqlid和条件对象返回sql语句。<br />
	 * <br />
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月15日 上午11:50:40
	 * @param id
	 * @param object
	 * @param parse
	 *            - 是否需要动态解析
	 * @return
	 */
	public String parseSql(String id, Object object, boolean parse) {
		if (!parse) {
			String sql = this.sqlMap.get(id);
			if (sql == null) {
				throw new RuntimeException("SQL语句" + id + "未找到");
			}
			if (log.isDebugEnabled()) {
				log.debug("获取SQL[" + id + ", " + sql + "]");
			}
			return sql;
		}
		Template template = this.sqlTemplates.get(id);
		if (template == null) {
			throw new RuntimeException("SQL模板" + id + "未找到");
		}
		StringWriter sw = new StringWriter();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("map", object);
			template.process(map, sw);
			String dynamicSql = sw.toString().replaceAll(
					"(where|set)\\s+(and|,)", "$1 ");
			if (log.isDebugEnabled()) {
				log.debug("获取动态SQL[" + id + ", " + dynamicSql + "]");
			}
			return dynamicSql;
		} catch (TemplateException e) {
			log.error("SQL模板解析出错", e);
			throw new RuntimeException("SQL模板" + id + "解析出错");
		} catch (IOException e) {
			log.error("SQL模板解析出错", e);
			throw new RuntimeException("SQL模板" + id + "解析出错");
		}
	}
}
