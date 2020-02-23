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

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

/**
 * 
 * @author kong
 *
 */

public class BoxUtility {
	// Convert from the world to box and otherwise
	public static final float WORLD_TO_BOX = 0.01f;
	public static final float BOX_TO_WORLD = 100f;

	// world.step
	public static final byte VELOCITY_ITER = 8;
	public static final byte POSITION_ITER = 3;

	// touch object
	private static Vector3 testPoint = new Vector3();
	private static Body hitBody = null;

	private static MouseJoint mouseJoint;

	private static Vector2 target = new Vector2();

	/**
	 * Convert from world to box
	 */
	public static float ConvertToBox(float argument) {
		return argument * WORLD_TO_BOX;
	}

	/**
	 * Convert from box to world
	 */
	public static float ConvertToWorld(float argument) {
		return argument * BOX_TO_WORLD;
	}

	/**
	 * Detect the collision
	 */
	public static boolean detectCollision(Contact contact, String objectA, String objectB) {
		if ((contact.getFixtureA().getBody().getUserData().equals(objectA)
				&& contact.getFixtureB().getBody().getUserData().equals(objectB))
				|| (contact.getFixtureA().getBody().getUserData().equals(objectB)
						&& contact.getFixtureB().getBody().getUserData().equals(objectA))) {
			return true;
		}
		return false;
	}

	/**
	 * Detect the collision
	 */
	public static boolean detectCollision(Contact contact, Body objectA, String objectB) {
		if ((contact.getFixtureA().getBody().getUserData().equals(objectA.getUserData())
				&& contact.getFixtureB().getBody().getUserData().equals(objectB))
				|| (contact.getFixtureA().getBody().getUserData().equals(objectB)
						&& contact.getFixtureB().getBody().getUserData().equals(objectA.getUserData()))) {
			return true;
		}
		return false;
	}

	/**
	 * Detect the collision
	 */
	public static boolean detectCollision(Contact contact, Body objectA, Body objectB) {
		if ((contact.getFixtureA().getBody().getUserData().equals(objectA.getUserData())
				&& contact.getFixtureB().getBody().getUserData().equals(objectB.getUserData()))
				|| (contact.getFixtureA().getBody().getUserData().equals(objectB.getUserData())
						&& contact.getFixtureB().getBody().getUserData().equals(objectA.getUserData()))) {
			return true;
		}
		return false;
	}

	/**
	 * We instantiate this vector and the callback here so we don't irritate the GC
	 * query callback, identify object when been touched
	 */
	private static QueryCallback callback = new QueryCallback() {
		@Override
		public boolean reportFixture(Fixture fixture) {
			// if the hit point is inside the fixture of the body we report it
			if (fixture.testPoint(ConvertToBox(testPoint.x), ConvertToBox(testPoint.y))) {
				hitBody = fixture.getBody();
				return false;
			}

			return true;
		}
	};

	/**
	 * The Touch Object
	 * 
	 * @param screenX:     screenX [pixel]
	 * @param screenY:     screenY [pixel]
	 * @param world:       world
	 * @param camera:      camera
	 * @param groundModel: ground model in the world
	 * @param notTouch:    objects not for touching
	 * @param maxForce:    The maximum constraint force that can be exerted to move
	 *                     the candidate body. Usually you will express as some
	 *                     multiple of the weight (multiplier * mass * gravity)
	 *                     (normal = 1000f)
	 */
	public static void touchObject(int screenX, int screenY, World world, OrthographicCamera camera,
			ObjectModel groundModel, String notTouch, float maxForce) {
		camera.unproject(testPoint.set(screenX, screenY, 0));

		hitBody = null;
		world.QueryAABB(callback, ConvertToBox(testPoint.x) - 0.0001f, ConvertToBox(testPoint.y) - 0.0001f,
				ConvertToBox(testPoint.x) + 0.0001f, ConvertToBox(testPoint.y) + 0.0001f);

		if (hitBody != null && hitBody.getUserData() != notTouch) {
			MouseJointDef def = new MouseJointDef();
			def.bodyA = groundModel.getBody();
			def.bodyB = hitBody;
			def.collideConnected = true;
			def.target.set(ConvertToBox(testPoint.x), ConvertToBox(testPoint.y));
			def.maxForce = maxForce * hitBody.getMass();

			mouseJoint = (MouseJoint) world.createJoint(def);
			hitBody.setAwake(true);
		}
	}

