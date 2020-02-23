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

/**
 * @author HeoRungDiNang
 */
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.GearJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;

public class ObjectsJoint {
	public static final byte DISTANCE = 0;
	public static final byte MOUSE = 1;
	public static final byte GEAR = 2;
	public static final byte PRISMATIC = 4;
	public static final byte PULLEY = 5;
	public static final byte REVOLUTE = 6;
	public static final byte ROPE = 7;
	public static final byte WELD = 8;
	public static final byte WHEEL = 9;

	private MouseJointDef mouseJointDef;
	private DistanceJointDef distanceJointDef;
	private GearJointDef gearJointDef;
	private PrismaticJointDef prismaticJointDef;
	private PulleyJointDef pullyJointDef;
	private RevoluteJointDef revoluteJointDef;
	private RopeJointDef ropeJointDef;
	private WeldJointDef weldJointDef;
	private WheelJointDef wheelJointDef;

	private byte jointType;

	private Joint joint;

	/**
	 * Quick joint function: make quick joint weld
	 * 
	 * @param objA: objects
	 * @param objB: wall, static body
	 */
	public static void quickWeld(ObjectModel objA, ObjectModel objB) {
		WeldJointDef weld = new WeldJointDef();
		weld.initialize(objA.getBody(), objB.getBody(), objB.getBody().getWorldCenter());
		if (weld.bodyA != weld.bodyB)
			objA.getBody().getWorld().createJoint(weld);
	}

	/**
	 * BodiesJoint class
	 * 
	 * @param bodyA:
	 * @param bodyB:
	 * @param jointType:       distance, gear, prismaticShape, pulley, revolution,
	 *                         rope, weld, wheel
	 * @param collideConnected
	 */
	public ObjectsJoint(World world, ObjectModel objectA, ObjectModel objectB, byte jointType, Vector2 anchorA,
			Vector2 anchorB, boolean collideConnected) {
		this.jointType = jointType;
		// create joint
		switch (jointType) {
		case MOUSE:
			// create the mouse joint
			mouseJointDef = new MouseJointDef();
			mouseJointDef.bodyA = objectA.getBody();
			mouseJointDef.bodyB = objectB.getBody();
			mouseJointDef.collideConnected = collideConnected;
			// create joint
			joint = (MouseJoint) world.createJoint(mouseJointDef);
			break;

		case DISTANCE:
			// create the distance joint
			distanceJointDef = new DistanceJointDef();
			distanceJointDef.bodyA = objectA.getBody();
			distanceJointDef.bodyB = objectB.getBody();
			distanceJointDef.collideConnected = collideConnected;
			// create anchors
			distanceJointDef.localAnchorA.set(BoxUtility.ConvertToBox(anchorA.x), BoxUtility.ConvertToBox(anchorA.y));
			distanceJointDef.localAnchorB.set(BoxUtility.ConvertToBox(anchorB.x), BoxUtility.ConvertToBox(anchorB.y));
			break;

		case GEAR:
			// create the gear joint
			gearJointDef = new GearJointDef();
			break;

		case PRISMATIC:
			// create the prismatic joint
			prismaticJointDef = new PrismaticJointDef();
			prismaticJointDef.bodyA = objectA.getBody();
			prismaticJointDef.bodyB = objectB.getBody();
			prismaticJointDef.collideConnected = collideConnected;
			// create anchors
			prismaticJointDef.localAnchorA.set(BoxUtility.ConvertToBox(anchorA.x), BoxUtility.ConvertToBox(anchorA.y));
			prismaticJointDef.localAnchorB.set(BoxUtility.ConvertToBox(anchorB.x), BoxUtility.ConvertToBox(anchorB.y));
			break;

		case PULLEY:
			// create the pulley joint
			pullyJointDef = new PulleyJointDef();
			break;

		case REVOLUTE:
			// create the revolution joint
			revoluteJointDef = new RevoluteJointDef();
			revoluteJointDef.bodyA = objectA.getBody();
			revoluteJointDef.bodyB = objectB.getBody();
			revoluteJointDef.collideConnected = collideConnected;
			// create anchors
			revoluteJointDef.localAnchorA.set(BoxUtility.ConvertToBox(anchorA.x), BoxUtility.ConvertToBox(anchorA.y));
			revoluteJointDef.localAnchorB.set(BoxUtility.ConvertToBox(anchorB.x), BoxUtility.ConvertToBox(anchorB.y));
			break;

		case ROPE:
			// create rope joint
			ropeJointDef = new RopeJointDef();
			ropeJointDef.bodyA = objectA.getBody();
			ropeJointDef.bodyB = objectB.getBody();
			ropeJointDef.collideConnected = collideConnected;
			// create anchors
			ropeJointDef.localAnchorA.set(BoxUtility.ConvertToBox(anchorA.x), BoxUtility.ConvertToBox(anchorA.y));
			ropeJointDef.localAnchorB.set(BoxUtility.ConvertToBox(anchorB.x), BoxUtility.ConvertToBox(anchorB.y));
			break;

		case WELD:
			// create weld joint
			weldJointDef = new WeldJointDef();
			weldJointDef.bodyA = objectA.getBody();
			weldJointDef.bodyB = objectB.getBody();
			weldJointDef.collideConnected = collideConnected;
			// create anchors
			weldJointDef.localAnchorA.set(BoxUtility.ConvertToBox(anchorA.x), BoxUtility.ConvertToBox(anchorA.y));
			weldJointDef.localAnchorB.set(BoxUtility.ConvertToBox(anchorB.x), BoxUtility.ConvertToBox(anchorB.y));
			break;

		case WHEEL:
			// create wheel joint
			wheelJointDef = new WheelJointDef();
			break;
		}
		// create joint
		createJoint(world);
	}

