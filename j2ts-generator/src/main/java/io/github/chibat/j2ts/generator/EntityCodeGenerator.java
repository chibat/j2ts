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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.types.ArraySchema.Items;

/**
 * 
 * @author tomofumi
 */
public class EntityCodeGenerator {

    protected static final String INDENT = "    ";
    protected final Map<String, Map<String, String>> moduleMap = new HashMap<>();
    protected final ObjectMapper objectMapper;
    protected boolean asClass = false;

    public EntityCodeGenerator() {
        this.objectMapper = new ObjectMapper();
    }

    public EntityCodeGenerator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public EntityCodeGenerator asClass() {
        this.asClass = true;
        return this;
    }

    public EntityCodeGenerator asInterface() {
        this.asClass = false;
        return this;
    }

    public EntityCodeGenerator readClass(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            generate(clazz);
        }
        return this;
    }

    public void writeFile(String path) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(new File(path)), "UTF-8")) {
            write(writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String asString() {
        try (Writer writer = new StringWriter()) {
            return write(writer).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void print() {
        try (OutputStreamWriter writer = new OutputStreamWriter(System.out)) {
            write(writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Writer write(Writer writer) {
        String declarationType = this.asClass ? "class" : "interface";
        try {
            for (Entry<String, Map<String, String>> moduleEntry : moduleMap.entrySet()) {
                String moduleName = moduleEntry.getKey();
                writer.write((this.asClass ? "\n" : "\ndeclare ") + "module " + moduleName + " {");
                Map<String, String> map = moduleEntry.getValue();
                for (Entry<String, String> classEntry : map.entrySet()) {
                    String className = classEntry.getKey();
                    String classCode = classEntry.getValue();
                    writer.write("\n"
                        + INDENT
                        + "export "
                        + declarationType
                        + " "
                        + className
                        + " ");
                    writer.append(classCode);
                }
                writer.write("\n}");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer;
    }

    protected void generate(Class<?> clazz) {
        SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
        try {
            objectMapper.acceptJsonFormatVisitor(objectMapper.constructType(clazz), visitor);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }
        int indent = 1;
        StringBuilder builder = new StringBuilder();
        generate(builder, indent, visitor.finalSchema());
        String classCode = builder.toString();
        if (" any".equals(classCode)) {
            return;
        }
        String canonicalName = clazz.getCanonicalName();
        String name = canonicalName == null ? clazz.getName() : canonicalName;
        String moduleName = name.substring(0, name.lastIndexOf('.'));
        String className = name.substring(name.lastIndexOf('.') + 1);
        Map<String, String> classMap = moduleMap.get(moduleName);
        if (classMap == null) {
            classMap = new HashMap<>();
            moduleMap.put(moduleName, classMap);
        }
        classMap.put(className, classCode);
    }

    protected void generate(StringBuilder builder, int indent, JsonSchema jsonSchema) {
        if (jsonSchema.isObjectSchema()) {
            builder.append("{\n");
            indent++;
            for (Entry<String, JsonSchema> js : jsonSchema
                .asObjectSchema()
                .getProperties()
                .entrySet()) {
                indent(builder, indent);
                builder.append(js.getKey() + ":");
                generate(builder, indent, js.getValue());
                builder.append(";\n");
            }
            indent--;
            indent(builder, indent);
            builder.append("}");
        } else if (jsonSchema.isArraySchema()) {
            Items items = jsonSchema.asArraySchema().getItems();
            if (items.isSingleItems()) {
                JsonSchema js = items.asSingleItems().getSchema();
                generate(builder, indent, js);
            } else if (items.isArrayItems()) {
                // TODO what ?
                // for (JsonSchema js: items.asArrayItems().getJsonSchemas()) {
                // hoge(js);
                // }
            }
            builder.append("[]");
        } else if (jsonSchema.isStringSchema()) {
            builder.append(" string");
        } else if (jsonSchema.isBooleanSchema()) {
            builder.append(" boolean");
        } else if (jsonSchema.isIntegerSchema()) {
            builder.append(" number");
        } else if (jsonSchema.isNumberSchema()) {
            builder.append(" number");
        } else if (jsonSchema.isAnySchema()) {
            builder.append(" any");
        } else if (jsonSchema.isContainerTypeSchema()) {
            builder.append(" any /* ContainerType */");
        } else if (jsonSchema.isNullSchema()) {
            builder.append(" any /* Null */");
        } else if (jsonSchema.isSimpleTypeSchema()) {
            builder.append(" any /* SimpleType */");
        } else if (jsonSchema.isUnionTypeSchema()) {
            builder.append(" any /* UnionType */");
        } else if (jsonSchema.isValueTypeSchema()) {
            builder.append(" any /* ValueType */");
        }
    }

    protected static void indent(StringBuilder builder, int indent) {
        for (int i = 0; i < indent; i++) {
            builder.append(INDENT);
        }
    }
}
