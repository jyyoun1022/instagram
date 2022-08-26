package org.codej.instagram.Util;

import java.util.ArrayList;
import java.util.List;

public class UtilParser {
	public static List<String> tagsParser(String tags) {
		String temp[] = tags.split("#");

		List<String> tagList = new ArrayList<>();

		for(int i=1; i<temp.length; i++) {
			tagList.add(temp[i]);
		}
		return tagList;
	}
}