	/**
	 * Get mouseJointDef target: the initial world target point dampingRatio: The
	 * damping ratio frequencyHz: the response speed maxForce: the maximum
	 * constraint force that can be exerted to move the candidate body
	 * 
	 * @return mouseJointDef
	 */
	public MouseJointDef getMouseJointDef() {
		return mouseJointDef;
	}

	/**
	 * Get distancejointDef: dampingRatio (the damping ration), frequencyHz (the
	 * mass-spring-damper frequency in Hz), length (the natural length between the
	 * anchor points)
	 * 
	 * @return distanceJointDef
	 */
	public DistanceJointDef getDistanceJointDef() {
		return distanceJointDef;
	}

	/**
	 * Get gearJointDef
	 * 
	 * @return gearJointDef
	 */
	public GearJointDef getGearJointDef() {
		return gearJointDef;
	}

	/**
	 * Make the gear joint
	 * 
	 * @param joint1: The first revoluteJoint / prismaticJoint attached to the gear
	 *                joint
	 * @param joint2: The second revoluteJoint / prismaticJoint attached to the gear
	 *                joint
	 * @param ratio:  The gear ratio
	 */
	public void makeGearJoint(Joint joint1, Joint joint2, float ratio) {
		gearJointDef.joint1 = joint1;
		gearJointDef.joint2 = joint2;
		gearJointDef.ratio = ratio;
	}

	/**
	 * Get prismaticJointDef: information in libgdxWeb
	 * 
	 * @return prismaticJointDef
	 */
	public PrismaticJointDef getPrismaticJointDef() {
		return prismaticJointDef;
	}

	/**
	 * Set translation limit: prismaticJointDef
	 * 
	 * @param min: in pixel
	 * @param max: in pixel
	 */
	public void setPrismaticTranslationLimit(float min, float max, float angle) {
		prismaticJointDef.enableLimit = true;
		prismaticJointDef.lowerTranslation = BoxUtility.ConvertToBox(min);
		prismaticJointDef.upperTranslation = BoxUtility.ConvertToBox(max);
		prismaticJointDef.referenceAngle = angle * MathUtils.degreesToRadians;
	}

