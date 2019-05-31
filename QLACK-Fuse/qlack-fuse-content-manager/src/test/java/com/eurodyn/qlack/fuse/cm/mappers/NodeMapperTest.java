// package com.eurodyn.qlack.fuse.cm.mappers;
//
// import static org.junit.Assert.assertEquals;
//
// import com.eurodyn.qlack.fuse.cm.InitTestValues;
// import com.eurodyn.qlack.fuse.cm.dto.NodeDTO;
// import com.eurodyn.qlack.fuse.cm.model.Node;
// import org.junit.Before;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.mockito.InjectMocks;
// import org.mockito.junit.MockitoJUnitRunner;
//
// /**
//  * @author European Dynamics
//  */
//
// @RunWith(MockitoJUnitRunner.class)
// public class NodeMapperTest {
//
//   @InjectMocks
//   private NodeMapperImpl nodeMapper;
//
//   private InitTestValues initTestValues;
//   private NodeDTO nodeDTO;
//
//   @Before
//   public void init() {
//     initTestValues = new InitTestValues();
//     nodeDTO = initTestValues.createNodeDTO();
//   }
//
//
//   @Test
//   public void testMapper() {
//     Node node = nodeMapper.mapToEntity(nodeDTO);
//     assertEquals(nodeDTO.getId(), node.getId());
//   }
// }
