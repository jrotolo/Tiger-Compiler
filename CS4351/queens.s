	.text
tigermain:
tigermain_framesize=64
	subu $sp tigermain_framesize
L50:
	sw $ra -24+tigermain_framesize($sp)
	sw $s0 -28+tigermain_framesize($sp)
	sw $s1 -32+tigermain_framesize($sp)
	sw $s2 -36+tigermain_framesize($sp)
	sw $s3 -40+tigermain_framesize($sp)
	sw $s4 -44+tigermain_framesize($sp)
	sw $s5 -48+tigermain_framesize($sp)
	sw $s6 -52+tigermain_framesize($sp)
	sw $s7 -56+tigermain_framesize($sp)
	sw $s8 -60+tigermain_framesize($sp)
	sw $a0 0+tigermain_framesize($sp)
	li $v0 8
	sw $v0 -4+tigermain_framesize($sp)
	add $v0 $sp -8+tigermain_framesize
	move $s0 $v0
	lw $v0 -4+tigermain_framesize($sp)
	move $a0 $v0
	move $a1 $0
	jal _initArray

	sw $v0 ($s0)
	add $v0 $sp -12+tigermain_framesize
	move $s0 $v0
	lw $v0 -4+tigermain_framesize($sp)
	move $a0 $v0
	move $a1 $0
	jal _initArray

	sw $v0 ($s0)
	add $v0 $sp -16+tigermain_framesize
	move $s0 $v0
	lw $v0 -4+tigermain_framesize($sp)
	lw $v1 -4+tigermain_framesize($sp)
	add $v0 $v0 $v1
	sub $v0 $v0 1
	move $a0 $v0
	move $a1 $0
	jal _initArray

	sw $v0 ($s0)
	add $v0 $sp -20+tigermain_framesize
	move $s0 $v0
	lw $v0 -4+tigermain_framesize($sp)
	lw $v1 -4+tigermain_framesize($sp)
	add $v0 $v0 $v1
	sub $v0 $v0 1
	move $a0 $v0
	move $a1 $0
	jal _initArray

	sw $v0 ($s0)
	addu $v0 $sp tigermain_framesize
	move $a0 $v0
	move $a1 $0
	jal tigermain.try.1
	lw $s8 -60+tigermain_framesize($sp)
	lw $s7 -56+tigermain_framesize($sp)
	lw $s6 -52+tigermain_framesize($sp)
	lw $s5 -48+tigermain_framesize($sp)
	lw $s4 -44+tigermain_framesize($sp)
	lw $s3 -40+tigermain_framesize($sp)
	lw $s2 -36+tigermain_framesize($sp)
	lw $s1 -32+tigermain_framesize($sp)
	lw $s0 -28+tigermain_framesize($sp)
	lw $ra -24+tigermain_framesize($sp)
	b L49
L49:

	addu $sp tigermain_framesize
	j $ra
	.text
tigermain.try.1:
tigermain.try.1_framesize=44
	subu $sp tigermain.try.1_framesize
L52:
	sw $ra -4+tigermain.try.1_framesize($sp)
	sw $s0 -8+tigermain.try.1_framesize($sp)
	sw $s1 -12+tigermain.try.1_framesize($sp)
	sw $s2 -16+tigermain.try.1_framesize($sp)
	sw $s3 -20+tigermain.try.1_framesize($sp)
	sw $s4 -24+tigermain.try.1_framesize($sp)
	sw $s5 -28+tigermain.try.1_framesize($sp)
	sw $s6 -32+tigermain.try.1_framesize($sp)
	sw $s7 -36+tigermain.try.1_framesize($sp)
	sw $s8 -40+tigermain.try.1_framesize($sp)
	sw $a0 0+tigermain.try.1_framesize($sp)
	move $s0 $a1
	lw $v1 0+tigermain.try.1_framesize($sp)
	lw $v1 -4($v1)
	beq $s0 $v1 L46
L47:
	move $s1 $0
	lw $v1 0+tigermain.try.1_framesize($sp)
	lw $v1 -4($v1)
	sub $v1 $v1 1
	move $s2 $v1
	ble $s1 $s2 L44
