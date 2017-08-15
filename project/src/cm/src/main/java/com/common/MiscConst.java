package com.common;

import java.util.*;

public class MiscConst {
	//支付后的节点
	private static String[] noCancelNode = { "23", "24", "25", "26", "27", "28", "29", "30", "33" };
	public static List<String> noCancelNodeList = Collections.unmodifiableList(Arrays.asList(noCancelNode));
}
