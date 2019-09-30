package com.eurodyn.qlack.fuse.imaging.util;

import com.eurodyn.qlack.fuse.imaging.dto.DotsPerInch;
import com.eurodyn.qlack.fuse.imaging.exception.ImagingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility methods to be used by various imaging functions.
 *
 * @author European Dynamics SA.
 */
public class ImagingUtil {

  /**
   * Default constructor
   */
  private ImagingUtil() {
  }

  /**
   * Find the horizontal and vertical DPIs of the image. In case this information is not present in
   * the image, 0 is returned instead.
   *
   * @param image The image to check for DPI metadata.
   * @return Returns the DPIs of the image.
   * @throws IOException When the image could not be properly parsed to extract DPIs.
   */
  public static DotsPerInch getDPI(byte[] image) throws IOException {
    DotsPerInch dotsPerInch = null;

    try (ImageInputStream stream = ImageIO
        .createImageInputStream(new ByteArrayInputStream(image))) {
      Iterator it = ImageIO.getImageReaders(stream);
      if (!it.hasNext()) {
        throw new ImagingException("Could not find a reader for the image.");
      }
      ImageReader reader = (ImageReader) it.next();
      reader.setInput(stream);
      IIOMetadata meta = reader.getImageMetadata(0);
      IIOMetadataNode root = (IIOMetadataNode) meta.getAsTree("javax_imageio_1.0");
      NodeList nodes = root.getElementsByTagName("HorizontalPixelSize");
      dotsPerInch = new DotsPerInch();
      if (nodes.getLength() > 0) {
        IIOMetadataNode dpcWidth = (IIOMetadataNode) nodes.item(0);
        NamedNodeMap nnm = dpcWidth.getAttributes();
        Node item = nnm.item(0);
        int xDPI = Math.round(25.4f / Float.parseFloat(item.getNodeValue()));
        dotsPerInch.setHorizontal(xDPI);
      } else {
        dotsPerInch.setHorizontal(0);
      }
      if (nodes.getLength() > 0) {
        nodes = root.getElementsByTagName("VerticalPixelSize");
        IIOMetadataNode dpcHeight = (IIOMetadataNode) nodes.item(0);
        NamedNodeMap nnm = dpcHeight.getAttributes();
        Node item = nnm.item(0);
        int yDPI = Math.round(25.4f / Float.parseFloat(item.getNodeValue()));
        dotsPerInch.setVertical(yDPI);
      } else {
        dotsPerInch.setVertical(0);
      }
    }

    return dotsPerInch;
  }

  /**
   * Finds a reader for the provided image and return its type.
   *
   * @param image The image to check.
   * @return Returns the type of the reader for this image, e.g. png.
   * @throws IOException When the image could not be properly parsed to get its type.
   */
  public static String getType(byte[] image) throws IOException {
    String type = null;
    ImageInputStream stream = ImageIO.createImageInputStream(new ByteArrayInputStream(image));
    Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
    if (readers.hasNext()) {
      type = readers.next().getFormatName();
    }

    return type;
  }
}
