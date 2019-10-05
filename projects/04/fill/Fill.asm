// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.


(LOOP)
@8192
D=A
@Count
M=D 

@KBD
D=M
@NONZERO
D;JGT
@ZERO
D;JEQ    //jump to ZERO if key=0, continue to NONZERO otherwise

(NONZERO)
@Count
D=M
@LOOP
D;JEQ     //stop NONZERO loop in count=0

@Count
D=M
@SCREEN
A=A+D
A=A-1
M=-1      //turn pixels black

@Count
M=M-1
D=M
@NONZERO
0;JMP     //restart NONZERO loop with count-- and +1 black pixel



(ZERO)
@Count
D=M
@LOOP
D;JEQ   //stop ZERO loop if count=0

@Count
D=M
@SCREEN
A=A+D
A=A-1
M=0      //turn pixels white

@Count
M=M-1   
@ZERO
0;JMP    //restart ZERO loop with count-- and +1 pixel white




