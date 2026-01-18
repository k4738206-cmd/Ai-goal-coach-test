package coach;

import java.util.*;

public class AIGoalCoachStub {

    public static GoalResponse process(String input) {
        if (input == null || input.trim().isEmpty() || input.matches(".*[^a-zA-Z ].*")) {
            return new GoalResponse(null, Collections.emptyList(), 1);
        }

        List<String> results = List.of(
                "Increase weekly practice by 20%",
                "Track progress weekly",
                "Achieve measurable improvement in 3 months"
        );

        return new GoalResponse(
                "Improve sales performance by 15% in the next quarter",
                results,
                8
        );
    }
}