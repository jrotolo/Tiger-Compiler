# SPIM S20 MIPS simulator.
# Trap handler for spim including Tiger runtime functions.
#
# Portions Copyright (C) 1990-1995 James Larus, larus@cs.wisc.edu.
# ALL RIGHTS RESERVED.
#
# SPIM is distributed under the following conditions:
#
# You may make copies of SPIM for your own use and modify those copies.
#
# All copies of SPIM must retain my name and copyright notice.
#
# You may not sell SPIM or distributed SPIM in conjunction with a commerical
# product or service without the expressed written consent of James Larus.
#
# THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
# IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
# WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
# PURPOSE.
#

# $Header: /u/l/a/larus/Software/SPIM/RCS/trap.handler,v 1.23 1997/07/09 21:45:23 larus Exp $


# Define the exception handling code.  This must go first!

	.kdata
__m1_:	.asciiz "  Exception "
__m2_:	.asciiz " occurred and ignored\n"
__e0_:	.asciiz "  [Interrupt] "
__e1_:	.asciiz	""
__e2_:	.asciiz	""
__e3_:	.asciiz	""
__e4_:	.asciiz	"  [Unaligned address in inst/data fetch] "
__e5_:	.asciiz	"  [Unaligned address in store] "
__e6_:	.asciiz	"  [Bad address in text read] "
__e7_:	.asciiz	"  [Bad address in data/stack read] "
__e8_:	.asciiz	"  [Error in syscall] "
__e9_:	.asciiz	"  [Breakpoint] "
__e10_:	.asciiz	"  [Reserved instruction] "
__e11_:	.asciiz	""
__e12_:	.asciiz	"  [Arithmetic overflow] "
__e13_:	.asciiz	"  [Inexact floating point result] "
__e14_:	.asciiz	"  [Invalid floating point result] "
__e15_:	.asciiz	"  [Divide by 0] "
__e16_:	.asciiz	"  [Floating point overflow] "
__e17_:	.asciiz	"  [Floating point underflow] "
__excp:	.word __e0_,__e1_,__e2_,__e3_,__e4_,__e5_,__e6_,__e7_,__e8_,__e9_
	.word __e10_,__e11_,__e12_,__e13_,__e14_,__e15_,__e16_,__e17_
s1:	.word 0
s2:	.word 0

	.ktext 0x80000080
	.set noat
	# Because we are running in the kernel, we can use $k0/$k1 without
	# saving their old values.
	move $k1 $at	# Save $at
	.set at
	sw $v0 s1	# Not re-entrent and we can't trust $sp
	sw $a0 s2
	mfc0 $k0 $13	# Cause
        sgt $v0 $k0 0x44 # ignore interrupt exceptions
        bgtz $v0 ret
        addu $0 $0 0
	li $v0 4	# syscall 4 (print_str)
	la $a0 __m1_
	syscall
	li $v0 1	# syscall 1 (print_int)
        srl $a0 $k0 2	# shift Cause reg
	syscall
	li $v0 4	# syscall 4 (print_str)
	lw $a0 __excp($k0)
	syscall
	bne $k0 0x18 ok_pc # Bad PC requires special checks
	mfc0 $a0, $14	# EPC
	and $a0, $a0, 0x3 # Is EPC word-aligned?
	beq $a0, 0, ok_pc
	li $v0 10	# Exit on really bad PC (out of text)
	syscall

ok_pc:
	li $v0 4	# syscall 4 (print_str)
	la $a0 __m2_
	syscall
	mtc0 $0, $13	# Clear Cause register
ret:	lw $v0 s1
	lw $a0 s2
	mfc0 $k0 $14	# EPC
	.set noat
	move $at $k1	# Restore $at
	.set at
	rfe		# Return from exception handler
	addiu $k0 $k0 4 # Return to next instruction
	jr $k0


# Standard startup code.  Invoke the routine main with no arguments.

	.text
	.globl __start
__start: 
	lw $a0, 0($sp)	# argc
	addiu $a1, $sp, 4 # argv
	addiu $a2, $a1, 4 # envp
	sll $v0, $a0, 2
	addu $a2, $a2, $v0
	jal main
	li $v0 10
	syscall		# syscall 10 (exit)

	.file	1 "runtime.c"

 # GNU C 2.7.2.2 [AL 1.1, MM 40] BSD Mips compiled by CC

 # Cc1 defaults:

 # Cc1 arguments (-G value = 8, Cpu = 3000, ISA = 1):
 # -quiet -dumpbase -mrnames -O -o

	.data
	.align	2
empty:
	.word	0
	.asciiz	""

	.align	2
ch:
	.word	buffer
