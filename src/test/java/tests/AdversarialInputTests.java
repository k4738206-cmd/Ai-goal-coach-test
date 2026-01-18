package tests;

import coach.AIGoalCoachStub;
import coach.GoalResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdversarialInputTests {

    @Test
    void gibberishInputReturnsLowConfidence() {
        GoalResponse response = AIGoalCoachStub.process("asdkjh123!@#");
        assertTrue(response.confidence_score <= 2);
        assertNull(response.refined_goal);
    }
}