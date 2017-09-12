package com.javateer.examples.tess4j;

import java.awt.image.BufferedImage;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * A very simple class that simply puts to use the Tess4J solution for Tesseract.
 */
public class TextImageReader {

  private Tesseract tesseract;

  public String readImageText(BufferedImage bufferedImage) {
    try {
      return tesseract.doOCR(bufferedImage);
    }
    catch (TesseractException e) {
      throw new RuntimeException(e);
    }
  }
}