L14:
L48:
	lw $s8 -40+tigermain.try.1_framesize($sp)
	lw $s7 -36+tigermain.try.1_framesize($sp)
	lw $s6 -32+tigermain.try.1_framesize($sp)
	lw $s5 -28+tigermain.try.1_framesize($sp)
	lw $s4 -24+tigermain.try.1_framesize($sp)
	lw $s3 -20+tigermain.try.1_framesize($sp)
	lw $s2 -16+tigermain.try.1_framesize($sp)
	lw $s1 -12+tigermain.try.1_framesize($sp)
	lw $s0 -8+tigermain.try.1_framesize($sp)
	lw $ra -4+tigermain.try.1_framesize($sp)
	b L51
L46:
	lw $v0 0+tigermain.try.1_framesize($sp)
	move $a0 $v0
	jal tigermain.printboard.0
	b L48
L44:
	lw $v1 0+tigermain.try.1_framesize($sp)
	lw $v1 -8($v1)
	move $t0 $s1
	blt $t0 0 _BADSUB
L15:
	lw $t1 -4($v1)
	bgt $t0 $t1 _BADSUB
L16:
	sll $t0 $t0 2
	add $v1 $v1 $t0
	lw $v1 ($v1)
	beq $v1 0 L19
L42:
L43:
	bge $s1 $s2 L14
L45:
	add $v1 $s1 1
	move $s1 $v1
	b L44
L19:
	lw $v1 0+tigermain.try.1_framesize($sp)
	lw $v1 -16($v1)
	add $t0 $s1 $s0

	blt $t0 0 _BADSUB
L17:
	lw $t1 -4($v1)
	bgt $t0 $t1 _BADSUB
L18:
	sll $t0 $t0 2
	add $v1 $v1 $t0
	lw $v1 ($v1)
	bne $v1 0 L42
L24:
	lw $v1 0+tigermain.try.1_framesize($sp)
	lw $v1 -20($v1)
	add $t0 $s1 7
	sub $t0 $t0 $s0

	blt $t0 0 _BADSUB
L22:
	lw $t1 -4($v1)
	bgt $t0 $t1 _BADSUB
L23:
	sll $t0 $t0 2
	add $v1 $v1 $t0
	lw $v1 ($v1)
	bne $v1 0 L42
L41:
	lw $v0 0+tigermain.try.1_framesize($sp)
	lw $v0 -8($v0)
	move $v1 $s1
	blt $v1 0 _BADSUB
L27:
	lw $t0 -4($v0)
	bgt $v1 $t0 _BADSUB
L28:
	li $t0 1
	sll $v1 $v1 2
	add $v0 $v0 $v1
	sw $t0 ($v0)
	lw $v0 0+tigermain.try.1_framesize($sp)
	lw $v0 -16($v0)
	add $v1 $s1 $s0

	blt $v1 0 _BADSUB
L29:
	lw $t0 -4($v0)
	bgt $v1 $t0 _BADSUB
L30:
	li $t0 1
	sll $v1 $v1 2
	add $v0 $v0 $v1
	sw $t0 ($v0)
	lw $v0 0+tigermain.try.1_framesize($sp)
	lw $v0 -20($v0)
	add $v1 $s1 7
	sub $v1 $v1 $s0

	blt $v1 0 _BADSUB
L31:
	lw $t0 -4($v0)
	bgt $v1 $t0 _BADSUB
L32:
	li $t0 1
	sll $v1 $v1 2
	add $v0 $v0 $v1
	sw $t0 ($v0)
	lw $v0 0+tigermain.try.1_framesize($sp)
	lw $v0 -12($v0)
	move $v1 $s0
	blt $v1 0 _BADSUB
L33:
	lw $t0 -4($v0)
	bgt $v1 $t0 _BADSUB
L34:
	sll $v1 $v1 2
	add $v0 $v0 $v1
	sw $s1 ($v0)
	lw $v0 0+tigermain.try.1_framesize($sp)
	move $a0 $v0
	add $v1 $s0 1
	move $a1 $v1
	jal tigermain.try.1
	lw $v1 0+tigermain.try.1_framesize($sp)
	lw $v1 -8($v1)
	move $t0 $s1
	blt $t0 0 _BADSUB
L35:
	lw $t1 -4($v1)
	bgt $t0 $t1 _BADSUB
