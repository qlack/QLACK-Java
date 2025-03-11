package com.eurodyn.qlack.fuse.cm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cm_version_deleted")
@Getter
@Setter
public class VersionDeleted {

    @Id
    private String id;

}

