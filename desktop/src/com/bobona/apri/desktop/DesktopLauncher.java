package com.bobona.apri.desktop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.bobona.animationMaker.Application;
import com.bobona.apri.Demo;

import java.util.Arrays;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(2560, 1440);
		boolean animationMaker = Arrays.asList(arg).contains("animationMaker");
		if (animationMaker) {
			config.setInitialVisible(false);
			new Lwjgl3Application(new ApplicationAdapter() {
				@Override
				public void create() {
					super.create();
					javafx.application.Application.launch(Application.class);
				}
			}, config);
		} else {
			new Lwjgl3Application(new Demo(), config);
		}
	}
}
