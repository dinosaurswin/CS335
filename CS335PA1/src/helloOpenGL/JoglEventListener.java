package helloOpenGL;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.*;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;





public class JoglEventListener implements GLEventListener, KeyListener {
	
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
	double r=3, j=0, x=-4, y=6.5, vel=.1;
	//r-radius, j-solid toggle, x-x translation, y-y translation
	boolean keys[]={false,false,false,false,false,false,false,false,false,false};
	//pressed = [w, a, s, d, z, f, o, p, =, -]
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
	    }
    
	    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, 
	            int height) {
	        final GL2 gl = gLDrawable.getGL().getGL2();

	        if (height <= 0) // avoid a divide by zero error!
	            height = 1;
	        final float h = (float) width / (float) height;
	        gl.glViewport(0, 0, width, height);
	        gl.glMatrixMode(GL2.GL_PROJECTION);
	        gl.glLoadIdentity();
	        glu.gluPerspective(45.0f, h, 1.0, 200.0);
	        gl.glMatrixMode(GL2.GL_MODELVIEW);
	        gl.glLoadIdentity();
	        gl.glTranslatef(0.0f, 0.0f, -40.0f);
	    }

		@Override
		public void display(GLAutoDrawable gLDrawable) {
			final GL2 gl = gLDrawable.getGL().getGL2();

			gl.glClearColor(backrgb[0], 0, 1, 1);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

			backrgb[0]+=0.0005;
			if (backrgb[0]> 1) backrgb[0] = 0; 
			
			
			// =============================================
			// draw your content in this function
			//
			// =============================================
	        // =============================================
			
			/****Drawing UK out of Triangles****/
			this.drawUK(gl);
			/****Drawing a Circle****/		   	
			this.drawCircle(gl);     
		}

		@Override
		public void dispose(GLAutoDrawable arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
		    char key= e.getKeyChar();
		    if(key == 't')//toggles the solidness of the circle
		    	j=(j-r)*-1;//will toggle between r and 0
		    
		    //Translational keys controlled with the WASD standard, and WAZF outlined in the assignment 
		    //WASD	    
		    if(key == 'w')//up shift
		    	keys[0]=true; 
		    if(key == 'a')//left shift
		    	keys[1]=true;
		    if(key == 's')//down shift
		    	keys[2]=true;
		    if(key == 'd')//right shift
		    	keys[3]=true;
		    //ZF
		    if(key == 'z') //down shift
		    	keys[4]=true;
		    if(key == 'f' )//right shift
		    	keys[5]=true;
		    
		    //Scaling keys, controlled with O/P for Increase/Decrease
		    if(key == 'o')
		    	keys[6]=true;
		    if(key == 'p')
		    	keys[7]=true;
		    
		    // +/- keys to increase/decrease the speed you can move the ball at
		    if(key == '=')//= is the same key as + on the keyboard
		    	keys[8]=true;
		    if(key == '-' && vel-.1>=0)//vel can't go negative
		    	keys[9]=true;

		}

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {
			char key= e.getKeyChar();
			//Translational keys controlled with the WASD standard, and WAZF outlined in the assignment 
		    //WASD	    
		    if(key == 'w')//up shift
		    	keys[0]=false; 
		    if(key == 'a')//left shift
		    	keys[1]=false;
		    if(key == 's')//down shift
		    	keys[2]=false;
		    if(key == 'd')//right shift
		    	keys[3]=false;
		    //ZF
		    if(key == 'z') //down shift
		    	keys[4]=false;
		    if(key == 'f' )//right shift
		    	keys[5]=false;
		    
		    //Scaling keys, controlled with O/P for Increase/Decrease
		    if(key == 'o')
		    	keys[6]=false;
		    if(key == 'p')
		    	keys[7]=false;
		    
		    // +/- keys to increase/decrease the speed you can move the ball at
		    if(key == '=')//= is the same key as + on the keyboard
		    	keys[8]=false;
		    if(key == '-')//vel can't go negative
		    	keys[9]=false;
		    
		    
		}
		
		public void moveCircle(){
			 
			if(keys[8])//no upper speed limit
			   	vel+=.1;
			if(keys[9] && vel-.1>0)//min speed of .1
			    vel-=.1;
			
			//increasing/decreasing radius
			if(keys[6]){
		    	r+=.1;//increases the radius
		    	if(j>0)//makes sure the circle can be solid or an outline
		    		j=r;
		    }
		    if(keys[7]){
		    	r-=.1;//decreases the radius
		    	if(r<=0)//can't go to 0 or below radius
		    		r=.1;
		    	if(j>0)//makes sure the circle can be solid or an outline
		    		j=r;
		    }
		    
		    //Moving the circle up/down/left/right 
		    if(keys[0])//up shift
		    	y+=vel;
		    if(keys[1])//left shift
		    	x-=vel;
		    if(keys[2]||keys[4])//down shift
		    	y-=vel;
		    if(keys[3]||keys[5])//right shift
		    	x+=vel;
		}
		
		public void drawCircle(GL2 gl){
			moveCircle();
			double red1 = 1, green1=.647, blue1=.0, // RGB values for center of circle
					red2 = .627, green2=.322, blue2=.176;// RGB values for outside of circle
			for(double R = j; R<=r; R+=.01){//goes from j->r, if j=0 then its solid
	        	gl.glBegin(gl.GL_LINE_LOOP);
	        	double redR=red1-((red1-red2)/r)*R, greenR=green1-((green1-green2)/r)*R, blueR=blue1-((blue1-blue2)/r)*R;
	        	//color dependent on radius to make a smooth gradient
	        	gl.glColor3d(redR, greenR, blueR);
	        	if(R >= r-.01)//last ring of the circle
	        		gl.glColor3f(0.0f, 0.0f, 0.0f);//gives it a black outline
	        	for(int i=0; i<=360; i++){//goes around the circle
	        		gl.glVertex2d(R*Math.cos(i*Math.PI/180.0)+x, R*Math.sin(i*Math.PI/180.0)+y);
	        	}
	        	gl.glEnd();
			}
			if(j!=r){//if its solid then add lines for a basket ball
				gl.glBegin(gl.GL_LINES);
				gl.glColor3f(0.0f, 0.0f, 0.0f);//black
        		gl.glVertex2d(r+x, y);	gl.glVertex2d(-r+x, y); //horizontal Basketball line
        		gl.glVertex2d(x, r+y);  gl.glVertex2d(x, -r+y); //Vertical Basket ball line 
        		gl.glEnd();
        		gl.glBegin(gl.GL_LINE_STRIP);
        		for(int i=-42; i<=42; i++){//draws a partial circle translated to the left
        			gl.glVertex2d(r*Math.cos(i*Math.PI/180.0)+x-1.5*r, r*Math.sin(i*Math.PI/180.0)+y);
        		}
        		gl.glEnd();
        		gl.glBegin(gl.GL_LINE_STRIP);
        		for(int i=-42+180; i<=42+180; i++){//draws a partial circle translated right
        			gl.glVertex2d(r*Math.cos(i*Math.PI/180.0)+x+1.5*r, r*Math.sin(i*Math.PI/180.0)+y);
        		}
        		gl.glEnd();
			}
		}
		
		public void drawUK(GL2 gl){
			//skip first index bc its just the dimensions
			//use next 3 #'s as the point #'s in vertex 
			gl.glColor3f(1.0f, 1.0f, 1.0f);//White fill
			gl.glBegin(GL.GL_TRIANGLES);//made of triangles
			for( int i=0; i<indices.length; i++){//goes through all the indices
				if(i%4==0)//ignores every 4th because they're all 3 for 3 points
					continue;
				//uses the index i to determine x-y-z of the i-th vertex
				gl.glVertex3f(vertices[3*indices[i]], vertices[3*indices[i]+1], vertices[3*indices[i]+2]);
			}
			gl.glEnd();
		
			gl.glColor3f(0.0f, 0.0f, 0.0f);//Black for the outline
			gl.glBegin(GL.GL_LINE_LOOP);//just an outline of lines
			for( int i=0; i<vertices.length; i+=3){//goes through all the vertexes
				if(i==11*3 || i==22*3){//stop the loop when the U breaks around the K
					gl.glEnd();//end the line loop
					gl.glBegin(GL.GL_LINE_LOOP);//start a new one
				}
				gl.glVertex3f(vertices[i], vertices[i+1], vertices[i+2]);
			}
			gl.glEnd();
			
		}

	  /*  
	public void init(GLDrawable gLDrawable) {
		final GL gl = glDrawable.getGL();
        final GLU glu = glDrawable.getGLU();

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-1.0f, 1.0f, -1.0f, 1.0f); // drawing square
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }*/
}








