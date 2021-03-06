package com.nukethemoon.libgdxjam.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.nukethemoon.libgdxjam.App;
import com.nukethemoon.libgdxjam.Log;

public class InputController implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		boolean result = Gdx.input.isKeyPressed(keycode);
		if (App.config.debugMode && result) {
			Log.l(InputController.class, "key down " + keycode);
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (App.config.debugMode) {
			Log.l(InputController.class, "touch down x " + screenX + " y " + screenY);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
