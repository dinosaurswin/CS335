
public class HW_Matrix_Manipulation {

	public static void main(String[] args) {
		int [][] a= {{1,2,3,4},
					{5,6,7,8}}, 
				b= {{2,0,3},
					{1,2,3},
					{1,2,3},
					{1,2,3}};
		int [][] r= multiplyMatrix(a,b);
		for(int i=0; i<r.length; i++){
			for(int j=0; j<r[0].length; j++)
				System.out.print(r[i][j]+"\t");
			System.out.print("\n");
		}
	}
	public static double[][] rotateMatrix(double [][]A, double Theta, double[] axisStart,double[] axisEnd){
		double zrot = Math.atan((axisEnd[1]-axisStart[1])/(axisEnd[0]-axisStart[0])),
				yrot = Math.atan( (Math.sqrt(Math.pow(axisEnd[1]-axisStart[1],2))+Math.pow(axisEnd[0]-axisStart[0],2))/(axisEnd[2]-axisStart[2]) );
		double [][] TranslationMatrix= {{1,0,0,-axisStart[0]},
									 {0,1,0,-axisStart[1]},
									 {0,0,1,-axisStart[2]},
									 {0,0,0,1}},
				RotateAboutZ= {	{Math.cos(zrot),-Math.sin(zrot),0,0},
						 	  	{Math.sin(zrot),Math.cos(zrot),0,0},
						 	  	{0,0,1,0},
						 	  	{0,0,0,1}	},
				RotateAboutY= {	{Math.cos(yrot),0,Math.sin(yrot),0},
								{0,1,0,0},
							 	{-Math.sin(yrot),0,Math.cos(yrot),0},
							 	{0,0,0,1}	},
				RotateAboutX= {	{1,0,0,0},
								{Math.cos(Theta),-Math.sin(Theta),0,0},
							 	{Math.sin(Theta),Math.cos(Theta),0,0},
							 	{0,0,0,1}	};
		// first translate to origin
		multiplyMatrix(multiplyMatrix(multiplyMatrix(multiplyMatrix(
				TranslationMatrix,
				RotateAboutZ),
				RotateAboutY),
				RotateAboutX),
				A);
		// align with an axis
		// rotate
		// undo axis alignment
		// undo translation
		
		return null;
	}
	/* I: Arrays to be multiplied A*B, with all rows having the same length: i.e. A[0].length = A[1].length
	 * O: Result = A*B
	 */
	public static double[][] multiplyMatrix(double [][]A, double [][]B){
		if(A[0].length != B.length)//can't multiply matrixes of mismatched dimensions
			return null;
		double [][] result= new double[A.length][B[0].length];
		for(int r=0; r<result.length;r++){//loops through rows of the result
			for(int c = 0; c<result[0].length; c++){//loops through columns of the result 
				int e = 0;//element in the result 
				for(int i = 0; i<A[0].length; i++){
					e+=A[r][i]*B[i][c];
				}
				result[r][c]=e;
			}
		}
		return result;
	}
	
	public static double[][] invertMatrix(double [][]A){
		return null;
	}
	

}
