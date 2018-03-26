package enums;

public enum SimplifyEnum {
  NONE(1, ""),
  HUNDRED(100, "hundred"),
  THOUSAND(1000, "thousand"),
  MILLION(1000000, "million"),//default
  BILLION(1000000000, "billion");

  private long value;
  private String text;

  SimplifyEnum(long value, String text) {
    this.value = value;
    this.text = text;
  }

  public long getValue() {
    return value;
  }

  public String getText() {
    return text;
  }
}
