package tests;

import coach.AIGoalCoachStub;
import coach.GoalResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FunctionalGoalTests {

    @Test
    void validGoalProducesRefinedGoal() {
        GoalResponse response = AIGoalCoachStub.process("I want to improve in sales");
        assertNotNull(response.refined_goal);
        assertTrue(response.key_results.size() >= 3);
        assertTrue(response.confidence_score >= 1 && response.confidence_score <= 10);
    }
}