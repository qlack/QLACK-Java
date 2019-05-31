package com.eurodyn.qlack.fuse.rules.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Knowledge Base Library entity, that contains the libraries of a Knowledge Base.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "rul_kbase_library")
@Getter
@Setter
@NoArgsConstructor
public class KnowledgeBaseLibrary extends QlackBaseModel {

    /**
     * the library of the Knowledge Base
     */
    @Lob
    private byte[] library;

    /**
     * the Knowledge Base
     */
    @ManyToOne
    @JoinColumn(name = "kbase_id")
    private KnowledgeBase base;
}
