package com.yanan.framework.dto.fragment;


import com.yanan.framework.dto.DtoContext;
import com.yanan.framework.dto.entry.Default;
import com.yanan.framework.fieldhandler.Singleton;

@Singleton(false)
public class DefaultFragment extends FragmentSet implements FragmentBuilder {
	static {
		DtoContext.registerFragmentSet(Default.class,DefaultFragment.class);
	}
	private Default defaults;
	//	参数列表
	@Override
	public PreparedFragment prepared(Object object) {
		return super.prepared(object);
	}
	//构建sql片段
	@Override
	public void build(Object wrapper) {
		setDefaults((Default) this.tagSupport);
		super.build(wrapper);
	}
	public Default getDefaults() {
		return defaults;
	}
	public void setDefaults(Default defaults) {
		this.defaults = defaults;
	}
}