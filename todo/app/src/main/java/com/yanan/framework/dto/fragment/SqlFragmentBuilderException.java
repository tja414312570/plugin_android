package com.yanan.framework.dto.fragment;

public class SqlFragmentBuilderException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7292258594556380388L;

	public SqlFragmentBuilderException(String msg, Throwable e) {
		super(msg,e);
	}

	public SqlFragmentBuilderException(String msg) {
		super(msg);
	}
}
