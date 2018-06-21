package com.my.br.util;

import com.my.br.ui.MainUI;

public class ProgressUtil {
	private long totalPoints = 0;
	private int index = 1;
	private long incressPoints = 0;

	public ProgressUtil(long totalPoints) {
		super();
		this.totalPoints = totalPoints;
	}

	public void showProgress(long currentPoint) {
		if (totalPoints == 0) {
			return;
		}
		if (currentPoint * 1.0 / totalPoints >= (index / 10.0)) {
			MainUI.prtln("Completed " + ((index * 10)) + "%.");
			index += 1;
		}
	}

	public void showProgressByIncress(long incressPoint) {
		incressPoints += incressPoint;
		if (incressPoints * 1.0 / totalPoints >= (index / 10.0)) {
			MainUI.prtln("Completed " + ((index * 10)) + "%.");
			index += 1;
		}
	}
}
