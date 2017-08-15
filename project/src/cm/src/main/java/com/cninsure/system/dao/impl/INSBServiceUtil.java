package com.cninsure.system.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 业务层通用更新方法
 */
@Repository
public class INSBServiceUtil {

	/**
	 * @param newList 当前数据
	 * @param oldList 旧数据
	 * @return Map<"add",value>需要新增数据  , Map<"delete",value>需要删除数据
	 */
	public Map<String, List<String>> updateUtil(List<String> newList,
			List<String> oldList) {
		
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		List<String> addList = new ArrayList<String>();
		List<String> tempList = new ArrayList<String>();
		List<String> deleteList = new ArrayList<String>();

		for (String str : newList) {
			tempList.add(str);
		}

		// 新增数据
		tempList.removeAll(oldList);
		addList = tempList;
		result.put("add", addList);

		// 删除数据
		oldList.removeAll(newList);
		deleteList = oldList;
		result.put("delete", deleteList);
		return result;
	}

}
