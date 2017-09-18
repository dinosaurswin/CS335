Jacob Richardson
9/21/16

New functions:
 drawCircle(GL2 gl)- draws a circle onto the screen via the gl passed to it. It can be toggled between the shape of a single line circle and a filled circle made to look like a basketball. 
moveCircle()- moves a circle based on which keys are currently being pressed. W moves the circle up, A moves the circle left, D/F moves the circle right, S/Z move the circle down. 
drawUK(GL2 gl)- draws the image outlined by the index and vertex array out of white triangles, and also adds a black outline of them.
Updated existing functions:
keyTyped(KeyEvent e)- sets booleans of the keys being pressed to true, this is better than doing the actual transformations in this function so that multiple keys can affect the ball at the same time. (the toggle key t is implemented here because it’s an on/off switch so I can be placed here safely).  
keyReleased(KeyEvent e)- sets the Booleans of the keys being pressed to be false to stop these transformations form continuing to happen. 
display(GLAutoDrawable arg0)- added function calls to drawCircle and drawUK

added new class variables-
double r- current radius of the circle
double j- used as the inner radius of the circle, so it can be 0 or r for a solid or outline respectively
double x- the magnitude of the current x displacement
double y- the magnitude of the current y displacement
double vel- the magnitude of how much the x/y will change in a single step
Boolean keys[]- array representing the keys currently being pressed in the form [w, a, s, d, z, f, o, p, =, -] which are the keys used as controls which can be simultaneously pressed 
