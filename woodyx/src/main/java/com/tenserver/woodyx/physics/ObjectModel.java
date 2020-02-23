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
package com.tenserver.woodyx.physics;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 
 * @author kong
 * 
 */

public class ObjectModel {
	public static final byte CIRCLE = 0;
	public static final byte POLYGON = 1;

	public static final byte STATIC = 2;
	public static final byte DYNAMIC = 3;
	public static final byte KINEMATIC = 4;

	public Vector2 boxPolygon;
	public float circleRadius;

	protected Body body;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;

	private CircleShape circleShape;
	private PolygonShape polygonShape;

	private Vector2 bodyOrigin;
	private boolean basicShape;

	/**
	 * Model constructor for special shape
	 * 
	 * @param world:       world
	 * @param type:        objectType (Static, Dynamic, KinemaTic) ObjectModel
	 * @param loader:      bodyEditorLoader (file description .json's file)
	 * @param name:        name of model in .json's file
	 * @param position:    initialize position in box
	 * @param angle:       initialize angle in box, unit: degree
	 * @param density:     density of model, usually in kilogram/m2
	 * @param friction:    friction of model, usually in range [0,1]
	 * @param restitution: restitution's model usually in range [0,1]
	 * @param width:       width of mode in box
	 * @param category:    category for filter collision
	 * @param mask:        mask for filter collision
	 * @param user:        userData (for check collision)
	 */
	public ObjectModel(World world, byte type, BodyEditorLoader loader, String name, Vector2 position, float angle,
			float density, float friction, float restitution, float width, int category, int mask, String user) {
		// flag
		basicShape = false;

		// create a bodyDef
		bodyDef = new BodyDef();
		bodyDef.type = getBodyType(type);

		// create a fixtureDef
		fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		if (category != 0 && mask != 0) {
			fixtureDef.filter.categoryBits = (short) category;
			fixtureDef.filter.maskBits = (short) mask;
		}

		// create the body, set position in box
		body = world.createBody(bodyDef);
		body.setUserData(user);
		body.setTransform(BoxUtility.ConvertToBox(position.x), BoxUtility.ConvertToBox(position.y),
				(angle * MathUtils.degreesToRadians));

		// create the body fixture automatically by using the loader
		loader.attachFixture(body, name, fixtureDef, BoxUtility.ConvertToBox(width));

		// set the origin of body
		bodyOrigin = loader.getOrigin(name, BoxUtility.ConvertToBox(width)).cpy();
	}

	/**
	 * Model constructor for basic shape
	 * 
	 * @param world:        world
	 * @param type:         objectType (Static, Dynamic, KinemaTic) ObjectModel
	 * @param shape:        basic shape of model (chain, edge, polygon, circle)
	 * @param boxPolygon:   set as box for polygon shape
	 * @param circleRadius: set radius for circle shape
	 * @param position:     initialize position in box
	 * @param angle:        initialize angle in box, unit: degree
	 * @param density:      density of model, usually in kilogram/m2
	 * @param friction:     friction of model, usually in range [0,1]
	 * @param restitution:  restitution's model usually in range [0,1]
	 * @param category:     category for filter collision
	 * @param mask:         mask for filter collision
	 * @param user:         userData (for check collision)
	 */
	public ObjectModel(World world, byte type, byte shape, Vector2 boxPolygon, float circleRadius, Vector2 position,
			float angle, float density, float friction, float restitution, int category, int mask, String user) {
		// parameters
		this.boxPolygon = boxPolygon;
		this.circleRadius = circleRadius;

		// flag
		basicShape = true;

		// create a bodyDef
		bodyDef = new BodyDef();
		bodyDef.type = getBodyType(type);

		// create a fixtureDef
		fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		if (category != 0 && mask != 0) {
			fixtureDef.filter.categoryBits = (short) category;
			fixtureDef.filter.maskBits = (short) mask;
		}

		// create bodyShape
		createShape(shape, boxPolygon, circleRadius);

		// create model, set position in box
		body = world.createBody(bodyDef);
		body.setUserData(user);
		body.createFixture(fixtureDef);
		body.setTransform(BoxUtility.ConvertToBox(position.x + boxPolygon.x / 2),
				BoxUtility.ConvertToBox(position.y + boxPolygon.y / 2), (angle * MathUtils.degreesToRadians));

		// dispose shape
		disposeShape(shape);

	}

	/**
	 * Get the body type
	 */
	private BodyType getBodyType(byte type) {
		switch (type) {
		case STATIC:
			return BodyType.StaticBody;
		case DYNAMIC:
			return BodyType.DynamicBody;
		case KINEMATIC:
			return BodyType.KinematicBody;
		}
		return null;
	}

	/**
	 * Check if the shape is the basic shape
	 */
	public boolean isBasicShape() {
		return basicShape;
	}

	/**
	 * Get the body of the model
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * Get the model origin
	 */
	public Vector2 getBodyOrigin() {
		return bodyOrigin;
	}

	/**
	 * Get the circle shape in box
	 */
	public CircleShape getCircleShape() {
		return circleShape;
	}

	/**
	 * Get the polygon shape in box
	 */
	public PolygonShape getPolygonShape() {
		return polygonShape;
	}

	/**
	 * Make the balloon style for model usually before world.step()
	 * 
	 * @param gravity: gravity of the world
	 * @param against: velocity (x, y) when the gravity is disposed
	 */
	public void makeBalloonStyle(Vector2 gravity, Vector2 against) {
		body.applyForceToCenter(
				new Vector2((-gravity.x + against.x) * body.getMass(), (-gravity.y + against.y) * body.getMass()),
				true);
	}

	/**
	 * Choose the basic shape
	 */
	private void createShape(byte shape, Vector2 boxPolygon, float circleRadius) {
		switch (shape) {
		case CIRCLE:
			circleShape = new CircleShape();
			circleShape.setRadius(BoxUtility.ConvertToBox(circleRadius));
			fixtureDef.shape = circleShape;
			break;
		case POLYGON:
			polygonShape = new PolygonShape();
			polygonShape.setAsBox(BoxUtility.ConvertToBox(boxPolygon.x / 2), BoxUtility.ConvertToBox(boxPolygon.y / 2));
			fixtureDef.shape = polygonShape;
			break;
		default:
			break;
		}
	}

	/**
	 * Dispose shape
	 */
	private void disposeShape(byte shape) {
		switch (shape) {
		case CIRCLE:
			circleShape.dispose();
			break;
		case POLYGON:
			polygonShape.dispose();
			break;
		default:
			break;
		}
	}

	/**
	 * Destroy the body
	 */
	public void dispose(World world) {
		if (body != null) {
			// destroy body
			world.destroyBody(body);
			body = null;
		}
	}
}
