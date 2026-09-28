class Solution {
    public int carFleet(int target, int[] position, int[] speed) {
        int[][] cars = new int[position.length][];
        for (int i = 0; i < position.length; i++) {
            cars[i] = new int[]{position[i], speed[i]};
        }
        Arrays.sort(cars, (a, b) -> Integer.compare(b[0], a[0])); // closest to target first

        int fleets = 0;
        double leaderTime = 0;

        for (int[] car : cars) {
            double time = timeToFinish(target, car[0], car[1]);
            if (time > leaderTime) {
                fleets++;
                leaderTime = time;
            }
        }
        return fleets;
    }

    private double timeToFinish(int target, int position, int speed) {
        return (double) (target - position) / speed;
    }
}