class DynamicArray {
    private int[] array;
    private int count;

    public DynamicArray(int capacity) {
        array = new int[capacity];
    }

    public int get(int i) {
        return this.array[i];
    }

    public void set(int i, int n) {
        this.array[i] = n;
    }

    public void pushToPosition(int i, int n) {
        if (count == array.length) {
            resize();
        }
        for (int index = count; index > i; index--) {
            array[index] = array[index - 1];
        }
        this.array[i] = n;
    }

    public void pushback(int n) {
        if (count == array.length) {
            resize();
        }
        array[count++] = n;
    }

    public int popback() {
        return array[count-- -1];
    }

    private void resize() {
        int[] newArray = new int[array.length * 2];
        for (int index = 0; index < array.length; index++) {
            newArray[index] = array[index];
        }
        array = newArray;
    }

    public int getSize() {
        return count;
    }

    public int getCapacity() {
        return array.length;
    }
}
