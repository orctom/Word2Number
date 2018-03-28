package utils;

import enums.FormatEnum;
import enums.SimplifyEnum;
import enums.WordEnum;
import exception.IllegalInputException;
import model.Segment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static enums.TypeEnum.*;
import static enums.WordEnum.*;

public class Word2Number {

  private static List<String> split(String text) {
    return Arrays.asList(text.toLowerCase().split("\\s+"));
  }

  public static String replace(String source) {
    return replace(source, SimplifyEnum.NONE);
  }

  public static String replace(String source, SimplifyEnum simplifyStandard) {
    List<String> sourceList = split(source);
    StringBuilder sb = new StringBuilder();
    int sourceListSize = sourceList.size();
    for (int start = 0; start < sourceListSize; ) {
      if (start != 0) {
        sb.append(' ');
      }

      // append  text till find the fist number
      String word = sourceList.get(start);
      if (WordEnum.isNotNumber(word)) {
        sb.append(word);
        start++;
        continue;
      }

      // find the end of the number
      int end = start + 1;
      for (; end < sourceListSize; end++) {
        if (WordEnum.isNotNumber(sourceList.get(end))) {
          break;
        }
      }

      // process the numbers
      List<Segment> resultList;
      List<String> numbers = sourceList.subList(start, end);
      resultList = markSubString(numbers);
      resultList.forEach(sub ->
          sb
              .append(sub.isNumber() ? transform(sub.getWords(), false, simplifyStandard) : sub.getWords().get(0))
              .append(" "));

      sb.deleteCharAt(sb.length() - 1);
      start = end;
    }
    return sb.toString();
  }

  public static String transform(String source) {
    return transform(source, true, SimplifyEnum.NONE);
  }


  public static String transform(String source, boolean needCheck, SimplifyEnum simplifyStandard) {
    return transform(split(source), needCheck, simplifyStandard);
  }

  public static String transform(List<String> sourceList, boolean needCheck, SimplifyEnum simplifyStandard) {
    List<WordEnum> wordList = map(sourceList);
    if (needCheck) {
      check(sourceList);
    }
    return calculate(format(wordList), simplifyStandard);
  }

  private static long getScale(long arg) {
    for (long i = 1; ; i++) {
      double divisor = Math.pow(10, i);
      if ((double) arg / divisor < 1) {
        return i;
      }
    }
  }

  private static void check(List<String> sourceList) {
    if (!isValidNumber(sourceList)) {
      throw new IllegalInputException();
    }
  }

  private static boolean isValidNumber(List<String> sourceList) {
    List<WordEnum> wordList = map(sourceList);
    int dotCount = 0;
    for (int i = 0; i < sourceList.size(); i++) {
      switch (wordList.get(i).getType()) {
        case NUMBER: {
          break;
        }
        case SCALE: {
          if (dotCount > 0) {
            return false;
          }
          break;
        }
        case SYMBOL: {
          if (DOT.same(wordList.get(i))) {
            if (++dotCount == 2 || i == sourceList.size() - 1 || NUMBER != wordList.get(i + 1).getType()) {
              return false;
            }
          } else if (MINUS.same(wordList.get(i)) || AND.same(wordList.get(i))) {
            if (i == sourceList.size() - 1 || dotCount > 0) {
              return false;
            } else if ((MINUS.same(wordList.get(i)) && MINUS.same(wordList.get(i + 1))) ||
                (AND.same(wordList.get(i)) && AND.same(wordList.get(i + 1)))) {
              return false;
            } else if (i == 0) {
              if (SCALE == wordList.get(i + 1).getType() ||
                  (DOT.same(wordList.get(i + 1)) && sourceList.size() == 2)) {
                return false;
              }
            } else {
              if (SYMBOL == wordList.get(i + 1).getType() || SCALE == wordList.get(i + 1).getType()) {
                return false;
              }
            }
          }
          break;
        }
        default: {
          return false;
        }
      }
    }
    return true;
  }

  private static List<Segment> markSubString(List<String> sourceList) {
    if (sourceList.isEmpty()) {
      return Collections.emptyList();
    }

    List<Segment> prefixList = new ArrayList<>();
    List<WordEnum> wordList = map(sourceList);
    List<String> item = new ArrayList<>();
    Segment itemMap;
    Segment illegalItemMap = null;
    int index = 0;
    int dotCount = 0;
    boolean finish = false;
    while (!finish) {
      String word = sourceList.get(index);
      switch (wordList.get(index).getType()) {
        case NUMBER: {
          item.add(word);
          break;
        }
        case SCALE: {
          if (dotCount == 0) {
            item.add(word);
          } else {
            illegalItemMap = new Segment(word, false);
            finish = true;
          }
          break;
        }
        case SYMBOL: {
          if (DOT.same(wordList.get(index))) {
            if (++dotCount == 2 || index == sourceList.size() - 1 || !NUMBER.equals(wordList.get(index + 1).getType())) {
              illegalItemMap = new Segment(word, false);
              finish = true;
            } else {
              item.add(word);
            }
          } else if (MINUS.same(wordList.get(index)) || AND.same(wordList.get(index))) {
            if (index == sourceList.size() - 1 || dotCount > 0) {
              illegalItemMap = new Segment(word, false);
              finish = true;
            } else if (MINUS.same(wordList.get(index)) && MINUS.same(wordList.get(index + 1)) || AND.same(wordList.get(index)) && AND.same(wordList.get(index + 1))) {
              illegalItemMap = new Segment(word, false);
              finish = true;
            } else if (index == 0) {
              if (NUMBER.equals(wordList.get(index + 1).getType()) || DOT.same(wordList.get(index + 1)) && sourceList.size() > 2) {
                item.add(word);
              } else if (SCALE.equals(wordList.get(index + 1).getType()) || DOT.same(wordList.get(index + 1)) && sourceList.size() == 2) {
                illegalItemMap = new Segment(word, false);
                finish = true;
              }
            } else {
              if (SYMBOL.equals(wordList.get(index + 1).getType()) || SCALE.equals(wordList.get(index + 1).getType())) {
                illegalItemMap = new Segment(word, false);
                finish = true;
              }
            }
          }
          break;
        }
      }
      index++;
      if (index == sourceList.size()) {
        finish = true;
      }
    }

    if (!item.isEmpty()) {
      itemMap = new Segment(item, true);
      prefixList.add(itemMap);
    }
    if (null != illegalItemMap) {
      prefixList.add(illegalItemMap);
    }

    List<Segment> suffixList = markSubString(sourceList.subList(index, sourceList.size()));
    prefixList.addAll(suffixList);
    return prefixList;
  }

