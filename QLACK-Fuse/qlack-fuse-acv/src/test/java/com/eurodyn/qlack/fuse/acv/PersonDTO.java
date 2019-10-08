package com.eurodyn.qlack.fuse.acv;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PersonDTO {

  private String name;
  private String email;
  private int age;
  private RoleDTO role;

}