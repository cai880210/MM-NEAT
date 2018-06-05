package popacman.prediction;

import java.util.BitSet;

/**
 * Created by Piers on 27/06/2016.
 */
public class PillModel {
    private BitSet pills;
    private int pillsEaten;
    private double totalPills;

    public PillModel(int indices) {
        this.totalPills = indices;
        this.pills = new BitSet(indices);
    }

    // Pacman visited this index
    public void update(int index) {
    	System.out.println("BEFORE: INDEX " + index + " HAS PILL: " + pills.get(index));
        if (pills.get(index)) {
            pillsEaten++;
            pills.set(index, false);
            System.out.println("WE ATE A PILL");
        }
        System.out.println("AFTER: INDEX " + index + " HAS PILL: " + pills.get(index));
    }

    // There is a pill here!
    public void observe(int index, boolean pillThere) {
    	System.out.println("BEFORE:" + pills);
        pills.set(index, pillThere);
    	System.out.println("AFTER:" + pills);
    }

    public int getPillsEaten() {
        return pillsEaten;
    }

    public double getPillsFraction() {
        return pillsEaten / totalPills;
    }

    public PillModel copy() {
        PillModel other = new PillModel((int) this.totalPills);
        other.pills = (BitSet) this.pills.clone();
        other.totalPills = this.totalPills;
        other.pillsEaten = pillsEaten;
        return other;
    }

    public BitSet getPills() {
        return pills;
    }
}
