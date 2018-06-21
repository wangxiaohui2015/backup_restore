package com.mchange.v2.log;

import java.util.logging.Level;

public class ModifyLogLevel {
	public static void modifyInfoLevel(Level level) {
		MLevel.INFO.level = level;
	}
}
