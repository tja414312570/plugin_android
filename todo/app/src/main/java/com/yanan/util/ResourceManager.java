package com.yanan.util;

import java.util.Objects;

/**
 * 资源管理工具，获取文件 支持多个ClassPath，支持自动查询主ClassPath
 * 
 * @author yanan
 *
 */
public class ResourceManager {

	/**
	 * 对路径进行处理
	 * @param path 路径
	 * @return 处理后路径
	 */
	public static String processPath(String path) {
		//nt系统
		if (Objects.requireNonNull(System.getProperty("os.name")).toUpperCase().contains("WINDOWS")) {
			if(path.startsWith("/")) {
				path = path.substring(1);
			}else {
				path = path.replace('\\', '/');
			}
		}
		if(path.startsWith("file:"))
			path = path.substring(5);
		return path.replace("%20", " ");
	}
}
