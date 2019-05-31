package com.eurodyn.qlack.fuse.acv.dto;

import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PersonDTO {

    @Id
    private String name;
    private String email;
    private int age;
    private RoleDTO role;

}
