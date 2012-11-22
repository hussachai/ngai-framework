package com.siberhus.ngaiready.admin.action;

import java.io.File;

import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.service.ICrudService;
import com.siberhus.ngaiready.action.BaseCrudAction;
import com.siberhus.ngaiready.admin.model.DocFileItem;
import com.siberhus.ngaiready.admin.service.impl.FileManagerService;

@UrlBinding("/action/admin/documentManager")
public class DocumentManagerAction extends BaseCrudAction<DocFileItem, Long>{
	
	@InjectService(implementation=FileManagerService.class)
	private ICrudService<DocFileItem, Long> fileManagerService;
	
	private DocFileItem model;
	
	private FileBean file;
	
	private String selectedDirPath;
	
	@Override
	public Resolution index(){
		
		File f = new File("D:/temp");
		
		return super.index();
	}
	
	public Resolution ajaxGetFileChildren(){
		return new StreamingResolution("");
	}
	
	@Override
	public ICrudService<DocFileItem, Long> getCrudService() {
		return fileManagerService;
	}
	
	@Override
	public DocFileItem getModel() {
		return model;
	}
	
	@Override
	public void setModel(DocFileItem model) {
		this.model = model;
	}
	
	@Override
	public Class<DocFileItem> getModelClass() {
		return DocFileItem.class;
	}
	
	@Override
	public String getPathPrefix() {
		
		return "/pages/admin/document-manager";
	}
	
	@Override
	public QTO createQueryTransferObjectForSearch() {
		QTO qto = new QTO("from FileItem where 1=1");
		if(getModel()==null){
			return qto;
		}
		
		return qto;
	}
	
	@DontValidate
	@Override
	public Resolution delete() {
		return super.delete();
	}

	@Override
	public Resolution save() {
		return super.save();
	}
	
	public FileBean getFile() {
		return file;
	}

	public void setFile(FileBean file) {
		this.file = file;
	}

	public String getSelectedDirPath() {
		return selectedDirPath;
	}

	public void setSelectedDirPath(String selectedDirPath) {
		this.selectedDirPath = selectedDirPath;
	}
	
}
