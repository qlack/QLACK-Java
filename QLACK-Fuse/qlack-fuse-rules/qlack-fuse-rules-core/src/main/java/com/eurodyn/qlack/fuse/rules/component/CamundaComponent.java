package com.eurodyn.qlack.fuse.rules.component;

import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This util method contains useful method that are used by the rules Qlack
 * component.
 *
 * @author European Dynamics SA
 */
@Component
@Log
public final class CamundaComponent {

    private CamundaComponent() {
    }

    /**
     * Serializes a list of maps containing the input variables that will be
     * evaluated through the Camunda DmnEngine
     *
     * @param variables the list of maps to be serialized
     * @return the serialized list of maps as a byte array
     */
    public static List<byte[]> serializeMap(List<Map<String, Object>> variables) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out;
        List<byte[]> byteList = new ArrayList<>();
        try {
            out = new ObjectOutputStream(byteOut);
            out.writeObject(variables);
            out.flush();
            byte[] bytes = byteOut.toByteArray();
            byteOut.close();
            byteList.add(bytes);
            return byteList;
        } catch (Exception e) {
            throw new QRulesException(e);
        }
    }

    /**
     * Deserializes a list of maps
     *
     * @param bytes the serialized list of maps
     * @return the list of maps
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> deserialize(List<byte[]> bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes.get(0));
        ObjectInput in;
        try {
            in = new ObjectInputStream(bis);
            List<Map<String, Object>> variables = (List<Map<String, Object>>) in.readObject();
            in.close();
            return variables;
        } catch (Exception e) {
            throw new QRulesException(e);
        }
    }

}
