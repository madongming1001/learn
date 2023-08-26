package com.madm.learnroute.javaee;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author madongming
 */
public class MyClassLoaderTest {

    public static void main(String[] args) throws Exception {
        /**
         * bootstrapLoader加载以下文件：-Xbootclasspath、sun.boot.class.path
         * extClassloader加载以下文件：java.ext.dirs
         * appClassLoader加载以下文件：java.class.path
         */
        CustomClassLoader classLoader = new CustomClassLoader("/Users/madongming/IdeaProjects/learn/classloader");
        Class clazz = classLoader.loadClass("com.madm.learnroute.model.Employee1");
        Constructor[] constructors = clazz.getConstructors();
        Object obj = null;
        for (Constructor constructor : constructors) {
            if (constructor.getParameterCount() == 2) {
                obj = constructor.newInstance(10L, "10");
                break;
            }
        }
        Method method = clazz.getDeclaredMethod("print", null);
        method.invoke(obj, null);
        System.out.println(clazz.getClassLoader().getClass().getName());
    }

    static class CustomClassLoader extends ClassLoader {
        private String classPath;


        public CustomClassLoader(String classPath) {
            this.classPath = classPath;
        }

        private byte[] loadByte(String name) throws Exception {
            name = name.replaceAll("\\.", "/");
            FileInputStream fis = new FileInputStream(classPath + "/" + name + ".class");
            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data);
            fis.close();
            return data;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                byte[] data = loadByte(name);
                return defineClass(name, data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassNotFoundException();
            }
        }

        /**
         * 模拟实现Tomcat的webappClassLoader加载自己war包应用内不同版本类实现相互共存与隔离
         * 破坏双亲委派是重写loadClass不往上查找就好了
         *
         * @param name    The <a href="#name">binary name</a> of the class
         * @param resolve If <tt>true</tt> then resolve the class
         * @return
         * @throws ClassNotFoundException
         */
        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            synchronized (getClassLoadingLock(name)) {
                // First, check if the class has already been loaded
                Class<?> c = findLoadedClass(name);
                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    if (!name.startsWith("Employee")) {
                        c = this.getParent().loadClass(name);
                    } else {
                        c = findClass(name);
                    }
                    // this is the defining class loader; record the stats
//                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
//                    sun.misc.PerfCounter.getFindClasses().increment();
                }
                if (resolve) {
                    resolveClass(c);
                }
                return c;
            }
        }
    }

}