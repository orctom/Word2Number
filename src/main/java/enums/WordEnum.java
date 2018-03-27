package enums;

public enum WordEnum {
  MINUS("-", 0L, 0L, TypeEnum.SYMBOL),
  MINUS_WORD("minus", 0L, 0L, TypeEnum.SYMBOL),
  NEGATIVE_WORD("negative", 0L, 0L, TypeEnum.SYMBOL),
  DOT(".", 0L, 0L, TypeEnum.SYMBOL),
  DOT_WORD("dot", 0L, 0L, TypeEnum.SYMBOL),
  POINT_WORD("point", 0L, 0L, TypeEnum.SYMBOL),
  AND("and", 0L, 0L, TypeEnum.SYMBOL),

  O("o", 0L, 1L, TypeEnum.NUMBER),
  ZERO("zero", 0L, 1L, TypeEnum.NUMBER),
  ONE("one", 1L, 1L, TypeEnum.NUMBER),
  TWO("two", 2L, 1L, TypeEnum.NUMBER),
  THREE("three", 3L, 1L, TypeEnum.NUMBER),
  FOUR("four", 4L, 1L, TypeEnum.NUMBER),
  FIVE("five", 5L, 1L, TypeEnum.NUMBER),
  SIX("six", 6L, 1L, TypeEnum.NUMBER),
  SEVEN("seven", 7L, 1L, TypeEnum.NUMBER),
  EIGHT("eight", 8L, 1L, TypeEnum.NUMBER),
  NINE("nine", 9L, 1L, TypeEnum.NUMBER),
  TEN("ten", 10L, 2L, TypeEnum.NUMBER),
  ELEVEN("eleven", 11L, 2L, TypeEnum.NUMBER),
  TWELVE("twelve", 12L, 2L, TypeEnum.NUMBER),
  THIRTEEN("thirteen", 13L, 2L, TypeEnum.NUMBER),
  FOURTEEN("fourteen", 14L, 2L, TypeEnum.NUMBER),
  FIFTEEN("fifteen", 15L, 2L, TypeEnum.NUMBER),
  SIXTEEN("sixteen", 16L, 2L, TypeEnum.NUMBER),
  SEVENTEEN("seventeen", 17L, 2L, TypeEnum.NUMBER),
  EIGHTEEN("eighteen", 18L, 2L, TypeEnum.NUMBER),
  NINETEEN("nineteen", 19L, 2L, TypeEnum.NUMBER),
  TWENTY("twenty", 20L, 2L, TypeEnum.NUMBER),
  THIRTY("thirty", 30L, 2L, TypeEnum.NUMBER),
  FORTY("forty", 40L, 2L, TypeEnum.NUMBER),
  FIFTY("fifty", 50L, 2L, TypeEnum.NUMBER),
  SIXTY("sixty", 60L, 2L, TypeEnum.NUMBER),
  SEVENTY("seventy", 70L, 2L, TypeEnum.NUMBER),
  EIGHTY("eighty", 80L, 2L, TypeEnum.NUMBER),
  NINETY("ninety", 90L, 2L, TypeEnum.NUMBER),

  HUNDRED("hundred", 100L, 3L, TypeEnum.SCALE),
  THOUSAND("thousand", 1000L, 4L, TypeEnum.SCALE),
  MILLION("million", 1000000L, 7L, TypeEnum.SCALE),
  BILLION("billion", 1000000000L, 10L, TypeEnum.SCALE),
  TRILLION("trillion", 1000000000000L, 13L, TypeEnum.SCALE),

  TEXT("", -1L, -1L, TypeEnum.ILLEGAL);

  private String key;
  private Long value;
  private Long scale;
  private TypeEnum type;

  WordEnum(String key, Long value, Long scale, TypeEnum type) {
    this.key = key;
    this.value = value;
    this.scale = scale;
    this.type = type;
  }

  public String getKey() {
    return key;
  }

  public Long getValue() {
    return value;
  }

  public Long getScale() {
    return scale;
  }

  public TypeEnum getType() {
    return type;
  }

  public boolean same(WordEnum word) {
    switch (this) {
      case MINUS:
      case MINUS_WORD: {
        return MINUS.equals(word) || MINUS_WORD.equals(word) || NEGATIVE_WORD.equals(word);
      }
      case DOT:
      case DOT_WORD:
      case POINT_WORD: {
        return DOT.equals(word) || DOT_WORD.equals(word) || POINT_WORD.equals(word);
      }
      case ZERO:
      case O: {
        return ZERO.equals(word) || O.equals(word);
      }
      default:
        return this.equals(word);
    }
  }

  public static boolean notContain(String key) {
    return WordEnum.TEXT.equals(getByKey(key));
  }

  public static WordEnum getByKey(String key) {
    for (WordEnum num : WordEnum.values()) {
      if (num.getKey().equals(key)) {
        return num;
      }
    }
    return WordEnum.TEXT;
  }
}
