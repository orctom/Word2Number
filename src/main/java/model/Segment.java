package model;

import java.util.Collections;
import java.util.List;

public class Segment {

  private List<String> words;
  private boolean isNumber;

  public Segment(String word, boolean isNumber) {
    this.words = Collections.singletonList(word);
    this.isNumber = isNumber;
  }

  public Segment(List<String> words, boolean isNumber) {
    this.words = words;
    this.isNumber = isNumber;
  }

  public List<String> getWords() {
    return words;
  }

  public boolean isNumber() {
    return isNumber;
  }
}
