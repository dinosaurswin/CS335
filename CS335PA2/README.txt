Jacob Richardson
10/2/16

New functions:
multiply4X4(float[] one, float[] two, float[] ans)- used to return the product of one*two in ans; this only works for 4x4 matrices; its implemeted to make multiply transformation matrices together easier, specifically for the composite rotation matrix

Updated existing functions:
transform- transforms a set of vertices by the transformations (either finalized or temporary) defined by the class variables and returns them in the second array given to it
display- added call to the transformation function before the vertices are displayed
keyTyped- reads in the keys typed once to change the mode between scale/rotate/translate and also uses 0 to reset the transfrmations and focal length
keyPressed- reads in keys as theyre being held down and increases/decreases the focal length accordingly for g/h
mouseDragged- makes alterations to the temporary transformation matrix variables based on where the mouse is being moved to
mousePressed- sets the intial position of the mouse click and resets the variables needed to be used for temporary transformations 
mouseReleased- finalizes the temporary transformations into the appropritate variables and resets the temporary ones no longer being used 

added new class variables-
float tr11,tr12,tr13,tr21,tr22,tr23,tr31,tr32,tr33- used for the temporary composite rotation matrix, the same indexs as r11..r33 but t indicating their temporary nature for use while the rotation is still being determined by the mouse dragging
float dx,dy- temporary x and y translations, for use while the mouse is still being dragged to display what it would look like if released at the urrent position
s- used as the temporary scaling factor to be used while the mouse is still being dragged to display what the current scaling would look like
boolean scale,trans,rotate- booleans used to indicate what mode the program is in, to be changed with keystrokes 
boolean changing- used to indicade whther the mouse is currently being dragged, which would mean the temporary transformation matrix should be used father than the final one 