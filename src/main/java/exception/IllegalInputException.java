package exception;

public class IllegalInputException extends RuntimeException {

  @Override
  public synchronized Throwable fillInStackTrace() {
    return null;
  }
}
