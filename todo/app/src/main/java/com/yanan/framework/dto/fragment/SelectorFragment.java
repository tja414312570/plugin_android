package com.yanan.framework.dto.fragment;

import com.yanan.framework.Plugin;
import com.yanan.framework.dto.DtoContext;
import com.yanan.framework.dto.entry.Default;
import com.yanan.framework.dto.entry.SelectorMapping;
import com.yanan.framework.dto.entry.TagSupport;
import com.yanan.framework.dto.mapper.PreparedSql;
import com.yanan.framework.fieldhandler.Singleton;

import java.util.List;

/**
 * 
 * @author yanan
 * @version 20181009
 */
@Singleton(false)
public class SelectorFragment extends SqlFragment implements FragmentBuilder {
	static {
		DtoContext.registerFragmentSet(SelectorMapping.class,SelectorFragment.class);
	}
	private SelectorMapping selectMapping;

	@Override
	public PreparedFragment prepared(Object object) {
		super.prepared(object);
		if (this.fragmentSet != null)
			return this.fragmentSet.prepared(object);
		return null;
	}

	@Override
	public void build(Object wrapper) {
		super.build(wrapper);
		this.selectMapping = (SelectorMapping) wrapper;
		// create a root FragmentSet
		String sql = this.baseMapping.getXml();
		// 去掉外面的标签,并重组sql语句,不能删除空格
		int index = sql.indexOf(SPLITPREFIX);
		int endex = sql.lastIndexOf(SUFFIX);
		if(endex == -1)
			endex = sql.length();
		sql = sql.substring(index + 1, endex);
		// 建立一个临时的FragmentSet
		FragmentSet currentFragmentSet;
		FragmentSet preFragmentSet = null;
		// 分隔片段
		// 获取sql里的动态语句标签//并对最外层分隔
		List<TagSupport> tags = selectMapping.getTags();
		if (tags == null || tags.size() == 0)// 如果没有动态标签
		{
			this.fragmentSet = currentFragmentSet =Plugin.createInstance(DtoContext.getFragmentSet(Default.class),false);
			currentFragmentSet.setXml(this.baseMapping.getXml());
//			currentFragmentSet.setContext(getContext());
			currentFragmentSet.setValue(this.baseMapping.getContent());
			currentFragmentSet.setSqlFragment(this);
			currentFragmentSet.build(null);
		} else {
			for (TagSupport tag : tags) {
				// 获得TagSupport的类型
				Class<?> tagClass = tag.getClass();
				// 截取类容
				int predex = sql.indexOf(tag.getXml());
				int len = tag.getXml().length();
				String preffix = sql.substring(0, predex);
				if (!preffix.trim().equals("")) {
					currentFragmentSet = Plugin.createInstance(DtoContext.getFragmentSet(Default.class),false);
					currentFragmentSet.setXml(preffix);
					currentFragmentSet.setValue(preffix);
//					currentFragmentSet.setContext(getContext());
					currentFragmentSet.setSqlFragment(this);
					currentFragmentSet.build(null);
					if (this.fragmentSet == null)
						this.fragmentSet = currentFragmentSet;
					if (preFragmentSet != null)
						preFragmentSet.setNextSet(currentFragmentSet);
					preFragmentSet = currentFragmentSet;
				}
				// 根据类型获取对应FragmentSet
				try {
//					currentFragmentSet = (FragmentSet) PlugsFactory.getPluginsInstanceByAttributeStrict(FragmentBuilder.class,
//							tagClass.getName() + ".fragment");
					currentFragmentSet = Plugin.createInstance(DtoContext.getFragmentSet(tagClass),false);
					// 判断根FragmentSet是否为空
					if (this.fragmentSet == null)
						this.fragmentSet = currentFragmentSet;
					if (preFragmentSet != null)
						preFragmentSet.setNextSet(currentFragmentSet);
					preFragmentSet = currentFragmentSet;
					currentFragmentSet.setXml(tag.getXml());
					currentFragmentSet.setValue(tag.getValue());
					currentFragmentSet.setTagSupport(tag);
					currentFragmentSet.setSqlFragment(this);
//					currentFragmentSet.setContext(getContext());
					currentFragmentSet.build(null);
					sql = sql.substring(predex + len);
				}catch (Exception e) {
					throw new SqlFragmentBuilderException("could not found sql fragment register for ["+tagClass.getName()+"]",e);
				}
			}
			// 截取类容
			if (sql != null && !sql.trim().equals("")) {
				currentFragmentSet = Plugin.createInstance(DtoContext.getFragmentSet(Default.class),false);
				currentFragmentSet.setXml(sql);
				currentFragmentSet.setValue(sql);
				currentFragmentSet.setSqlFragment(this);
//				currentFragmentSet.setContext(getContext());
				currentFragmentSet.build(null);
				if (this.fragmentSet == null)
					this.fragmentSet = currentFragmentSet;
				if (preFragmentSet != null)
					preFragmentSet.setNextSet(currentFragmentSet);
				preFragmentSet = currentFragmentSet;
			}
		}
	}

	public PreparedSql getPreparedSql(Object parameter) {
		PreparedFragment preparedFragment = this.prepared(parameter);
		PreparedSql preparedSql = new PreparedSql(preparedFragment.getSql(), preparedFragment.getVariable(), this);
		return preparedSql;
	}
}