consts:
	.space	2048
buffer:
	.space	256

	.text
	.globl	_initArray
_initArray:
	.frame	$sp,0,$ra		# vars= 0, regs= 0/0, args= 0, extra= 0
	.mask	0x00000000,0
	.fmask	0x00000000,0
	move	$v1,$a0
	sll	$a0,$v1,2
	li	$v0,0x00000009		# 9
	addu	$a0,$a0,4
 #APP
	syscall
 #NO_APP
	sw	$v1,0($v0)
	addu	$v0,$v0,4
	addu	$v1,$v1,-1
	move	$a0,$v0
	bltz	$v1,$L8
$L9:
	sw	$a1,0($a0)
	addu	$v1,$v1,-1
	addu	$a0,$a0,4
	bgez	$v1,$L9
$L8:
	j	$ra
	.end	_initArray
	.text
	.globl	_allocRecord
_allocRecord:
	.frame	$sp,0,$ra		# vars= 0, regs= 0/0, args= 0, extra= 0
	.mask	0x00000000,0
	.fmask	0x00000000,0
	move	$v1,$a0
	sll	$a0,$v1,2
	li	$v0,0x00000009		# 9
	addu	$a0,$a0,4
 #APP
	syscall
 #NO_APP
	sw	$v1,0($v0)
	addu	$v0,$v0,4
	addu	$v1,$v1,-1
	move	$a0,$v0
	bltz	$v1,$L14
$L16:
	sw	$zero,0($a0)
	addu	$v1,$v1,-1
	addu	$a0,$a0,4
	bgez	$v1,$L16
$L14:
	j	$ra
	.end	_allocRecord
	.text
	.globl	_strcmp
_strcmp:
	.frame	$sp,0,$ra		# vars= 0, regs= 0/0, args= 0, extra= 0
	.mask	0x00000000,0
	.fmask	0x00000000,0
$L19:
	lbu	$v0,0($a0)
	addu	$a0,$a0,1
	lbu	$v1,0($a1)
	addu	$a1,$a1,1
	beq	$v0,$zero,$L20
	beq	$v0,$v1,$L19
$L20:
	subu	$v0,$v0,$v1
	j	$ra

	.end	_strcmp
	.text
	.globl	_print
_print:
	.frame	$sp,0,$ra		# vars= 0, regs= 0/0, args= 0, extra= 0
	.mask	0x00000000,0
	.fmask	0x00000000,0
	li	$v0,0x00000004		# 4
 #APP
	syscall
 #NO_APP
	j	$ra
	.end	_print
	.text
	.globl	_flush
_flush:
	.frame	$sp,0,$ra		# vars= 0, regs= 0/0, args= 0, extra= 0
	.mask	0x00000000,0
	.fmask	0x00000000,0
	j	$ra
	.end	_flush
	.text
	.globl	main
main:
	.frame	$sp,0,$ra		# vars= 0, regs= 0/0, args= 0, extra= 0
	.mask	0x00000000,0
	.fmask	0x00000000,0
	li	$v1,0x000000ff		# 255
	li	$a0,0x00000001		# 1
$L33:
	sll	$v0,$v1,3
	sw	$a0,consts($v0)
	sb	$v1,consts+4($v0)
	sb	$zero,consts+5($v0)
	addu	$v1,$v1,-1
	bgez	$v1,$L33

	move	$v0,$0		# static link
	j	tigermain
	.end	_main
	.text
	.globl	_ord
_ord:
	.frame	$sp,0,$ra		# vars= 0, regs= 0/0, args= 0, extra= 0
	.mask	0x00000000,0
	.fmask	0x00000000,0
	lw	$v0,-4($a0)
	#nop
	blez	$v0,$L36
	lb	$v0,0($a0)
	j	$ra
$L36:
	li	$v0,-1
	j	$ra
	.end	_ord
	.text
	.globl	_chr
_chr:
	.frame	$sp,0,$ra		# vars= 0, regs= 0/0, args= 0, extra= 0
	.mask	0x00000000,0
	.fmask	0x00000000,0
	sltu	$v0,$a0,256
	beq	$v0,$zero,$L38
	sll	$v1,$a0,3
	la	$v0,consts+4
	addu	$v0,$v1,$v0
	j	$L39
$L38:
	la	$v0,empty+4
$L39:
	j	$ra
	.end	_chr
	.text
	.globl	_size
_size:
	.frame	$sp,0,$ra		# vars= 0, regs= 0/0, args= 0, extra= 0
	.mask	0x00000000,0
	.fmask	0x00000000,0
	lw	$v0,-4($a0)
	j	$ra
	.end	_size
	.text
	.globl	_substring
