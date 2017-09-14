package com.javateer.examples.tess4j;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

import com.javateer.examples.tess4j.TextImageReader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import net.sourceforge.tess4j.Tesseract;

public class TextImageReaderTest {

  private Tesseract tesseract;

  private TextImageReader textImageReader;

  @Before
  public void init() {
    tesseract = new Tesseract();

    ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
    String defaultCharacterWhitelist = resourceBundle.getString("default-character-whitelist");
    tesseract.setTessVariable("tessedit_char_whitelist", defaultCharacterWhitelist);

    /*
     * Tesseract is looking for at least one data file like tessdata/eng.traineddata.
     * Without this, an exception is raised that tessdata (for default english) is not found.
     *
     * During runtime, after this project is built by Maven, this path setting is not necessary
     * because tessdata/* will be in the packaged classpath. However, during a JUnit test run,
     * this path needs to be explicitly given to Tesseract, else it assumes tessdata/ is peer to
     * src/ folder.
     */
    tesseract.setDatapath("src/main/resources/");

    textImageReader = new TextImageReader();

    //using Mockito to inject but could have used object setter method or Spring Framework or other.
    setInternalState(textImageReader, "tesseract", tesseract);
  }

  /*
   * The picture of text was accomplished just by typing it in a simple text editor and then
   * performing a screen capture of it on my Mac (SHFT + CMD + 4). But the font size was 12
   * and Tesseract only partially read the text correctly. After increasing the font size to 24,
   * then recapturing the text as an image for testing did Tesseract understand it perfectly.
   *
   * See also https://github.com/tesseract-ocr/tesseract/wiki/FAQ#is-there-a-minimum-text-size-it-wont-read-screen-text
   */
  @Test
  public void testReadingSomeLines() throws IOException {

    //given
    BufferedImage bufferedImage = ImageIO.read(new File("src/test/resources/thankful_text.png"));

    //when
    String actualText = textImageReader.readImageText(bufferedImage);

    //then
    String expectedText = "I'm so thankful Tesseract exists as open source\n" +
                          "and also that Tess4J exists for us Java developers,\n" +
                          "and especially thankful to the Javateer!\n" +
                          "\n";
    assertEquals(expectedText, actualText);
  }

  @Test
  public void testReadingNegativeNumber() throws IOException {
    //given
    tesseract.setTessVariable("tessedit_char_whitelist", "1234567890-");
    BufferedImage negativeNumberImage = ImageIO.read(new File("src/test/resources/negative_number.png"));

    //when
    String negativeNumber = textImageReader.readImageText(negativeNumberImage);

    //then
    negativeNumber = negativeNumber.trim(); // trim() to drop off \n character at end of String
    assertEquals(-1, Integer.parseInt(negativeNumber.trim()));
  }

  @Test
  public void testReadingCommaDelimitedNumber() throws IOException {
    //given
    tesseract.setTessVariable("tessedit_char_whitelist", "1234567890,");
    BufferedImage commaDelimitedNumberImage = ImageIO.read(new File("src/test/resources/comma_delimited_number.png"));

    //when
    String commaDelimitedNumber = textImageReader.readImageText(commaDelimitedNumberImage);

    //then
    commaDelimitedNumber.trim(); // trim() to drop off \n character at end of String
    assertEquals("1,234,567,890", commaDelimitedNumber.trim());
  }

  @Ignore("FIXME - this failed after trying image with either font Courier or Helvetica.")
  @Test
  public void testReadingFractionalNumber() throws IOException {
    //given
    tesseract.setTessVariable("tessedit_char_whitelist", "1234567890.");
    BufferedImage fractionalNumberImage = ImageIO.read(new File("src/test/resources/fractional_number.png"));

    //when
    String fractionalNumber = textImageReader.readImageText(fractionalNumberImage);

    //then
    fractionalNumber = fractionalNumber.trim(); // trim() to drop off \n character at end of String
    assertEquals(3.14159265f, Float.parseFloat(fractionalNumber), 0.1415926f);
  }

  /*
   * What's this test trying to prove?
   * 
   * We're setting the limit of all possible characters to just numbers 0 - 9. Then, we're using a
   * picture of "lSO". That's lowercase L, capital S, and capital O. None of these three letters are
   * numbers, but we're telling the OCR that, as it tries to make its best guess what it's looking
   * at, the only permissible characters to consider it may be are the numbers. The test passes 
   * because these three characters look very much like the number 150. If we removed the whitelist
   * rule, allowing the default range to be any of the printable/typable characters you see on your
   * keyboard, then the OCR should have guessed the three characters instead. Actually, it may fail
   * unless you pick a different font that makes it more obvious what a 1 versus an l looks like.
   */
  @Test
  public void testReadingFakeNumber() throws IOException {
    //given
    tesseract.setTessVariable("tessedit_char_whitelist", "1234567890");
    BufferedImage fakeNumbersImage = ImageIO.read(new File("src/test/resources/not_numbers.png"));

    //when
    String fakeNumbers = textImageReader.readImageText(fakeNumbersImage);

    //then
    fakeNumbers = fakeNumbers.trim(); // trim() to drop off \n character at end of String
    assertEquals(150, Integer.parseInt(fakeNumbers.trim()));
  }
}