  private static FormatEnum getFormatType(List<WordEnum> list) {
    boolean hasDot = false;
    for (WordEnum word : list) {
      if (DOT.same(word)) {
        if (!list.get(list.size() - 1).same(DOT)) {
          hasDot = true;
        }
      }
    }
    return hasDot ? FormatEnum.DECIMAL : FormatEnum.ROUND;
  }

  private static List<WordEnum> map(List<String> list) {
    return list.stream().map(WordEnum::getByKey).collect(Collectors.toList());
  }

  /**
   * grammar analysis
   * reference: https://en.wikipedia.org/wiki/English_numerals
   */
  private static List<Data> format(List<WordEnum> list) {
    FormatEnum formatType = getFormatType(list);
    List<WordEnum> roundList = new ArrayList<>();
    List<Data> resultList = new ArrayList<>();

    for (int i = 0; i < list.size() - 1; i++) {
      if (DOT.same(list.get(i))) {
        roundList.addAll(list.subList(0, i));
        if (roundList.size() == 0) {
          roundList.add(WordEnum.ZERO);
        }
        resultList.addAll(list.subList(i + 1, list.size()).stream()
            .map(x -> new Data(x.getValue(), -1L))
            .collect(Collectors.toList()));
        break;
      }
    }

    if (FormatEnum.ROUND == formatType) {
      roundList = list;
    }

    // phone numbers and years
    if (roundList.stream()
        .map(word -> NUMBER == word.getType())
        .reduce((x, y) -> x && y)
        .orElse(false)) {
      for (int i = roundList.size() - 1; i >= 0; i--) {
        if (roundList.get(i).getValue() >= 20 && i != roundList.size() - 1 && !WordEnum.ZERO.same(roundList.get(i + 1))) {
          resultList.get(0).arg += roundList.get(i).getValue();
          resultList.get(0).scale = getScale(resultList.get(0).arg);
        } else {
          resultList.add(0, new Data(roundList.get(i).getValue(), roundList.get(i).getScale()));
        }
      }

    } else {
      Data data = new Data(0L, 1L);
      long arg = 0;
      long scale = 1;
      for (int i = 0; i < roundList.size(); i++) {
        WordEnum temp = roundList.get(Long.valueOf(i).intValue());
        switch (temp.getType()) {
          case NUMBER: {
            arg += temp.getValue();
            scale = getScale(arg);
            break;
          }
          case SCALE: {
            if (scale + temp.getScale() - 1 >= data.scale) {
              if (data.arg == 0 && arg == 0) {
                data.arg = 1L;
              }
              data.arg = (data.arg + arg) * temp.getValue();
              data.scale = getScale(data.arg);
              arg = 0;
              scale = 1;
            } else {
              if (arg == 0) {
                arg = 1;
              }
              arg = arg * temp.getValue();
              scale = getScale(arg);
            }
            break;
          }
        }
      }

      data.arg += arg;
      data.scale = getScale(data.arg);
      resultList.add(0, data);
      if (MINUS.same(roundList.get(0)) &&
          (FormatEnum.DECIMAL == formatType || !(roundList.size() == 2 && roundList.get(1).getValue().equals(0L)))) {
        resultList.add(0, Data.MINUS_SYMBOL_DATA);
      }
    }
    return resultList;
  }

  private static String calculate(List<Data> list, SimplifyEnum simplifyStandard) {
    StringBuilder sb = new StringBuilder();
    if (Data.MINUS_SYMBOL_DATA.equals(list.get(0))) {
      sb.append('-');
    }

    list.stream().filter(data -> data.scale > 0).map(data -> data.arg.toString()).forEach(sb::append);
    if (list.stream().anyMatch(data -> data.scale < 0)) {
      sb.append('.');
      list.stream().filter(data -> data.scale < 0).map(data -> data.arg.toString()).forEach(sb::append);
      return sb.toString();
    } else {
      Double numberValue = Double.parseDouble(sb.toString());
      if (simplifyStandard != SimplifyEnum.NONE &&
          getScale(Long.valueOf(sb.toString())) >= getScale(simplifyStandard.getValue())) {
        numberValue /= simplifyStandard.getValue();
        final String numberString = numberValue.toString();
        int dotIndex = numberString.indexOf(".");
        String roundPart = numberString.substring(0, dotIndex);
        String decimalPart = numberString.substring(dotIndex + 1);
        decimalPart = decimalPart.equals("0") ? "" : "." + decimalPart;
        return roundPart + decimalPart + " " + simplifyStandard.getText();
      } else {
        return sb.toString();
      }
    }
  }

  private static final class Data {
    Long arg;
    Long scale;
    static final Data MINUS_SYMBOL_DATA = new Data(0L, 0L);

    Data(Long arg, Long scale) {
      this.arg = arg;
      this.scale = scale;
    }
  }
}
