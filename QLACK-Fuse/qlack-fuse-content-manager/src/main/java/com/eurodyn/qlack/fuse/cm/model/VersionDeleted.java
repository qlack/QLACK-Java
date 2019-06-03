package com.eurodyn.qlack.fuse.cm.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cm_version_deleted")
@Getter
@Setter
public class VersionDeleted extends QlackBaseModel {


}

