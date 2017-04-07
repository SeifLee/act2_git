package cn.com.geoway.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 使用spring框架整合
 * 
 * @author lvxiaofei
 * @version 1.0，
 */
//@Path("/activiti01")
@RestController
public class ActivitiRest01 {
	
	/** 注入流程引擎对象 */
	@Resource
	private ProcessEngine processEngine;
	
	//@GET
	//@Produces(MediaType.TEXT_PLAIN)
	@RequestMapping(value="/activiti01/{a}/{b}/{c}",method=RequestMethod.GET,produces={"text/plain"})
	public String testname(@PathVariable String a,
							@PathVariable String b, 
							@PathVariable String c) throws Exception {
		System.out.println(""+ a + b + c);
		
		// 声明参数文件的对象
		File pf = new File("D:/abc.properties");
		// 判断文件对象是否存在
		if (!pf.exists()) {
			// 不存在就创建一个
			pf.createNewFile();
		}
		
		// 定义一个文件输出流
		FileOutputStream oFile = new FileOutputStream(pf, false);//false表示不追加,直接覆盖
		// 定义Properties对象
		Properties properties = new Properties();
		properties.setProperty("a", a.toString());
		properties.setProperty("b", b.toString());
		properties.setProperty("c", c.toString());
		// 将属性存储到文件中
		properties.store(oFile, "properties write test");		
		
		/*ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		ProcessEngine processEngine = ctx.getBean(ProcessEngine.class);*/
		
		// 创建一个部署构建器对象,用于加载流程定义文件
		DeploymentBuilder deploymentBuilder = processEngine
				.getRepositoryService().createDeployment();
		// 部署流程
		deploymentBuilder.name("plus")// 设置流程的显示名称
				.addClasspathResource("process/plus.bpmn")// 设置流程文件
				.deploy();// 部署流程
		// 获取runtimeservie
		RuntimeService runtimeService = processEngine.getRuntimeService();

		// 启动流程实例
		ProcessInstance instance = runtimeService
				.startProcessInstanceByKey("plus");

		// 获取流程实例id
		System.out.println(instance.getId());
		
		
		List<HistoricVariableInstance> list = processEngine.getHistoryService() // 获取历史服务
	            .createHistoricVariableInstanceQuery()//创建一个历史的流程变量查询  
	            .variableName("a+b+c")// 设置查询条件-变量名
	            .processInstanceId(instance.getId())//设置查询条件--流程实例id
	            .list();// 列出查询结果

		int size = list.size();
		System.out.println(size);
		
		// 结果唯一,所以只需要获取第0个元素
		HistoricVariableInstance historicVariableInstance = list.get(0);
		// 将结果转换为其原来的数据类型
		Integer value = (Integer) historicVariableInstance.getValue();
		// 返回结果
		return value.toString();
	}
}
