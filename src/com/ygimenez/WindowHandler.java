package com.ygimenez;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

import java.util.ArrayList;
import java.util.List;

public class WindowHandler {
	interface User32 extends StdCallLibrary {
		User32 INSTANCE = Native.load("user32", User32.class);

		interface WNDENUMPROC extends StdCallLibrary.StdCallCallback {
			boolean callback(Pointer HWND, Pointer arg);
		}

		boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer userData);
		int GetWindowTextA(Pointer HWND, byte[] lpString, int nMaxCount);
		boolean IsWindowVisible(Pointer HWND);
	}

	public static List<WindowData> getAllWindowNames() {
		final List<WindowData> windowNames = new ArrayList<>();
		final User32 user32 = User32.INSTANCE;
		user32.EnumWindows((HWND, arg) -> {
			byte[] windowText = new byte[512];
			user32.GetWindowTextA(HWND, windowText, 512);
			String wText = Native.toString(windowText, "windows-1252").trim();
			if (!wText.isEmpty() && user32.IsWindowVisible(HWND)) {
				windowNames.add(new WindowData(HWND, wText));
			}
			return true;
		}, null);

		return windowNames;
	}

	public static String getWindowName(Pointer HWND) {
		final User32 user32 = User32.INSTANCE;
		byte[] windowText = new byte[512];
		user32.GetWindowTextA(HWND, windowText, 512);
		return Native.toString(windowText, "windows-1252").trim();
	}
}
