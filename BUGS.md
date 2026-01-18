   Documented Bugs – AI Goal Coach

     Bug 1: Hallucination on Gibberish Input

       Summary
The AI generates valid-looking goals even when given completely invalid input (gibberish).

       Steps to Reproduce
1. Send input: `"asdkjh123!@  "`
2. Observe the response

       Expected Behavior
- `confidence_score` ≤ 2 (low confidence)
- `refined_goal` = `null` (no goal generated)
- `key_results` = empty list

       Actual Behavior
- AI generates a valid-looking refined goal
- Confidence score in normal range eg 8
- Key results are populated

       Impact
-   Severity  : Critical
-   Risk  : Users receive meaningless goals for invalid inputs
-   User Trust  : Degrades confidence in the system

       Test Coverage
  Test  : `AdversarialInputTests.gibberishInputReturnsLowConfidence()`

```java
@Test
void gibberishInputReturnsLowConfidence() {
    GoalResponse response = AIGoalCoachStub.process("asdkjh123!@  ");
    assertTrue(response.confidence_score <= 2);
    assertNull(response.refined_goal);
}
```

       Fix Status
  Fixed   - Stub implementation correctly rejects gibberish input with low confidence

       Prevention
- Adversarial input tests run in CI/CD
- Confidence threshold validation
- Input validation before AI processing

---

     Bug 2: Confidence Score Out of Range

       Summary
Confidence score exceeds the valid range (1-10), returning values like 12 or -1.

       Steps to Reproduce
1. Submit a normal, valid goal: `"Improve communication skills"`
2. Check the `confidence_score` field

       Expected Behavior
- `confidence_score` is an integer between 1 and 10 (inclusive)

       Actual Behavior
- `confidence_score` = 12 (out-of-range values)
-  negative values or 0

       Impact
-   Severity  : High
-   Risk  : Breaks API contract, causes client-side errors
-   Data Integrity  : Invalid data in downstream systems

       Test Coverage
  Test  : `SchemaValidationTest.responseSchemaIsValid()`

```java
@Test
void responseSchemaIsValid() {
    GoalResponse response = AIGoalCoachStub.process("Improve communication skills");
    assertNotNull(response);
    assertNotNull(response.key_results);
    assertTrue(response.confidence_score >= 1 && response.confidence_score <= 10);
}
```

       Fix Status
   Fixed   - Stub implementation enforces valid range

       Prevention
- Schema validation in every test
- Range checks in response model
- Contract testing

---

     Bug 3: Missing Key Results

       Summary
Valid goal inputs sometimes return empty key results list.

       Steps to Reproduce
1. Submit a sales-related goal: `"I want to improve in sales"`
2. Check the `key_results` field

       Expected Behavior
- `key_results` contains 3-5 actionable items
- List is non-null and non-empty for valid goals

       Actual Behavior
- `key_results` = empty list `[]`
- Or `key_results` = `null`

       Impact
-   Severity  : High
-   User Experience  : Incomplete goal refinement
-   Functionality  : Missing critical output

       Test Coverage
  Test  : `FunctionalGoalTests.validGoalProducesRefinedGoal()`

```java
@Test
void validGoalProducesRefinedGoal() {
    GoalResponse response = AIGoalCoachStub.process("I want to improve in sales");
    assertNotNull(response.refined_goal);
    assertTrue(response.key_results.size() >= 3);
    assertTrue(response.confidence_score >= 1 && response.confidence_score <= 10);
}
```

       Fix Status
   Fixed   - Stub implementation always returns 3 key results for valid inputs

       Prevention
- Functional tests validate minimum key results count
- Schema validation ensures non-null list
- Business logic validation

---

     Bug 4: Null Refined Goal for Valid Input

       Summary
Sometimes valid goal inputs return `null` for `refined_goal`.

       Steps to Reproduce
1. Submit a valid goal: `"I want to improve my fitness"`
2. Check `refined_goal` field

       Expected Behavior
