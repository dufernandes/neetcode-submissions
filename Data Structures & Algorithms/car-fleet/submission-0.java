class Solution {
    public int carFleet(int target, int[] position, int[] speed) {
        if (position.length == 1) return 1;

        Deque<Integer> fleet = new ArrayDeque<>();
        Map<Integer, Integer> cars = new HashMap<>();

        for (int i = 0; i < position.length; i++) {
            cars.put(position[i], speed[i]);
        }

        Arrays.sort(position);

        for (int i = position.length - 1; i >= 0; i --) {
            if (fleet.isEmpty()) {
                fleet.push(position[i]); 
                continue;
            }

            if (timeToFinish(target, position[i], cars.get(position[i])) > timeToFinish(target, fleet.peekFirst(), cars.get(fleet.peekFirst()))) {
                fleet.push(position[i]);
            }
        }

        return fleet.size();
    }

    double timeToFinish(int end, int start, int speed) {
        return (double) (end - start) / (double) speed;
    }
}
