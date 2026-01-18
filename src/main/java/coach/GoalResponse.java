package coach;

import java.util.List;

public class GoalResponse {
    public String refined_goal;
    public List<String> key_results;
    public int confidence_score;

    public GoalResponse(String refined_goal, List<String> key_results, int confidence_score) {
        this.refined_goal = refined_goal;
        this.key_results = key_results;
        this.confidence_score = confidence_score;
    }
}