- `refined_goal` is a non-null string for valid inputs
- Contains meaningful, refined goal text

       Actual Behavior
- `refined_goal` = `null`
- Even when confidence score is high (e.g., 8)

       Impact
-   Severity  : High
-   User Experience  : No output for valid input
-   Functionality  : Core feature broken

       Test Coverage
  Test  : `FunctionalGoalTests.validGoalProducesRefinedGoal()`

```java
@Test
void validGoalProducesRefinedGoal() {
    GoalResponse response = AIGoalCoachStub.process("I want to improve in sales");
    assertNotNull(response.refined_goal);  // Catches this bug
    // ...
}
```

       Fix Status
   Fixed   - Stub implementation returns non-null refined goal for valid inputs

       Prevention
- Functional tests assert non-null refined goals
- Business logic validation
- Error handling and logging

---

     Bug 5: Empty String Handling

       Summary
Empty string input is not properly handled, may cause exceptions or unexpected behavior.

       Steps to Reproduce
1. Send empty string: `""`
2. Observe response

       Expected Behavior
- `confidence_score` ≤ 2
- `refined_goal` = `null`
- `key_results` = empty list
- No exceptions thrown

       Actual Behavior
-  throw `NullPointerException` 
- returns high confidence score
- generates a goal from empty input

       Impact
-   Severity  : Medium
-   Stability  : System crashes on edge case
-   User Experience  : Poor error handling

       Test Coverage
  Test  : `AdversarialInputTests` (can be extended)

```java
@Test
void emptyStringReturnsLowConfidence() {
    GoalResponse response = AIGoalCoachStub.process("");
    assertTrue(response.confidence_score <= 2);
    assertNull(response.refined_goal);
}
```

       Fix Status
  Fixed   - Stub implementation handles empty strings correctly

       Prevention
- Edge case testing
- Input validation
- Graceful error handling

---

     Bug 6: Special Characters in Input

       Summary
Inputs containing only special characters (e.g., `"!@  $%"`) are not properly rejected.

       Steps to Reproduce
1. Send input: `"!@  $%^&*()"`
2. Check response

       Expected Behavior
- `confidence_score` ≤ 2
- `refined_goal` = `null`
- No goal generated

       Actual Behavior
- May generate a goal
- Confidence score may be normal

       Impact
-   Severity  : Medium
-   Data Quality  : Invalid goals generated
-   System Reliability  : Poor input validation

       Test Coverage
  Test  : `AdversarialInputTests` (can be extended)

       Fix Status
  Fixed   - Stub implementation rejects non-alphabetic inputs

       Prevention
- Input validation
- Adversarial testing
- Character set validation

---

     Test Coverage Summary

| Bug ID | Severity | Test Class | Test Method | Status |
|--------|----------|------------|--------------|--------|
| Bug 1  | Critical | AdversarialInputTests | gibberishInputReturnsLowConfidence |  Caught |
| Bug 2  | High     | SchemaValidationTest | responseSchemaIsValid |  Caught |
| Bug 3  | High     | FunctionalGoalTests | validGoalProducesRefinedGoal |  Caught |
| Bug 4  | High     | FunctionalGoalTests | validGoalProducesRefinedGoal |  Caught |
| Bug 5  | Medium   | AdversarialInputTests | (extendable) |  Handled |
| Bug 6  | Medium   | AdversarialInputTests | (extendable) |  Handled |

     Lessons Learned

1.   Adversarial Testing is Critical  : Invalid inputs can cause AI systems to hallucinate
2.   Schema Validation Catches Contract Issues  : Range violations and type mismatches
3.   Functional Tests Ensure Core Behavior  : Valid inputs must produce complete outputs
4.   Edge Cases Matter  : Empty, null, and special character inputs need handling
5.   Confidence Scores Need Validation  : They're critical for determining output quality

     Prevention Strategy

-  Comprehensive test suite covering all bug scenarios
-  CI/CD integration catches issues early
-  Schema validation prevents contract violations
-  Adversarial testing prevents hallucination
-  Functional testing ensures core behavior
