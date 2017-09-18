package helloOpenGL;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.DoubleBuffer;
import java.util.ArrayList;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;




public class JoglEventListener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	
	float rot; 
	int windowWidth, windowHeight;
	float orthoX=40;
	float tVal_x, tVal_y, rVal_x, rVal_y, rVal;
	double rtMat[] = new double[16];
	int mouseX0, mouseY0;
	int saveRTnow=0, mouseDragButton=0;
	float time=0;//number of times drawn
	
	float camz = 10.0f, camx=0f,camy=0f,tcamz = 10.0f, tcamx=0f,tcamy=0f;
    private GLU glu = new GLU();
    
	ArrayList<float[]> Sphere= new ArrayList();
    
    boolean animate=true,dragging=false;//whether or not to move the model
    
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
	       
	        defineSphere(3,Sphere);//fills Sphere with vertexes of a unit sphere
	      //  System.out.println(Sphere.size());
	        glu.gluLookAt(0, 0, camz, 0, 0, 0, 0, 1, 0);
	        /* glu.gluLookAt(	0,0,20f, //eye
							0,0,0, //center
							0,1,0	);//up
	        */
	        gl.glEnable(gl.GL_LIGHT0);//enable the light
	        float lightPos[] = new float[] { 0, 0, 0, 1f};//set pos to be 0,0,0
	        gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, lightPos,0);
	        float diff[] = new float[] { 1f, 1f, 1f, 1f};//full intensity white/yellow light
	        gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, diff,0);//make the light
	        
	    
	        
	    }

	    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
	    	windowWidth = width;
	    	windowHeight = height;
	        final GL2 gl = gLDrawable.getGL().getGL2();

	        if (height <= 0) // avoid a divide by zero error!
	            height = 1;
	        final float h = (float) width / (float) height;
	        gl.glViewport(0, 0, width, height);
	        gl.glMatrixMode(GL2.GL_PROJECTION);
	        gl.glLoadIdentity();
	        
	       // gl.glOrtho(-orthoX*0.5, orthoX*0.5, -orthoX*0.5*height/width, orthoX*0.5*height/width, -100, 100);
	        glu.gluPerspective(45.0f, h, 1, 100000.0);
	        }
	    
		public void display(GLAutoDrawable gLDrawable) {
			
			final GL2 gl = gLDrawable.getGL().getGL2();
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			
			gl.glMatrixMode(GL2.GL_MODELVIEW);
	    	gl.glLoadIdentity();
	    	
	    	if(dragging)
	    		glu.gluLookAt(	tcamx, tcamy, tcamz, 
						0, 0, 0, 
						0, 1, 0);
	
	    	else
	    		glu.gluLookAt(	camx, camy, camz, 
	    						0, 0, 0, 
	    						0, 1, 0);
	    	
	    	gl.glEnable(gl.GL_NORMALIZE);
			//draw axis
	    	gl.glBegin(gl.GL_LINES);
	    	//x R
	    	float maxLine = (float) Math.sqrt(windowWidth*windowWidth+windowHeight*windowHeight);//max distance for a line in any direction being displayed
	    	gl.glColor3f(1f, 0f, 0f);
	    	gl.glVertex3d(-maxLine,0,0);
	    	gl.glVertex3d(maxLine,0,0);
	    
	    	//y	G
	    	gl.glColor3f(0f, 1f, 0f);
	    	gl.glVertex3d(0,-maxLine,0);
	    	gl.glVertex3d(0,maxLine,0);
	    	
	    	//z B
	    	gl.glColor3f(0f, 0f, 1f);
	    	gl.glVertex3d(0,0,-maxLine);
	    	gl.glVertex3d(0,0,maxLine);
	    	
	    	gl.glEnd();
	    
	    	float unit = 0.6f;//the base unit for the system
	    	//sun
	    	float Sx=0,Sy=0,Sz=0;//sun is the center of the solar system
	    	gl.glColor3f(.93f,.93f,0f);//yellow sun
	    	
	    	gl.glPushMatrix();//matrix for the sun added
	    	gl.glTranslatef(Sx, Sy, Sz);//moves to Sx,y,z
	    	gl.glScalef(unit, unit, unit);//scales to be 1*unit all around
	    	
	    	drawSphere(gl, Sphere);//draws the sun-sphere 
	    	gl.glRotatef(time%34*360/34, 0.0f, 0.0f, 1.0f);
	    	//every degree around the sun is roughly equal to 1 day so the sun should rotate 360 degrees in 34 days 
	    	
	    	
	    	float lightPos[] = new float[] { Sx, Sy, Sz, 1f};//set pos to be the same as the sun
		    gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, lightPos,0);
	    	
		    gl.glPopMatrix();//gets rid of sun matrix
		    
	    	gl.glEnable(gl.GL_LIGHTING);//turn on the light
	    	gl.glEnable(gl.GL_COLOR_MATERIAL);//allow material to be colored
	    	
	    	//earth
	    	float Er=5*unit, //orbits sun at 5U
	    			Ex=(float) (Sx+Er*Math.cos((time)*Math.PI/180.0)),
	    			Ey=(float) (Sy + Ex*Math.tan(12*Math.PI/180.0)), 
	    			Ez=(float) (Sz+Er*Math.sin((time)*Math.PI/180.0));
	    
	    	gl.glPushMatrix();//earths Matrix
	    	gl.glColor3f(.39f,.59f,1f);//blue earth
	    	gl.glTranslatef(Ex, Ey, Ez);//moves to the Ex,y,z
	    	gl.glScalef(unit/3, unit/3, unit/3);//scales to be unit/3 in all directions
	    	gl.glRotatef(time*360, 0.0f, 0.0f, 1.0f);
	    	//every degree around the sun is roughly equal to 1 day so earth should rotate 360 degrees in that time 
	    	drawSphere(gl, Sphere);//draw earth-sphere
	    	gl.glPopMatrix();//removes earth matrix
	    	
	    	//moon
	    	float Mr=unit, //orbits earth at 1U
	    			Mx=(float) (Ex+Mr*Math.cos((time)*Math.PI/180.0)), 
	    			My=Ey=(float) (Ey + Mx*Math.tan(-12*Math.PI/180.0)), 
	    			Mz=(float) (Ez+Mr*Math.sin((time)*Math.PI/180.0));
	    	gl.glColor3f(.92f,.92f,.92f);//white/gray moon
	    	
	       	gl.glPushMatrix();//moon matrix
	    	gl.glTranslatef(Mx, My, Mz);//Moves to Mx,y,z
	    	gl.glScalef(unit/9, unit/9, unit/9);//scales to unit/9 in all directions
	    	gl.glRotatef(time%30*360/30, 0.0f, 0.0f, 1.0f);
	    	drawSphere(gl, Sphere);//draws the moon
	    	
	    	//every degree around the sun is roughly equal to 1 day so moon should rotate 360 degrees in 30 dyas 
	    	
	    	gl.glPopMatrix();//gets rid of the moon matrix
	    	
	    	gl.glDisable(gl.GL_COLOR_MATERIAL);//turns off color
	    	gl.glDisable(gl.GL_LIGHTING);//turns off light
	    
	    	if(animate)//animations are on
	    		time=(float) ((time+.5)%360);//increases the time unit
		}
		//recursively defines a unit sphere out of triangles
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
		//draws a sphere (if you add transformations to the matrix beforehand itll be changed accordingly)
		void drawSphere(GL2 gl,ArrayList<float[]> vert){
			gl.glBegin(gl.GL_TRIANGLES);//made of triangles
			for(int i=0; i<vert.size(); i++){//goes through all the points
				float[] a = vert.get(i);//the point is an array in the list
				gl.glNormal3f(a[0], a[1], a[2]);//sets the normal
				gl.glVertex3f(a[0],a[1],a[2]);//adds the point
			}
			gl.glEnd();//ends all the triangles
		}
		//defines triangles recursively
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
		public void keyTyped(KeyEvent e) {
			// single event key typed processes go here
		    char key= e.getKeyChar(); 
		    if(key=='t')//toggle animations
		    	animate=!animate;
		}

		public void keyPressed(KeyEvent e) {
		}

		public void mouseDragged(MouseEvent e) {
			dragging=true;
			//current position
			float XX = (e.getX()-windowWidth*0.5f)*orthoX/windowWidth;
			float YY = -(e.getY()-windowHeight*0.5f)*orthoX/windowWidth;
			//original position
			float X0 = (mouseX0-windowWidth*0.5f)*orthoX/windowWidth;
			float Y0 = -(mouseY0-windowHeight*0.5f)*orthoX/windowWidth;
			
			if(mouseDragButton == 1){//left mouse button means move
				float x = XX-X0, y=YY-Y0;
				tcamy= camy-y/10;//moves y axis 
				tcamx= camx-x/10;//move x axis
			
			}
			if(mouseDragButton == 3){//right mouse button draggin means zoom in/out
				float x = XX-X0, y=YY-Y0;
				tcamz= camz-y/10;
			}
		}
		//the mouse is pressed thus the drag is about to start and the temp transformations should be displayed
		public void mousePressed(MouseEvent e) {
					mouseX0 = e.getX();//intial mouse position on screen
					mouseY0 = e.getY();
					
					if(e.getButton()==MouseEvent.BUTTON1) {	// Left button
						mouseDragButton=1;
					}
					else if(e.getButton()==MouseEvent.BUTTON3) {	// Right button
						mouseDragButton=3;
					}
				}
		//the mouse is being released thus the drag is over and transformations can be finalized
		public void mouseReleased(MouseEvent e) {
		}
		//unused override functions
		public void dispose(GLAutoDrawable arg0) {}
		public void keyReleased(KeyEvent e) {
			dragging=false;
			camx=tcamx;
			camy=tcamy;
			camz=tcamz;
		}
		public void mouseMoved(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
}



