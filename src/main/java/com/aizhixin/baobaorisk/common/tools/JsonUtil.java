package com.aizhixin.baobaorisk.common.tools;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class JsonUtil {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    /**
     * object to JSON File
     * @param out OutputStream
     * @param obj   object
     */
    public static void encode(OutputStream out, Object obj) {
        try {
            objectMapper.writeValue(out, obj);
        } catch (IOException e) {
            log.warn("encode(Object)", e);
        }
    }

    /**
     * object to JSON
     * @param obj   object
     * @return  Json string
     */
    public static String encode(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("encode(Object)", e);
        }
        return null;
    }

    /**
     * 将json string反序列化成对象
     *
     * @param json      json
     * @param valueType valueType
     * @return      Object
     */
    public static <T> T decode(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            log.warn("decode(String, Class<T>)", e);
        }
        return null;
    }

    /**
     * 将json array反序列化为对象
     *
     * @param json  json
     * @param jsonTypeReference jsonTypeReference
     * @return  Object
     */
    public static <T> T decode(String json, TypeReference<T> jsonTypeReference) {
        try {
            return (T) objectMapper.readValue(json, jsonTypeReference);
        } catch (IOException e) {
            log.warn("decode(String, JsonTypeReference<T>)", e);
        }
        return null;
    }

    /**
     * 将json array反序列化为对象
     *
     * @param file  file
     * @param jsonTypeReference jsonTypeReference
     * @return  Object
     */
    public static <T> T decode(File file, TypeReference<T> jsonTypeReference) {
        try {
            return (T) objectMapper.readValue(file, jsonTypeReference);
        } catch (IOException e) {
            log.warn("decode(String, JsonTypeReference<T>)", e);
        }
        return null;
    }
}
