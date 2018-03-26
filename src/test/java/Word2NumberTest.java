import enums.SimplifyEnum;
import exception.IllegalInputException;
import org.junit.Test;
import utils.Word2Number;

import java.util.Collections;
import java.util.List;

public class Word2NumberTest {
  @Test
  public void testTransform() {
    String testString;
    String[] testStringArray = new String[]{"one", "o", "zero", "five", "nine"};
    System.out.println();
    System.out.println("test transform");

    testString = Word2Number.transform(Collections.singletonList("zero"));
    System.out.println(testString);
    assert "0".equals(testString);

    testString = Word2Number.transform(Collections.singletonList("zero"), false, SimplifyEnum.NONE);
    System.out.println(testString);
    assert "0".equals(testString);

    testString = Word2Number.transform(testStringArray);
    System.out.println(testString);
    assert "10059".equals(testString);

    testString = Word2Number.transform(testStringArray, false, SimplifyEnum.NONE);
    System.out.println(testString);
    assert "10059".equals(testString);

    testString = Word2Number.transform("Hundred", false, SimplifyEnum.NONE);
    System.out.println(testString);
    assert "100".equals(testString);

    testString = Word2Number.transform("Minus One Hundred Dot One Two Three Four Five", true, SimplifyEnum.NONE);
    System.out.println(testString);
    assert "-100.12345".equals(testString);

    testString = Word2Number.transform("Dot One Two Three Four Five");
    System.out.println(testString);
    assert "0.12345".equals(testString);

    testString = Word2Number.transform("one two");
    System.out.println(testString);
    assert "12".equals(testString);

    testString = Word2Number.transform("one thousand and five");
    System.out.println(testString);
    assert "1005".equals(testString);

    testString = Word2Number.transform("two thousand and sixteen");
    System.out.println(testString);
    assert "2016".equals(testString);

    testString = Word2Number.transform("nineteen ninety nine");
    System.out.println(testString);
    assert "1999".equals(testString);

    testString = Word2Number.transform("six billion");
    System.out.println(testString);
    assert "6000000000".equals(testString);

    testString = Word2Number.transform("seven million");
    System.out.println(testString);
    assert "7000000".equals(testString);

    testString = Word2Number.transform("point nine");
    System.out.println(testString);
    assert "0.9".equals(testString);

    testString = Word2Number.transform("one point nine");
    System.out.println(testString);
    assert "1.9".equals(testString);

    testString = Word2Number.transform("two point nine five four");
    System.out.println(testString);
    assert "2.954".equals(testString);

    testString = Word2Number.transform("eight thousand nine hundred and thirty one");
    System.out.println(testString);
    assert "8931".equals(testString);

    testString = Word2Number.transform("one hundred and twenty nine thousand million");
    System.out.println(testString);
    assert "129000000000".equals(testString);

    testString = Word2Number.transform("one million and eighty nine hundred hundred");
    System.out.println(testString);
    assert "1890000".equals(testString);

    testString = Word2Number.transform("hundred hundred");
    System.out.println(testString);
    assert "10000".equals(testString);

    testString = Word2Number.transform("thousand hundred");
    System.out.println(testString);
    assert "1100".equals(testString);

    testString = Word2Number.transform("negative point five");
    System.out.println(testString);
    assert "-0.5".equals(testString);

    testString = Word2Number.transform("fifteen");
    System.out.println(testString);
    assert "15".equals(testString);

    testString = Word2Number.transform("thousand hundred", true, SimplifyEnum.HUNDRED);
    System.out.println(testString);
    assert "11 hundred".equals(testString);

    testString = Word2Number.transform("thousand hundred", true, SimplifyEnum.THOUSAND);
    System.out.println(testString);
    assert "1.1 thousand".equals(testString);

    testString = Word2Number.transform("two million", true, SimplifyEnum.MILLION);
    System.out.println(testString);
    assert "2 million".equals(testString);

    testString = Word2Number.transform("eleven billion", true, SimplifyEnum.BILLION);
    System.out.println(testString);
    assert "11 billion".equals(testString);
  }

  @Test
  public void testExtract() {
    List<List<String>> resultList;
    System.out.println();
    System.out.println("test extract");

    resultList = Word2Number.extract("the nineteen ninety nine was past nineteen years");
    resultList.forEach(x -> {
      x.forEach(y -> System.out.print(y + ' '));
      System.out.println();
    });
    assert resultList.size() == 2;
  }

  @Test
  public void testReplace() {
    String result;
    System.out.println();
    System.out.println("test replace");

    result = Word2Number.replace("the nineteen ninety nine has past nineteen years");
    System.out.println(result);
    assert "the 1999 has past 19 years".equals(result);

    result = Word2Number.replace("the whether will be hotter in next twenty four hours");
    System.out.println(result);
    assert "the whether will be hotter in next 24 hours".equals(result);

    result = Word2Number.replace("the score is point nine");
    System.out.println(result);
    assert "the score is 0.9".equals(result);

    result = Word2Number.replace("the score is one point nine");
    System.out.println(result);
    assert "the score is 1.9".equals(result);

    result = Word2Number.replace("the score is two point nine five four");
    System.out.println(result);
    assert "the score is 2.954".equals(result);

    result = Word2Number.replace("you got one point");
    System.out.println(result);
    assert "you got 1 point".equals(result);

    result = Word2Number.replace("minus one point five plus minus three point five equals minus five");
    System.out.println(result);
    assert "-1.5 plus -3.5 equals -5".equals(result);

    result = Word2Number.replace("i have ten thousand three hundred point");
    System.out.println(result);
    assert "i have 10300 point".equals(result);

    result = Word2Number.replace("i have ten thousand three hundred point thousand point");
    System.out.println(result);
    assert "i have 10300 point 1000 point".equals(result);

    result = Word2Number.replace("i have two point three billion dollars");
    System.out.println(result);
    assert "i have 2.3 billion dollars".equals(result);

    result = Word2Number.replace("um could you please back up sir you are kinda too close");
    System.out.println(result);
    assert "um could you please back up sir you are kinda too close".equals(result);

    result = Word2Number.replace("absolute zero is taken as minus two hundred and seventy three point one five degree celsius on the celsius scale");
    System.out.println(result);
    assert "absolute 0 is taken as -273.15 degree celsius on the celsius scale".equals(result);

    result = Word2Number.replace("random sentence minus point three four hundred point");
    System.out.println(result);
    assert "random sentence -0.34 hundred point".equals(result);

    result = Word2Number.replace("minus hundred");
    System.out.println(result);
    assert "minus 100".equals(result);

    result = Word2Number.replace("minus minus one");
    System.out.println(result);
    assert "minus -1".equals(result);

    result = Word2Number.replace("minus minus point");
    System.out.println(result);
    assert "minus minus point".equals(result);

    result = Word2Number.replace("you may get negative point");
    System.out.println(result);
    assert "you may get negative point".equals(result);

    result = Word2Number.replace("minus point");
    System.out.println(result);
    assert "minus point".equals(result);

    result = Word2Number.replace("i will see you at five and you had better not to be late");
    System.out.println(result);
    assert "i will see you at 5 and you had better not to be late".equals(result);

    result = Word2Number.replace("minus one point five minus minus three point five equals two");
    System.out.println(result);
    assert "-1.5 minus -3.5 equals 2".equals(result);

    result = Word2Number.replace("minus one and minus point five equals minus one point five");
    System.out.println(result);
    assert "-1 and -0.5 equals -1.5".equals(result);
  }

  @Test
  public void testException() {
    try {
      Word2Number.transform("asd");
    } catch (IllegalInputException e) {

    }
  }
}
