package cn.com.geoway.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 简单页面对应的controller
 * 
 * @author lvxiaofei
 * @version 1.0，2017-04-05 09:30:08
 */
@RestController
@RequestMapping(value = "/activiti02")
public class ActivitiRest02 {

	/** 注入流程引擎对象 */
	@Resource
	private ProcessEngine processEngine;
	
	/**
	 * 执行流程
	 * 
	 * @param param1
	 *            参数1
	 * @param param2
	 *            参数2
	 * @param param3
	 *            参数3
	 * @param processFile
	 *            流程定义文件
	 * @param model
	 *            SpringMVC中的model模型
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/computation", method = RequestMethod.POST)
	public String computation(@ModelAttribute("param1") String param1,
			@ModelAttribute("param2") String param2,
			@ModelAttribute("param3") String param3,
			@ModelAttribute("processFile") MultipartFile processFile,
			@ModelAttribute("processFileName") String processFileName,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {

		// 声明参数文件的对象
		File pf = new File("D:/abc.properties");
		// 判断文件对象是否存在
		if (!pf.exists()) {
			// 不存在就创建一个
			pf.createNewFile();
		}

		// 定义一个文件输出流
		FileOutputStream oFile = new FileOutputStream(pf, false);// false表示不追加,直接覆盖
		// 定义Properties对象
		Properties properties = new Properties();
		properties.setProperty("a", param1);
		properties.setProperty("b", param2);
		properties.setProperty("c", param3);
		// 将属性存储到文件中
		properties.store(oFile, "params for activiti");

		// 获取文件输入流
		InputStream is = processFile.getInputStream();

		// 创建一个部署构建器对象,用于加载流程定义文件
		DeploymentBuilder deploymentBuilder = processEngine
				.getRepositoryService().createDeployment();

		System.out.println(processFile.getName());
		// 部署流程
		deploymentBuilder.name("plus")// 设置流程的显示名称
				.addInputStream(processFileName, is).deploy();// 部署流程

		// 获取runtimeservie
		RuntimeService runtimeService = processEngine.getRuntimeService();

		// 启动流程实例
		ProcessInstance instance = runtimeService
				.startProcessInstanceByKey("plus");

		// 获取流程实例id
		System.out.println(instance.getId());

		List<HistoricVariableInstance> list = processEngine.getHistoryService() // 获取历史服务
				.createHistoricVariableInstanceQuery()// 创建一个历史的流程变量查询
				.variableName("a+b+c")// 设置查询条件-变量名
				.processInstanceId(instance.getId())// 设置查询条件--流程实例id
				.list();// 列出查询结果

		// 结果唯一,所以只需要获取第0个元素
		HistoricVariableInstance historicVariableInstance = list.get(0);
		// 将结果转换为其原来的数据类型
		Integer value = (Integer) historicVariableInstance.getValue();
		model.addAttribute("result", value);
		// 设置服务器编码
		response.setCharacterEncoding("UTF-8");
		// 设置浏览器编码
		response.setHeader("Content-Type", "text/html;charset=utf-8");
		response.getWriter().print(value);
		return null;
	}

	/**
	 * 查询历史流程
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public String history(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 获取历史流程服务对象
		HistoryService historyService = processEngine.getHistoryService();

		// 创建历史流程实例查询对象
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService
				.createHistoricProcessInstanceQuery();
		// 查询所有流程实例
		List<HistoricProcessInstance> proInstances = historicProcessInstanceQuery
				.list();
		// 创建map集合
		Map<String, HashMap<String, Integer>> map = new HashMap<>();
		// 遍历
		for (HistoricProcessInstance hpi : proInstances) {

			// 获取流程实例id
			String id = hpi.getId();

			// 创建一个历史的流程变量查询
			HistoricVariableInstanceQuery variableInstanceQuery = historyService
					.createHistoricVariableInstanceQuery();
			// 设置查询条件
			variableInstanceQuery.processInstanceId(id);
			// 查询当前流程实例中所有的流程变量
			List<HistoricVariableInstance> varInstances = variableInstanceQuery
					.list();
			// 创建map集合
			HashMap<String, Integer> map1 = new HashMap<>();

			// 遍历
			for (HistoricVariableInstance hvi : varInstances) {
				String variableName = hvi.getVariableName();
				if ("a".equals(variableName)) {
					Integer a = (Integer) hvi.getValue();
					map1.put("a", a);
				}
				if ("b".equals(variableName)) {
					Integer b = (Integer) hvi.getValue();
					map1.put("b", b);
				}
				if ("c".equals(variableName)) {
					Integer c = (Integer) hvi.getValue();
					map1.put("c", c);
				}
				if ("a+b+c".equals(variableName)) {
					Integer abc = (Integer) hvi.getValue();
					map1.put("abc", abc);
				}
			}

			// 将流程实例和流程变量的map集合以json数据格式返回到页面
			map.put(id, map1);
		}

		// 创建jsonobj
		JSONObject jo = JSONObject.fromObject(map);
		System.out.println(jo.toString());

		// 设置服务器编码
		response.setCharacterEncoding("UTF-8");
		// 设置浏览器编码
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().print(jo.toString());

		return null;
	}

}
