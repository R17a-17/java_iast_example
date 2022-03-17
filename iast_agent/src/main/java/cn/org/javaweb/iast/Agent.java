package cn.org.javaweb.iast;

import javax.naming.InitialContext;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Base64;

/**
 * @author iiusky - 03sec.com
 */
public class Agent {

    public static void premain(String agentArgs, Instrumentation inst)
            throws UnmodifiableClassException {
        // 返回当前 JVM 配置是否支持类的重转换
        System.out.println("是否支持" + inst.isRetransformClassesSupported());
        //注册ClassFileTransformer实例，注册多个会按照注册顺序进行调用。所有的类被加载完毕之后会调用ClassFileTransformer实例，相当于它们通过了redefineClasses方法进行重定义。布尔值参数canRetransform决定这里被重定义的类是否能够通过retransformClasses方法进行回滚。
        //注册提供的转换器 这里主要是针对source、sink、propagator、http类进行匹配并埋点
        inst.addTransformer(new AgentTransform(), true);
        // 重转换已加载过的类
        inst.retransformClasses(Runtime.class);
        inst.retransformClasses(Base64.class);
        inst.retransformClasses(StringBuilder.class);
    }
}
