package com.wyd.reactorweb.mvc.mhandler.assist;

import cn.hutool.core.util.StrUtil;
import com.wyd.reactorweb.annotation.MyRequestMapping;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.Label;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.wyd.reactorweb.constant.AsmConstants.MYINVOKE;

/**
 * @program: reactor_web
 * @description: 动态生成 controller 调用类
 * @author: Stone
 * @create: 2023-11-09 09:32
 **/
public class GenerateClassUtil {


    /**
    * @Description: 生成类的字节码
    * @Param: generateClassName：生成类的类名，invokeClass：需要被调用的类
    * @Author: Stone
    * @Date: 2023/11/10
    */
    public static byte[] generate( String generateClassName, Class<?> invokeClass) {
        Map<String, Method> urlToMethodMap = new HashMap<>();
        // 获取 url 和 方法对应信息存到 map 中
        fillUrlToMethodMap(invokeClass, urlToMethodMap);
        if (urlToMethodMap.size() == 0)  return null;
        return doGenerate(invokeClass, generateClassName, urlToMethodMap);
    }

    private static byte[] doGenerate(Class<?> targetClass, String className, Map<String, Method> urlToMethodMap) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null,"java/lang/Object", new String[]{MYINVOKE});
        Iterator<Map.Entry<String, Method>> iterator = urlToMethodMap.entrySet().iterator();

        // 添加构造函数
        MethodVisitor constructor = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.visitCode();
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1, 1);
        constructor.visitEnd();

        // invoke 方法
        MethodVisitor invoke = cw.visitMethod(Opcodes.ACC_PUBLIC, "invoke", "(Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        invoke.visitCode();

        Label lastIf;
        int maxStack = 3;
        while (iterator.hasNext()) {
            Map.Entry<String, Method> entry = iterator.next();
            String pathUrl = entry.getKey();
            Method method = entry.getValue();

            invoke.visitVarInsn(Opcodes.ALOAD, 1);
            invoke.visitLdcInsn(pathUrl);
            invoke.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
            // if 判断
            lastIf = new Label();
            invoke.visitJumpInsn(Opcodes.IFEQ, lastIf);
            invoke.visitVarInsn(Opcodes.ALOAD, 2);
            invoke.visitTypeInsn(Opcodes.CHECKCAST, targetClass.getTypeName().replace('.', '/'));
            // 方法调用参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            maxStack = Math.max(maxStack, 2 + parameterTypes.length);
            for (int i = 0; i < parameterTypes.length; i++) {
                invoke.visitVarInsn(Opcodes.ALOAD, 3);
                invoke.visitIntInsn(Opcodes.BIPUSH, i);
                invoke.visitInsn(Opcodes.AALOAD);
                invoke.visitTypeInsn(Opcodes.CHECKCAST, parameterTypes[i].getTypeName().replace('.', '/'));
            }
            invoke.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    targetClass.getName().replace('.', '/'), method.getName(), getMethodDescriptor(method), false);
            // 如果返回类型是空，需要返回 null
            if (void.class.isAssignableFrom(method.getReturnType())) {
                invoke.visitInsn(Opcodes.ACONST_NULL);
            }
            invoke.visitInsn(Opcodes.ARETURN);

            invoke.visitLabel(lastIf);
        }
        // 所有方法 if 判断都没有之后，抛出异常
        invoke.visitTypeInsn(Opcodes.NEW, "java/lang/RuntimeException");
        invoke.visitInsn(Opcodes.DUP);
        invoke.visitLdcInsn("无此方法");
        invoke.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;)V", false);
        invoke.visitInsn(Opcodes.ATHROW);
        invoke.visitMaxs(maxStack, 4);
        invoke.visitEnd();

        cw.visitEnd();
        return cw.toByteArray();
    }

    // 填充方法路径
    private static void fillUrlToMethodMap(Class<?> targetClass, Map<String, Method> urlToMethodMap) {
        MyRequestMapping classPathMapping = AnnotationUtils.findAnnotation(targetClass, MyRequestMapping.class);
        String classPath = "";
        if (classPathMapping != null) {
            classPath = fitPath(classPathMapping.value());
        }
        Method[] declaredMethods = targetClass.getMethods();
        for (Method method : declaredMethods) {
            MyRequestMapping methodPathMapping = AnnotationUtils.findAnnotation(method, MyRequestMapping.class);
            if (methodPathMapping != null) {
                String methodPath = fitPath(methodPathMapping.value());
                urlToMethodMap.put(classPath + methodPath, method);
            }
        }
    }

    // 根据方法获取方法描述符
    private static String getMethodDescriptor(Method method) {
        // 获取参数类型数组
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuilder parameterTypesStr = new StringBuilder();
        for (Class<?> parameterType : parameterTypes) {
            String name = getNeededName(parameterType);
            if (parameterType.isPrimitive()) parameterTypesStr.append(name);
            else if (name.contains("[")) parameterTypesStr.append(name.replace('.', '/'));
            else parameterTypesStr.append("L").append(name.replace('.', '/')).append(";");
        }
        // 获取返回值类型
        Class<?> returnType = method.getReturnType();
        String returnTypeStr = null;
        if (returnType.isPrimitive()) returnTypeStr = getNeededName(returnType);
        if (StrUtil.isBlank(returnTypeStr)) {
            returnTypeStr = "L" + returnType.getName().replace('.', '/') + ";";
        }
        // 构建方法描述符字符串
        return "(" + parameterTypesStr + ")" + returnTypeStr;
    }
    private static String getNeededName(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == byte.class) return "B";
            if (type == short.class) return "S";
            if (type == int.class) return "I";
            if (type == long.class) return "J";
            if (type == float.class) return "F";
            if (type == double.class) return "D";
            if (type == char.class) return "C";
            if (type == boolean.class) return "Z";
            if (type == void.class) return "V";
            return "";
        } else return type.getName();
    }

    // 给路径加上 "/" 前缀
    private static String fitPath(String classPath) {
        if (!classPath.startsWith("/")) {
            return '/' + classPath;
        }
        return classPath;
    }

/*    public static void main(String[] args) throws Exception {
        // 测试一下生成的字节码
        GenerateClass generateClass = new GenerateClass();
        byte[] code = generateClass.generate(Controller1.class);
        FileOutputStream fos = new FileOutputStream("TempClass.class");
        fos.write(code);
        fos.close();

        Class<?> myClass = generateClass.defineClass("TempClass", code, 0, code.length);
        Object o = myClass.getConstructor().newInstance();
        ((MyInvoke) o).invoke("/asmTest/test2", new Controller1(), new Object[] {"123"});
    }*/
}
