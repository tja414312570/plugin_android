package com.yanan.framework.dto.fragment;

import com.yanan.framework.dto.DtoContext;
import com.yanan.framework.dto.entry.Case;
import com.yanan.framework.fieldhandler.Singleton;

@Singleton(false)
public class CaseFragment extends FragmentSet implements FragmentBuilder {
	static {
		DtoContext.registerFragmentSet(Case.class,CaseFragment.class);
	}
	private Case cases;
	//	参数列表
	@Override
	public PreparedFragment prepared(Object object) {
		return super.prepared(object);
	}
	//构建sql片段
	@Override
	public void build(Object wrapper) {
		this.setCases((Case) this.tagSupport);
		super.build(wrapper);
	}
	public Case getCases() {
		return cases;
	}
	public void setCases(Case cases) {
		this.cases = cases;
	}
}