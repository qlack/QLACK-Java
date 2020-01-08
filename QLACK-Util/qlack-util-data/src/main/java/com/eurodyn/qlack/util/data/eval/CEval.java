package com.eurodyn.qlack.util.data.eval;

/**
 * A set of conditional evaluators.
 */
public class CEval {

  /**
   * A conditional evaluator resembling of the ternary operator.
   * @param condition The condition to evaluate.
   * @param trueValue The result in case of a `true` condition.
   * @param falseValue The result in case fo a `false` condition.
   * @param <T> The result value.
   * @return
   */
  public static <T> T eval(boolean condition, T trueValue, T falseValue) {
    if (condition) {
      return trueValue;
    } else {
      return falseValue;
    }
  }
}
