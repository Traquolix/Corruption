package fr.traquolix.stats;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstract base class representing a game statistic.
 * This class provides common functionality that can be shared among different statistics.
 */
@Getter
public abstract class AbstractStat {
    @Setter
    // The value of the statistic.
    protected double value;

    /**
     * Constructor for the AbstractStat class.
     */
    public AbstractStat() {}

    /**
     * Increases the value of the statistic by the given integer amount.
     *
     * @param integer The amount to add to the current value.
     */
    public void add(double integer) {
        this.value += integer;
    }

    /**
     * Decreases the value of the statistic by the given integer amount.
     *
     * @param integer The amount to subtract from the current value.
     */
    public void remove(double integer) {
        this.value -= integer;
    }
}
