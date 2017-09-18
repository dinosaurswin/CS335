package helloOpenGL;

import java.awt.Color;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Ball {
	private double x,y,z;//center
	private float[][] hitVerts = new float[8][3];// bottom first then top
	// random textures later for now random colors
	private float[][] colors= {{1,0,0}, {0,1,0}, {0,0,1}, {.5f,.5f,0}, {.5f,0,.5f}} ;
	private int colorIndex;
	private double sx,sy,sz, r;
	private boolean move = false;
	 ArrayList<float[]> thisSphere;//= new ArrayList();
	//static variables for the entire class, only need to define a unit sphere once
	private static boolean first=true;//the spheres not been defined
	private static ArrayList<float[]> Sphere= new ArrayList();//sphere array to define
	private double g=.1, playspeed=1;
	
	public Ball(){
		this(0,0,0,0,0,0,4);
	}
	public Ball(double rr){
		this(0,0,0,0,0,0,rr);
	}
	public Ball(float xx, float yy, float zz){
		this(xx,yy,zz,0,0,0,1);
	}
	public Ball(double xx, double yy, double zz, double rr){
		this(xx,yy,zz,0,0,0,rr);
	}
	public Ball(double xx, double yy, double zz, double speedx, double speedy, double speedz, double rr){
		x = xx; 
		y = yy;
		z = zz;
		sx = speedx;
		sy = speedy;
		sz = speedz; 
		r = rr;
		colorIndex = (int)(Math.random()*colors.length);
		if(first){
			first=false;
			this.defineSphere(3, Sphere);//defines a unit sphere
		}
		thisSphere = new ArrayList(Sphere);
		this.updateHitBoxes();
	}
	public void draw(GL2 gl){
	//	GLUT glut = new GLUT();
	//	glut.glutSolidSphere(r, 1, 1);
		if(move)
			this.move();
		gl.glBegin(gl.GL_LINE_STRIP);
		gl.glColor3f(1f,1f,1f);
		this.updateHitBoxes();
/*		for(int i=0; i<hitVerts.length;i++){
			gl.glColor3f(i/7f,1f,1f);
			gl.glVertex3f(hitVerts[i][0],hitVerts[i][1],hitVerts[i][2]);
		}
	*/
		gl.glEnd();
		gl.glColor3f(colors[colorIndex][0],colors[colorIndex][1],colors[colorIndex][2]);
		//gl.glColor3f(1f, 0, 0);
		gl.glScaled(r, r, r);
		gl.glTranslated(x, y, z);
		
		this.drawSphere(gl);
		
		//System.out.println("X:"+x+" Y:"+y+" Z:"+z);
	}
	//true when it hits something flase when it doesnt
	public boolean checkHitBoxes(ArrayList<float[]> hit){
		/* 3 ______ 0		7 ______ 4
		 * |		|		|		 |
		 * |	B	|		|	T	 |
		 * 2 ______ 1 		6 ______ 5
		 */
		
		if(hit.size() == 4){
			// z-dimension constant
			//y isn't and x isn't
			float [] bl = hit.get(0);//bottom left
			float [] tr = hit.get(2);//top right
			
			/*  -----tr
			 * |	 |
			 * bl----
			 */
			if(hitVerts[3][2]>=bl[2] && hitVerts[2][2]<=tr[2])
			for(int i=0; i<hitVerts.length; i++){
				if(hitVerts[i][0]>=bl[0] && hitVerts[i][0]<=tr[0])
					if(hitVerts[i][1]>=bl[1] && hitVerts[i][1]<=tr[1])
						
							return true;
			}
		}
		else
		for(int j=0; j<hit.size(); j++){
			float[] b = hit.get(j);//the point is an array in the list
			if(b[0] <= hitVerts[3][0] && b[0] >= hitVerts[0][0]){ // x-direction <----->
				if(b[1] >= hitVerts[3][1] && b[1] <= hitVerts[7][1]){// y direction 
					if(b[2] <= hitVerts[3][2] && b[2] >= hitVerts[2][2]){// z direction
						//System.out.println("Hit Rim");
						return true;
					}
				}
			}
		}
		return false;
	}
	public void move(){
		x+=sx*playspeed;
		z+=sz*playspeed;
		if(y+sy*playspeed>0)
			y+=sy*playspeed;
		
		/*
		if(playspeed == 1/10.0){
			g=.1/10+.035;
		}
		
		if(y-g>0)
			y-=g;
		*/
		//gravity
	//	if(y-g*playspeed>0)
	//		y-=g*playspeed;
		
	//	if(sy-g*playspeed>=0)
			sy-=g*playspeed;
		
		
	}
	//sets up the hit boxes for the sphere
	public void updateHitBoxes (){
		//bottom square y position
		hitVerts[0][1]=(float) (y-r);
		hitVerts[1][1]=(float) (y-r);
		hitVerts[2][1]=(float) (y-r);
		hitVerts[3][1]=(float) (y-r);
		
		//top square y position
		hitVerts[4][1]=(float) (y+r);
		hitVerts[5][1]=(float) (y+r);
		hitVerts[6][1]=(float) (y+r);
		hitVerts[7][1]=(float) (y+r);
		
		//left square position x position
		hitVerts[0][0]=(float) (x-r);
		hitVerts[1][0]=(float) (x-r);
		hitVerts[4][0]=(float) (x-r);
		hitVerts[5][0]=(float) (x-r);
		
		//right square position x position
		hitVerts[2][0]=(float) (x+r);
		hitVerts[3][0]=(float) (x+r);
		hitVerts[6][0]=(float) (x+r);
		hitVerts[7][0]=(float) (x+r);
		
		//front square z position
		hitVerts[1][2]=(float) (z-r);
		hitVerts[2][2]=(float) (z-r);
		hitVerts[5][2]=(float) (z-r);
		hitVerts[6][2]=(float) (z-r);
		
		//front square z position
		hitVerts[0][2]=(float) (z+r);
		hitVerts[3][2]=(float) (z+r);
		hitVerts[4][2]=(float) (z+r);
		hitVerts[7][2]=(float) (z+r);
	}
	
	//accessors
	public double getx(){return x;}
	public double gety(){return y;}
	public double getz(){return z;}
	
	public double getsx(){return sx;}
	public double getsy(){return sy;}
	public double getsz(){return sz;}
	
	public double getr(){return r;}
	public int getColor(){return colorIndex;}
	
	//mutators 
	public void setx(double xx){x = xx;}
	public void sety(double yy){y = yy;}
	public void setz(double zz){z = zz;}
	public void setPos(float xx, float yy, float zz){x = xx;	y = yy; z = zz;}
	
	public void setsx(double xx){sx = xx;}
	public void setsy(double yy){sy = yy;}
	public void setsz(double zz){sz = zz;}
	public void setSpeed(double xx, double yy, double zz){sx = xx;	sy = yy;	sz = zz;}
	
	public void stop(){move=false;}
	public void start(){move=true;}
	
	public void setColor(int d){ colorIndex = d;}
	
	public void playbackSpeed(double ps){playspeed=ps;}
	
	public void defineSphere(int ndiv, ArrayList<float[]> vert){
	    float X = .525731112119133606f, Z=.850650808352039932f;
		float[][] vdata  = {//12 intial vertecies    
			    {-X, 0.0f, Z}, {X, 0.0f, Z}, {-X, 0.0f, -Z}, {X, 0.0f, -Z},    
			    {0.0f, Z, X}, {0.0f, Z, -X}, {0.0f, -Z, X}, {0.0f, -Z, -X},    
			    {Z, X, 0.0f}, {-Z, X, 0.0f}, {Z, -X, 0.0f}, {-Z, -X, 0.0f} 
			};
		int[][] tindices = { //20 intial inndicies
			    {0,4,1}, {0,9,4}, {9,5,4}, {4,5,8}, {4,8,1},    
			    {8,10,1}, {8,3,10}, {5,3,8}, {5,2,3}, {2,7,3},    
			    {7,10,3}, {7,6,10}, {7,11,6}, {11,0,6}, {0,1,6}, 
			    {6,1,10}, {9,0,11}, {9,11,2}, {9,2,5}, {7,2,11} };
		for(int i=0; i<20; i++)//20 triangle faces
			defineTriangle(vdata[tindices[i][0]], vdata[tindices[i][1]], vdata[tindices[i][2]], ndiv, vert);//, radius, gl);
	}
	void defineTriangle(float[] a, float[] b, float[] c, int div, ArrayList<float[]> vert){//float r, GL2 gl) {
	    if (div<=0) {//adds the triangles to be drawn later
	    	vert.add(a);
	    	vert.add(b);
	    	vert.add(c);
	    } else {
	        float[] ab=new float[3], ac=new float[3], bc=new float[3];
	        for (int i=0;i<3;i++) {
	            ab[i]=(float)(a[i]+b[i])/2;
	            ac[i]=(a[i]+c[i])/2;
	            bc[i]=(b[i]+c[i])/2;
	        }
	        normalize(ab); normalize(ac); normalize(bc);
	        defineTriangle(a, ab, ac, div-1, vert);//, r,gl);
	        defineTriangle(b, bc, ab, div-1, vert);//r,gl);
	        defineTriangle(c, ac, bc, div-1, vert);//r,gl);
	        defineTriangle(ab, bc, ac, div-1, vert);//r,gl);  
	       }  
	}
	//normalizes a point A
	void normalize(float[] a) {
	    float d=(float) Math.sqrt(a[0]*a[0]+a[1]*a[1]+a[2]*a[2]);
	    a[0]/=d; a[1]/=d; a[2]/=d;
	}
	//draws a sphere (if you add transformations to the matrix beforehand itll be changed accordingly)
	void drawSphere(GL2 gl){
		gl.glBegin(gl.GL_TRIANGLES);//made of triangles
		for(int i=0; i<thisSphere.size(); i++){//goes through all the points
			float[] a = thisSphere.get(i);//the point is an array in the list
			gl.glNormal3f(a[0], a[1], a[2]);//sets the normal
			gl.glVertex3f(a[0],a[1],a[2]);//adds the point
			//gl.glReadPixels(a[0],a[1], 1, 1, gl.GL_RGBA, gl.GL_FLOAT, buffer);
		}
		gl.glEnd();//ends all the triangles
	}

}
