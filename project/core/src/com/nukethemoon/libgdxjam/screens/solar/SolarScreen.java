package com.nukethemoon.libgdxjam.screens.solar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.nukethemoon.libgdxjam.Log;
import com.nukethemoon.libgdxjam.App;
import com.nukethemoon.libgdxjam.screens.ark.ArkScreen;
import com.nukethemoon.libgdxjam.screens.planet.PlanetScreen;
import com.nukethemoon.libgdxjam.screens.planet.WorldController;

import java.util.ArrayList;
import java.util.List;

public class SolarScreen implements Screen {

	Vector2 tmpVector = new Vector2(0, 0);


	private final Vector2 shipPosition = new Vector2(INITIAL_ARK_POSITION_X, INITIAL_ARK_POSITION_Y);


	//0 - 359
	private float shipRotationDegrees = 0;
	private float currentRotation = 90;

	private int[] shipSpeedLevels = new int[]{0, 1, 2, 4, 6, 8};
	private final int MAX_SPEED_LEVEL = shipSpeedLevels.length - 1;
	private static final float SPEED_DECREASE_BY_DECAY_RATE = 0.02f;
	private static final float SPEED_DECREASE_BY_BRAKES_RATE = 0.1f;
	private float currentSpeedDecay = 0;
	private int currentSpeedLevel = 0;

	private WorldController world;


	private static final int INITIAL_ARK_POSITION_Y = 10;
	private static final int INITIAL_ARK_POSITION_X = 10;
	private List<Vector2> planetPositions = new ArrayList<Vector2>();

	private SpriteBatch batch;

	private Sprite arkSprite;
	private int screenHeight;
	private int screenWidth;

	float arkHeight;
	float arkWidth;


	public SolarScreen(Skin uiSkin, InputMultiplexer multiplexer) {
		batch = new SpriteBatch();
		arkSprite = new Sprite(App.TEXTURES.findRegion("ship_placeholder"));
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		arkSprite.setX(INITIAL_ARK_POSITION_X);
		arkSprite.setY(INITIAL_ARK_POSITION_Y);
		arkWidth = arkSprite.getWidth();
		arkHeight = arkSprite.getHeight();

		planetPositions = calculatePlanetPositions();
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderArc();
		renderPlanets();
		handleArkMovementInput(delta);
		handleAppNavigation();
	}


	private void renderArc() {
		batch.begin();
		if (shipRotationDegrees != 0) {


			currentRotation += shipRotationDegrees;
			if (currentRotation < 0) {
				currentRotation += 360;
			}
			currentRotation = currentRotation % 360;
			arkSprite.rotate(shipRotationDegrees);
			Log.l(PlanetScreen.class, "currentRotation: " + currentRotation + " -  new Rotation:" + shipRotationDegrees);
			shipRotationDegrees = 0;

		}
		arkSprite.draw(batch);
		batch.end();
	}

	private int calculateBoostedSpeed() {
		currentSpeedDecay += SPEED_DECREASE_BY_DECAY_RATE;
		if (currentSpeedDecay > 1) {
			currentSpeedDecay = 0;
			currentSpeedLevel -= 1;
		}
		if (Gdx.app.getInput().isKeyPressed(19)) {
			currentSpeedLevel += 1;
		}
		if (Gdx.app.getInput().isKeyPressed(20)) {
			currentSpeedDecay += SPEED_DECREASE_BY_BRAKES_RATE;
		}

		if (currentSpeedLevel < 0) {
			currentSpeedLevel = 0;
		}
		if (currentSpeedLevel > MAX_SPEED_LEVEL) {
			currentSpeedLevel = MAX_SPEED_LEVEL;
		}

		return shipSpeedLevels[currentSpeedLevel];
	}

	private int calculateConstantSpeed() {
		return 4;
	}

	private void drawOrigin() {
	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void resize(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
	}

	@Override
	public void pause() {

	}


	private boolean isArkCollidingWithPlanet() {
		return false;
	}

	private boolean isArcSelected() {
		return false;
	}

	private void openPlanetScreen() {
		App.openScreen(PlanetScreen.class);
	}

	private void openArkScreen() {
		App.openScreen(ArkScreen.class);
	}


	private List<Vector2> calculatePlanetPositions() {
		return null;
	}


	private void renderPlanets() {

	}

	/*
		 x     y
	deg sin  cos
	---------------
	0    0    1
	90   1    0
	180  0   -1
	270 -1    0
	*/
	private void handleArkMovementInput(float delta) {
		if (Gdx.app.getInput().isKeyPressed(Input.Keys.UP)) {
			moveArcPosition(delta);
		} else if (Gdx.app.getInput().isKeyPressed(Input.Keys.RIGHT)) {
			shipRotationDegrees = shipRotationDegrees + (50 * delta);
		} else if (Gdx.app.getInput().isKeyPressed(Input.Keys.DOWN)) {
			moveArcPosition(delta);
		} else if (Gdx.app.getInput().isKeyPressed(Input.Keys.LEFT)) {
			shipRotationDegrees = shipRotationDegrees - (50 * delta);
		}
	}

	private void moveArcPosition(float delta) {
		double radians = Math.toRadians(currentRotation);
		float translateX = (float) Math.cos(radians) ;
		float translateY = (float) Math.sin(radians) ;

		arkSprite.translate(translateX, translateY);



		shipPosition.x = arkSprite.getX();
		shipPosition.y = arkSprite.getY();
		checkIfArkIsOffScreenAndCorrect();
	}

	private void checkIfArkIsOffScreenAndCorrect() {
		if (isArkOffscreenLeft()) {
			arkSprite.translateX(0 - shipPosition.x + screenWidth - 20);
		}
		if (isArkOffscreenRight()) {
			arkSprite.translateX(-1 * (arkWidth + screenWidth - 20));
		}
		if (isArkOffscreenTop()) {
			arkSprite.translateY(-1 * (arkHeight + screenHeight - 20));
		}
		if (isArkOffscreenBottom()) {
			arkSprite.translateY(0 - shipPosition.y + screenHeight - 20);
		}

	}

	private boolean isArkOffscreenBottom() {
		return shipPosition.y < 0 - arkHeight;
	}

	private boolean isArkOffscreenTop() {
		return screenHeight < shipPosition.y;
	}

	private boolean isArkOffscreenRight() {
		return screenWidth < shipPosition.x;
	}

	private boolean isArkOffscreenLeft() {
		return shipPosition.x < 0 - arkWidth;
	}

	private void handleAppNavigation() {
		if (isArkCollidingWithPlanet()) {
			openPlanetScreen();
		}

		if (isArcSelected()) {
			openArkScreen();
		}
	}


}
