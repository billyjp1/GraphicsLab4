package solution.shape;

import java.lang.Math;

/**
 * @author ags
 */
public class Cylinder extends Shape {

	private static final long serialVersionUID = 3256437006337718072L;

	static double curTol = -1;
	private transient static Mesh cylinderMesh;
	/**
	 * Required for IO
	 */
	public Cylinder() {}

	/**
	 * @see solution.shape.Shape#buildMesh()
	 */
	public void buildMesh() {
		// TODO: Build mesh 
		if(TOLERANCE == curTol) {
			mesh = cylinderMesh;
			return;
		}
		curTol = TOLERANCE;
		double flatness = Math.toDegrees(Math.acos(1-curTol)) * 2;
		flatness = 360 / flatness;
		int numVerts = (int)Math.ceil(flatness);
		if (numVerts%2==1) numVerts++;
		numVerts *= 2;
		int numTriData = ((numVerts-2) + numVerts)*2;
		
		float[] vertData = new float[numVerts*15];
		float[] normData = new float[numVerts*15];
		
		int[] triData = new int[numTriData*3];
//System.out.println("Verts = "+numVerts+"   Triangles = "+numTriData);
		
		int k = 0;
		int j = 6;
		double theta = (Math.PI*2) / (numVerts/2);
		for (int i = 0; i < numVerts; i++) {
			//Top circle
			vertData[k] = (float)Math.cos(i*theta);
			vertData[k+1] = 1;
			vertData[k+2] = (float)Math.sin(i*theta);
			normData[k] = 0;
			normData[k+1] = 1;
			normData[k+2] = 0;
			
			vertData[k+3] = (float)Math.cos(i*theta);
			vertData[k+4] = 1;
			vertData[k+5] = (float)Math.sin(i*theta);
			normData[k+3] = (float)Math.cos(i*theta); 
			normData[k+4] = 0;
			normData[k+5] = (float)Math.sin(i*theta);
			k+=12;
			
			//bottom circle
			vertData[j] = (float)Math.cos(i*theta);
			vertData[j+1] = -1;
			vertData[j+2] = (float)Math.sin(i*theta);
			normData[j] = 0;
			normData[j+1] = -1;
			normData[j+2] = 0;
			
			vertData[j+3] = (float)Math.cos(i*theta);
			vertData[j+4] = -1;
			vertData[j+5] = (float)Math.sin(i*theta);
			normData[j+3] = (float)Math.cos(i*theta);
			normData[j+4] = 0;
			normData[j+5] = (float)Math.sin(i*theta);
			j+=12;
		}
	
		//top triangles
		k = 0;
		int i = 0;
		for (; i < numVerts-2; i++) {
			triData[i*3] = 0;
			triData[i*3+1] = (k+8);
			triData[i*3+2] = (k+4);
			k+=4;
		}
		
		//bottom triangles
		j = 2;
		for (; i < (numVerts-2)*2; i++) {
			triData[i*3] = 2;
			triData[i*3+1] = (j+4);
			triData[i*3+2] = (j+8);
			j+=4;
		}
		
		//side triangles
		k = 1;
		j = 3;
		for (; i < numTriData; i+=2) {
			triData[i*3] = k;
			triData[i*3+1] = j+4;
			triData[i*3+2] = j;
//System.out.println("TOP:"+vertData[triData[i*3]*3+1]);   //Top
//System.out.println("BOT:"+vertData[triData[i*3+1]*3+1]); //Bottom
//System.out.println("BOT:"+vertData[triData[i*3+2]*3+1]); //Bottom
			triData[i*3+3] = k;
			triData[i*3+4] = k+4;
			triData[i*3+5] = j+4;
//System.out.println("TOP:"+vertData[triData[i*3+3]*3+1]); //Top
//System.out.println("BOT:"+vertData[triData[i*3+4]*3+1]); //Bottom
//System.out.println("TOP:"+vertData[triData[i*3+5]*3+1]); //Top
			k+=4;
			j+=4;
		}
		i-=2;
		triData[i*3] = k; 
		triData[i*3+1] = 3;
		triData[i*3+2] = j;
		
		triData[i*3+3] = k;
		triData[i*3+4] = 1;
		triData[i*3+5] = 3;
		
		cylinderMesh = new Mesh(vertData, triData, normData);
		mesh = cylinderMesh;
	}
}
