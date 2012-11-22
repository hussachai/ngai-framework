package com.siberhus.ngaiready.action;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.action.AbstractCrudAction;
import com.siberhus.ngai.core.CrudHelper;
import com.siberhus.ngai.core.Ngai;
import com.siberhus.ngai.model.IModel;
import com.siberhus.ngai.view.datagrid.DisplayTagGridHandler;
import com.siberhus.ngai.view.datagrid.IGridHandler;

/**
 * 
 * @author hussachai
 *
 * @param <T>
 * @param <ID>
 */
public abstract class BaseCrudAction<T, ID extends Serializable> extends
		AbstractCrudAction<T, ID> {
	
	private String layout;
	private Boolean multipleSelect;
	private String lookupElementId;
	private String lookupModelId;
	private String lookupModelName;
	
	@SuppressWarnings("unchecked")
	public Class<ID> getIdClass(){
		return (Class<ID>)Long.class;
	}
	
	@Override
	public IGridHandler getGridService() {
		return DisplayTagGridHandler.getInstance();
	}
	
	@Override
	public String getIndexPage() {
		
		return "/list.jsp";
	}
	
	@Override
	public String getEditPage() {
		
		return "/edit.jsp";
	}
	
	@Override
	public String getViewPage() {
		
		return "/view.jsp";
	}
	
	@Override
	@DefaultHandler
	@DontValidate
	public Resolution index(){
		bindLayoutAttribute();
		bindChildPageAttributes();
		return super.index();
	}
	
	@Override
	@DontValidate
	public Resolution edit() {
		bindLayoutAttribute();
		return super.edit();
	}
	
	@Override
	@DontValidate
	public Resolution view() {
		bindLayoutAttribute();
		return super.view();
	}
	
	@DontValidate
	public Resolution export() throws Exception{
		
		if(!IModel.class.isAssignableFrom(getModelClass())){
			return new StreamingResolution("plain/text",getModelClass()+" does not support export feature.");
		}
		HttpServletRequest request = getContext().getRequest();
		String fileName = StringUtils.trim(request.getParameter("fileName"));
		String escapeCsv = request.getParameter("escapeCsv");
		String columnSeparator = StringUtils.trim(request.getParameter("columnSeparator"));
		String fileFormat = request.getParameter("fileFormat");
		String fileEncoding = request.getParameter("fileEncoding");
		
		boolean isEscapeCsv = "true".equalsIgnoreCase(escapeCsv);
		String lineSeparator = null;
		if(fileFormat.startsWith("WIN")){
			lineSeparator = "\r\n";
		}else if(fileFormat.startsWith("UNIX")){
			lineSeparator = "\n";
		}else if(fileFormat.startsWith("MAC")){
			lineSeparator = "\r";
		}
		if("\\t".equals(columnSeparator)){
			columnSeparator = "\t";
		}
		HttpServletResponse response = getContext().getResponse();
		response.setCharacterEncoding(fileEncoding);
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"" +fileName + "\"");
		OutputStream out = response.getOutputStream();
		BufferedOutputStream bout = null;
		if(out instanceof BufferedOutputStream){
			bout = (BufferedOutputStream)out;
		}else{
			bout = new BufferedOutputStream(out);
		}
		OutputStreamWriter writer = new OutputStreamWriter(bout, 
				Charset.forName(fileEncoding));
		
		QTO qto =  createQueryTransferObjectForSearch();
		int subResultSize = 100;  
		int fullListSize = getCrudService().getSearchResultCount(qto).intValue();
		int totalRound = (fullListSize/subResultSize)+1;
		EntityManager em = Ngai.getEntityManager();
		Query query = CrudHelper.createQueryObject(em, 
				qto.getQueryString(), qto.getParameterList());
		
		IModel<Long> model = (IModel<Long>)getModelClass().newInstance();
		for(String colName : model.toNames()){
			writer.append(colName);
			writer.append(columnSeparator);
		}
		writer.append(lineSeparator);
		
		for(int i=0,j=0; i<totalRound;i++,j+=subResultSize){
			query.setFirstResult(j).setMaxResults(subResultSize);
			List<?> resultList = query.getResultList();
			for(Object result : resultList){
				model = (IModel<Long>)result;
				for(Object valueObj : model.toValues()){
					String value = ObjectUtils.toString(valueObj);
					if(isEscapeCsv){
						value = StringEscapeUtils.escapeCsv(value);
					}
					writer.append(value);
					writer.append(columnSeparator);
				}
				writer.append(lineSeparator);
			}
		}
		writer.flush();
		writer.close();
		return null;
	}
	
	public String getLayout() {
		if(StringUtils.isEmpty(layout)){
			return "standard";
		}
		return layout;
	}
	
	public void setLayout(String layout) {
		this.layout = layout;
	}
	
	public String getLookupElementId() {		
		return lookupElementId;
	}

	public void setLookupElementId(String lookupElementId) {
		this.lookupElementId = lookupElementId;
	}

	public String getLookupModelId() {
		if(StringUtils.isEmpty(lookupModelId)){
			return "id";
		}
		return lookupModelId;
	}
	
	public void setLookupModelId(String lookupModelId) {
		this.lookupModelId = lookupModelId;
	}
	
	public String getLookupModelName() {
		if(StringUtils.isEmpty(lookupModelName)){
			return "id";
		}
		return lookupModelName;
	}

	public void setLookupModelName(String lookupModelName) {
		this.lookupModelName = lookupModelName;
	}

	public Boolean getMultipleSelect() {
		return multipleSelect;
	}
	
	public void setMultipleSelect(Boolean multipleSelect) {
		this.multipleSelect = multipleSelect;
	}
	
	public boolean isMinimumLayout(){
		String layout = getLayout();
		if(layout.startsWith("min")){
			return true;
		}
		return false;
	}
	
	protected void bindLayoutAttribute(){
		HttpServletRequest request = getContext().getRequest();
		layout = request.getParameter("layout");
	}
	
	protected void bindChildPageAttributes(){
		HttpServletRequest request = getContext().getRequest();
		multipleSelect = "true".equalsIgnoreCase(request.getParameter("multiSel"));
		lookupElementId = request.getParameter("elemId");
		lookupModelId = request.getParameter("modelId");
		lookupModelName = request.getParameter("modelName");
	}
}