L36:
	sll $t0 $t0 2
	add $v1 $v1 $t0
	sw $0 ($v1)
	lw $v1 0+tigermain.try.1_framesize($sp)
	lw $v1 -16($v1)
	add $t0 $s1 $s0

	blt $t0 0 _BADSUB
L37:
	lw $t1 -4($v1)
	bgt $t0 $t1 _BADSUB
L38:
	sll $t0 $t0 2
	add $v1 $v1 $t0
	sw $0 ($v1)
	lw $v1 0+tigermain.try.1_framesize($sp)
	lw $v1 -20($v1)
	add $t0 $s1 7
	sub $t0 $t0 $s0

	blt $t0 0 _BADSUB
L39:
	lw $t1 -4($v1)
	bgt $t0 $t1 _BADSUB
L40:
	sll $t0 $t0 2
	add $v1 $v1 $t0
	sw $0 ($v1)
	b L43
L51:

	addu $sp tigermain.try.1_framesize
	j $ra
	.text
tigermain.printboard.0:
tigermain.printboard.0_framesize=40
	subu $sp tigermain.printboard.0_framesize
L54:
	sw $ra -4+tigermain.printboard.0_framesize($sp)
	sw $s0 -8+tigermain.printboard.0_framesize($sp)
	sw $s1 -12+tigermain.printboard.0_framesize($sp)
	sw $s2 -16+tigermain.printboard.0_framesize($sp)
	sw $s3 -20+tigermain.printboard.0_framesize($sp)
	sw $s4 -24+tigermain.printboard.0_framesize($sp)
	sw $s5 -28+tigermain.printboard.0_framesize($sp)
	sw $s6 -32+tigermain.printboard.0_framesize($sp)
	sw $s7 -36+tigermain.printboard.0_framesize($sp)
	sw $s8 -40+tigermain.printboard.0_framesize($sp)
	sw $a0 0+tigermain.printboard.0_framesize($sp)
	move $s0 $0
	lw $v0 0+tigermain.printboard.0_framesize($sp)
	lw $v0 -4($v0)
	sub $v0 $v0 1
	move $s1 $v0
	ble $s0 $s1 L12
L0:
	la $v0 L11
	move $a0 $v0
	jal _print
	lw $s8 -40+tigermain.printboard.0_framesize($sp)
	lw $s7 -36+tigermain.printboard.0_framesize($sp)
	lw $s6 -32+tigermain.printboard.0_framesize($sp)
	lw $s5 -28+tigermain.printboard.0_framesize($sp)
	lw $s4 -24+tigermain.printboard.0_framesize($sp)
	lw $s3 -20+tigermain.printboard.0_framesize($sp)
	lw $s2 -16+tigermain.printboard.0_framesize($sp)
	lw $s1 -12+tigermain.printboard.0_framesize($sp)
	lw $s0 -8+tigermain.printboard.0_framesize($sp)
	lw $ra -4+tigermain.printboard.0_framesize($sp)
	b L53
L12:
	move $s2 $0
	lw $v0 0+tigermain.printboard.0_framesize($sp)
	lw $v0 -4($v0)
	sub $v0 $v0 1
	move $s3 $v0
	ble $s2 $s3 L9
L1:
	la $v0 L11
	move $a0 $v0
	jal _print
	bge $s0 $s1 L0
L13:
	add $v0 $s0 1
	move $s0 $v0
	b L12
L9:
	lw $v0 0+tigermain.printboard.0_framesize($sp)
	lw $v0 -12($v0)
	move $v1 $s0
	blt $v1 0 _BADSUB
L2:
	lw $t0 -4($v0)
	bgt $v1 $t0 _BADSUB
L3:
	sll $v1 $v1 2
	add $v0 $v0 $v1
	lw $v0 ($v0)
	beq $v0 $s2 L6
L7:
	la $v0 L5

L8:
	move $a0 $v0
	jal _print
	bge $s2 $s3 L1
L10:
	add $v0 $s2 1
	move $s2 $v0
	b L9
L6:
	la $v0 L4

	b L8
L53:

	addu $sp tigermain.printboard.0_framesize
	j $ra
	.data
	.word 1
L11:	.asciiz	"\n"
	.data
	.word 2
L5:	.asciiz	" ."
	.data
	.word 2
L4:	.asciiz	" O"
