// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.

//
//R0=RAM[0]
//R1=RAM[1]
//result=RAM[2]
//
//for(i=R1; i<=0; i--){
//result= R0 + R0;
//}
//
//END



@R2          
M=0

@R1         //R1=count
D=M
@count
M=D

(LOOP)
@count
D=M
@END
D;JEQ

@R0  
D=M 
@R2
M=D+M        //R2 = R2+R0

@count
M=M-1       // count-- 

@LOOP
0;JMP

(END)
0;JMP






