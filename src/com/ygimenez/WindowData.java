package com.ygimenez;

import com.sun.jna.Pointer;

public class WindowData {
	private final Pointer HWND;
	private final String title;

	public WindowData(Pointer HWND, String title) {
		this.HWND = HWND;
		this.title = title;
	}

	public Pointer getHWND() {
		return HWND;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return this.title;
	}
}
