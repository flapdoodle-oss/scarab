package de.flapdoodle.unravel.asm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.google.common.collect.Lists;

import se.jbee.jvm.Classes;
import se.jbee.sample.checks.InnerClasses;
import se.jbee.sample.checks.Java8;

public class AsmTest {

	@Test
	public void loadClass() throws IOException {
		ClassReader cr = new ClassReader(Classes.byteCodeOf(InnerClasses.class).get());
		ClassVisitor classVisitor=new ClassVisitor(Opcodes.ASM5) {
			@Override
			public void visitInnerClass(String name, String outerName, String innerName, int access) {
				super.visitInnerClass(name, outerName, innerName, access);
				System.out.println("name:"+name);
				System.out.println("outerName:"+outerName);
				System.out.println("innerName:"+innerName);
				System.out.println("access:"+access);
			}
		};
		cr.accept(classVisitor, 0);
	}
	
	@Test
	public void java8() throws IOException {
		ClassReader cr = new ClassReader(Classes.byteCodeOf(Java8.class).get());
		
		Map<String, Integer> invocationCounter = new HashMap<> ();
		 
		class InvocationCountMethodVisitor extends MethodVisitor {
		  InvocationCountMethodVisitor () { super (Opcodes.ASM5); }
		 
		  @Override public void visitMethodInsn (
		    int opcode, String owner, String name, String desc, boolean itf) {
		    final String calledMethod = owner + "." + name + " " + desc;
		    final int prev = invocationCounter.containsKey (calledMethod) ?
		      invocationCounter.get (calledMethod) : 
		      0;
		    invocationCounter.put (calledMethod, prev+1);
		  }
		}
		
		ClassVisitor classVisitor=new ClassVisitor(Opcodes.ASM5) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
				MethodVisitor ret = super.visitMethod(access, name, desc, signature, exceptions);
				System.out.println("access:"+access);
				System.out.println("name:"+name);
				System.out.println("desc:"+desc);
				System.out.println("signature:"+signature);
				System.out.println("exceptions:"+exceptions);
				if (name.equals("lambdas")) {
					return new InvocationCountMethodVisitor();
				}
				return ret;
			}
		};
		cr.accept(classVisitor, 0);
		
		System.out.println(invocationCounter);
	}
	
	@Test
	public void methodCallStack() throws IOException {
		ClassReader cr = new ClassReader(Classes.byteCodeOf(Java8.class).get());
		ClassVisitor classVisitor=new ClassVisitor(Opcodes.ASM5) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
				MethodVisitor ret = super.visitMethod(access, name, desc, signature, exceptions);
				System.out.println("name:"+name);
				return new MethodVisitor(Opcodes.ASM5) {
					@Override
					public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
						System.out.println("enter:"+name);
						super.visitMethodInsn(opcode, owner, name, desc, itf);
						System.out.println("exit:"+name);
					}
					
					@Override
					public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
						System.out.println("invokeDyn->:"+name+":"+Lists.newArrayList(bsmArgs));
						super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
						System.out.println("invokeDyn<-:"+name+":"+Lists.newArrayList(bsmArgs));
					}
				};
			}
		};
		cr.accept(classVisitor, 0);
	}
}