	/**
	 * The Touch Object
	 */
	public static boolean touchObject(int screenX, int screenY, World world, OrthographicCamera camera,
			ObjectModel groundModel, String touchedObject) {
		camera.unproject(testPoint.set(screenX, screenY, 0));

		hitBody = null;
		world.QueryAABB(callback, ConvertToBox(testPoint.x) - 0.0001f, ConvertToBox(testPoint.y) - 0.0001f,
				ConvertToBox(testPoint.x) + 0.0001f, ConvertToBox(testPoint.y) + 0.0001f);

		if (hitBody != null && hitBody.getUserData().equals(touchedObject)) {
			MouseJointDef def = new MouseJointDef();
			def.bodyA = groundModel.getBody();
			def.bodyB = hitBody;
			def.collideConnected = true;
			def.target.set(ConvertToBox(testPoint.x), ConvertToBox(testPoint.y));
			def.maxForce = 1000 * hitBody.getMass();

			mouseJoint = (MouseJoint) world.createJoint(def);
			hitBody.setAwake(true);
			return true;
		}
		return false;
	}

	/**
	 * The Touch Object
	 */
	public static boolean touchObject(int screenX, int screenY, World world, OrthographicCamera camera,
			ObjectModel groundModel, ObjectModel touchedObject) {
		camera.unproject(testPoint.set(screenX, screenY, 0));

		hitBody = null;
		world.QueryAABB(callback, ConvertToBox(testPoint.x) - 0.0001f, ConvertToBox(testPoint.y) - 0.0001f,
				ConvertToBox(testPoint.x) + 0.0001f, ConvertToBox(testPoint.y) + 0.0001f);

		if (hitBody != null && hitBody.getUserData().equals(touchedObject.getBody().getUserData())) {
			MouseJointDef def = new MouseJointDef();
			def.bodyA = groundModel.getBody();
			def.bodyB = hitBody;
			def.collideConnected = true;
			def.target.set(ConvertToBox(testPoint.x), ConvertToBox(testPoint.y));
			def.maxForce = 1000 * hitBody.getMass();

			mouseJoint = (MouseJoint) world.createJoint(def);
			hitBody.setAwake(true);
			return true;
		}
		return false;
	}

	/**
	 * The Touch Object
	 */
	public static boolean touchObject(int screenX, int screenY, World world, OrthographicCamera camera,
			ObjectModel groundModel, ObjectModel touchedObject, float maxForce) {
		camera.unproject(testPoint.set(screenX, screenY, 0));

		hitBody = null;
		world.QueryAABB(callback, ConvertToBox(testPoint.x) - 0.0001f, ConvertToBox(testPoint.y) - 0.0001f,
				ConvertToBox(testPoint.x) + 0.0001f, ConvertToBox(testPoint.y) + 0.0001f);

		if (hitBody != null && hitBody.equals(touchedObject.getBody())) {
			MouseJointDef def = new MouseJointDef();
			def.bodyA = groundModel.getBody();
			def.bodyB = hitBody;
			def.collideConnected = true;
			def.target.set(ConvertToBox(testPoint.x), ConvertToBox(testPoint.y));
			def.maxForce = maxForce * hitBody.getMass();

			mouseJoint = (MouseJoint) world.createJoint(def);
			hitBody.setAwake(true);
			return true;
		}
		return false;
	}

	/**
	 * The Touch Object
	 */
	public static String touchObject(int screenX, int screenY, World world, OrthographicCamera camera,
			ObjectModel groundModel, float maxForce) {
		camera.unproject(testPoint.set(screenX, screenY, 0));

		hitBody = null;
		world.QueryAABB(callback, ConvertToBox(testPoint.x) - 0.0001f, ConvertToBox(testPoint.y) - 0.0001f,
				ConvertToBox(testPoint.x) + 0.0001f, ConvertToBox(testPoint.y) + 0.0001f);

		if (hitBody != null) {
			MouseJointDef def = new MouseJointDef();
			def.bodyA = groundModel.getBody();
			def.bodyB = hitBody;
			def.collideConnected = true;
			def.target.set(ConvertToBox(testPoint.x), ConvertToBox(testPoint.y));
			def.maxForce = maxForce * hitBody.getMass();

			mouseJoint = (MouseJoint) world.createJoint(def);
			hitBody.setAwake(true);
			return (String) hitBody.getUserData();
		}
		return null;
	}

	/**
	 * Get the test point from camera.unproject()
	 */
	public static Vector3 getTestPoint() {
		return testPoint;
	}

	/**
	 * Release the object
	 */
	public static void releaseObject(World world) {
		if (mouseJoint != null) {
			world.destroyJoint(mouseJoint);
			mouseJoint = null;
		}
	}

	/**
	 * Drag the object
	 */
	public static void dragObject(OrthographicCamera camera, int screenX, int screenY) {
		if (mouseJoint != null) {
			camera.unproject(testPoint.set(screenX, screenY, 0));
			mouseJoint.setTarget(target.set(ConvertToBox(testPoint.x), ConvertToBox(testPoint.y)));
		}
	}
}
