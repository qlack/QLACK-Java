package com.eurodyn.qlack.tools.mavenizr.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * Marshals an object to xml
 * @author EUROPEAN DYNAMICS SA.
 */
@Slf4j
@Component
public class JAXBMarshalUtil {

    public static final String ENCODING = "UTF-8";

    /**
     * Marshals given object to XML and outputs and logs an XML string
     *
     * @param obj The object
     * @throws JAXBException for various reasons such as (but not limited to):
     *
     *                       <ol>
     *                       <li>if an error was encountered while creating the <tt>JAXBContext</tt></li>,
     *                       <li>when there is an error retrieving the given property or value property name</li>
     *                       <li>If any unexpected problem occurs during the marshalling.</li>
     *                       </ol>
     */
    public void marshal(Object obj) {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(obj.getClass());

            Marshaller mar = context.createMarshaller();
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mar.setProperty(Marshaller.JAXB_ENCODING, ENCODING);
            StringWriter stringWriter = new StringWriter();
            mar.marshal(obj, stringWriter);
            log.info(stringWriter.toString());
        } catch (JAXBException e) {
            log.error(e.getMessage());
        }
    }
}
