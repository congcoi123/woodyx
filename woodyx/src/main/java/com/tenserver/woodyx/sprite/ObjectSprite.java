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
package com.tenserver.woodyx.sprite;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tenserver.woodyx.physics.BoxUtility;
import com.tenserver.woodyx.physics.ObjectModel;

/**
 * 
 * @author kong
 *
 */
public class ObjectSprite extends Sprite {
	private Vector2 bodyPosition = new Vector2();
	private Vector2 originSize = new Vector2();
	private TextureRegion textureRegion;

	public ObjectSprite(float x, float y) {
		this.setPosition(x, y);
	}

	public ObjectSprite(float x, float y, float width, float height, boolean isInBox) {
		// set the origin size
		originSize = new Vector2(width, height);
		float worldToBox = 1;
		if (isInBox) {
			worldToBox = BoxUtility.WORLD_TO_BOX;
		}
		this.setSize(width * worldToBox, height * worldToBox);
		this.setPosition(x * worldToBox, y * worldToBox);
	}

	public ObjectSprite(TextureRegion textureRegion, float x, float y, float width, float height, boolean isInBox) {
		// set the origin size
		originSize = new Vector2(width, height);
		float worldToBox = 1;
		if (isInBox) {
			worldToBox = BoxUtility.WORLD_TO_BOX;
		}
		this.textureRegion = textureRegion;
		this.setRegion(textureRegion);
		this.setSize(width * worldToBox, height * worldToBox);
		this.setPosition(x * worldToBox, y * worldToBox);
	}

	public ObjectSprite(TextureRegion textureRegion, float x, float y, boolean isInBox) {
		// set the origin size
		originSize = new Vector2(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
		float worldToBox = 1;
		if (isInBox) {
			worldToBox = BoxUtility.WORLD_TO_BOX;
		}
		this.textureRegion = textureRegion;
		this.setRegion(textureRegion);
		this.setSize(textureRegion.getRegionWidth() * worldToBox, textureRegion.getRegionHeight() * worldToBox);
		this.setPosition(x * worldToBox, y * worldToBox);
	}

	/**
	 * Retrieve the texture region
	 */
	public TextureRegion getRegion() {
		return this.textureRegion;
	}

	/**
	 * Render the Sprite
	 */
	public void renderKeyFrame(TextureRegion textureRegion, SpriteBatch batch) {
		this.setRegion(textureRegion);
		this.draw(batch);
	}

	/**
	 * Set the sprite for center position
	 */
	public void setSpriteCenter(Sprite sprite, float x, float y) {
		sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);
	}

	/**
	 * Set the origin center
	 */
	public void setOriginCenter(Sprite sprite) {
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
	}

	/**
	 * Retrieve the position of sprite
	 */
	public Vector2 getPosition() {
		Vector2 position = new Vector2(this.getX(), this.getY());
		return position;
	}

	/**
	 * Retrieve the origin size
	 */
	public Vector2 getOriginSize() {
		return originSize;
	}

	/**
	 * Update the follow model
	 */
	public void updateFollowModel(ObjectModel model, float rangeCenterX, float rangeCenterY, boolean isUpdateRotation) {
		if (model.isBasicShape()) {
			// polygon
			bodyPosition.x = model.getBody().getPosition().x
					- BoxUtility.ConvertToBox(model.boxPolygon.x / 2 - rangeCenterX);
			bodyPosition.y = model.getBody().getPosition().y
					- BoxUtility.ConvertToBox(model.boxPolygon.y / 2 - rangeCenterY);
			if (isUpdateRotation)
				this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
		} else {
			bodyPosition = model.getBody().getPosition().sub(model.getBodyOrigin());
			if (isUpdateRotation)
				this.setOrigin(model.getBodyOrigin().x, model.getBodyOrigin().y);
		}
		this.setPosition(bodyPosition.x, bodyPosition.y);
		if (isUpdateRotation)
			this.setRotation(model.getBody().getAngle() * MathUtils.radiansToDegrees);
	}
}
