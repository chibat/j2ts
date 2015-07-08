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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author tomofumi
 */
public class Main {

    /**
     * 
     * @param basePackageNames
     * @param outputFileClass
     * @param outputFileInterface
     * @param objectMapperClass
     */
    public static void execute(String basePackageNames, String outputFileClass,
            String outputFileInterface, String objectMapperClass) {

        if (outputFileInterface == null && outputFileClass == null) {
            throw new NullPointerException(
                "Must specify arguments 'outputFileInterface' or 'outputFileClass'");
        }

        ObjectMapper objectMapper = null;
        try {
            objectMapper =
                objectMapperClass == null ? new ObjectMapper() : (ObjectMapper) Class.forName(
                    objectMapperClass).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        final List<Class<?>> classesForClass = new ArrayList<>();
        final List<Class<?>> classesForInterface = new ArrayList<>();

        for (String pn : basePackageNames.split(",")) {
            Package p = new Package(pn);
            classesForClass.addAll(p.classesForClass);
            classesForInterface.addAll(p.classesForInterface);
        }

        if (outputFileClass != null) {
            new EntityCodeGenerator(objectMapper)
                .asClass()
                .readClass(classesForClass.toArray(new Class[0]))
                .writeFile(outputFileClass);
        }

        if (outputFileInterface != null) {
            new EntityCodeGenerator(objectMapper)
                .asInterface()
                .readClass(classesForInterface.toArray(new Class[0]))
                .writeFile(outputFileInterface);
        }
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length <= 1) {
            throw new IllegalArgumentException(
                "USAGE: args[0]:required. base package names, args[1]: output file for class, args[2]: output file for interface, args[3]: option. object mapper class name");
        }
        String basePackageNames = args[0];
        String outputFileClass = args[1];
        String outputFileInterface = null;
        String objectMapperClass = null;
        if (args.length > 2) {
            outputFileInterface = args[2];
        }
        if (args.length > 3) {
            objectMapperClass = args[3];
        }
        execute(basePackageNames, outputFileClass, outputFileInterface, objectMapperClass);
    }
}
