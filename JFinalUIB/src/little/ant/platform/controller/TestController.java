package little.ant.platform.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import little.ant.platform.annotation.Controller;
import little.ant.platform.common.DictKeys;
import little.ant.platform.service.OperatorService;
import little.ant.platform.service.SysLogService;
import little.ant.platform.tools.ToolSqlXml;
import little.ant.weixin.lucene.DocKeyword;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@Controller(controllerKey = "/jf/platform/test")
public class TestController extends BaseController {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(TestController.class);

	public void operator() {
		OperatorService.service.list(splitPage);
		render("/platform/test/operator.html");
	}

	public void sysLog() {
		defaultOrder("startdate", "desc");
		SysLogService.service.list(splitPage);
		render("/platform/test/sysLog.html");
	}
	
	public void lucene() {
		new DocKeyword().search(splitPage);
		render("/platform/test/lucene.html");
	}
	
	public void autoComplete(){
		String keyword = getPara("keyword");
		String size = "10";//getPara("size");
		if(null == keyword || keyword.isEmpty()){
			renderText("[]");
			
		}else{
			Map<String, String> param = new HashMap<String, String>();
			param.put("url", keyword);
			param.put("size", size);
			
			LinkedList<Object> paramValue = new LinkedList<Object>();
			String sql = ToolSqlXml.getSql("pingtai.test.autoComplete", param, paramValue);
			List<Record> list = Db.use(DictKeys.db_dataSource_main).find(sql, paramValue.toArray());
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (Record record : list) {
				sb.append("\"").append(record.getStr("url")).append("\"");
				if(list.indexOf(record) != (list.size() - 1)){
					sb.append(", ");
				}
			}
			sb.append("]");
			
			renderText(sb.toString());
		}
	}
	
}

