package cn.org.javaweb.iast;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.regex.Pattern;

/**
 * @author iiusky - 03sec.com
 */
public class AgentTransform implements ClassFileTransformer {

	/**
	 * @param loader
	 * @param className
	 * @param classBeingRedefined
	 * @param protectionDomain
	 * @param classfileBuffer
	 * @return
	 * @throws IllegalClassFormatException
	 */
	@Override
	public byte[] transform(ClassLoader loader, String className,
	                        Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
	                        byte[] classfileBuffer) throws IllegalClassFormatException {

		className = className.replace("/", ".");

		if (className.contains("cn.org.javaweb.iast")) {
			System.out.println("Skip class: " + className);
			return classfileBuffer;
		}

		if (className.contains("java.lang.invoke")) {
			System.out.println("Skip class: " + className);
			return classfileBuffer;
		}

		byte[] originalClassfileBuffer = classfileBuffer;

		// 用ASM增强字节码
		// ClassReader读取原始的字节流
		ClassReader  classReader  = new ClassReader(classfileBuffer);
		// ClassWriter用于将没有改变的字节码部分和修改了的字节码进行拼接
		ClassWriter  classWriter  = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
		// ClassVisitor用于修改字节码，这里调用自定义的IASTClassVisitor对source、sink、propagator进行埋点
		ClassVisitor classVisitor = new IASTClassVisitor(className, classWriter);
		classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
		// 获取最新的字节流
		classfileBuffer = classWriter.toByteArray();

		// 记录部分类的修改，方便自己查看
		className = className.replace("/", ".");
		String regexp = "(Decoder|Servlet|connector|Request|Parameters|Base64|Runtime|ProcessBuilder|StringBuilder|InitialContext)";
		if (Pattern.compile(regexp).matcher(className).find()) {
			ClassUtils.dumpClassFile("./iast_agent/target/classes/", className, classfileBuffer, originalClassfileBuffer);
		}

		// 返回新的classfile流，用于替换原来的流
		return classfileBuffer;
	}

}
