package cn.org.javaweb.iast.visitor.handler;

import cn.org.javaweb.iast.visitor.Handler;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.lang.reflect.Modifier;


/**
 * @author iiusky - 03sec.com
 */
public class SinkClassVisitorHandler implements Handler {

	private static final String METHOD_DESC = "()Ljava/lang/Process;";

	@Override
	public MethodVisitor ClassVisitorHandler(MethodVisitor mv, final String className, int access,
	                                         final String name, final String desc, String signature, String[] exceptions) {
		if (("start".equals(name) && METHOD_DESC.equals(desc))||(className.equals("com.mysql.cj.jdbc.StatementImpl")&&name.equals("executeQuery")) || (className.equals("javax.naming.InitialContext") && name.equals("lookup"))) {
			System.out.printf("%s.%s 被增强\n",className,name);
			final boolean isStatic = Modifier.isStatic(access);
			final Type    argsType = Type.getType(Object[].class);

			System.out.println("Sink Process 类名:" + className + ",方法名: " + name + "方法的描述符是：" + desc);
			return new AdviceAdapter(Opcodes.ASM5, mv, access, name, desc) {
				@Override
				protected void onMethodEnter() {
					loadArgArray();
					int argsIndex = newLocal(argsType);
					storeLocal(argsIndex, argsType);
					loadThis();
					loadLocal(argsIndex);
					push(className);
					push(name);
					push(desc);
					push(isStatic);

					mv.visitMethodInsn(INVOKESTATIC, "cn/org/javaweb/iast/core/Sink", "enterSink",
							"([Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V",
							false);
					super.onMethodEnter();
				}

				protected void onMethodExit(int opcode) {
					Type returnType = Type.getReturnType(desc);
					if (returnType == null || Type.VOID_TYPE.equals(returnType)) {
						push((Type) null);
					} else {
						mv.visitInsn(Opcodes.DUP);
					}
					push(className);
					push(name);
					push(desc);
					push(isStatic);
					mv.visitMethodInsn(INVOKESTATIC, "cn/org/javaweb/iast/core/Sink",
							"leaveSink",
							"(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V",
							false);
					super.onMethodExit(opcode);
				}
			};
		}

		return mv;
	}
}


