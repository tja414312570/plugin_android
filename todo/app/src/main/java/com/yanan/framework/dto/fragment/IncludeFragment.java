package com.yanan.framework.dto.fragment;

import android.util.Log;

import com.yanan.framework.dto.DtoContext;
import com.yanan.framework.dto.SqlExecuteException;
import com.yanan.framework.dto.entry.BaseMapping;
import com.yanan.framework.dto.entry.Include;
import com.yanan.framework.fieldhandler.Singleton;

@Singleton(false)
public class IncludeFragment extends FragmentSet implements FragmentBuilder {
	static {
		DtoContext.registerFragmentSet(Include.class,IncludeFragment.class);
	}
	// 逻辑表达式
	private String id;
	private SqlFragment sql;
	//	参数列表
	@Override
	public PreparedFragment prepared(Object object) {
		return super.prepared(object);
	}
	//构建sql片段
	@Override
	public void build(Object wrapper) {
		Include include = (Include) this.tagSupport;
		if(include.getId()!=null&&!include.getId().trim().equals("")){
			this.id = include.getId();
		}else if(include.getValue()!=null&&!include.getValue().trim().equals("")){
			this.id = include.getValue();
		}else{
			throw new SqlExecuteException("mapper \""+this.sqlFragment.getId()+"\" not id attr");
		}
//		if(id.indexOf(".")==-1) {
//			try{
//				 System.out.println("id:"+id);
//				this.sql = this.context.getSqlFragmentManger().getSqlFragment(this.id);
//			}catch (Exception e) {
//			}
//		}
		if(sql==null){
			String nid = id;
			BaseMapping mapping =sqlFragmentManager.getWrapper(nid);
			String ids = "["+id+"]";
			if(mapping==null && !id.contains(".")) {
				nid = this.sqlFragment.getBaseMapping().getWrapperMapping().getNamespace()+"."+id;
				mapping =sqlFragmentManager.getWrapper(nid);
				ids += ","+"["+nid+"]";
			}
			if(this.sql == null && this.getSqlFragment().getBaseMapping().getParentMapping()!= null) {
				nid = this.sqlFragment.getBaseMapping().getParentMapping().getWrapperMapping().getNamespace()+"."+id;
				mapping =sqlFragmentManager.getWrapper(nid);
				ids += ","+"["+nid+"]";
			}
			if(mapping==null)
				if(this.getSqlFragment().getBaseMapping().getParentMapping() == null)
					throw new SqlFragmentBuilderException("mapper \""+ids+"\" could not be found at wrap id \""
						+this.sqlFragment.getId()+"\" ");
				else
					throw new SqlFragmentBuilderException("mapper \""+ids+"\" could not be found at wrap id \""
							+this.sqlFragment.getId()+"\" ");
			Log.d("INCLUDE_FRAGMENT","sql fragment \""+this.getSqlFragment().getBaseMapping().getId()+"\" child fragment use id ["+nid+"]");
			mapping.setParentMapping(this.getSqlFragment().getBaseMapping());
			this.sql = DtoContext.buildFragment(mapping,sqlFragmentManager);
		}
		for(String args : sql.getArguments()){
			this.sqlFragment.addParameter(args);
		}
		this.childSet = sql.fragmentSet;
		super.build(wrapper);
	}

}