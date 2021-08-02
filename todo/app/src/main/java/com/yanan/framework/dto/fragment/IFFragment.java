package com.yanan.framework.dto.fragment;

import com.yanan.framework.dto.DtoContext;
import com.yanan.framework.dto.entry.ForEach;
import com.yanan.framework.dto.entry.IF;
import com.yanan.framework.fieldhandler.Singleton;

import java.util.ArrayList;
import java.util.List;
@Singleton(false)
public class IFFragment extends FragmentSet implements FragmentBuilder {
	static {
		DtoContext.registerFragmentSet(IF.class,IFFragment.class);
	}
	// 逻辑表达式
	private String test;
	private List<String> testArgument = new ArrayList<String>();
	//	参数列表
	@Override
	public PreparedFragment prepared(Object object) {
		if((boolean) this.test(test,testArgument, object))
			return super.prepared(object);
		if(this.nextSet!=null)
			return this.nextSet.prepared(object);
		return new PreparedFragment();
	}
	//构建sql片段
	@Override
	public void build(Object wrapper) {
		IF ifTagSupport = (IF) this.tagSupport;
		this.test = ifTagSupport.getTest();
		this.test = switchExpress(test);
		String condition = this.test;
		while(Symbol.JAVASCRIPT.match(condition)!=null){
			condition = condition.replaceAll(Symbol.JAVASCRIPT.match(condition).value, " ");
		}
		super.build(wrapper);
		String[] strs = condition.split(" ");
		for(String str  : strs){
			if(str.matches("[a-zA-Z_$][a-zA-Z0-9_$]*") && !str.trim().equals("null")){
				if(!testArgument.contains(str)){
					int i =  str.indexOf(".");
					testArgument.add(i != -1?str.substring(0,i):str);
				}
				this.sqlFragment.addParameter(str);
			}
		}
	}

}