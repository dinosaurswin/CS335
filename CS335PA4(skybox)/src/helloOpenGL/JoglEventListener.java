package helloOpenGL;






import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.awt.image.DataBufferByte; 










import java.nio.ByteOrder;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.awt.TextRenderer;





public class JoglEventListener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	
	float rot, rotX, x = 0, y=5f, z=0, lookx=0, looky=0, lookz=0;
	boolean a=false, s=false, d=false, w=false, jump=false;//keys for movement 
	 
	float fraction = 0.5f;
	
	/*
	 * Custom variables for mouse drag operations 
	 */
	int windowWidth, windowHeight;
	float orthoX=40;
	double rtMat[] = new double[16];
	int mouseX0, mouseY0;
	//GLUquadric earth;
	int texID[]  = new int[3]; 
    
	 //float[] lightPos = { 10,10,10,1 };        // light position

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
	        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background
	        gl.glEnable(GL.GL_DEPTH_TEST);              // Enables Depth Testing

	        gl.glMatrixMode(GL2.GL_MODELVIEW);
	        gl.glLoadIdentity();
	        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, rtMat, 0);
	        gl.glEnable(GL2.GL_RESCALE_NORMAL );
	        // load an image; 
	        try {
	        	
	        	gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
	        	//load skybox texture map
				BufferedImage aImage = ImageIO.read(new File("map.jpg"));
				ByteBuffer buf = ByteBuffer.wrap( ((DataBufferByte)  aImage.getRaster().getDataBuffer()).getData());
		        
				gl.glGenTextures(3, texID, 0);
				gl.glBindTexture(GL.GL_TEXTURE_2D, texID[0]);
				
				gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

				gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, aImage.getWidth(), 
	                    aImage.getHeight(), 0, GL2.GL_BGR, GL.GL_UNSIGNED_BYTE, buf);
				
				
				gl.glEnable(GL.GL_TEXTURE_2D);
				
				gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
				gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
				
				gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
	        	
				//load ground texture map
				aImage = ImageIO.read(new File("ground2.jpg"));
				buf = ByteBuffer.wrap( ((DataBufferByte)  aImage.getRaster().getDataBuffer()).getData());
				
				//gl.glGenTextures(2, texID, 0);
				gl.glBindTexture(GL.GL_TEXTURE_2D, texID[1]);
				
				gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

				gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, aImage.getWidth(), 
	                    aImage.getHeight(), 0, GL2.GL_BGR, GL.GL_UNSIGNED_BYTE, buf);
				
				
				gl.glEnable(GL.GL_TEXTURE_2D);
				
				gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
				gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
				
				//load ground texture map
				aImage = ImageIO.read(new File("building.jpg"));
				buf = ByteBuffer.wrap( ((DataBufferByte)  aImage.getRaster().getDataBuffer()).getData());
				
				//gl.glGenTextures(2, texID, 0);
				gl.glBindTexture(GL.GL_TEXTURE_2D, texID[2]);
				
				gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

				gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, aImage.getWidth(), 
	                    aImage.getHeight(), 0, GL2.GL_BGR, GL.GL_UNSIGNED_BYTE, buf);
				
				
				gl.glEnable(GL.GL_TEXTURE_2D);
				
				gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
				gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
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
	        //gl.glOrtho(-orthoX*0.5, orthoX*0.5, -orthoX*0.5*height/width, orthoX*0.5*height/width, -100, 100);
	        glu.gluPerspective(65.0f, h, 0.1, 4001.0);
	        
	        gl.glMatrixMode(GL2.GL_MODELVIEW);
	        gl.glLoadIdentity();
	        //glu.gluLookAt(0, 0, 6, 0, 0, 0, 0, 1, 0);
	    }
	    
	    public void drawGround(final GL2 gl) {
	    	gl.glBindTexture(GL.GL_TEXTURE_2D,  texID[1]);//load the ground texture
	    	gl.glBegin(GL2.GL_QUADS);//start drawing the plane
	    	
	    	gl.glNormal3f(0, -1, 0);
	    	
	    	gl.glTexCoord2f(0.0f, 0.0f);   	gl.glVertex3f(-5000, 1, -5000);
	    	gl.glTexCoord2f(0.0f, 1.0f);   	gl.glVertex3f(-5000, 1, 5000);
	    	gl.glTexCoord2f(1.0f, 1.0f);   	gl.glVertex3f(5000, 1, 5000);
	    	gl.glTexCoord2f(1.0f, 0.0f);   	gl.glVertex3f(5000, 1, -5000);
	    	
	    	gl.glEnd();
	    }
	    
	    public void drawBuilding(final GL2 gl){
	    	gl.glBindTexture(GL.GL_TEXTURE_2D,  texID[2]);//load building texture
	    	gl.glBegin(GL2.GL_QUADS);
	         
	    	// front plane
	        gl.glNormal3f(0,  0, 1);
	        gl.glColor3f(1, 1, 1);
	        	         
	        gl.glTexCoord2f(0.0f, 0.0f);	gl.glVertex3f(0, 0, 1);
	        gl.glTexCoord2f(0.0f, 1.0f);	gl.glVertex3f(1, 0, 1);
	        gl.glTexCoord2f(1.0f, 1.0f);	gl.glVertex3f(1, 1, 1); 
	        gl.glTexCoord2f(1.0f, 0.0f);	gl.glVertex3f(0, 1, 1);
	        
	         // back plane
	        gl.glNormal3f(0,  0, -1);
	        gl.glColor3f(1, 1, 1);
	        
	        gl.glTexCoord2f(0.0f, 0.0f);	gl.glVertex3f(0, 0, 0); 
	        gl.glTexCoord2f(0.0f, 1.0f);	gl.glVertex3f(1, 0, 0);
	        gl.glTexCoord2f(1.0f, 1.0f);	gl.glVertex3f(1, 1, 0); 
	        gl.glTexCoord2f(1.0f, 0.0f);	gl.glVertex3f(0, 1, 0);
	        
	         // left plane 
	         gl.glNormal3f(-1,  0, 0);
	         gl.glColor3f(1, 1, 1);
	         
	         gl.glTexCoord2f(0.0f, 0.0f);	gl.glVertex3f(0, 0, 0); 
	         gl.glTexCoord2f(0.0f, 1.0f);	gl.glVertex3f(0, 1, 0);
	         gl.glTexCoord2f(1.0f, 1.0f);	gl.glVertex3f(0, 1, 1); 
	         gl.glTexCoord2f(1.0f, 0.0f);	gl.glVertex3f(0, 0, 1);
	         
	         // right plane
	         gl.glNormal3f(1,  0, 0);
	         gl.glColor3f(1, 1, 1);
	         
	         gl.glTexCoord2f(0.0f, 0.0f);	gl.glVertex3f(1, 0, 0); 
	         gl.glTexCoord2f(0.0f, 1.0f);	gl.glVertex3f(1, 1, 0);
	         gl.glTexCoord2f(1.0f, 1.0f);	gl.glVertex3f(1, 1, 1); 
	         gl.glTexCoord2f(1.0f, 0.0f);	gl.glVertex3f(1, 0, 1);
	         
	         
	         // on the XZ plane,  
	         // up plane; 
	         gl.glNormal3f(0,  1, 0);
	         gl.glColor3f(1, 1, 1);
	         
	         gl.glTexCoord2f(0.0f, 0.0f);	gl.glVertex3f(0, 1, 0); 
	         gl.glTexCoord2f(0.0f, 1.0f);	gl.glVertex3f(1, 1, 0);
	         gl.glTexCoord2f(1.0f, 1.0f);	gl.glVertex3f(1, 1, 1); 
	         gl.glTexCoord2f(1.0f, 0.0f);	gl.glVertex3f(0, 1, 1);
	         
	         // down plane; 
	         gl.glNormal3f(0,  -1, 0);
	         gl.glColor3f(1, 1, 1);
	         
	         gl.glTexCoord2f(0.0f, 0.0f);	gl.glVertex3f(0, 0, 0); 
	         gl.glTexCoord2f(0.0f, 1.0f);	gl.glVertex3f(1, 0, 0);
	         gl.glTexCoord2f(1.0f, 1.0f);	gl.glVertex3f(1, 0, 1); 
	         gl.glTexCoord2f(1.0f, 0.0f);	gl.glVertex3f(0, 0, 1);
	        
	         gl.glEnd(); 
	    }

	    public void drawSkyBox(final GL2 gl) {
	    	
	    	gl.glBindTexture(GL.GL_TEXTURE_2D,  texID[0]);//load the texture for the skybox
	    	gl.glBegin(GL2.GL_QUADS);//start drawing the faces of the cube
	         
	    	// front plane
	         gl.glNormal3f(0,  0, 1);
	         gl.glColor3f(1, 0, 0);
	         
	         gl.glTexCoord2f(2.0f/3.0f, 1.0f/4.0f);		gl.glVertex3f(0, 0, 1);//bottom right      
	         gl.glTexCoord2f(1.0f/3.0f, 1.0f/4.0f);     gl.glVertex3f(1, 0, 1);//bottom left
	         gl.glTexCoord2f(1.0f/3.0f, 0f);			gl.glVertex3f(1, 1, 1); //top left
	         gl.glTexCoord2f(2.0f/3.0f, 0.0f);          gl.glVertex3f(0, 1, 1);//top right
	        
	         // up plane; 
	         gl.glNormal3f(0,  1, 0);
	         gl.glColor3f(0, 0, 1);
	         
	         gl.glTexCoord2f(2.0f/3.0f, 3.0f/4.0f);		gl.glVertex3f(0, 1, 0); //top right
	         gl.glTexCoord2f(1.0f/3.0f, 3.0f/4.0f);	    gl.glVertex3f(1, 1, 0);//top left
	         gl.glTexCoord2f(1.0f/3.0f, 1f); 	        gl.glVertex3f(1, 1, 1); //bottom left
	         gl.glTexCoord2f(2.0f/3.0f, 1f);            gl.glVertex3f(0, 1, 1);//bottom right
	        
	         // back plane
	         gl.glNormal3f(0,  0, -1);
	         gl.glColor3f(1, 0, 0);
	         
	         gl.glTexCoord2f(2.0f/3.0f, 2.0f/4.0f);		gl.glVertex3f(0, 0, 0); //top right
	         gl.glTexCoord2f(1.0f/3.0f, 2.0f/4.0f);		gl.glVertex3f(1, 0, 0);//top left
	         gl.glTexCoord2f(1.0f/3.0f, 3.0f/4.0f);		gl.glVertex3f(1, 1, 0);//bottom left 
	         gl.glTexCoord2f(2.0f/3.0f, 3.0f/4.0f);		gl.glVertex3f(0, 1, 0); //bottom right
	         
	         // down plane; 
	         gl.glNormal3f(0,  -1, 0);
	         gl.glColor3f(0, 0, 1);
	         
	         gl.glTexCoord2f(2.0f/3.0f, 2.0f/4.0f);		gl.glVertex3f(0, 0, 0); //bottom right
	         gl.glTexCoord2f(1.0f/3.0f, 2.0f/4.0f);		gl.glVertex3f(1, 0, 0);//bottom left
	         gl.glTexCoord2f(1.0f/3.0f, 1.0f/4.0f);		gl.glVertex3f(1, 0, 1);//top left
	         gl.glTexCoord2f(2.0f/3.0f, 1.0f/4.0f);		gl.glVertex3f(0, 0, 1);//top right      
	         	        
	         // right plane 
	         gl.glNormal3f(-1,  0, 0);
	         gl.glColor3f(0, 1, 0);
	         
	         gl.glTexCoord2f(2.0f/3.0f, 2.0f/4.0f);		gl.glVertex3f(0, 0, 0); //bottom left
	         gl.glTexCoord2f(3.0f/3.0f, 2.0f/4.0f);		gl.glVertex3f(0, 1, 0);//bottom right
	         gl.glTexCoord2f(3.0f/3.0f, 1.0f/4.0f);		gl.glVertex3f(0, 1, 1); //top right
	         gl.glTexCoord2f(2.0f/3.0f, 1.0f/4.0f);		gl.glVertex3f(0, 0, 1);//top left
	   
	         
	         // left plane
	         gl.glNormal3f(1,  0, 0);
	         gl.glColor3f(0, 1, 0);
	         	         
	         gl.glTexCoord2f(1.0f/3.0f, 2.0f/4.0f);		gl.glVertex3f(1, 0, 0);//bottom right
	         gl.glTexCoord2f(0.0f/3.0f, 2.0f/4.0f);		gl.glVertex3f(1, 1, 0);//bottom left
	         gl.glTexCoord2f(0.0f/3.0f, 1.0f/4.0f);		gl.glVertex3f(1, 1, 1); //top left
	         gl.glTexCoord2f(1.0f/3.0f, 1.0f/4.0f);		gl.glVertex3f(1, 0, 1);//top right
	        
	         gl.glEnd(); 
	    }
		
		public void display(GLAutoDrawable gLDrawable) {
			this.move();//updates the variables from movement 
			final GL2 gl = gLDrawable.getGL().getGL2();

			gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

	        gl.glPushMatrix();//new matrix for gluLookAt
	         
	        glu.gluLookAt(	x, y, z, //sets position of the eye
	        		 		lookx+x, looky+y, lookz + z,//looks at look x,y,z with the displacement of where the eye is x,y,z 
	        		 		0, 1, 0);
	         
	        gl.glPushMatrix();//new building matrix
	        gl.glScalef(10.0f, 15.0f, 10.0f);//scales the building
	        gl.glTranslatef(3.0f, -0.1f, 3.0f);//moves the building
	        drawBuilding(gl);//draws the building 
	        gl.glPopMatrix();//gets rid of building matrix
	        
	        gl.glPushMatrix();//new ground matrix	         
	        gl.glTranslatef(0.0f, -3.0f, 0.0f);//moves the ground
	        drawGround(gl);//draws the ground
	        gl.glPopMatrix();//gets rid of the ground matrix
	        
	        gl.glPushMatrix();//sky box matrix
	        gl.glTranslatef(x, 0, z);//translates the sky box with your movement 
	        gl.glScalef(2000, 2000, 2000);//scales the sky box
	        gl.glTranslatef(-0.5f, -0.1f, -0.5f);//moves the sky box so that the x,z is centered at 0,0 and the y is just below the ground
	        drawSkyBox(gl);//draws the sky box
	        gl.glPopMatrix();//gets rid of sky box matrix
	        
	        gl.glPopMatrix();//gets rid of look at matrix 
	        	                  
	        gl.glMatrixMode(GL2.GL_PROJECTION);//start projection mode
	        gl.glPushMatrix(); //new matrix for projection
	         
	        glu.gluOrtho2D(0, windowWidth, 0, windowHeight);
	        gl.glRasterPos2f(windowWidth/2, windowHeight/2);
	        
	        gl.glPopMatrix();//gets rid of projection matrix
	        //draw the HUD
	        gl.glPushMatrix();//new matrix for HUD
	        String textx = "X-Pos: " + x;
	        String texty = "Y-Pos: " + y;
	        String textz = "Z-Pos: " + z;

	        TextRenderer renderer = new TextRenderer(new Font("Arial", Font.PLAIN, 30), true, true);//load the renderer with the font
	        renderer.beginRendering(1000, 1000);//renderer to draw the HUD
	         
	        renderer.setColor(0.0f, 0.0f, 1.0f, 1);//blue text
	         
	        renderer.draw(textx, 0, 80);//displays x-position
	        renderer.draw(texty, 0, 40);//displays y-position
	        renderer.draw(textz, 0, 0);//displays z-position
	         
	        renderer.flush(); //empties renderer
	        renderer.endRendering();//stops rendering
	        gl.glPopMatrix();//gets rid of HUD matrix
		}
		public void move(){
			if(jump){//in the air
				if(y>5)//you're actually in the air
					y-=.25;
				else{//you just landed
					jump=false;//you're back on the ground
					y=5;//resets y to the correct value if for some reason it went below 5
				}
			//	return;//can't move other directions while in the air
			}
			if (d){//move right
				x +=  Math.sin(Math.toRadians( 3*rot ) + (Math.PI/2)) * fraction;
				z += -Math.cos(Math.toRadians( 3*rot ) + (Math.PI/2)) * fraction;
			}
			
			if (a){//move left
				x -=  Math.sin(Math.toRadians( 3*rot ) + (Math.PI/2)) * fraction;
				z -= -Math.cos(Math.toRadians( 3*rot ) + (Math.PI/2)) * fraction;
			}
			
			if (w){//move forward
				x += lookx * fraction;
				z += lookz * fraction;
			}
			
			if(s){//move back
				x -= lookx * fraction;
				z -= lookz * fraction;
			}
			
		}
		public void keyTyped(KeyEvent e) {
		    char key= e.getKeyChar();
			if (key == KeyEvent.VK_ESCAPE )
				System.exit(0);
			if (key == ' ' && !jump){//can only jump once at a time
				y+=10;
				jump=true;
			}
			if (key == 'w')
				w=true;
			if (key == 's')
				s=true;
			if (key == 'a')
				a=true;
			if(key == 'd')
				d=true;
		}
		public void keyReleased(KeyEvent e) {
			char key= e.getKeyChar();
			if (key == 'w')
				w=false;
			if (key == 's')
				s=false;
			if (key == 'a')
				a=false;
			if(key == 'd')
				d=false;
		}
		public void mouseDragged(MouseEvent e) {
			
		}
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//set initial mouse positions 
			mouseX0 = e.getX();
			mouseY0 = e.getY();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

	@Override
		public void mouseMoved(MouseEvent e) {
			rot += (e.getX()-mouseX0)*0.5;		
			rotX += (e.getY() - mouseY0)*0.5;
		
			lookx = (float) Math.sin(Math.toRadians( 3*rot ) );
			looky = -rotX/15;
			lookz = (float) -Math.cos(Math.toRadians( 3*rot ) );
				
			mouseX0 = e.getX(); 
			mouseY0 = e.getY(); 
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void keyPressed(KeyEvent e) {
			
		}
		

		@Override
		public void dispose(GLAutoDrawable arg0) {
			// TODO Auto-generated method stub
			
		}
		
}



