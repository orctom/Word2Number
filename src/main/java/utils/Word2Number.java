package utils;

import enums.FormatEnum;
import enums.SimplifyEnum;
import enums.WordEnum;
import enums.TypeEnum;
import exception.IllegalInputException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Word2Number {
  public static SimplifyEnum SIMPLIFY_STANDARD = SimplifyEnum.NONE;

  public static List<List<String>> extract(String source) {
    List<String> sourceList = Stream.of(source.toLowerCase().split("[ ]")).collect(Collectors.toList());
    List<List<String>> resultList = new ArrayList<>();
    for (int start = 0; start < sourceList.size(); ) {
      if (!WordEnum.contains(sourceList.get(start))) {
        start++;
      } else {
        for (int end = start + 1; ; end++) {
          if (end == sourceList.size() || !WordEnum.contains(sourceList.get(end))) {
            resultList.add(sourceList.subList(start, end));
            start = end + 1;
            break;
          }
        }
      }
    }
    return resultList;
  }

  public static String replace(String source) {
    List<String> sourceList = Stream.of(source.toLowerCase().split("[ ]")).collect(Collectors.toList());
    StringBuilder sb = new StringBuilder();
    for (int start = 0; start < sourceList.size(); ) {
      if (start != 0) {
        sb.append(' ');
      }
      if (!WordEnum.contains(sourceList.get(start))) {
        sb.append(sourceList.get(start));
        start++;
      } else {
        for (int end = start + 1; ; end++) {
          List<Map<List<String>, Boolean>> resultList;
          if (end == sourceList.size() || !WordEnum.contains(sourceList.get(end))) {
            if (checkWithoutException(sourceList.subList(start, end))) {
              resultList = new ArrayList<>();
              resultList.add(Collections.singletonMap(sourceList.subList(start, end), true));
            } else {
              resultList = markSubString(sourceList.subList(start, end));
            }
            resultList.forEach(sub ->
                sub.forEach((key, value) -> {
                  sb.append(value ? calculate(format(map(key))) : key.get(0));
                  sb.append(' ');
                })
            );
            sb.deleteCharAt(sb.length() - 1);
            start = end;
            break;
          }
        }
      }
    }
    return sb.toString();
  }

  public static String transform(String source) {
    return transform(source, true);
  }

  public static String transform(String source, boolean needCheck) {
    String[] sourceArray = source.toLowerCase().split("[ ]");
    return transform(sourceArray, needCheck);
  }

  public static String transform(String[] sourceArray) {
    return transform(sourceArray, true);
  }

  public static String transform(String[] sourceArray, boolean needCheck) {
    List<String> sourceList = Stream.of(sourceArray).collect(Collectors.toList());
    return transform(sourceList, needCheck);
  }

  public static String transform(List<String> sourceList) {
    return transform(sourceList, true);
  }

  public static String transform(List<String> sourceList, boolean needCheck) {
    List<WordEnum> wordList = map(sourceList);
    if (needCheck) {
      check(sourceList);
    }
    return calculate(format(wordList));
  }

  private static long getScale(long arg) {
    for (long i = 1; ; i++) {
      double divisor = Math.pow(10, i);
      if ((double) arg / divisor < 1) {
        return i;
      }
    }
  }

  private static List<Map<List<String>, Boolean>> markSubString(List<String> sourceList) {
    if (sourceList.isEmpty()) {
      return new ArrayList<>();
    }

    List<Map<List<String>, Boolean>> prefixList = new ArrayList<>();
    List<WordEnum> wordList = map(sourceList);
    List<String> item = new ArrayList<>();
    Map<List<String>, Boolean> itemMap = new HashMap<>();
    Map<List<String>, Boolean> illegalItemMap = new HashMap<>();
    int index = 0;
    int dotCount = 0;
    boolean finish = false;
    while (!finish) {
      switch (wordList.get(index).getType()) {
        case NUMBER: {
          item.add(sourceList.get(index));
          break;
        }
        case SCALE: {
          if (dotCount == 0) {
            item.add(sourceList.get(index));
          } else {
            illegalItemMap.put(Collections.singletonList(sourceList.get(index)), false);
            finish = true;
          }
          break;
        }
        case SYMBOL: {
          if (WordEnum.DOT.same(wordList.get(index))) {
            if (++dotCount == 2 || index == sourceList.size() - 1 || !TypeEnum.NUMBER.equals(wordList.get(index + 1).getType())) {
              illegalItemMap.put(Collections.singletonList(sourceList.get(index)), false);
              finish = true;
            } else {
              item.add(sourceList.get(index));
            }
          } else if (WordEnum.MINUS.same(wordList.get(index)) || WordEnum.AND.same(wordList.get(index))) {
            if (index == sourceList.size() - 1 || (!TypeEnum.NUMBER.equals(wordList.get(index + 1).getType()) && !TypeEnum.SCALE.equals(wordList.get(index + 1).getType()) && !WordEnum.DOT.same(wordList.get(index + 1)))) {
              if (index == sourceList.size() - 1 || !TypeEnum.NUMBER.equals(wordList.get(index + 1).getType())) {
                illegalItemMap.put(Collections.singletonList(sourceList.get(index)), false);
                finish = true;
              } else {
                item.add(sourceList.get(index));
              }
            }
          }
          break;
        }
        default: {
          illegalItemMap.put(Collections.singletonList(sourceList.get(index)), false);
          finish = true;
        }
      }
      index++;
      if (index == sourceList.size()) {
        finish = true;
      }
    }

    if (!item.isEmpty()) {
      itemMap.put(item, true);
      prefixList.add(itemMap);
    }
    if (!illegalItemMap.isEmpty()) {
      prefixList.add(illegalItemMap);
    }

    List<Map<List<String>, Boolean>> suffixList = markSubString(sourceList.subList(index, sourceList.size()));
    prefixList.addAll(suffixList);
    return prefixList;
  }

  private static void check(List<String> sourceList) {
    List<WordEnum> wordList = map(sourceList);
    int dotCount = 0;
    for (int i = 0; i < sourceList.size(); i++) {
      switch (wordList.get(i).getType()) {
        case NUMBER: {
          break;
        }
        case SCALE: {
          if (dotCount > 0) {
            throw new IllegalInputException();
          }
          break;
        }
        case SYMBOL: {
          if (WordEnum.DOT.same(wordList.get(i))) {
            if (++dotCount == 2 || i == sourceList.size() - 1 || !TypeEnum.NUMBER.equals(wordList.get(i + 1).getType())) {
              throw new IllegalInputException();
            }
          } else if (WordEnum.MINUS.same(wordList.get(i)) || WordEnum.AND.same(wordList.get(i))) {
            if (i == sourceList.size() - 1 || (!TypeEnum.NUMBER.equals(wordList.get(i + 1).getType()) && !TypeEnum.SCALE.equals(wordList.get(i + 1).getType()) && !WordEnum.DOT.same(wordList.get(i + 1)))) {
              if (i == sourceList.size() - 1 || !TypeEnum.NUMBER.equals(wordList.get(i + 1).getType())) {
                throw new IllegalInputException();
              }
            }
          }
          break;
        }
        default: {
          throw new IllegalInputException();
        }
      }
    }
  }

  private static boolean checkWithoutException(List<String> sourceList) {
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
          if (WordEnum.DOT.same(wordList.get(i))) {
            if (++dotCount == 2 || i == sourceList.size() - 1 || !TypeEnum.NUMBER.equals(wordList.get(i + 1).getType())) {
              return false;
            }
          } else if (WordEnum.MINUS.same(wordList.get(i)) || WordEnum.AND.same(wordList.get(i))) {
            if (i == sourceList.size() - 1 || (!TypeEnum.NUMBER.equals(wordList.get(i + 1).getType()) && !TypeEnum.SCALE.equals(wordList.get(i + 1).getType()) && !WordEnum.DOT.same(wordList.get(i + 1)))) {
              if (i == sourceList.size() - 1 || !TypeEnum.NUMBER.equals(wordList.get(i + 1).getType())) {
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

  private static FormatEnum getFormatType(List<WordEnum> list) {
    boolean hasDot = false;
    for (WordEnum word : list) {
      if (WordEnum.DOT.same(word)) {
        if (!list.get(list.size() - 1).same(WordEnum.DOT)) {
          hasDot = true;
        }
      }
    }
    return hasDot ? FormatEnum.DECIMAL : FormatEnum.ROUND;
  }

  private static List<WordEnum> map(List<String> list) {
    return list.stream().map(WordEnum::getByKey).collect(Collectors.toList());
  }

  //grammar analysis
  //reference: https://en.wikipedia.org/wiki/English_numerals
  private static List<Data> format(List<WordEnum> list) {
    FormatEnum formatType = getFormatType(list);
    List<WordEnum> roundList = new ArrayList<>();
    List<Data> resultList = new ArrayList<>();

    for (int i = 0; i < list.size() - 1; i++) {
      if (WordEnum.DOT.same(list.get(i))) {
        roundList.addAll(list.subList(0, i));
        if (roundList.size() == 0) {
          roundList.add(WordEnum.ZERO);
        }
        resultList.addAll(list.subList(i + 1, list.size()).stream().map(x -> new Data(x.getValue(), -1L)).collect(Collectors.toList()));
        break;
      }
    }
    if (FormatEnum.ROUND.equals(formatType)) {
      roundList = list;
    }

    if (roundList.stream().map(word -> TypeEnum.NUMBER.equals(word.getType())).reduce((x, y) -> x && y).orElse(false)) {
      for (int i = roundList.size() - 1; i >= 0; i--) {
        if (roundList.get(i).getValue() > 20 && i != roundList.size() - 1) {
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
      if (WordEnum.MINUS.same(roundList.get(0))) {
        resultList.add(0, Data.MINUS_SYMBOL_DATA);
      }
    }
    return resultList;
  }

  private static String calculate(List<Data> list) {
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
      if (!SIMPLIFY_STANDARD.equals(SimplifyEnum.NONE) && getScale(Long.parseLong(sb.toString())) >= getScale(SIMPLIFY_STANDARD.getValue())) {
        numberValue /= SIMPLIFY_STANDARD.getValue();
        String numberString = numberValue.toString();
        int dotIndex = numberString.split("[.]")[0].length();
        String roundPart = numberString.substring(0, dotIndex);
        String decimalPart = numberString.substring(dotIndex + 1);
        decimalPart = decimalPart.equals("0") ? "" : "." + decimalPart;
        return roundPart + decimalPart + " " + SIMPLIFY_STANDARD.getText();
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
