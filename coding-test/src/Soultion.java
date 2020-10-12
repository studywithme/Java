import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

class Solution {

    public String mapSolution(String[] participant, String[] completion) {
        Map<String, Integer> playerMap = new HashMap();

        for (String player : participant) playerMap.put(player, playerMap.getOrDefault(player, 0) + 1);
        for (String player : completion) playerMap.put(player, playerMap.get(player) - 1);

        StringBuilder answer = new StringBuilder();
        for (String key : playerMap.keySet()) {
            if (playerMap.get(key) > 0)
                answer.append(key);
        }
        return answer.toString();
    }

    public String listSolution(String[] participant, String[] completion) {
        List<String> participants = new ArrayList<>();
        List<String> completions = new ArrayList<>();
        Collections.addAll(participants, participant);
        Collections.addAll(completions, completion);

        participants.removeAll(completions);
        return participants.stream().collect(Collectors.joining(","));
    }

    @Test
    public void testMapSolution() {
        String[] participant = {"leo", "eden", "leo", "test"};
        String[] completion = {"leo", "eden"};
        String result = mapSolution(participant, completion);
        System.out.println(result);
    }
}
