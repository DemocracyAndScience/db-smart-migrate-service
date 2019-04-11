package com.system.utils;

import java.util.Comparator;
import java.util.function.Predicate;

import com.system.entity.VersionInfo;

public class CollectionUtils {

	public static final Comparator<VersionInfo> VERSION_INFO_COMPARATOR = new Comparator<VersionInfo>() {
		@Override
		public int compare(VersionInfo o1, VersionInfo o2) {
			if (o1.getVersion().hashCode() > o2.getVersion().hashCode()) {
				return -1;
			} else if (o1.getVersion().equals(o2.getVersion())) {
				return 0;
			} else {
				return 1;
			}
		}
	};

	public static Predicate<String> stringPredicate(String name) {
		Predicate<String> strPredicate = new Predicate<String>() {

			@Override
			public boolean test(String t) {
				if (t.equalsIgnoreCase(name)) {
					return true;
				}
				return false;
			}
		};
		return strPredicate;
	}

}
