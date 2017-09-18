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
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.awt.TextRenderer;

public class JoglEventListener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

	float rot, rotX, x = 0, y = 5f, z = 10f, lookx = 0, looky = 0, lookz = 1, BallX = x, BallY = y, BallZ = z,
			force = 0, polex=0;
	boolean t=false, a = false, s = false, d = false, w = false, left = false, right = false, up=false, down=false, space = false, moving=false, poleDir=false, replay=false, speed1=false, speed2=false, speed3=false;// keys
																									// for
																									// movement
	boolean win2=false, win = false, lose = false, spaceJamOn = true; // win/ ose conditions

	float fraction = 0.5f;
	ArrayList<Ball> Balls;
	ArrayList<float[]> Rim=new ArrayList<float[]>(), BackBoard=new ArrayList<float[]>();
	Ball prev, replayBall;

	/*
	 * Custom variables for mouse drag operations
	 */
	int windowWidth, windowHeight;
	float orthoX = 40;
	double rtMat[] = new double[16];
	int mouseX0, mouseY0;
	// GLUquadric earth;
	int texID[] = new int[4];

	// float[] lightPos = { 10,10,10,1 }; // light position

	private GLU glu = new GLU();

	public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) {
	}

	/**
	 * Called by the drawable immediately after the OpenGL context is
	 * initialized for the first time. Can be used to perform one-time OpenGL
	 * initialization such as setup of lights and display lists.
	 * 
	 * @param gLDrawable
	 *            The GLAutoDrawable object.
	 */
	public void init(GLAutoDrawable gLDrawable) {
		GL2 gl = gLDrawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
		gl.glEnable(GL.GL_DEPTH_TEST); // Enables Depth Testing

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, rtMat, 0);
		gl.glEnable(GL2.GL_RESCALE_NORMAL);

		gl.glEnable(gl.GL_LIGHT0);// enable the light
		float lightPos[] = new float[] { 0, 0, 0, 1f };// set pos to be 0,0,0
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, lightPos, 0);
		float diff[] = new float[] { 1f, 1f, 1f, 1f };// full intensity
														// white/yellow light
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, diff, 0);// make the light

		// create Balls and add the first ball
		Balls = new ArrayList();
		Balls.add(new Ball(x, y - 2f, z + 3.5f, 1));

		// load an image;
		try {

			gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
			// load skybox texture map
			BufferedImage aImage = ImageIO.read(new File("map.jpg"));
			ByteBuffer buf = ByteBuffer.wrap(((DataBufferByte) aImage.getRaster().getDataBuffer()).getData());

			gl.glGenTextures(3, texID, 0);
			gl.glBindTexture(GL.GL_TEXTURE_2D, texID[0]);

			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, aImage.getWidth(), aImage.getHeight(), 0, GL2.GL_BGR,
					GL.GL_UNSIGNED_BYTE, buf);

			// gl.glEnable(GL.GL_TEXTURE_2D);

			gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
			gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);

			gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);

			// load ground texture map
			aImage = ImageIO.read(new File("ground2.jpg"));
			buf = ByteBuffer.wrap(((DataBufferByte) aImage.getRaster().getDataBuffer()).getData());

			// gl.glGenTextures(2, texID, 0);
			gl.glBindTexture(GL.GL_TEXTURE_2D, texID[1]);

			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, aImage.getWidth(), aImage.getHeight(), 0, GL2.GL_BGR,
					GL.GL_UNSIGNED_BYTE, buf);

			// gl.glEnable(GL.GL_TEXTURE_2D);

			gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
			gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);

			// load building texture map
			aImage = ImageIO.read(new File("building.jpg"));
			buf = ByteBuffer.wrap(((DataBufferByte) aImage.getRaster().getDataBuffer()).getData());

			// gl.glGenTextures(2, texID, 0);
			gl.glBindTexture(GL.GL_TEXTURE_2D, texID[2]);

			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, aImage.getWidth(), aImage.getHeight(), 0, GL2.GL_BGR,
					GL.GL_UNSIGNED_BYTE, buf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// goes inside display - but probably should be moved elsewhere

	}

	// make the sounds:
	public void MakeSound() // throws UnsupportedAudioFileException, Exception
	{
		try {
			// MakeSound();

			if (spaceJamOn) {
				File soundFile = new File("Space_Jam_Theme_Song.wav");
				AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);

				DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
				Clip clip = (Clip) AudioSystem.getLine(info);
				clip.open(sound);

				clip.start();
				spaceJamOn = false;
			}

			if (win) {
				File soundFile = new File("Cheering.wav");
				AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);

				DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
				Clip clip = (Clip) AudioSystem.getLine(info);
				clip.open(sound);

				clip.start();
				win = false;
			} else if (lose) {
				File soundFile = new File("womp-womp.wav");
				AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);

				DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
				Clip clip = (Clip) AudioSystem.getLine(info);
				clip.open(sound);

				clip.start();
				lose = false;
			}
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
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
		// gl.glOrtho(-orthoX*0.5, orthoX*0.5, -orthoX*0.5*height/width,
		// orthoX*0.5*height/width, -100, 100);
		glu.gluPerspective(65.0f, h, 0.1, 4001.0);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		// glu.gluLookAt(0, 0, 6, 0, 0, 0, 0, 1, 0);
	}

	public void drawGround(final GL2 gl) {
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBindTexture(GL.GL_TEXTURE_2D, texID[1]);// load the ground texture
		gl.glBegin(GL2.GL_QUADS);// start drawing the plane

		gl.glNormal3f(0, -1, 0);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(-5000, 1, -5000);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(-5000, 1, 5000);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(5000, 1, 5000);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(5000, 1, -5000);

		gl.glEnd();
		gl.glDisable(GL.GL_TEXTURE_2D);
	}

	public void drawPole(final GL2 gl,float polex) {
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBindTexture(GL.GL_TEXTURE_2D, texID[2]);// load building texture
		gl.glBegin(GL2.GL_QUADS);

		// front plane
		gl.glNormal3f(0, 0, 1);
		gl.glColor3f(1, 1, 1);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(0, 0, 1);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(1, 0, 1);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(1, 1, 1);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(0, 1, 1);

		// back plane
		gl.glNormal3f(0, 0, -1);
		gl.glColor3f(1, 1, 1);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(0, 0, 0);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(1, 0, 0);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(1, 1, 0);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(0, 1, 0);

		// left plane
		gl.glNormal3f(-1, 0, 0);
		gl.glColor3f(1, 1, 1);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(0, 0, 0);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(0, 1, 0);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(0, 1, 1);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(0, 0, 1);

		// right plane
		gl.glNormal3f(1, 0, 0);
		gl.glColor3f(1, 1, 1);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(1, 0, 0);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(1, 1, 0);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(1, 1, 1);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(1, 0, 1);

		// on the XZ plane,
		// up plane;
		gl.glNormal3f(0, 1, 0);
		gl.glColor3f(1, 1, 1);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(0, 1, 0);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(1, 1, 0);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(1, 1, 1);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(0, 1, 1);

		// down plane;
		gl.glNormal3f(0, -1, 0);
		gl.glColor3f(1, 1, 1);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(0, 0, 0);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(1, 0, 0);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(1, 0, 1);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(0, 0, 1);

		// Back board
		BackBoard = new ArrayList();
		gl.glColor3f(1, 1, 1);
		float [] bp = {-4.8f + .5f, 1, 0};
		float [] bp2= { 4.8f + .5f, 1, 0};
		float [] bp3 = {4.8f + .5f, 2.97f / 7 + 1, 0};
		float [] bp4 = {-4.8f + .5f, 2.97f / 7 + 1, 0};
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(bp[0],bp[1],bp[2]);// bottom left
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(bp2[0],bp2[1],bp2[2]);// bottom right
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(bp3[0],bp3[1],bp3[2]); // top right
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(bp4[0],bp4[1],bp4[2]);// top left
		
		bp[0] = bp[0]+polex;
		bp[1] = (bp[1]-.1f)*15f;
		bp[2] = bp[2]+40f;
		BackBoard.add(bp);
		bp2[0] = bp2[0]+polex;
		bp2[1] = (bp2[1]-.1f)*15f;
		bp2[2] = bp2[2]+40f;
		BackBoard.add(bp2);
		bp3[0] = bp3[0]+polex;
		bp3[1] = (bp3[1]-.1f)*15f;
		bp3[2] = bp3[2]+40f;
		BackBoard.add(bp3);
		bp4[0] = bp4[0]+polex;
		bp4[1] = (bp4[1]-.1f)*15f;
		bp4[2] = bp4[2]+40f;
		BackBoard.add(bp4);
		
		gl.glEnd();

		gl.glDisable(GL.GL_TEXTURE_2D);
		// hoop
		gl.glBegin(gl.GL_LINE_STRIP);
		gl.glColor3f(1f, 0f, 0f);
		Rim = new ArrayList();
		for (double y = .88; y <= 1; y += .01) {
			for (double r = 1.75; r <= 2; r += .01) {
				for (int i = 0; i < 360; i++) {
					float [] p = {(float)(r * Math.sin(i * Math.PI / 180) + .5),(float)(y / 15 + 1), (float)(r * Math.cos(i * Math.PI / 180) - 2)};
					gl.glVertex3f(p[0], p[1], p[2]);
					p[0] = p[0]+polex;
					p[1] = (p[1]-.1f)*15f;
					p[2] = p[2]+40f;
					Rim.add(p);
				}
			}
		}
		gl.glEnd();
		// netting
		gl.glBegin(gl.GL_LINES);
		gl.glColor3f(1f, 1f, 1f);
		for (int i = 0; i < 360; i += 10) {
			// one direction
			gl.glVertex3d(2 * Math.sin(i * Math.PI / 180) + .5, .88 / 15 + 1, 2 * Math.cos(i * Math.PI / 180) - 2);
			gl.glVertex3d(1.25 * Math.sin(i * Math.PI / 180 + Math.PI / 3) + .5, .88 / 15 + 1 - .25,
					1.25 * Math.cos(i * Math.PI / 180 + Math.PI / 3) - 2);
			// other direction
			gl.glVertex3d(2 * Math.sin(i * Math.PI / 180 + Math.PI / 3) + .5, .88 / 15 + 1,
					2 * Math.cos(i * Math.PI / 180 + Math.PI / 3) - 2);
			gl.glVertex3d(1.25 * Math.sin(i * Math.PI / 180) + .5, .88 / 15 + 1 - .25,
					1.25 * Math.cos(i * Math.PI / 180) - 2);

			// bottom loop
			gl.glVertex3d(1.25 * Math.sin(i * Math.PI / 180 + Math.PI / 3) + .5, .88 / 15 + 1 - .25,
					1.25 * Math.cos(i * Math.PI / 180 + Math.PI / 3) - 2);
			gl.glVertex3d(1.25 * Math.sin(i * Math.PI / 180) + .5, .88 / 15 + 1 - .25,
					1.25 * Math.cos(i * Math.PI / 180) - 2);
		}
		gl.glEnd();

		gl.glEnable(GL.GL_TEXTURE_2D);
	}

	public void drawSkyBox(final GL2 gl) {
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBindTexture(GL.GL_TEXTURE_2D, texID[0]);// load the texture for the
														// skybox
		gl.glBegin(GL2.GL_QUADS);// start drawing the faces of the cube

		// front plane
		gl.glNormal3f(0, 0, 1);
		gl.glColor3f(1, 0, 0);

		gl.glTexCoord2f(2.0f / 3.0f, 1.0f / 4.0f);
		gl.glVertex3f(0, 0, 1);// bottom right
		gl.glTexCoord2f(1.0f / 3.0f, 1.0f / 4.0f);
		gl.glVertex3f(1, 0, 1);// bottom left
		gl.glTexCoord2f(1.0f / 3.0f, 0f);
		gl.glVertex3f(1, 1, 1); // top left
		gl.glTexCoord2f(2.0f / 3.0f, 0.0f);
		gl.glVertex3f(0, 1, 1);// top right

		// up plane;
		gl.glNormal3f(0, 1, 0);
		gl.glColor3f(0, 0, 1);

		gl.glTexCoord2f(2.0f / 3.0f, 3.0f / 4.0f);
		gl.glVertex3f(0, 1, 0); // top right
		gl.glTexCoord2f(1.0f / 3.0f, 3.0f / 4.0f);
		gl.glVertex3f(1, 1, 0);// top left
		gl.glTexCoord2f(1.0f / 3.0f, 1f);
		gl.glVertex3f(1, 1, 1); // bottom left
		gl.glTexCoord2f(2.0f / 3.0f, 1f);
		gl.glVertex3f(0, 1, 1);// bottom right

		// back plane
		gl.glNormal3f(0, 0, -1);
		gl.glColor3f(1, 0, 0);

		gl.glTexCoord2f(2.0f / 3.0f, 2.0f / 4.0f);
		gl.glVertex3f(0, 0, 0); // top right
		gl.glTexCoord2f(1.0f / 3.0f, 2.0f / 4.0f);
		gl.glVertex3f(1, 0, 0);// top left
		gl.glTexCoord2f(1.0f / 3.0f, 3.0f / 4.0f);
		gl.glVertex3f(1, 1, 0);// bottom left
		gl.glTexCoord2f(2.0f / 3.0f, 3.0f / 4.0f);
		gl.glVertex3f(0, 1, 0); // bottom right

		// down plane;
		gl.glNormal3f(0, -1, 0);
		gl.glColor3f(0, 0, 1);

		gl.glTexCoord2f(2.0f / 3.0f, 2.0f / 4.0f);
		gl.glVertex3f(0, 0, 0); // bottom right
		gl.glTexCoord2f(1.0f / 3.0f, 2.0f / 4.0f);
		gl.glVertex3f(1, 0, 0);// bottom left
		gl.glTexCoord2f(1.0f / 3.0f, 1.0f / 4.0f);
		gl.glVertex3f(1, 0, 1);// top left
		gl.glTexCoord2f(2.0f / 3.0f, 1.0f / 4.0f);
		gl.glVertex3f(0, 0, 1);// top right

		// right plane
		gl.glNormal3f(-1, 0, 0);
		gl.glColor3f(0, 1, 0);

		gl.glTexCoord2f(2.0f / 3.0f, 2.0f / 4.0f);
		gl.glVertex3f(0, 0, 0); // bottom left
		gl.glTexCoord2f(3.0f / 3.0f, 2.0f / 4.0f);
		gl.glVertex3f(0, 1, 0);// bottom right
		gl.glTexCoord2f(3.0f / 3.0f, 1.0f / 4.0f);
		gl.glVertex3f(0, 1, 1); // top right
		gl.glTexCoord2f(2.0f / 3.0f, 1.0f / 4.0f);
		gl.glVertex3f(0, 0, 1);// top left

		// left plane
		gl.glNormal3f(1, 0, 0);
		gl.glColor3f(0, 1, 0);

		gl.glTexCoord2f(1.0f / 3.0f, 2.0f / 4.0f);
		gl.glVertex3f(1, 0, 0);// bottom right
		gl.glTexCoord2f(0.0f / 3.0f, 2.0f / 4.0f);
		gl.glVertex3f(1, 1, 0);// bottom left
		gl.glTexCoord2f(0.0f / 3.0f, 1.0f / 4.0f);
		gl.glVertex3f(1, 1, 1); // top left
		gl.glTexCoord2f(1.0f / 3.0f, 1.0f / 4.0f);
		gl.glVertex3f(1, 0, 1);// top right

		gl.glEnd();
		gl.glDisable(GL.GL_TEXTURE_2D);
	}

	public void drawCourt(final GL2 gl) {
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBindTexture(GL.GL_TEXTURE_2D, texID[2]);// load building texture
		// draw the court
		gl.glBegin(gl.GL_QUADS);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3d(.5 - 22.5, 0, 1);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3d(.5 + 22.5, 0, 1);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3d(.5 + 22.5, 0, -96);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3d(.5 - 22.5, 0, -96);

		gl.glEnd();

	}
	
	public void display(GLAutoDrawable gLDrawable) {
		MakeSound();
		this.move();// updates the variables from movement
		final GL2 gl = gLDrawable.getGL().getGL2();

		gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();// new matrix for gluLookAt

		glu.gluLookAt(x, y, z, // sets position of the eye
				lookx + x, looky + y, lookz + z, // looks at look x,y,z with the
													// displacement of where the
													// eye is x,y,z
				0, 1, 0);
		if(!replay){
			int numballs = 5;
			if (Balls.size() > numballs) {// can only have x # of balls on the screen
				for (int i = 0; i < Balls.size() - numballs; i++) {
					Balls.remove(0);// pop first
				}
			}
			for (int i = 0; i < Balls.size(); i++) {
				gl.glPushMatrix();
				if (i == Balls.size() - 1) {// the ball you're holding
					Balls.get(i).stop();
				}
				Balls.get(i).draw(gl);
				
				if(Balls.get(i).checkHitBoxes(Rim)){
					System.out.println("HIT THE RIM");
					Balls.get(i).setsx(Balls.get(i).getsx()*-1);
					Balls.get(i).setsy(Balls.get(i).getsy()*-2);
				}
				else{
				//	System.out.println(Balls.get(i).getx()+"\t"+Balls.get(i).gety()+"\t"+Balls.get(i).getz());
					if(Balls.get(i).getx() == 0.5)
						if(Balls.get(i).gety() == 14.5)
							if(Balls.get(i).getz() == 38){
								win=true;
								win2=true;
								}
				}
				if(i== Balls.size()-2 && Balls.get(i).gety() < 8.0 && !win2 && Balls.get(i).getsy() <= 0){
					lose=true;
					win2=true;
				}
				
				if(Balls.get(i).checkHitBoxes(BackBoard)){
					Balls.get(i).setsx(Balls.get(i).getsx()*-1);
					Balls.get(i).setsz(Balls.get(i).getsz()*-1);
				}
				
				gl.glPopMatrix();

			}
		}
		else if(prev!=null){//replay mode
			
			gl.glPushMatrix();
			prev.draw(gl);
			gl.glPopMatrix();
			
			if(replayBall!=null){
				gl.glPushMatrix();
				replayBall.draw(gl);
				//replayBall.checkHitBoxes(Rim);
				if(replayBall.checkHitBoxes(Rim)){
					replayBall.setsx(replayBall.getsx()*-1);
					replayBall.setsy(replayBall.getsy()*-1);
				}
				if(replayBall.checkHitBoxes(BackBoard)){
					replayBall.setsx(replayBall.getsx()*-1);
					replayBall.setsz(replayBall.getsz()*-1);
				}
				gl.glPopMatrix();
			}
			
		}
		
		gl.glPushMatrix();// new building matrix
		gl.glScalef(1.0f, 15.0f, 1.0f);// scales the pole 
		if(moving)
			if(poleDir)
				if(polex+1 >= 22)
					poleDir=!poleDir;
				else
					polex+=1;
			else
				if(polex-1 <= -22)
					poleDir=!poleDir;
				else
					polex-=1;	
		else
			polex = 0;
		gl.glTranslatef(polex, -0.1f, 40.0f);// moves the pole
		drawPole(gl,polex);// draws the building
		gl.glPopMatrix();// gets rid of building matrix

		gl.glPushMatrix();// new building matrix
		gl.glTranslatef(0.0f, -0.1f, 40.0f);// moves the pole
		drawCourt(gl);// draws the building
		gl.glPopMatrix();// gets rid of building matrix

		gl.glPushMatrix();// new ground matrix
		gl.glTranslatef(0.0f, -3.0f, 0.0f);// moves the ground
		drawGround(gl);// draws the ground
		gl.glPopMatrix();// gets rid of the ground matrix

		gl.glPushMatrix();// sky box matrix
		gl.glTranslatef(x, 0, z);// translates the sky box with your movement
		gl.glScalef(2000, 2000, 2000);// scales the sky box
		gl.glTranslatef(-0.5f, -0.1f, -0.5f);// moves the sky box so that the
												// x,z is centered at 0,0 and
												// the y is just below the
												// ground
		drawSkyBox(gl);// draws the sky box
		gl.glPopMatrix();// gets rid of sky box matrix

		gl.glPopMatrix();// gets rid of look at matrix

		gl.glMatrixMode(GL2.GL_PROJECTION);// start projection mode
		gl.glPushMatrix(); // new matrix for projection

		glu.gluOrtho2D(0, windowWidth, 0, windowHeight);
		gl.glRasterPos2f(windowWidth / 2, windowHeight / 2);

		gl.glPopMatrix();// gets rid of projection matrix
		// draw the HUD
		gl.glPushMatrix();// new matrix for HUD

		TextRenderer renderer;
	
		if(!replay){
			renderer = new TextRenderer(new Font("Arial", Font.PLAIN, 90), true, true);
			renderer.beginRendering(1000, 1000);// renderer to draw the HUD
			String textf = "Force: " + force;
			renderer.setColor(force/100.0f, 1.0f-force/100.0f, 0.0f, 1);//text color
			renderer.draw(textf, 0, 0);// displays force
		}
		else{
			renderer = new TextRenderer(new Font("Arial", Font.PLAIN, 40), true, true);
			renderer.beginRendering(1000, 1000);// renderer to draw the HUD
			String textf = "Playback: \n1) 1x \n2) 1/4x \n3) 1/10x";
			renderer.setColor(1f, 1f, 1f, 1);//text color
			renderer.draw("Playback: ", 0, 120);// displays x-position
			if(speed1)
				renderer.setColor(0f, 0f, 0f, 1);//text color
			else
				renderer.setColor(1f, 1f, 1f, 1);//text color
			renderer.draw("1) 1x", 0, 80);// displays y-position
			if(speed2)
				renderer.setColor(0f, 0f, 0f, 1);//text color
			else
				renderer.setColor(1f, 1f, 1f, 1);//text color
			renderer.draw("2) 1/4x", 0, 40);// displays z-position
			if(speed3)
				renderer.setColor(0f, 0f, 0f, 1);//text color
			else
				renderer.setColor(1f, 1f, 1f, 1);//text color
			renderer.draw("3) 1/10x", 0, 0);// displays force
		}
			
		renderer.flush(); // empties renderer
		renderer.endRendering();// stops rendering
		gl.glPopMatrix();// gets rid of HUD matrix
	}

	public void move() {
		if(!replay){
			Ball B = Balls.get(Balls.size() - 1);
			if (w)// && B.gety() + .5f < 12f)
				B.sety(B.gety() + .5);
			if (s && B.gety() - .5 > 0)
				B.sety(B.gety() - .5);
			if (d && B.getx() - .5 > -22)
				B.setx(B.getx() - .5);
			if (a && B.getx() - .5 < 22)
				B.setx(B.getx() + .5);
		
			if (space) {
				if (force + 1 > 100) {// you fail a shot
					space = false;
					force = 0;
					Balls.get(Balls.size() - 1).start();
					Balls.get(Balls.size() - 1).setSpeed(0, 0, 0);
					Balls.add(new Ball(x, y - 2f, z + 3.5f, 1));
					Balls.get(Balls.size() - 1).stop();
				} else
					force += 1;
			}
			// only side to side movement for now
			if (right && x - 1 >= -22) {// move right
				x -= 1;
			}	

			if (left && x + 1 <= 22.5) {// move left
				x += 1;
			}
		}
		else{//replay mode
			if (left) {// move left
				x += 1;
			}
			if (right) {// move right
				x -= 1;
			}	
			if (up) {// move left
				z += 1;
			}
			if (down) {// move right
				z -= 1;
			}
			
		}
	}

	public void keyTyped(KeyEvent e) {
		char key = e.getKeyChar();
		// System.out.println(key);
		if (key == KeyEvent.VK_ESCAPE)
			System.exit(0);
		if(key == 't')
			t=!t;
		if(key == 'm')
			moving=!moving;
		if(key == 'r'){
			speed1=false; speed2=false; speed3=false;
			replayBall=null;
			replay=!replay;
			z=10f;//sets z back to its default value
			x=0;//resets x position
		}
		Ball B = Balls.get(Balls.size() - 1);
		if (key == 'e')
			B.setz(B.getz() + .5f);
		if (key == 'q')
			B.setz(B.getz() - .5f);
		if (key == ' ') {
			space = true;
		}
		if (key == 'w')
			w = true;
		if (key == 's')
			s = true;
		if (key == 'a')
			a = true;
		if (key == 'd')
			d = true;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT) 
			right = true;
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT)
			left = true;
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP)
			up = true;
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN)
			down = true;
		
		if(replay && (key == '1' || key == '2' || key == '3')){
			speed1=false; speed2=false; speed3=false;
			replayBall =  new Ball(prev.getx(),prev.gety(),prev.getz(),prev.getsx(),prev.getsy(),prev.getsz(),prev.getr());
			replayBall.setColor(prev.getColor());
			if(key == '1'){
				speed1=true;
				replayBall.playbackSpeed(1);
			}
			if(key == '2'){
				speed2=true;
				replayBall.playbackSpeed(1/4.0);
			}
			if(key == '3'){
				speed3=true;
				replayBall.playbackSpeed(1/10.0);	
			}
			replayBall.start();
		}
	}

	public void keyReleased(KeyEvent e) {
		char key = e.getKeyChar();
		if (key == 'w')
			w = false;
		if (key == 's')
			s = false;
		if (key == 'a')
			a = false;
		if (key == 'd')
			d = false;
		if (key == ' ' && space && !replay) {
			space = false;
			Balls.get(Balls.size() - 1).start();
			Balls.get(Balls.size() - 1).setSpeed(0, force / 50f, force / 50f);
			prev = new Ball(Balls.get(Balls.size() - 1).getx(),		Balls.get(Balls.size() - 1).gety(),		Balls.get(Balls.size() - 1).getz(), 
							Balls.get(Balls.size() - 1).getsx(),	Balls.get(Balls.size() - 1).getsy(),	Balls.get(Balls.size() - 1).getsz(),
							Balls.get(Balls.size() - 1).getr());
			prev.setColor(Balls.get(Balls.size() - 1).getColor());
			Balls.add(new Ball(x, y - 2f, z + 3.5f, 1));
			Balls.get(Balls.size() - 1).stop();
			force = 0;// resets the force
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT)
			right = false;
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_LEFT)
			left = false;
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP)
			up = false;
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN)
			down = false;
	}

	public void mouseDragged(MouseEvent e) {
		rot += (e.getX() - mouseX0) * 0.5;
		rotX += (e.getY() - mouseY0) * 0.5;

		lookx = (float) Math.sin(Math.toRadians(3 * rot));
		looky = -rotX / 15;
		lookz = (float) -Math.cos(Math.toRadians(3 * rot));

		mouseX0 = e.getX();
		mouseY0 = e.getY();
	}

	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// set initial mouse positions
		mouseX0 = e.getX();
		mouseY0 = e.getY();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		/*
		 * rot += (e.getX()-mouseX0)*0.5; rotX += (e.getY() - mouseY0)*0.5;
		 * 
		 * lookx = (float) Math.sin(Math.toRadians( 3*rot ) ); looky = -rotX/15;
		 * lookz = (float) -Math.cos(Math.toRadians( 3*rot ) );
		 * 
		 * mouseX0 = e.getX(); mouseY0 = e.getY();
		 */
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// for some reason these only work in keypressed not key typed
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT) 
			right = true;
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_LEFT)
			left = true;
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP)
			up = true;
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN)
			down = true;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

}
