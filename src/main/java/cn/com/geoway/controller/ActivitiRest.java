package cn.com.geoway.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * 将计算流程的服务暴露出去
 * 
 * @author lvxiaofei
 * @version 1.0，
 */
@Path("/activiti")
public class ActivitiRest {
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String testname(@QueryParam("a") final Integer a,
							@QueryParam("b") final Integer b,
							@QueryParam("c") final Integer c) throws Exception {
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
		
		
		// 创建流程引擎配置文件
		ProcessEngineConfiguration cfg = ProcessEngineConfiguration
				.createStandaloneProcessEngineConfiguration();

		// 配置数据库驱动
		cfg.setJdbcDriver("org.postgresql.Driver");
		// 配置数据库URL
		cfg.setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/activiti01");
		// 配置用户名
		cfg.setJdbcUsername("postgres");
		// 配置密码
		cfg.setJdbcPassword("root");

		// 配置数据库自动建表
		cfg.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

		// 根据流程引擎配置对象,创建流程引擎
		ProcessEngine processEngine = cfg.buildProcessEngine();

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
