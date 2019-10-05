(AGAIN)

@SCREEN

D=A

@addr

M=D     //addr=16384

@8192

D=A

@n

M=D      //n=8192

@i

M=0      //i=0

@KBD

D=M


@BLACK       //blakc orwhite

D;JGT

@WHITE

D;JEQ         //black or white

(BLACK)

@i

D=M

@n

D=D-M

@END

D;JEQ

@addr

A=M

M=-1

@i

M=M+1

@1

D=A

@addr

M=D+M //addr=addr+1

@BLACK

0;JMP

(WHITE)

@i

D=M

@n

D=D-M

@END

D;JEQ

@addr

A=M

M=0

@i

M=M+1

@1

D=A

@addr

M=D+M

@WHITE

0;JMP

(END)

@AGAIN

0;JMP
