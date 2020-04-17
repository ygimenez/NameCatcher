package com.ygimenez;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class Main extends TrayIcon {
	private static WindowData selectedWindow = null;

	public Main(Image image, String tooltip, PopupMenu popup) {
		super(image, tooltip, popup);
	}

	public static void main(String[] args) {
		try {
			if (!SystemTray.isSupported()) {
				JOptionPane.showMessageDialog(null, "Seu dispositivo não suporta adicionar programas na bandeja do sistema.\nA aplicação será encerrada.", "Erro ao abrir aplicação", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			} else {
				Dimension size = SystemTray.getSystemTray().getTrayIconSize();

				MenuItem createdBy = new MenuItem("Criado por: KuuHaKu");
				createdBy.setEnabled(false);

				MenuItem selectWindow = new MenuItem("Selecionar janela");
				selectWindow.addActionListener(evt -> {
					List<WindowData> windows = WindowHandler.getAllWindowNames();
					selectedWindow = windows.get(JOptionPane.showOptionDialog(
							null,
							"Por favor escolha uma janela para capturar o título.",
							"Selecione uma janela",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE,
							null,
							windows.toArray(),
							null
					));
				});

				MenuItem exit = new MenuItem("Sair");
				exit.addActionListener(evt -> System.exit(0));

				PopupMenu menu = new PopupMenu();

				menu.add(createdBy);
				menu.add(selectWindow);
				menu.addSeparator();
				menu.add(exit);

				TrayIcon app = new Main(ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("icon.png"))).getScaledInstance(size.width, -1, Image.SCALE_SMOOTH), "Name Catcher", menu);

				SystemTray.getSystemTray().add(app);

				Executors.newSingleThreadExecutor().execute(() -> {
					try {
						FileWriter writer;
						while (true) {
							if (selectedWindow != null) {
								writer = new FileWriter("title.txt", false);
								writer.write(WindowHandler.getWindowName(selectedWindow.getHWND()));
								writer.close();
								Thread.sleep(250);
							} else {
								Thread.sleep(250);
							}
						}
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				});
			}
		} catch (AWTException | IOException e) {
			JOptionPane.showMessageDialog(null, "Erro: " + e, "Erro ao abrir aplicação", JOptionPane.ERROR_MESSAGE);
		}
	}
}
