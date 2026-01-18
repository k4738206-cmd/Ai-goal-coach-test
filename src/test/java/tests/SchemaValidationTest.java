package tests;

import coach.AIGoalCoachStub;
import coach.GoalResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SchemaValidationTest {

    @Test
    void responseSchemaIsValid() {
        GoalResponse response = AIGoalCoachStub.process("Improve communication skills");
        assertNotNull(response);
        assertNotNull(response.key_results);
        assertTrue(response.confidence_score >= 1 && response.confidence_score <= 10);
    }
}