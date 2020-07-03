package com.eurodyn.qlack.tools.mavenizr.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Dependency class. Marshalled to {@code <dependency></dependency>}
 *
 * @author EUROPEAN DYNAMICS SA.
 */
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@XmlRootElement(name = "dependency")
public class Dependency {
    private String groupId;
    private String artifactId;
    private String version;
    private String scope;
}
