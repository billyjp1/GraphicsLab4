package solution.manip;

import javax.vecmath.Point3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import jgl.GL;
import modeler.GLJPanel;
import solution.scene.Transformation;

public class ScaleManip extends Manip {

	Vector2f PICKED_MOUSE = new Vector2f(1000, 1000);
	
	
	public ScaleManip() {}
	
	public ScaleManip(Transformation t) {

		this.t = t;
	}

	public void dragged(Vector2f mousePosition, Vector2f mouseDelta) {
		//TODO: Implement this method
		Point3f p1 = new Point3f();
		Point3f p2 = new Point3f();
		Vector3f v1 = new Vector3f();
		Vector3f v2 = new Vector3f();
		Vector3f SCALE_VALUE = new Vector3f();// = new Vector3f(t.getScale());
		if (!PICKED_MOUSE.equals(pickedMousePoint)) {
			PICKED_MOUSE = new Vector2f(pickedMousePoint);
			SCALE_VALUE = new Vector3f(t.getScale());
		}
		
		if (axisMode == PICK_OTHER) {
			Vector3f middleVec = new Vector3f(2,2,2);
			middleVec.scale(mouseDelta.y);
			middleVec.add(t.getScale());
			t.setScale(middleVec);
			return;
		} 
		
		computeAxisRay(p1, v1);
		computeViewingRay(pickedMousePoint, p2, v2);
		float t1 = computePseudointersection(p2,v2,p1,v1);
		Vector3f tV1 = new Vector3f(v1);
		tV1.scale(t1);
		
		computeAxisRay(p1,v1);
		computeViewingRay(mousePosition, p2, v2);
		float t2 = computePseudointersection(p2,v2,p1,v1);
		Vector3f tV2 = new Vector3f(v1);
		tV2.scale(t2);
		
//System.out.println(tV2.x+" "+tV2.y+" "+tV2.z);
		float x = tV2.x / tV1.x;
		if (tV2.x == 0) x = 1;
		float y = tV2.y / tV1.y;
		if (tV2.y == 0) y = 1;
		float z = tV2.z / tV1.z;
		if (tV2.z == 0) z = 1;
		t.setScale(new Vector3f(x*SCALE_VALUE.x, y*SCALE_VALUE.y, z*SCALE_VALUE.z));
		/*t.S.setX(x);
		t.S.setX(y);
		t.S.setX(z);*/
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Render the manipulator
	////////////////////////////////////////////////////////////////////////////////////////////////////

	Vector3f skewX = new Vector3f();
	Vector3f skewY = new Vector3f();
	Vector3f skewZ = new Vector3f();
	public void glRender(GLJPanel canvas, double scale) {

		GL gl = canvas.getGL();
		//		GL2 gl = d.getGL().getGL2();
		gl.glPushAttrib(GL.GL_COLOR_BUFFER_BIT);

		gl.glPushAttrib(GL.GL_LIGHTING_BIT);
		gl.glDisable(GL.GL_LIGHTING);

		gl.glPushAttrib(GL.GL_DEPTH_BUFFER_BIT);
		gl.glDisable(GL.GL_DEPTH_TEST);

		gl.glPushMatrix();
		glDoLocation(gl, t);

		gl.glScaled(scale, scale, scale);
		glRenderRotation(gl, t);

		normalizedTransformedAxes(skewX, skewY, skewZ);

		gl.glPushMatrix();
		glRotateTo(gl,skewX);
		gl.glColor4d(0.8, 0, 0, 1);
		gl.glLoadName(PICK_X);
		glRenderBoxOnAStick(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		glRotateTo(gl,skewY);
		gl.glColor4d(0, 0.8, 0, 1);
		gl.glLoadName(PICK_Y);
		glRenderBoxOnAStick(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		glRotateTo(gl,skewZ);
		gl.glColor4d(0, 0, 0.8, 1);
		gl.glLoadName(PICK_Z);
		glRenderBoxOnAStick(gl);
		gl.glPopMatrix();    

		gl.glLoadName(PICK_OTHER);
		gl.glColor4d(0.8, 0.8, 0, 1);
		glRenderBox(gl);

		gl.glPopMatrix();

		gl.glPopAttrib();
		gl.glPopAttrib();
		gl.glPopAttrib();
	}

}