_substring:
	.frame	$sp,0,$ra		# vars= 0, regs= 0/0, args= 0, extra= 0
	.mask	0x00000000,0
	.fmask	0x00000000,0
	move	$t0,$a0
	bltz	$a1,$L42
	lw	$v1,-4($t0)
	#nop
	slt	$v0,$a1,$v1
	beq	$v0,$zero,$L42
	addu	$v0,$a1,$a2
	slt	$v0,$v1,$v0
	beq	$v0,$zero,$L51
	subu	$a2,$v1,$a1
$L51:
	addu	$a0,$a2,4
	srl	$a0,$a0,2
	li	$v0,0x00000009		# 9
	addu	$a0,$a0,4
 #APP
	syscall
 #NO_APP
	sw	$a2,0($v0)
	addu	$a3,$v0,4
	addu	$a0,$t0,$a1
	addu	$a2,$a2,-1
	move	$v1,$a3
	bltz	$a2,$L46
$L48:
	lbu	$v0,0($a0)
	#nop
	sb	$v0,0($v1)
	addu	$a0,$a0,1
	addu	$a2,$a2,-1
	addu	$v1,$v1,1
	bgez	$a2,$L48
$L46:
	sb	$zero,0($v1)
	move	$v0,$a3
	j	$L50
$L42:
	la	$v0,empty+4
$L50:
	j	$ra
	.end	_substring
	.text
	.globl	_concat
_concat:
	.frame	$sp,0,$ra		# vars= 0, regs= 0/0, args= 0, extra= 0
	.mask	0x00000000,0
	.fmask	0x00000000,0
	move	$t0,$a0
	lw	$a2,-4($t0)
	lw	$a3,-4($a1)
	move	$v0,$a1
	beq	$a2,$zero,$L66
	addu	$v1,$a2,$a3
	bne	$a3,$zero,$L54
	move	$v0,$t0
	j	$L66
$L54:
	addu	$a0,$v1,4
	srl	$a0,$a0,2
	li	$v0,0x00000009		# 9
	addu	$a0,$a0,4
 #APP
	syscall
 #NO_APP
	sw	$v1,0($v0)
	addu	$t1,$v0,4
	move	$a0,$t0
	addu	$a2,$a2,-1
	move	$v1,$t1
	bltz	$a2,$L57
$L59:
	lbu	$v0,0($a0)
	#nop
	sb	$v0,0($v1)
	addu	$a0,$a0,1
	addu	$a2,$a2,-1
	addu	$v1,$v1,1
	bgez	$a2,$L59
$L57:
	addu	$a3,$a3,-1
	move	$a0,$a1
	bltz	$a3,$L62
$L64:
	lbu	$v0,0($a0)
	#nop
	sb	$v0,0($v1)
	addu	$a0,$a0,1
	addu	$a3,$a3,-1
	addu	$v1,$v1,1
	bgez	$a3,$L64
$L62:
	sb	$zero,0($v1)
	move	$v0,$t1
$L66:
	j	$ra
	.end	_concat
	.text
	.globl	_not
_not:
	.frame	$sp,0,$ra		# vars= 0, regs= 0/0, args= 0, extra= 0
	.mask	0x00000000,0
	.fmask	0x00000000,0
	sltu	$v0,$a0,1
	j	$ra
	.end	_not
	.text
	.globl	_getchar
_getchar:
	.frame	$sp,0,$ra		# vars= 0, regs= 0/0, args= 0, extra= 0
	.mask	0x00000000,0
	.fmask	0x00000000,0
	lw	$v0,ch
	#nop
	lb	$v0,0($v0)
	#nop
	li	$a1,0x00000100		# 256
	bne	$v0,$zero,$L69
	la	$a0,buffer
	sw	$a0,ch
	li	$v0,0x00000008		# 8
 #APP
	syscall
 #NO_APP
$L69:
	lw	$v1,ch
	#nop
	addu	$v0,$v1,1
	sw	$v0,ch
	lb	$v0,0($v1)
	#nop
	sll	$v0,$v0,3
	la	$v1,consts+4
	addu	$v0,$v0,$v1
	j	$ra
	.end	_getchar

	.data
	.align 2
_badptr:
	.asciiz	"Attempt to use a null pointer\n"
	.align 2
_badsub:
	.asciiz	"Subscript out of bounds\n"
	.text
	.globl	_BADPTR
_BADPTR:
	la	$a0,_badptr
	b	_failure
	.globl	_BADSUB
_BADSUB:
	la	$a0,_badsub
	b	_failure
_failure:
	li	$v0,4
	syscall
	li	$v0,10
	syscall
