package solution.shape;

/**
 * @author ags
 */
public class Sphere extends Shape {

	private static final long serialVersionUID = 3256437006337718072L;

	static double curTol = -1;
	private int numRings;
	private transient static Mesh sphereMesh;
	/**
	 * Required for IO
	 */
	public Sphere() {}

	/**
	 * @see solution.shape.Shape#buildMesh()
	 */
	public void buildMesh() {
		//TODO: Build mesh
		if(TOLERANCE == curTol) {
			mesh = sphereMesh;
			return;
		}
		curTol = TOLERANCE;
		double flatness = Math.toDegrees(Math.acos(1-curTol)) * 2;
		flatness = 360 / flatness;
		int numVerts = (int)Math.ceil(flatness);
		if (numVerts%2==1) numVerts++;
		int numTriData = (int) Math.pow(numVerts, 2);
		
		float[] vertData = new float[numVerts*600];
		float[] normData = new float[numVerts*600]; 
		int[] triData = new int[numTriData*3];
		
		numRings = (numVerts/2);
//System.out.println(numVerts + "    " + numRings);

		//Top of sphere
		int i = 0;
		int j = 0;
		vertData[j] = 0;
		vertData[j+1] = 1;
		vertData[j+2] = 0;
		normData[j] = 0;
		normData[j+1] = 1;
		normData[j+2] = 0;
		j+=3;
		
		//angle to the second ring from the top
		double ringAngle = getRingAngle(1);
//System.out.println(ringAngle + "   degree " + Math.toDegrees(ringAngle));
		double theta = (Math.PI*2)/numVerts; 
		for (i=0;i < numVerts; i++) {
			vertData[j] = (float)(Math.cos(ringAngle) * Math.cos(theta*i));
			vertData[j+1] = (float)Math.sin(ringAngle);
			vertData[j+2] = (float)(Math.cos(ringAngle) * Math.sin(theta*i));
//System.out.println("X: "+vertData[j]+"   Y: "+vertData[j+1]+"   Z: "+vertData[j+2]);
			normData[j] = (float)(Math.cos(ringAngle) * Math.cos(theta*i));
			normData[j+1] = (float)Math.sin(ringAngle);
			normData[j+2] = (float)(Math.cos(ringAngle) * Math.sin(theta*i));
			j+=3;
		}
		
		//Body of sphere
		int k;
		for (k = 2; k < numRings-1;k++) {
			ringAngle = getRingAngle(k);
			for (i=0; i < numVerts; i++) {
				vertData[j] = (float)(Math.cos(ringAngle) * Math.cos(theta*i));
				vertData[j+1] = (float)Math.sin(ringAngle);
				vertData[j+2] = (float)(Math.cos(ringAngle) * Math.sin(theta*i));
//System.out.println("X: "+vertData[j]+"   Y: "+vertData[j+1]+"   Z: "+vertData[j+2]);
				normData[j] = (float)(Math.cos(ringAngle) * Math.cos(theta*i));
				normData[j+1] = (float)Math.sin(ringAngle);
				normData[j+2] = (float)(Math.cos(ringAngle) * Math.sin(theta*i));
				j+=3;
			}
		}
		
		//Bottom of sphere
		int bottomLocationOffset = j/3;
		ringAngle = getRingAngle(k);
		for (i=0;i < numVerts; i++) {
			vertData[j] = (float)(Math.cos(ringAngle) * Math.cos(theta*i));
			vertData[j+1] = (float)Math.sin(ringAngle);
			vertData[j+2] = (float)(Math.cos(ringAngle) * Math.sin(theta*i));
//System.out.println("X: "+vertData[j]+"   Y: "+vertData[j+1]+"   Z: "+vertData[j+2]);
			normData[j] = (float)(Math.cos(ringAngle) * Math.cos(theta*i));
			normData[j+1] = (float)Math.sin(ringAngle);
			normData[j+2] = (float)(Math.cos(ringAngle) * Math.sin(theta*i));
			j+=3;
		}

		int botCenter = j/3;
		vertData[j] = 0;
		vertData[j+1] = -1;
		vertData[j+2] = 0;
		normData[j] = 0;
		normData[j+1] = -1;
		normData[j+2] = 0;
		
		
		//Top of sphere
		j = 0;
		for(i = 0;i < numVerts; i++) {
			triData[j] = 0;
			triData[j+1] = ((i+1)%numVerts)+1;
			triData[j+2] = (i%numVerts)+1;
//System.out.println(triData[j]+" "+ triData[j+1]+" "+triData[j+2]);
			j+=3;
		}
		
		//Body of sphere
		int curRing = 1;
		for (k = 1; k < numRings-1;k++) {
			for (int g = 0; g < numVerts; g++) {
				triData[j] = curRing + g;
				triData[j+1] = curRing + (g+1)%numVerts;
				triData[j+2] = curRing + numVerts + g%numVerts;
//System.out.println("p1: "+triData[j]+"   p2: "+triData[j+1] + "   p3: "+triData[j+2]);
				triData[j+3] = curRing + (g+1)%numVerts;
				triData[j+4] = curRing + numVerts + (g+1)%numVerts;
				triData[j+5] = curRing + numVerts + (g)%numVerts;
//System.out.println("p1: "+triData[j+3]+"   p2: "+triData[j+4] + "   p3: "+triData[j+5]);
				j+=6;
			}
			curRing+=numVerts;
		}
		
		//Bottom of sphere
		for (i=0;i < numVerts; i++) {
			triData[j] = botCenter;
			triData[j+1] = ((i+1)%numVerts)+bottomLocationOffset;
			triData[j+2] = ((i+2)%numVerts)+bottomLocationOffset;
//System.out.println(triData[j]+" "+ triData[j+1]+" "+triData[j+2]);
//System.out.println(vertData[triData[j]*3]+" "+ vertData[triData[j+1]*3+1]+" "+vertData[triData[j+2]*3+2]);
			j+=3;
		}
		
		
		sphereMesh = new Mesh(vertData, triData, normData);
		mesh = sphereMesh;
	}
	
	private double getRingAngle(int ringNum) {
		return Math.PI/2 - ((Math.PI / (numRings)) * ringNum);
	}
}
