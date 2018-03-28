import enums.SimplifyEnum;
import exception.IllegalInputException;
import org.junit.Test;
import utils.Word2Number;

import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class Word2NumberTest {

  @Test
  public void testTransform() {
    String testString;

    testString = Word2Number.transform("Hundred", false, SimplifyEnum.NONE);
    System.out.println(testString);
    assertEquals("100", testString);

    testString = Word2Number.transform("Minus One Hundred Dot One Two Three Four Five", true, SimplifyEnum.NONE);
    System.out.println(testString);
    assertEquals("-100.12345", testString);

    testString = Word2Number.transform("Dot One Two Three Four Five");
    System.out.println(testString);
    assertEquals("0.12345", testString);

    testString = Word2Number.transform("one two");
    System.out.println(testString);
    assertEquals("12", testString);

    testString = Word2Number.transform("one thousand and five");
    System.out.println(testString);
    assertEquals("1005", testString);

    testString = Word2Number.transform("two thousand and sixteen");
    System.out.println(testString);
    assertEquals("2016", testString);

    testString = Word2Number.transform("nineteen ninety nine");
    System.out.println(testString);
    assertEquals("1999", testString);

    testString = Word2Number.transform("six billion");
    System.out.println(testString);
    assertEquals("6000000000", testString);

    testString = Word2Number.transform("seven million");
    System.out.println(testString);
    assertEquals("7000000", testString);

    testString = Word2Number.transform("point nine");
    System.out.println(testString);
    assertEquals("0.9", testString);

    testString = Word2Number.transform("one point nine");
    System.out.println(testString);
    assertEquals("1.9", testString);

    testString = Word2Number.transform("two point nine five four");
    System.out.println(testString);
    assertEquals("2.954", testString);

    testString = Word2Number.transform("eight thousand nine hundred and thirty one");
    System.out.println(testString);
    assertEquals("8931", testString);

    testString = Word2Number.transform("one hundred and twenty nine thousand million");
    System.out.println(testString);
    assertEquals("129000000000", testString);

    testString = Word2Number.transform("one million and eighty nine hundred hundred");
    System.out.println(testString);
    assertEquals("1890000", testString);

    testString = Word2Number.transform("hundred hundred");
    System.out.println(testString);
    assertEquals("10000", testString);

    testString = Word2Number.transform("thousand hundred");
    System.out.println(testString);
    assertEquals("1100", testString);

    testString = Word2Number.transform("negative point five");
    System.out.println(testString);
    assertEquals("-0.5", testString);

    testString = Word2Number.transform("fifteen");
    System.out.println(testString);
    assertEquals("15", testString);

    testString = Word2Number.transform("thousand hundred", true, SimplifyEnum.HUNDRED);
    System.out.println(testString);
    assertEquals("11 hundred", testString);

    testString = Word2Number.transform("thousand hundred", true, SimplifyEnum.THOUSAND);
    System.out.println(testString);
    assertEquals("1.1 thousand", testString);

    testString = Word2Number.transform("two million", true, SimplifyEnum.MILLION);
    System.out.println(testString);
    assertEquals("2 million", testString);

    testString = Word2Number.transform("eleven billion", true, SimplifyEnum.BILLION);
    System.out.println(testString);
    assertEquals("11 billion", testString);

    testString = Word2Number.transform("hundred hundred", true, SimplifyEnum.THOUSAND);
    System.out.println(testString);
    assertEquals("10 thousand", testString);
  }

  @Test
  public void testReplace() {
    String result;
    System.out.println();
    System.out.println("test replace");

    result = Word2Number.replace("the nineteen ninety nine has past nineteen years");
    System.out.println(result);
    assertEquals("the 1999 has past 19 years", result);

    result = Word2Number.replace("the whether will be hotter in next twenty four hours");
    System.out.println(result);
    assertEquals("the whether will be hotter in next 24 hours", result);

    result = Word2Number.replace("the score is point nine");
    System.out.println(result);
    assertEquals("the score is 0.9", result);

    result = Word2Number.replace("the score is one point nine");
    System.out.println(result);
    assertEquals("the score is 1.9", result);

    result = Word2Number.replace("the score is two point nine five four");
    System.out.println(result);
    assertEquals("the score is 2.954", result);

    result = Word2Number.replace("you got one point");
    System.out.println(result);
    assertEquals("you got 1 point", result);

    result = Word2Number.replace("minus one point five plus minus three point five equals minus five");
    System.out.println(result);
    assertEquals("-1.5 plus -3.5 equals -5", result);

    result = Word2Number.replace("i have ten thousand three hundred point");
    System.out.println(result);
    assertEquals("i have 10300 point", result);

    result = Word2Number.replace("i have ten thousand three hundred point thousand point");
    System.out.println(result);
    assertEquals("i have 10300 point 1000 point", result);

    result = Word2Number.replace("i have two point three billion dollars");
    System.out.println(result);
    assertEquals("i have 2.3 billion dollars", result);

    result = Word2Number.replace("um could you please back up sir you are kinda too close");
    System.out.println(result);
    assertEquals("um could you please back up sir you are kinda too close", result);

    result = Word2Number.replace("absolute zero is taken as minus two hundred and seventy three point one five degree celsius on the celsius scale");
    System.out.println(result);
    assertEquals("absolute 0 is taken as -273.15 degree celsius on the celsius scale", result);

    result = Word2Number.replace("random sentence minus point three four hundred point");
    System.out.println(result);
    assertEquals("random sentence -0.34 hundred point", result);

    result = Word2Number.replace("minus hundred");
    System.out.println(result);
    assertEquals("minus 100", result);

    result = Word2Number.replace("minus minus one");
    System.out.println(result);
    assertEquals("minus -1", result);

    result = Word2Number.replace("minus minus point");
    System.out.println(result);
    assertEquals("minus minus point", result);

    result = Word2Number.replace("you may get negative point");
    System.out.println(result);
    assertEquals("you may get negative point", result);

    result = Word2Number.replace("minus point");
    System.out.println(result);
    assertEquals("minus point", result);

    result = Word2Number.replace("i will see you at five and you had better not to be late");
    System.out.println(result);
    assertEquals("i will see you at 5 and you had better not to be late", result);

    result = Word2Number.replace("minus one point five minus minus three point five equals two");
    System.out.println(result);
    assertEquals("-1.5 minus -3.5 equals 2", result);

    result = Word2Number.replace("minus one and minus point five equals minus one point five");
    System.out.println(result);
    assertEquals("-1 and -0.5 equals -1.5", result);

    result = Word2Number.replace("i went to high school on two thousand o four");
    System.out.println(result);
    assertEquals("i went to high school on 2004", result);

    result = Word2Number.replace("point point point");
    System.out.println(result);
    assertEquals("point point point", result);

    result = Word2Number.replace("point thousand");
    System.out.println(result);
    assertEquals("point 1000", result);
  }

  @Test(expected = IllegalInputException.class)
  public void testException1() {
    Word2Number.transform("asd");
    fail();
  }

  @Test(expected = IllegalInputException.class)
  public void testException2() {
    Word2Number.transform("asd");
    fail();
  }

  @Test(expected = IllegalInputException.class)
  public void testException3() {
    Word2Number.transform("point thousand");
    fail();
  }

  @Test(expected = IllegalInputException.class)
  public void testException4() {
    Word2Number.transform("point one point one point");
    fail();
  }

  @Test(expected = IllegalInputException.class)
  public void testException5() {
    Word2Number.transform("one point");
    fail();
  }

  @Test(expected = IllegalInputException.class)
  public void testException6() {
    Word2Number.transform("minus minus one");
    fail();
  }

  @Test(expected = IllegalInputException.class)
  public void testException7() {
    Word2Number.transform("minus dot");
    fail();
  }

  @Test(expected = IllegalInputException.class)
  public void testException9() {
    Word2Number.transform("two point five thousand");
    fail();
  }

  @Test(expected = IllegalInputException.class)
  public void testException10() {
    Word2Number.transform("one minus minus one");
    fail();
  }

  @Test(expected = IllegalInputException.class)
  public void testException11() {
    Word2Number.transform("one hundred point point five six");
    fail();
  }

  @Test(expected = IllegalInputException.class)
  public void testException13() {
    Word2Number.transform("one point one minus one hundred");
    fail();
  }

  @Test(expected = IllegalInputException.class)
  public void testException14() {
    Word2Number.transform("one minus hundred");
    fail();
  }
}