package com.yanan.framework.dto.fragment;

import com.yanan.framework.dto.DtoContext;
import com.yanan.framework.dto.GeneralCache;
import com.yanan.framework.dto.entry.Trim;
import com.yanan.framework.dto.entry.Val;
import com.yanan.framework.fieldhandler.Singleton;
import com.yanan.util.asserts.Assert;
@Singleton(false)
public class ValFragment extends FragmentSet implements FragmentBuilder {
	static {
		DtoContext.registerFragmentSet(Val.class,ValFragment.class);
	}
	private String id;
	private VarFragment varFragment;
	//	参数列表
	@Override
	public PreparedFragment prepared(Object object) {
		Val val = (Val) this.tagSupport;
		if(val.getId()!=null&&!val.getId().trim().equals("")){
			this.id = this.sqlFragment.getId()+"."+val.getId();
		}else if(val.getValue()!=null&&!val.getValue().trim().equals("")){
			this.id = this.sqlFragment.getId()+"."+val.getValue();
		}else{
			throw new SqlFragmentBuilderException("mapper variable \""+this.sqlFragment.getId()+"\" not id attr");
		}
		varFragment = GeneralCache.getCache().get(this.id);
		Assert.isNotNull(varFragment, new SqlFragmentBuilderException("mapper variable \""+id+"\" could not be found at wrap id \""
				+this.sqlFragment.getId()));
		//构建子项目
		return varFragment.preparedVar(object);
	}
	//构建sql片段
	@Override
	public void build(Object wrapper) {
		super.build(wrapper);
	}
}