	/**
	 * Get pulleyJointDef
	 * 
	 * @return pulleyJointDef
	 */
	public PulleyJointDef getPulleyJointDef() {
		return pullyJointDef;
	}

	/**
	 * Get revoluteJointDef
	 * 
	 * @return revoluteJointDef
	 */
	public RevoluteJointDef getRevoluteJointDef() {
		return revoluteJointDef;
	}

	/**
	 * Set the angle limit: revoluteJointDef
	 * 
	 * @param min
	 * @param max
	 */
	public void setRevoluteAngleLimit(float min, float max) {
		revoluteJointDef.enableLimit = true;
		revoluteJointDef.lowerAngle = min * MathUtils.degreesToRadians;
		revoluteJointDef.upperAngle = max * MathUtils.degreesToRadians;
	}

	/**
	 * Get ropeJointDef
	 * 
	 * @return ropeJointDef
	 */
	public RopeJointDef getRopeJointDef() {
		return ropeJointDef;
	}

	/**
	 * Set the maximum length for rope joint and distance joint
	 * 
	 * @param length
	 */
	public void setDistanceRopeMaxLength(float length) {
		switch (jointType) {
		case ROPE:
			ropeJointDef.maxLength = BoxUtility.ConvertToBox(length);
			break;
		case DISTANCE:
			distanceJointDef.length = BoxUtility.ConvertToBox(length);
			break;
		}
	}

	/**
	 * Get weldJointDef
	 * 
	 * @return weldJointDef
	 */
	public WeldJointDef getWeldJointDef() {
		return weldJointDef;
	}

	/**
	 * Get wheelJointDef
	 * 
	 * @return wheelJointDef
	 */
	public WheelJointDef getWheelJointDef() {
		return wheelJointDef;
	}

	/**
	 * Use motor: revolueJointDef, prismaticJointDef, wheelJointDef
	 * 
	 * @param torque:      torque of motor, for friction usually minimum
	 * @param speed:       speed of motor, for friction usually 0
	 * @param enableMotor: enable motor
	 */
	public void setMotor(float torque, float speed, boolean enableMotor) {
		switch (jointType) {
		case REVOLUTE:
			revoluteJointDef.enableMotor = enableMotor;
			revoluteJointDef.maxMotorTorque = torque;
			revoluteJointDef.motorSpeed = speed * MathUtils.degreesToRadians;
			break;

		case PRISMATIC:
			prismaticJointDef.enableMotor = enableMotor;
			prismaticJointDef.maxMotorForce = torque;
			prismaticJointDef.motorSpeed = speed * MathUtils.degreesToRadians;
			break;

		case WHEEL:
			wheelJointDef.enableMotor = enableMotor;
			wheelJointDef.maxMotorTorque = torque;
			wheelJointDef.motorSpeed = speed * MathUtils.degreesToRadians;
			break;
		}
	}

	/**
	 * Create joints for 2 body
	 * 
	 * @param world
	 */
	public void createJoint(World world) {
		switch (jointType) {
		case MOUSE:
			joint = (MouseJoint) world.createJoint(mouseJointDef);
			break;

		case DISTANCE:
			joint = world.createJoint(distanceJointDef);
			break;

		case GEAR:
			joint = world.createJoint(gearJointDef);
			break;

		case PRISMATIC:
			joint = world.createJoint(prismaticJointDef);
			break;

		case PULLEY:
			joint = world.createJoint(pullyJointDef);
			break;

		case REVOLUTE:
			joint = world.createJoint(revoluteJointDef);
			break;

		case ROPE:
			joint = world.createJoint(ropeJointDef);
			break;

		case WELD:
			joint = world.createJoint(weldJointDef);
			break;

		case WHEEL:
			joint = world.createJoint(wheelJointDef);
			break;
		}
	}

	/**
	 * Retrieve the current joint
	 */
	public Joint getJoint() {
		return joint;
	}

	/**
	 * Destroy the current joint
	 */
	public void dispose(World world) {
		if (joint != null) {
			world.destroyJoint(joint);
			joint = null;
		}
	}
}
