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
package com.tenserver.woodyx.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tenserver.woodyx.accessor.SpriteAccessor;
import com.tenserver.woodyx.physics.BoxUtility;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quint;

/**
 * 
 * @author kong
 *
 */
public class XCamera extends OrthographicCamera {

	private Sprite spZoom, spFall, spVibrate;
	private int devide;
	private TweenManager tweenZoom, tweenFall, tweenVibrate;
	private boolean isZoomOut;
	private boolean isZoomIn;
	private boolean isFalled;
	private boolean isFinishVibrate;

	/**
	 * Create a normal camera with view in the sreen's center
	 */
	public XCamera(float viewportWidth, float viewportHeight, boolean isInBox) {
		// set the viewport
		float worldToBox = 1;
		if (isInBox) {
			worldToBox = BoxUtility.WORLD_TO_BOX;
		}
		this.viewportWidth = viewportWidth * worldToBox;
		this.viewportHeight = viewportHeight * worldToBox;
		// set other parameters
		isZoomIn = false;
		isZoomOut = false;
		isFalled = false;
		isFinishVibrate = true;
		devide = 0;
	}

	/**
	 * Set the camera to the center of screen
	 */
	public void setCameraToCenter() {
		this.setToOrtho(false, viewportWidth, viewportHeight);
		this.position.set(viewportWidth / 2, viewportHeight / 2, 0);
	}

	/**
	 * Set the camera's position
	 */
	public void setPosition(float x, float y) {
		this.position.set(x, y, 0);
	}

	/**
	 * Set the zoom effect
	 */
	public void setZoom(float target, float duration) {
		// set type of the zoom
		if (target < 1) {
			devide = 1000;
			isZoomIn = true;
			isZoomOut = false;
		} else {
			devide = 100;
			isZoomOut = true;
			isZoomIn = false;
		}

		// initialize the parameters
		if (tweenZoom == null) {
			tweenZoom = new TweenManager();
		}
		if (spZoom == null) {
			spZoom = new Sprite();
		}
		spZoom.setPosition(0, 0);

		// zoom's type
		Tween.to(spZoom, SpriteAccessor.CPOS_XY, duration).target(target * devide, 0).path(TweenPaths.catmullRom)
				.ease(Back.IN).start(tweenZoom);
	}

	/**
	 * Set the falling effect
	 */
	public void setFall(int rotateDir, float duration) {
		// set the falling flag
		isFalled = false;

		// initialize parameters
		if (tweenFall == null) {
			tweenFall = new TweenManager();
		}
		if (spFall == null) {
			spFall = new Sprite();
		}
		spFall.setPosition(0, 0);

		Tween.to(spFall, SpriteAccessor.CPOS_XY, duration).target(rotateDir * 90, 0).ease(Quint.IN)
				.path(TweenPaths.catmullRom).setCallback(new TweenCallback() {
					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						isFalled = true;
					}
				}).start(tweenFall);
	}

	/**
	 * Set the vibration effect
	 */
	public void setVibrate(float range, int times, float duration) {
		// set the vibration flag
		isFinishVibrate = false;

		// initialize parameters
		if (tweenVibrate == null) {
			tweenVibrate = new TweenManager();
		}
		if (spVibrate == null) {
			spVibrate = new Sprite();
		}
		spVibrate.setPosition(-range, -range / 2);

		Timeline.createSequence()
				.push(Tween.to(spVibrate, SpriteAccessor.CPOS_XY, duration).target(range, range / 2).ease(Elastic.INOUT)
						.path(TweenPaths.catmullRom).repeat(times, 0))
				.push(Tween.to(spVibrate, SpriteAccessor.CPOS_XY, duration).target(-range, -range / 2)
						.ease(Linear.INOUT).path(TweenPaths.catmullRom))
				.setCallback(new TweenCallback() {
					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						// reset camera position
						setPosition(viewportWidth / 2, viewportHeight / 2);
						isFinishVibrate = true;
					}
				}).start(tweenVibrate);
	}

	/**
	 * Update for this camera
	 */
	public void act(float deltaTime) {
		// update the tween-effect
		updateTween(deltaTime);
		// update the camera
		this.update();
	}

	/**
	 * Retrieve the zoom out
	 */
	public boolean getZoomOut() {
		return isZoomOut;
	}

	/**
	 * Retrieve the zoom in
	 */
	public boolean getZoomIn() {
		return isZoomIn;
	}

	/**
	 * Check if the falling effect is enabled
	 */
	public boolean getIsFall() {
		return isFalled;
	}

	/**
	 * Check if the vibration effect is finished
	 */
	public boolean getFinishVibrate() {
		return isFinishVibrate;
	}

	// Update the tween-effect
	private void updateTween(float deltaTime) {
		// update the tween-effect
		if (tweenZoom != null) {
			tweenZoom.update(deltaTime);
		}
		if (tweenFall != null) {
			tweenFall.update(deltaTime);
		}
		if (tweenVibrate != null) {
			tweenVibrate.update(deltaTime);
		}

		// update the zoom effect
		if (spZoom != null) {
			if (devide == 1000) {
				this.zoom = (1 - spZoom.getX() / devide);
			} else
				this.zoom = spZoom.getX() / devide;
		}

		// update the vibration effect
		if (spVibrate != null) {
			if (!isFinishVibrate) {
				this.translate(spVibrate.getX(), spVibrate.getY());
			}
		}

		// update the falling effect
		if (spFall != null) {
			if (!isFalled) {
				this.rotate(spFall.getX() / 10);
			}
		}
	}
}
