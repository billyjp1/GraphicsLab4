package solution.manip;

import javax.vecmath.*;

import jgl.GL;
import modeler.GLJPanel;
import solution.scene.Transformation;

public class TranslateManip extends Manip {

	public TranslateManip() {}
	
	public TranslateManip(Transformation t) {

		this.t = t;
	}

	public void dragged(Vector2f mousePosition, Vector2f mouseDelta) {
		//TODO: Implement this method
		Point3f p1 = new Point3f();
		Point3f p2 = new Point3f();
		Vector3f v1 = new Vector3f();
		Vector3f v2 = new Vector3f();
		
		if (axisMode == PICK_OTHER) {
			computeViewingRay(mousePosition, p2, v2);
			Vector3f tV1 = new Vector3f(p2);
			tV1.add(v2);
			
			Vector2f oldPos = new Vector2f(mousePosition);
			oldPos.sub(mouseDelta);
			computeViewingRay(oldPos, p2, v2);
			Vector3f tV2 = new Vector3f(p2);
			tV2.add(v2);
			
			tV1.sub(tV2);
			Vector3f newTrans = new Vector3f(t.getTranslate());
			newTrans.add(tV1);
	//System.out.println(newTrans.x+" "+newTrans.y+" "+newTrans.z);
			t.setTranslate(newTrans);
			
			
			return;
		}
		
		computeAxisRay(p1, v1);
		computeViewingRay(mousePosition, p2, v2);
		float t1 = computePseudointersection(p2,v2,p1,v1);
		Vector3f tV1 = new Vector3f(v1);
		tV1.scale(t1);
		
		computeAxisRay(p1, v1);
		Vector2f oldPos = new Vector2f(mousePosition);
		oldPos.sub(mouseDelta);
		computeViewingRay(oldPos, p2, v2);
		float t2 = computePseudointersection(p2,v2,p1,v1);
		Vector3f tV2 = new Vector3f(v1);
		tV2.scale(t2);
		
		tV1.sub(tV2);
		Vector3f newTrans = new Vector3f(t.getTranslate());
		newTrans.add(tV1);
//System.out.println(newTrans.x+" "+newTrans.y+" "+newTrans.z);
		t.setTranslate(newTrans);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Render the manipulator
	////////////////////////////////////////////////////////////////////////////////////////////////////

	Vector3f skewX = new Vector3f();
	Vector3f skewY = new Vector3f();
	Vector3f skewZ = new Vector3f();
	public void glRender(GLJPanel canvas, double scale) {

		GL gl = canvas.getGL();
		gl.glPushAttrib(GL.GL_COLOR_BUFFER_BIT);

		gl.glPushAttrib(GL.GL_LIGHTING_BIT);
		gl.glDisable(GL.GL_LIGHTING);

		gl.glPushAttrib(GL.GL_DEPTH_BUFFER_BIT);
		gl.glDisable(GL.GL_DEPTH_TEST);

		gl.glPushMatrix();
		glDoLocation(gl, t);

		gl.glScaled(scale, scale, scale);

		normalizedTransformedAxes(skewX, skewY, skewZ);

		gl.glPushMatrix();
		glRotateTo(gl,skewX);
		gl.glColor4d(0.8, 0, 0, 1);
		gl.glLoadName(PICK_X);
		glRenderArrow(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		glRotateTo(gl,skewY);
		gl.glColor4d(0, 0.8, 0, 1);
		gl.glLoadName(PICK_Y);
		glRenderArrow(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		glRotateTo(gl,skewZ);
		gl.glColor4d(0, 0, 0.8, 1);
		gl.glLoadName(PICK_Z);
		glRenderArrow(gl);
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

