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
 * The Dependencies class. Marshalled to {@code <dependencies></dependencies>}
 *
 * @author EUROPEAN DYNAMICS SA.
 */
@Data
@RequiredArgsConstructor
@XmlRootElement(name = "dependencies")
@XmlAccessorType(XmlAccessType.FIELD)
public class Dependencies {
    @XmlElement(name = "dependency")
    private List<Dependency> dependencies = new ArrayList<>();
}
