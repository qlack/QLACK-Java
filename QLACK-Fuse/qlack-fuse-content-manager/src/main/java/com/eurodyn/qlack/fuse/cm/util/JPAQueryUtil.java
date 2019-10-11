package com.eurodyn.qlack.fuse.cm.util;

import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.model.QNode;
import com.eurodyn.qlack.fuse.cm.model.QNodeAttribute;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * A Util class for the DocumentService class operations.
 *
 * @author European Dynamics SA
 */
public class JPAQueryUtil {

  private static final QNode Q_NODE = QNode.node;

  private static final QNodeAttribute Q_NODE_ATTRIBUTE = new QNodeAttribute("nodeAttribute");

  /**
   * Default Constructor
   */
  private JPAQueryUtil() {
  }

  /**
   * Creates a new JPAQuery to retrieve the nodes (folder/file) which have a required node attribute
   * name.
   *
   * @param em the Entity Manager instance
   * @param name the name to search for
   * @param parentNodeID the id of the parent folder
   * @return the created query
   */
  public static JPAQuery<Node> createJpaQueryForName(EntityManager em, String name,
      String parentNodeID) {
    return new JPAQueryFactory(em)
        .selectFrom(Q_NODE).innerJoin(Q_NODE.attributes, Q_NODE_ATTRIBUTE)
        .where(Q_NODE.parent.id.eq(parentNodeID)
            .and(Q_NODE_ATTRIBUTE.name.eq(CMConstants.ATTR_NAME))
            .and(Q_NODE_ATTRIBUTE.value.eq(name)));
  }

  /**
   * Creates a new JPAQuery to search all the nodes that their name is contained in a list of
   * strings.
   *
   * @param em the Entity Manager instance
   * @param fileNames the names to search for
   * @param parentNodeID the id of the parent folder
   * @return the created query
   */
  public static JPAQuery<Node> createJpaQueryForMultipleNames(EntityManager em,
      List<String> fileNames,
      String parentNodeID) {
    return new JPAQueryFactory(em)
        .selectFrom(Q_NODE).innerJoin(Q_NODE.attributes, Q_NODE_ATTRIBUTE)
        .where(Q_NODE.parent.id.eq(parentNodeID)
            .and(Q_NODE_ATTRIBUTE.name.eq(CMConstants.ATTR_NAME))
            .and(Q_NODE_ATTRIBUTE.value.in(fileNames)));
  }

}
