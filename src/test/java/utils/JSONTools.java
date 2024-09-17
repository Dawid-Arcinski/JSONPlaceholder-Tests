package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JSONTools {


    public static String readJSONFile(String path) {

        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        try {
            json = mapper.writeValueAsString(
                    mapper.readTree(new File(path))
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }

    public static JsonNode[] getElementsFromJsonArray(String json, String key) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            JsonNode rootNode = mapper.readTree(json).get(key);
            JsonNode[] elements = new JsonNode[rootNode.size()];

            for (int i = 0; i < rootNode.size(); i++) {
                elements[i] = rootNode.get(i);
            }

            return elements;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonNode[] getElementsFromJsonArray(String json) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            JsonNode rootNode = mapper.readTree(json);
            JsonNode[] elements = new JsonNode[rootNode.size()];

            for (int i = 0; i < rootNode.size(); i++) {
                elements[i] = rootNode.get(i);
            }

            return elements;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getStringFromJSON(String json, String key) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode node = mapper.readTree(json).get(key);

            if (node != null) {
                return node.asText();
            } else {
                return null;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Integer getIntegerFromJSON(String json, String key) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode node = mapper.readTree(json).get(key);

            if (node != null && node.isInt()) {
                return node.asInt();
            } else {
                return -1;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean getBooleanFromJSON(String json, String key) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode node = mapper.readTree(json).get(key);

            if (node != null && node.isBoolean()) {
                return node.asBoolean();
            } else {
                return false;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getNestedField(String json, String nestedField) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(json);
            String[] fieldPath = nestedField.split("\\.");
            JsonNode currentNode = rootNode;

            for (String field : fieldPath) {
                currentNode = currentNode.path(field);
                if (currentNode.isMissingNode()) {
                    return null;
                }
            }

            return currentNode.asText();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }

}
