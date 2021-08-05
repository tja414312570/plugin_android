package com.yanan.framework.dto.fragment;

import com.yanan.framework.Plugin;
import com.yanan.framework.dto.DtoContext;
import com.yanan.framework.dto.entry.When;
import com.yanan.framework.fieldhandler.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton(false)
public class WhenFragment extends FragmentSet implements FragmentBuilder {
	static {
		DtoContext.registerFragmentSet(When.class,WhenFragment.class);
	}
	// 逻辑表达式
	private String test;
	private List<String> testArgument = new ArrayList<String>();
	private When whens;
	private boolean breaks;
	//	参数列表
	@SuppressWarnings("unchecked")
	@Override
	public PreparedFragment prepared(Object objects) {
		if((boolean) this.test(test,testArgument, objects)) {
			if(!this.breaks) {
				return super.prepared(objects);
			}else {
				/**
				 * 通过调用PlugsFactory生成PreparedFragment的实例。
				 */
				PreparedFragment preparedFragment = Plugin.createInstance(PreparedFragment.class,false);
				/**
				 * 将所有的变量都添加到预执行片段中
				 */
				preparedFragment.addAllVariable(this.preparedParameter(this.parameters, objects));
				/**
				 * if next node is not null and child node is not null
				 */
				if (this.childSet != null) {
					this.childSet.setSqlFragment(this.sqlFragment);
					PreparedFragment child = this.childSet.prepared(objects);
					preparedFragment.setSql(child.getSql());
					preparedFragment.addParameter(child.getArguments());
					preparedFragment.addAllVariable(child.getVariable());
				} else {
					preparedFragment.setSql(this.preparedParameterSql(this.value, objects));
					preparedFragment.addParameter(this.parameters);
				}
				return preparedFragment;
			}
		}
		if(this.nextSet!=null)
			return this.nextSet.prepared(objects);
		return new PreparedFragment();
	}
	//构建sql片段
	@Override
	public void build(Object wrapper) {
		this.whens = (When) this.tagSupport;
		this.test = whens.getTest();
		this.breaks = whens.isBreaks();
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