package org.net5ijy.freemaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Template;

public class TestFreeMarker {

	@Test
	public void testFreemarker1() throws Exception {

		// 封装SQL查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "admin1");
		map.put("age", 28);

		// 数据model
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("map", map);

		// 从类路径下的sql.txt文件创建一个模板对象
		Template t = new Template("sql", new InputStreamReader(
				TestFreeMarker.class.getClassLoader().getResourceAsStream(
						"sql.txt"), "utf-8"), null);

		// 定义一个字符串输出流
		StringWriter out = new StringWriter();

		// 使用数据model解析模板到输出流
		t.process(model, out);

		// 转为字符串
		String sql = out.toString();

		// SQL字符串优化
		// 把“where and”替换为“where”
		// 把一个或多个空白替换为一个空格
		sql = sql.replaceAll("where\\s*and", "where").replaceAll("\\s+", " ");

		System.out.println(sql);
	}

	@Test
	public void testFreemarker2() throws Exception {

		// 获取文件里面保存的原始sql字符串
		String sqlModel = getSqlModel(TestFreeMarker.class.getClassLoader()
				.getResourceAsStream("sql.txt"));

		// 封装SQL查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "admin1");
		map.put("age", 28);

		// 数据model
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("map", map);

		Template t = new Template("sql", sqlModel, null);

		// 定义一个字符串输出流
		StringWriter out = new StringWriter();

		// 使用数据model解析模板到输出流
		t.process(model, out);

		// 转为字符串
		String sql = out.toString();

		// SQL字符串优化
		// 把“where and”替换为“where”
		// 把一个或多个空白替换为一个空格
		sql = sql.replaceAll("where\\s*and", "where").replaceAll("\\s+", " ");

		System.out.println(sql);
	}

	private String getSqlModel(InputStream in) {

		StringBuilder sb = new StringBuilder();

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(in));

			String line = reader.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = reader.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return sb.toString();
	}
}
