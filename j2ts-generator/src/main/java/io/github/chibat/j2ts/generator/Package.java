/*
 * Copyright 2015- Tomofumi Chiba
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.chibat.j2ts.generator;

import io.github.chibat.j2ts.annotation.TypeScriptClass;
import io.github.chibat.j2ts.annotation.TypeScriptInterface;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 
 * @author tomofumi
 */
public class Package {

    private final ClassLoader classLoader;
    public final Set<Class<?>> classesForClass = new HashSet<>();
    public final Set<Class<?>> classesForInterface = new HashSet<>();

    public Package(String packageName) {
        this.classLoader = Thread.currentThread().getContextClassLoader();
        final String resourceName = packageNameToResourceName(packageName);
        Enumeration<URL> urls;
        try {
            urls = classLoader.getResources(resourceName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();

            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                try {
                    scanClassesWithFile(packageName, new File(url.getFile()));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if ("jar".equals(protocol)) {
                try {
                    scanClassesWithJarFile(packageName, url);
                } catch (ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new IllegalArgumentException("Unsupported Class Load Protodol["
                    + protocol
                    + "]");
            }
        }
    }

    private void scanClassesWithFile(String packageName, File dir) throws ClassNotFoundException {

        for (String path : dir.list()) {
            File entry = new File(dir, path);
            if (entry.isFile() && isClassFile(entry.getName())) {
                Class<?> clazz =
                    classLoader.loadClass(packageName + "." + fileNameToClassName(entry.getName()));
                if (clazz.getAnnotation(TypeScriptClass.class) != null) {
                    this.classesForClass.add(clazz);
                } else if (clazz.getAnnotation(TypeScriptInterface.class) != null) {
                    this.classesForInterface.add(clazz);
                }
            } else if (entry.isDirectory()) {
                scanClassesWithFile(packageName + "." + entry.getName(), entry);
            }
        }
    }

    private void scanClassesWithJarFile(String rootPackageName, URL jarFileUrl) throws IOException,
            ClassNotFoundException {

        final JarURLConnection jarUrlConnection = (JarURLConnection) jarFileUrl.openConnection();

        try (JarFile jarFile = jarUrlConnection.getJarFile()) {
            Enumeration<JarEntry> jarEnum = jarFile.entries();

            String packageNameAsResourceName = packageNameToResourceName(rootPackageName);

            while (jarEnum.hasMoreElements()) {
                JarEntry jarEntry = jarEnum.nextElement();
                if (jarEntry.getName().startsWith(packageNameAsResourceName)
                    && isClassFile(jarEntry.getName())) {
                    // System.out.println(jarEntry.getName());
                    Class<?> clazz =
                        classLoader.loadClass(resourceNameToClassName(jarEntry.getName()));
                    if (clazz.getAnnotation(TypeScriptClass.class) != null) {
                        this.classesForClass.add(clazz);
                    } else if (clazz.getAnnotation(TypeScriptInterface.class) != null) {
                        this.classesForInterface.add(clazz);
                    }
                }
            }
        }
    }

    private String fileNameToClassName(String name) {
        return name.substring(0, name.length() - ".class".length());
    }

    private String resourceNameToClassName(String resourceName) {
        return fileNameToClassName(resourceName).replace('/', '.');
    }

    private boolean isClassFile(String fileName) {
        return fileName.endsWith(".class")
            && !fileName.endsWith("$1.class")
            && !fileName.endsWith("package-info.class");
    }

    private String packageNameToResourceName(String packageName) {
        return packageName.replace('.', '/');
    }

    public static <T> T uncheckCall(Callable<T> callable) {
        try {
            return callable.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}