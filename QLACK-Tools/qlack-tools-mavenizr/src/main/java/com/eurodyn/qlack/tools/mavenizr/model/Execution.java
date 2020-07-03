package com.eurodyn.qlack.tools.mavenizr.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Execution class. Marshalled to {@code <execution></execution>}
 *
 * @author EUROPEAN DYNAMICS SA.
 */
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@XmlRootElement(name = "execution")
public class Execution {

    private String id;
    private Configuration configuration;
    private String phase = "install";
    private Goals goals = new Goals();

}
