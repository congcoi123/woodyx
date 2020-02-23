/*
The MIT License

Copyright (c) 2014 kong <congcoi123@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.tenserver.woodyx.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.tenserver.woodyx.camera.XCamera;
import com.tenserver.woodyx.physics.BoxUtility;

/**
 * 
 * @author kong
 *
 */

public abstract class XRenderer {
	protected Stage stage;

	protected Array<SpriteBatch> batchs;
	protected Array<XCamera> cameras;

	private Box2DDebugRenderer debuger;

	public XRenderer(Stage stage, Array<SpriteBatch> batchs, Array<XCamera> cameras) {
		this.stage = stage;
		this.batchs = batchs;
		this.cameras = cameras;
	}

	/**
	 * Clear the current screen
	 */
	public void clearScreen() {
		// clear the bitmap
		Gdx.gl20.glClearColor(1, 1, 1, 1);
		Gdx.gl20.glClearDepthf(1.0f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
	}

	/**
	 * Reset the stage
	 */
	public void resetStage() {
		stage.clear();
	}

	/**
	 * Render the stage
	 */
	public void renderStage() {
		stage.draw();
	}

	/**
	 * Update the stage
	 */
	public void updateStage(float deltaTime) {
		stage.act(deltaTime);
	}

	/**
	 * Clear force if world is not <code>null</code>
	 */
	public void clearWorld(World world) {
		world.clearForces();
	}

	/**
	 * Update the current world
	 */
	public void updateWorld(World world, float deltaTime) {
		world.step(deltaTime, BoxUtility.VELOCITY_ITER, BoxUtility.POSITION_ITER);
	}

	/**
	 * Update the cameras
	 */
	public void updateCameras(float deltaTime) {
		for (XCamera camera : cameras) {
			if (camera != null) {
				camera.act(deltaTime);
			}
		}
	}

	/**
	 * Set the projection matrix
	 */
	public void projectBatchs() {
		for (int i = 0; i < batchs.size; i++) {
			if (batchs.get(i) != null) {
				batchs.get(i).setProjectionMatrix(cameras.get(i).combined);
			}
		}
	}

	/**
	 * Set the camera follow the target
	 */
	Vector3 camPoint = new Vector3();
	public void setCameraFollow(int cameraIndex, Vector2 target, boolean isFollowX, boolean isFollowY) {
		getCamera(cameraIndex).project(camPoint.set(target.x, target.y, 0));
		// set follow
		if (isFollowX)
			getCamera(cameraIndex).position.x = target.x;
		if (isFollowY)
			getCamera(cameraIndex).position.y = target.y;
	}

	/**
	 * Debug box2d: initialize
	 */
	public void startDebugBox(int cameraIndex) {
		// initialize the debugger
		debuger = new Box2DDebugRenderer();
	}

	/**
	 * Render debug box2d: render
	 */
	public void renderDebug(World world, int cameraIndex) {
		objDrawable(cameraIndex, true);
		debuger.render(world, getCamera(cameraIndex).combined);
		objDrawable(cameraIndex, false);
	}

	/**
	 * Prepare for drawing background
	 */
	public void bgDrawable(int batchIndex, boolean flag) {
		if (flag) {
			getSpriteBatch(batchIndex).disableBlending();
			getSpriteBatch(batchIndex).begin();
		} else {
			getSpriteBatch(batchIndex).end();
		}
	}

	/**
	 * Prepare for drawing objects
	 */
	public void objDrawable(int batchIndex, boolean flag) {
		if (flag) {
			getSpriteBatch(batchIndex).enableBlending();
			getSpriteBatch(batchIndex).begin();
		} else {
			getSpriteBatch(batchIndex).end();
		}
	}

	/**
	 * Retrieve the sprite batch
	 */
	public SpriteBatch getSpriteBatch(int index) {
		return batchs.get(index);
	}

	/**
	 * Retrieve the camera by index
	 */
	public XCamera getCamera(int index) {
		return cameras.get(index);
	}
}
