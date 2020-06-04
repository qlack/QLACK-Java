package com.eurodyn.qlack.tools.mavenizr.model;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The Executions class. Marshalled to {@code <executions></executions>}
 *
 * @author EUROPEAN DYNAMICS SA.
 */
@Data
@RequiredArgsConstructor
@XmlRootElement(name = "executions")
@XmlAccessorType(XmlAccessType.FIELD)
public class Executions {
    @XmlElement(name = "execution")
    private List<Execution> executions = new ArrayList<>();
}

