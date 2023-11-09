package com.wyd.reactor_web.asm;

import com.sun.org.apache.bcel.internal.generic.BIPUSH;
import com.wyd.reactor_web.asm.target.MyTargetClass;
import com.wyd.reactor_web.asm.target.demo.User;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.Label;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: reactor_web
 * @description: 动态生成 controller 调用类
 * @author: Stone
 * @create: 2023-11-09 09:32
 **/
public class GenerateClass {

    private Map<String, Method> urlToMethodMap = new HashMap<>();

    public byte[] generate( Class<?> targetClass ) {
        // 获取 url 和 方法对应信息存到 map 中
        fillUrlToMethodMap(targetClass);
        if (urlToMethodMap.size() == 0)  return null;

        // 生成字节码文件
        return doGenerate(targetClass);
    }

    private byte[] doGenerate(Class<?> targetClass) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "TempClass", null,"java/lang/Object", new String[]{"com/wyd/reactor_web/asm/target/demo/MyInvoke"});
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
            invoke.visitTypeInsn(Opcodes.CHECKCAST, targetClass.getTypeName());
            // 方法调用参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                invoke.visitVarInsn(Opcodes.ALOAD, 3);
                invoke.visitIntInsn(Opcodes.BIPUSH, i);
                invoke.visitInsn(Opcodes.AALOAD);
                invoke.visitTypeInsn(Opcodes.CHECKCAST, parameterTypes[i].getTypeName().replace('.', '/'));
            }
            invoke.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    targetClass.getName().replace('.', '/'), method.getName(), getMethodDescriptor(method), false);





            if (!iterator.hasNext()) {
                // 当前是最后一个，使用最后的 else 标签，跳转到报错部分

            }
        }


        return null;
    }

    // 根据方法获取方法描述符
    public String getMethodDescriptor(Method method) {
        // 获取参数类型数组
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuilder parameterTypesStr = new StringBuilder();
        for (Class<?> parameterType : parameterTypes) {
            String name = getNeededName(parameterType);
            if (parameterType.isPrimitive()) parameterTypesStr.append(name);
            else if (name.contains("[")) parameterTypesStr.append(name.replace('.', '/'));
            else parameterTypesStr.append("L" + name.replace('.', '/')).append(";");
        }
        // 获取返回值类型
        Class<?> returnType = method.getReturnType();
        if (returnType.isPrimitive()) return getNeededName(returnType);
        String returnTypeStr;
        if (returnType.equals(Void.class)) {
            returnTypeStr = "V";
        } else returnTypeStr = "L" + returnType.getName().replace('.', '/') + ";";

        // 构建方法描述符字符串
        return "(" + parameterTypesStr + ")" + returnTypeStr;
    }
    private String getNeededName(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == byte.class) return "B";
            if (type == short.class) return "S";
            if (type == int.class) return "I";
            if (type == long.class) return "J";
            if (type == float.class) return "F";
            if (type == double.class) return "D";
            if (type == char.class) return "C";
            if (type == boolean.class) return "Z";
            return "";
        } else return type.getName();
    }

    // 填充方法路径
    private void fillUrlToMethodMap(Class<?> targetClass) {
        RequestMapping classPathMapping = AnnotationUtils.findAnnotation(targetClass, RequestMapping.class);
        String classPath = "";
        if (classPathMapping != null) {
            classPath = fitPath(classPathMapping.name());
        }
        
        Method[] declaredMethods = targetClass.getMethods();
        for (Method method : declaredMethods) {
            RequestMapping methodPathMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
            if (methodPathMapping != null) {
                String methodPath = fitPath(methodPathMapping.name());
                urlToMethodMap.put(classPath + methodPath, method);
            }
        }
    }

    // 给路径加上 "/" 前缀
    private String fitPath(String classPath) {
        if (!classPath.startsWith("/")) {
           return '/' + classPath;
        }
        return classPath;
    }
}
