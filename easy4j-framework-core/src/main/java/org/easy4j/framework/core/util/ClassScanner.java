package org.easy4j.framework.core.util;

import com.google.common.base.Strings;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <p>类扫描器</p>
 *
 * @author bjliuyong
 * @version 1.0
 * @created date 15-9-14
 */
public class ClassScanner {

    /**
     * 获取包下面 所有的类
     * @param packageName
     * @return
     * @throws Exception
     */
    public static List<Class<?>>  getClassList(String packageName) throws Exception{
        return getClassList(packageName , new ClassFilter() {});
    }

    /**
     * 获取包名下面 指定注解的所有类
     * @param packageName
     * @param annotationType
     * @return
     * @throws Exception
     */
    public static List<Class<?>>  getClassListByAnnotationType(String packageName ,Class<? extends Annotation> annotationType) throws Exception{
        return getClassList(packageName , ClassFilter.newAnnotationFilter(annotationType));
    }

    /**
     * 获取包名下面 superClass所有之类
     * @param packageName
     * @param superClass
     * @return
     * @throws Exception
     */
    public static List<Class<?>>  getClassListBySuperClass(String packageName ,Class<?> superClass) throws Exception{
        return getClassList(packageName , ClassFilter.newSuperClassFilter(superClass));
    }

    /**
     * 获取指定包名中指定注解的相关类
     */
    public static List<Class<?>> getClassList(String packageName , ClassFilter classFilter) throws Exception{

        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));

        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();

            // 获取协议名（分为 file 与 jar）
            String protocol = url.getProtocol();

            if(protocol.equals("file")) {
                String packagePath = url.getPath();
                addClass(classFilter, packagePath, packageName) ;
            } else if(protocol.equals("jar")){
                addClassFromJar(url , classFilter);
            }

        }

        return classFilter.getClassList();
    }

    private static void  addClassFromJar(URL url ,ClassFilter classFilter) throws Exception {
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();
            // 判断该 entry 是否为 class
            if (jarEntryName.endsWith(".class")) {
                // 获取类名
                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                // 执行添加类操作
                doAddClass(classFilter, className);
            }
        }
    }


    private static void addClass(ClassFilter classFilter, String packagePath, String packageName) {

        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });

        for (File file : files) {

            String fileName = file.getName();
            if(file.isFile()){

                String className = fileName.substring(0, fileName.lastIndexOf("."));

                if(!Strings.isNullOrEmpty(packageName)){
                    className = packageName + "." + className;
                }

                // 执行添加类操作
                doAddClass(classFilter, className);
            } else {
                // 获取子包
                String subPackagePath = fileName;
                if (!Strings.isNullOrEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                // 子包名
                String subPackageName = fileName;
                if (!Strings.isNullOrEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                // 递归调用
                addClass(classFilter, subPackagePath, subPackageName);
            }
        }
    }


    private static void doAddClass(ClassFilter classFilter, String className) {
        // 加载类
        Class<?> cls ;
        try {
            cls = Class.forName(className, false,Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        classFilter.add(cls);
    }

}
