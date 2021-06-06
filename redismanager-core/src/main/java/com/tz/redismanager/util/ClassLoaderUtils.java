package com.tz.redismanager.util;

import com.tz.redismanager.trace.TraceLoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>类加载工具</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-06-06 15:30
 **/
public class ClassLoaderUtils {

    private static final Logger logger = TraceLoggerFactory.getLogger(ClassLoaderUtils.class);

    private static ConcurrentHashMap<String, String> loadJarMap = new ConcurrentHashMap<>();

    private static ReentrantLock reentrantLock = new ReentrantLock();

    public static void loadJar(String jarPath) throws RuntimeException {
        if (StringUtils.isBlank(jarPath)) {
            return;
        }
        if (loadJarMap.containsKey(jarPath)) {
            logger.info("jar已加载,jarPath:{}", jarPath);
            return;
        }
        // 从URLClassLoader类中获取类所在文件夹的方法，jar也可以认为是一个文件夹
        File jarFile = new File(jarPath);
        if (jarFile.exists() == false) {
            logger.error("jar file not found.jarPath:{}", jarPath);
            return;
        }
        //获取类加载器的addURL方法，准备动态调用
        Method method = null;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (Exception e) {
            logger.error("find addURL method exception.jarPath:{}", jarPath, e);
        }
        // 获取方法的访问权限，保存原始值
        boolean accessible = method.isAccessible();
        reentrantLock.lock();
        try {
            //修改访问权限为可写
            if (accessible == false) {
                method.setAccessible(true);
            }
            // 获取系统类加载器
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            //获取jar文件的url路径
            java.net.URL url = jarFile.toURI().toURL();
            //jar路径加入到系统url路径里
            method.invoke(classLoader, url);
            loadJarMap.put(jarPath, jarPath);
        } catch (Exception e) {
            logger.error("invoke addURL method exception.jarPath:{}", jarPath, e);
        } finally {
            reentrantLock.unlock();
            //回写访问权限
            method.setAccessible(accessible);
        }
    }

}
