# Tess4J Example

This is a simple, _working_ example of optical character recognition (OCR). This is about showing
a picture of text to the software and the software determining what the text is, returned in the
form of a java.lang.String object.

The most popular, open source solution is [Tesseract](https://github.com/tesseract-ocr/tesseract "Tesseract") &copy;,
but this is a program written in C code.
For us Java Developers, we have a few JNI wrappers already made available to the community for
free use. This codebase demonstrates usage of what may be the most popular one; [Tess4J](http://tess4j.sourceforge.net/) &copy;.

This codebase demonstrating Tess4J came about by the frustration of not finding a simple, _working_ example.
The Tess4J site has you pull down old code meant to be built with Ant instead of Maven or Gradle.
Even after doing so, the Tesseract libraries needed for Mac were absent; only Windows and Linux libraries
where bundled into the Tess4J codebase.

Then the [discovery](http://www.microshell.com/programming/java/performing-optical-character-recognition-in-java/)
of Tess4J available from Maven Central resolved the Mac-specific Tesseract libraries. Still, trying
to follow the example and advice from that blog post resulted in runtime exceptions,
due to the default Tesseract english data file not being found. That blog's advice is a hack and
even the advice by the Tess4J author himself in that blog's feedback comment is not desirable. This codebase
demonstrates encapsulating the Tesseract data file so that the code _just works_ without bothering with external
conditions like setting a special system environment variable.

I hope this is reference code you've been looking for. I sure wish someone came along before me
and volunteered this for us all! I hardly explored the Tess4J API. Therefore, I invite you to help grow
this codebase with more test-proven examples and explanatory comments, so show me your pull requests.
