package com.nukethemoon.libgdxjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Disposable;

public class Models implements Disposable {

	public static Model FUEL;
	public static btCollisionShape FUEL_SHAPE;

	public static void init() {
		ModelLoader loader = new ObjLoader();
		BoundingBox boundingBox = new BoundingBox();

		FUEL = loader.loadModel(Gdx.files.internal("models/fuel.obj"));
		FUEL.calculateBoundingBox(boundingBox);
		FUEL_SHAPE = new btBoxShape(boundingBox.getDimensions(new Vector3()).scl(0.65f));
	}


	@Override
	public void dispose() {
		FUEL.dispose();
		FUEL_SHAPE.dispose();
	}
}
