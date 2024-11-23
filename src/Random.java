package knu.mus.maze;

/** Xorshift algorithm implementation */
public class Random {
    private int state;

    /** Constructs new generator with default seed */
    public Random() {
        this.state = 42;
    }

    /** Constructs new generator 
     * @param seed
     * new seed
     * */
    Random(int seed) {
        this.state = seed;
    }

    /** Set new seed 
     * @param seed
     * new seed
     * */
    public void setSeed(int seed) {
        assert seed != 0;
        this.state = seed;
    }

    /** Generate next random integer in range INT_MIN to INT_MAX (_EXCLUDING ZERO_) according to xorshift algorithm 
     * @return 
     * random integer
     * */
    public final int nextInt() {
        int x = this.state;

        x ^= x << 13;
        x ^= x >> 17;
        x ^= x << 5;

        this.state = x;

        return this.state;
    }
    
    /** deterministicly generate random integer 
     * @param from
     * minimum value
     * @param to 
     * maximum value (exclusive)
     * @return 
     * random integer
     * */
    public final int nextIntRange(int from, int to) {
        assert to > from;
        int r = Math.abs(nextInt());
        return r % (to - from) + from;
    }
}
