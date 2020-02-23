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
package com.tenserver.woodyx.accessor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * 
 * @author kong
 *
 */
public class SpriteAccessor implements TweenAccessor<Sprite> {

	public static final byte SKEW_X2X3 = 0;
	public static final byte POS_XY = 1;
	public static final byte CPOS_XY = 2;
	public static final byte SCALE_XY = 3;
	public static final byte ROTATION = 4;
	public static final byte OPACITY = 5;
	public static final byte TINT = 6;

	@Override
	public int getValues(Sprite target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case SKEW_X2X3:
			float[] vs = target.getVertices();
			returnValues[0] = vs[SpriteBatch.X2] - target.getX();
			returnValues[1] = vs[SpriteBatch.X3] - target.getX() - target.getWidth();
			return 2;

		case POS_XY:
			returnValues[0] = target.getX();
			returnValues[1] = target.getY();
			return 2;

		case CPOS_XY:
			returnValues[0] = target.getX() + target.getWidth() / 2;
			returnValues[1] = target.getY() + target.getHeight() / 2;
			return 2;

		case SCALE_XY:
			returnValues[0] = target.getScaleX();
			returnValues[1] = target.getScaleY();
			return 2;

		case ROTATION:
			returnValues[0] = target.getRotation();
			return 1;

		case OPACITY:
			returnValues[0] = target.getColor().a;
			return 1;

		case TINT:
			returnValues[0] = target.getColor().r;
			returnValues[1] = target.getColor().g;
			returnValues[2] = target.getColor().b;
			return 3;

		default:
			assert false;
			return -1;
		}

	}

	@Override
	public void setValues(Sprite target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case SKEW_X2X3:
			float x2 = target.getX();
			float x3 = x2 + target.getWidth();
			float[] vs = target.getVertices();
			vs[SpriteBatch.X2] = x2 + newValues[0];
			vs[SpriteBatch.X3] = x3 + newValues[1];
			break;

		case POS_XY:
			target.setPosition(newValues[0], newValues[1]);
			break;

		case CPOS_XY:
			target.setPosition(newValues[0] - target.getWidth() / 2, newValues[1] - target.getHeight() / 2);
			break;

		case SCALE_XY:
			target.setScale(newValues[0], newValues[1]);
			break;

		case ROTATION:
			target.setRotation(newValues[0]);
			break;

		case OPACITY:
			Color c = target.getColor();
			c.set(c.r, c.g, c.b, newValues[0]);
			target.setColor(c);
			break;

		case TINT:
			c = target.getColor();
			c.set(newValues[0], newValues[1], newValues[2], c.a);
			target.setColor(c);
			break;

		default:
			assert false;
		}
	}

}
