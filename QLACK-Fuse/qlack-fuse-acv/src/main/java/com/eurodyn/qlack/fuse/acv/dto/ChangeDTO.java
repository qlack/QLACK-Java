package com.eurodyn.qlack.fuse.acv.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangeDTO {

    private String propertyName;
    private Object from;
    private Object to;

}
