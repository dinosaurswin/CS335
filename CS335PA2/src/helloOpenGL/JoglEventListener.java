package helloOpenGL;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.DoubleBuffer;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;




public class JoglEventListener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	
	float[] vertices={5.97994f, -0.085086f, -0.010798f, 
			5.97994f, 10.0043f, -0.010798f, 
			7.99077f, 10.0043f, -0.010798f, 
			7.99077f, 11.3449f, -0.010798f, 
			-0.405339f, 11.3449f, -0.010798f, 
			-0.405339f, 9.98083f, -0.010798f, 
			1.65252f, 9.98083f, -0.010798f, 
			1.65252f, 0.549879f, -0.010798f, 
			-0.722839f, 0.549879f, -0.010798f, 
			-0.722839f, -1.69612f, -0.010798f, 
			2.6168f, -1.69612f, -0.010798f, 
			-7.24925f, 0.42055f, -0.010798f, 
			-9.35415f, 0.42055f, -0.010798f, 
			-9.35415f, 10.0043f, -0.010798f, 
			-7.37859f, 10.0043f, -0.010798f, 
			-7.37859f, 11.3802f, -0.010798f, 
			-15.8217f, 11.3802f, -0.010798f, 
			-15.8217f, 9.99258f, -0.010798f, 
			-13.8109f, 9.99258f, -0.010798f, 
			-13.8109f, -0.061591f, -0.010798f, 
			-10.2361f, -1.73139f, -0.010798f, 
			-7.26099f, -1.73139f, -0.010798f, 
			-6.1909f, 0.855631f, -0.010798f, 
			-8.11942f, 0.855631f, -0.010798f, 
			-8.11942f, 2.31379f, -0.010798f, 
			0.217914f, 2.31379f, -0.010798f, 
			0.217914f, 0.926204f, -0.010798f, 
			-1.73415f, 0.926204f, -0.010798f, 
			-1.73415f, -4.10675f, -0.010798f, 
			9.23724f, 0.937952f, -0.010798f, 
			7.26169f, 0.937952f, -0.010798f, 
			7.26169f, 2.38434f, -0.010798f, 
			15.6696f, 2.38434f, -0.010798f, 
			15.6696f, 1.00851f, -0.010798f, 
			14.964f, 1.00851f, -0.010798f, 
			7.75558f, -2.44873f, -0.010798f, 
			14.4231f, -9.36318f, -0.010798f, 
			16.0576f, -9.36318f, -0.010798f, 
			16.0576f, -10.6685f, -0.010798f, 
			7.62625f, -10.6685f, -0.010798f, 
			7.62625f, -9.33965f, -0.010798f, 
			9.67236f, -9.33965f, -0.010798f, 
			4.49827f, -3.90687f, -0.010798f, 
			-1.35784f, -6.59973f, -0.010798f, 
			-1.35784f, -9.3279f, -0.010798f, 
			0.217914f, -9.3279f, -0.010798f, 
			0.217914f, -10.6919f, -0.010798f, 
			-8.22526f, -10.6919f, -0.010798f, 
			-8.22526f, -9.32786f, -0.010798f, 
			-6.20266f, -9.32786f, -0.010798f};
	int[] indices={3, 2, 3, 1, 
			3, 1, 3, 6, 
			3, 1, 6, 10, 
			3, 10, 6, 7, 
			3, 10, 7, 8, 
			3, 4, 5, 6, 
			3, 4, 6, 3, 
			3, 10, 8, 9, 
			3, 1, 10, 0, 
			3, 13, 14, 15, 
			3, 13, 15, 18, 
			3, 13, 18, 20, 
			3, 13, 20, 12, 
			3, 16, 17, 18, 
			3, 16, 18, 15, 
			3, 12, 20, 21, 
			3, 12, 21, 11, 
			3, 20, 18, 19, 
			3, 49, 22, 44, 
			3, 44, 22, 28, 
			3, 44, 28, 43, 
			3, 43, 28, 29, 
			3, 43, 29, 42, 
			3, 42, 29, 35, 
			3, 42, 35, 41, 
			3, 41, 35, 36, 
			3, 41, 36, 38, 
			3, 38, 36, 37, 
			3, 39, 40, 41, 
			3, 39, 41, 38, 
			3, 29, 30, 32, 
			3, 29, 32, 34, 
			3, 29, 34, 35, 
			3, 46, 47, 49, 
			3, 46, 49, 44, 
			3, 46, 44, 45, 
			3, 22, 23, 25, 
			3, 22, 25, 27, 
			3, 22, 27, 28, 
			3, 25, 23, 24, 
			3, 27, 25, 26, 
			3, 49, 47, 48, 
			3, 32, 30, 31, 
			3, 34, 32, 33};
	float backrgb[] = new float[4]; 
	float rot; 
	
	/*
	 * Custom variables for mouse drag operations 
	 */
	int windowWidth, windowHeight;
	float orthoX=40;
	float tVal_x, tVal_y, rVal_x, rVal_y, rVal;
	double rtMat[] = new double[16];
	int mouseX0, mouseY0;
	int saveRTnow=0, mouseDragButton=0;
	
	float focalLength = 10.0f;
	float r11 = 1.0f, r12 = 0.0f, r13 = 0.0f, tx = 0.0f,
	      r21 = 0.0f, r22 = 1.0f, r23 = 0.0f, ty = 0.0f,
	      r31 = 0.0f, r32 = 0.0f, r33 = 1.0f, tz = 0.0f;
	float tr11 = 1.0f, tr12 = 0.0f, tr13 = 0.0f, 
		  tr21 = 0.0f, tr22 = 1.0f, tr23 = 0.0f,
		  tr31 = 0.0f, tr32 = 0.0f, tr33 = 1.0f, //temp rotation matrix variables
		  dx=0f,dy=0f,s=1f;	//temp translation and scale variables
	boolean scale=false, trans=false, rotate=false, changing=false;//booleans for transformation mode
    private GLU glu = new GLU();

	
	 public void displayChanged(GLAutoDrawable gLDrawable, 
	            boolean modeChanged, boolean deviceChanged) {
	    }

	    /** Called by the drawable immediately after the OpenGL context is
	     * initialized for the first time. Can be used to perform one-time OpenGL
	     * initialization such as setup of lights and display lists.
	     * @param gLDrawable The GLAutoDrawable object.
	     */
	    public void init(GLAutoDrawable gLDrawable) {
	        GL2 gl = gLDrawable.getGL().getGL2();
	        //gl.glShadeModel(GL.GL_LINE_SMOOTH);              // Enable Smooth Shading
	        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background
	        gl.glClearDepth(1.0f);                      // Depth Buffer Setup
	        gl.glEnable(GL.GL_DEPTH_TEST);              // Enables Depth Testing
	        gl.glDepthFunc(GL.GL_LEQUAL);               // The Type Of Depth Testing To Do
	        // Really Nice Perspective Calculations
	        //gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

	        gl.glMatrixMode(GL2.GL_MODELVIEW);
	        gl.glLoadIdentity();
	        
	        
	        
	    }

	    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, 
	            int height) {
	    	windowWidth = width;
	    	windowHeight = height;
	        final GL2 gl = gLDrawable.getGL().getGL2();

	        if (height <= 0) // avoid a divide by zero error!
	            height = 1;
	        final float h = (float) width / (float) height;
	        gl.glViewport(0, 0, width, height);
	        gl.glMatrixMode(GL2.GL_PROJECTION);
	        gl.glLoadIdentity();
	        gl.glOrtho(-orthoX*0.5, orthoX*0.5, -orthoX*0.5*height/width, orthoX*0.5*height/width, -100, 100);
	        //glu.gluPerspective(45.0f, h, 1, 100000.0);

	    }
	    
	    public void project(float[] vertices_in, float[] vertices_out) {
	    
	        
	    	float[] projMatrix = 
		    	{
		    	   1, 0, 0, 0,
		    	   0, 1, 0, 0,
		    	   0, 0, 0, 0,
		    	   0,   0,   1.0f/focalLength, 1
		    	};
		    	
		    	for(int i = 0; i < vertices_in.length; i += 3){
		    		float tempZ = vertices_in[i+2] + 10;  // this translation in Z is needed to pull the camera away from the object. 
		    		// don't change the above line unless you are sure about what you are doing.
		    		
		    		vertices_out[i] = (projMatrix[0] * vertices_in[i] + projMatrix[1] * vertices_in[i + 1] + projMatrix[2] * tempZ + projMatrix[3]);
		    		vertices_out[i + 1] = projMatrix[4] * vertices_in[i] + projMatrix[5] * vertices_in[i + 1] + projMatrix[6] * tempZ+ projMatrix[7];
		    		vertices_out[i + 2] = projMatrix[8] * vertices_in[i] + projMatrix[9] * vertices_in[i + 1] + projMatrix[10] * tempZ+ projMatrix[11];
		    	   
		    		float temp = projMatrix[12] * vertices_in[i] + projMatrix[13] * vertices_in[i + 1] + projMatrix[14] * tempZ+ projMatrix[15];
		    	   
		    	    vertices_out[i]   = vertices_out[i]   / temp;
		    	    vertices_out[i+1] = vertices_out[i+1] / temp;
		    	    vertices_out[i+2] = vertices_out[i+2] / temp;
	    	
		    	}
	    }
	    
	    public void transform(float[] vertices_in, float[] vertices_out){
	    	int length = vertices_in.length;
	    	float[] transMatrix = //regular composite transformation matrix
		    	{  r11, r12, r13, tx,
		    	   r21, r22, r23, ty,
		    	   r31, r32, r33, tz,
		    	   0,   0,   0,   1		},
		    	changingMatrix = //matrix for rotating before the rotation is finalized
		    	{  s*tr11, tr12, tr13, tx+dx,
		    	   tr21, s*tr22, tr23, ty+dy,
		    	   tr31, tr32, s*tr33, tz,
		    	   0,   0,   0,   1		}, Matrix;
	    	if(!changing)//whether or not currently changing
	    		Matrix=transMatrix;
	    	else//uses temp matrix for displaying in progress transformations 
	    		Matrix=changingMatrix;
	    	//vertices come in as {x y z, x1,y1,z1 ... xn,yn,zn}
	    	for(int i=0; i<length; i+=3){//each point [xi,yi,zi] gets transformed
	    		vertices_out[i] = Matrix[0]*vertices_in[i]+ Matrix[1]*vertices_in[i+1]+ Matrix[2]*vertices_in[i+2]+ Matrix[3]*1;
	    		vertices_out[i+1] = Matrix[4]*vertices_in[i]+ Matrix[5]*vertices_in[i+1]+ Matrix[6]*vertices_in[i+2]+ Matrix[7]*1;
	    		vertices_out[i+2] = Matrix[8]*vertices_in[i]+ Matrix[9]*vertices_in[i+1]+ Matrix[10]*vertices_in[i+2]+ Matrix[11]*1;
	    	}
	    }
		public void display(GLAutoDrawable gLDrawable) {
			final GL2 gl = gLDrawable.getGL().getGL2();

			gl.glClearColor(backrgb[0], 0, 1, 1);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
			
	    	gl.glMatrixMode(GL2.GL_MODELVIEW);
	    	gl.glLoadIdentity();
	    	
			float[] vertices_new = new float[vertices.length],//for projected vertices
			TransVert = new float[vertices.length];//for transformed vertices
			
			transform(vertices, TransVert);//transforms vertices and stores them in TransVert
			
			project(TransVert, vertices_new);//projects vertices and stores them in vertices_new
	        
			gl.glBegin(GL.GL_TRIANGLES);        // Drawing Using Triangles
        	for(int i=0; i<44; i++) {
        		gl.glColor3f(0.7f, 0.7f, 0.7f);
        		//(e.getX()-windowWidth*0.5f)*orthoX/windowWidth
        		float width = (-windowWidth*0.5f)*orthoX/windowWidth;
        		float height = (-windowHeight*0.5f)*orthoX/windowHeight;
        		//System.out.println(Math.abs(vertices_new[(indices[i*4+1])*3]/width));
        		gl.glColor3f(1-Math.abs(vertices_new[(indices[i*4+1])*3]/width), 1-Math.abs(vertices_new[(indices[i*4+1])*3+1]/height), 1-Math.abs(vertices_new[(indices[i*4+1])*3]+ vertices_new[(indices[i*4+1])*3+1])/(width*height));
        		gl.glVertex3f(vertices_new[(indices[i*4+1])*3],
        				vertices_new[(indices[i*4+1])*3+1],
        				vertices_new[(indices[i*4+1])*3+2]);
        		gl.glColor3f(1-Math.abs(vertices_new[(indices[i*4+2])*3]/width), 1-Math.abs(vertices_new[(indices[i*4+2])*3+1]/height), 1-Math.abs(vertices_new[(indices[i*4+2])*3]+ vertices_new[(indices[i*4+2])*3+1])/(width*height));
        		gl.glVertex3f(vertices_new[(indices[i*4+2])*3],
        				vertices_new[(indices[i*4+2])*3+1],
        				vertices_new[(indices[i*4+2])*3+2]);
        		gl.glColor3f(1-Math.abs(vertices_new[(indices[i*4+3])*3]/width), 1-Math.abs(vertices_new[(indices[i*4+3])*3+1]/height), 1-Math.abs(vertices_new[(indices[i*4+3])*3]+ vertices_new[(indices[i*4+3])*3+1])/(width*height));
        		gl.glVertex3f(vertices_new[(indices[i*4+3])*3],
        				vertices_new[(indices[i*4+3])*3+1],
        				vertices_new[(indices[i*4+3])*3+2]);
        	}
	        gl.glEnd();                         // Finished Drawing The Triangle
		}

		public void keyTyped(KeyEvent e) {
			// single event key typed processes go here
		    char key= e.getKeyChar(); 
			if(key == '0'){//resets the transformation matrix and focal length
				r11 = 1.0f; r12 = 0.0f; r13 = 0.0f; tx = 0.0f;
				r21 = 0.0f; r22 = 1.0f; r23 = 0.0f; ty = 0.0f;
				r31 = 0.0f; r32 = 0.0f; r33 = 1.0f; tz = 0.0f;
				
				focalLength=10;
			}
			if(key == 's'||key == 't'||key == 'r')//a key we care about is pressed
			{scale = false;	rotate = false;	trans = false;}//reset booleans so only one can be active
			if(key == 's')//scaling mode
				scale = true;
			if(key == 'r')//rotation mode
				rotate = true;
			if(key == 't')//translation mode
				trans = true;
		}

		public void keyPressed(KeyEvent e) {
			// Continuous key changes go here
			char key= e.getKeyChar();
			if(key == 'g')//increases focal length
				focalLength += 0.5;//zoom in
			if(key == 'h' && focalLength-.5>=0)//decreases focal length, don't want to go negative
				focalLength -= 0.5;//zoom out
		}

		public void mouseDragged(MouseEvent e) {
			//current position
			float XX = (e.getX()-windowWidth*0.5f)*orthoX/windowWidth;
			float YY = -(e.getY()-windowHeight*0.5f)*orthoX/windowWidth;
			//original position
			float X0 = (mouseX0-windowWidth*0.5f)*orthoX/windowWidth;
			float Y0 = -(mouseY0-windowHeight*0.5f)*orthoX/windowWidth;
			
			if(rotate){
				//translate the first point to origin, rotate, translate back
				float[] toOrgin=//translates to the origin
						{	1f, 0,	0,	-vertices[1],
							0,	1f,	0,	-vertices[2],
							0,	0,	1f,	-vertices[3],
							0,	0,	0,	1},
						fromOrgin=//translates back to initial position
						{	1f, 0,	0,	vertices[1],
							0,	1f,	0,	vertices[2],
							0,	0,	1f,	vertices[3],
							0,	0,	0,	1},
						rotMatrix=//starts as an identity matrix to be changed into rotation matrix depending what axis it rotates about
						{	1f, 0,	0,	0,
							0,	1f,	0,	0,
							0,	0,	1f,	0,
							0,	0,	0,	1f	};
				if(mouseDragButton == 1){//left button
						if(Math.abs(YY-Y0)>Math.abs(XX-X0)){//determines which axis is trying to be rotated about
							/* Rotate around X
							 1 	0		0		0
							 0	cosX 	-sinX	0
							 0	sinX	cosX	0
							 0	0		0		1 	*/
							double x=(YY-Y0)/4;//uses change in Y for flip around the X-axis
							rotMatrix[5]=(float) Math.cos(x);
							rotMatrix[6]=(float) -Math.sin(x);
							rotMatrix[9]=(float) Math.sin(x);
							rotMatrix[10]=(float) Math.cos(x);
						}
						else{
							/* Rotate around Y
							 cosY 	0	sinY	0
							 0		1	0		0
							 -sinY 	0	cosY	0
							 0		0	0		1 	*/
							double y=(XX-X0)/4;//uses change in X for flip around the Y-axis
							rotMatrix[0]=(float) Math.cos(y);
							rotMatrix[2]=(float) Math.sin(y);
							rotMatrix[8]=(float) -Math.sin(y);
							rotMatrix[10]=(float) Math.cos(y);
						}
					}
					else if(mouseDragButton == 3){// right button means rotate around Z
						/* Rotate around Z
						 cosZ 	-sinZ	0	0
						 sinZ	cosZ	0	0
						 0		0		1	0
						 0		0		0	1	*/
						double z= Math.atan2(YY,XX) - Math.atan2(Y0,X0);//follows angle made by the mouse from intial click to current
						rotMatrix[0]=(float) Math.cos(z);
						rotMatrix[1]=(float) -Math.sin(z);
						rotMatrix[4]=(float) Math.sin(z);
						rotMatrix[5]=(float) Math.cos(z);
					
					}
				float[]	tofill=new float[4*4], finalRot=new float[4*4];//empty arrays for multiplication
				//fromOrigin*rotation*toOrigin = RotationMatrix
				multiply4X4(fromOrgin,rotMatrix,tofill);// tofill = fromOrigin*rotation
				multiply4X4(tofill,toOrgin,finalRot);// finalRotation = tofill*toOrigin
				float[] prevTrans = //previous transformations from before this rotation
			    	{  r11, r12, r13, tx,
			    	   r21, r22, r23, ty,
			    	   r31, r32, r33, tz,
			    	   0,   0,   0,   1	},
			    	newTrans=new float[4*4];//new composite transformations to be filled
				multiply4X4(finalRot,prevTrans,newTrans);//rotationMatrix*previousTransformations = newTransformations
				tr11 = newTrans[0]; tr12 = newTrans[1]; tr13 = newTrans[2];
				tr21 = newTrans[4]; tr22 = newTrans[5]; tr23 = newTrans[6];
				tr31 = newTrans[8]; tr32 = newTrans[9]; tr33 = newTrans[10];
				//sets the temp matrixes values to match what they are calculated to be in newTrans 
			}
			
			if(trans){//translate using dx,dy as temp increase for tx,ty
				dx=XX-X0;//move x with mouse
				dy=YY-Y0;//move y with mouse
			}
			
			if(scale){//scaling in all directions by 1/10th of the length from point (x0,y0) to (xx,yy)
				s = ((float)Math.sqrt( (Math.pow( (XX-X0), 2)) + (Math.pow( (YY-Y0), 2)) ))/10 + 1;//+1(so it wont scale down when you intially click
				if(mouseDragButton == 3)//right drag for scaling down
					s=1/s;//scaling down means it shrinks by s rather than grows by s
			}
		}
		/*takes in 2 matrixes to multiply and one to return the answer in
			one * two = ans
			only takes in 4 by 4 matrices to multiply, since they are all in single arrays 
			and there's no good way to tell where rows/columns begin and end dynamically*/
		public void multiply4X4(float[] one, float[] two, float[] ans){
			if(one.length != 4*4 && two.length != 4*4 && ans.length != 4*4)//makes sure its a 4x4 * 4x4
				return;
			for(int i=0; i<4*4;i+=4)//goes through the 4 rows
				for(int j=0; j<4; j++)//goes through the 4 cols
					ans[i+j] = one[i]*two[j] + one[i+1]*two[j+4] + one[i+2]*two[j+8] + one[i+3]*two[j+12];
		}
		//the mouse is pressed thus the drag is about to start and the temp transformations should be displayed
		public void mousePressed(MouseEvent e) {
			mouseX0 = e.getX();//intial mouse position on screen
			mouseY0 = e.getY();
			if(trans || scale || rotate){//starts transforming the shape
				changing = true;//switches to showing the temporary transformation matrix
				//resets the temp matrix
				tr11 = r11; tr12 = r12; tr13 = r13;
				tr21 = r21; tr22 = r22; tr23 = r23;
				tr31 = r31; tr32 = r32; tr33 = r33;
			}
			if(e.getButton()==MouseEvent.BUTTON1) {	// Left button
				mouseDragButton=1;
			}
			else if(e.getButton()==MouseEvent.BUTTON3) {	// Right button
				mouseDragButton=3;
			}
		}
		//the mouse is being released thus the drag is over and transformations can be finalized
		public void mouseReleased(MouseEvent e) {
			if(trans || scale || rotate)//stops changing the shape
				changing = false;
			if(trans){//finalizes the tx,ty translation 
				tx+=dx;
				ty+=dy;
				dx=0;//resets temp translation variables
				dy=0;
			}
			if(scale){//finalizes the scaling
				r11 = s*tr11;
				r22 = s*tr22;
				r33 = s*tr33;
				s=1f;//resets temp scaling factor
			}
			if(rotate){//finalizes the rotation
				r11 = tr11; r12 = tr12; r13 = tr13;
				r21 = tr21; r22 = tr22; r23 = tr23;
				r31 = tr31; r32 = tr32; r33 = tr33;
			}
		}
		//unused override functions
		public void dispose(GLAutoDrawable arg0) {}
		public void keyReleased(KeyEvent e) {}
		public void mouseMoved(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
}



