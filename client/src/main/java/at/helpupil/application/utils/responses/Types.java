package at.helpupil.application.utils.responses;

/**
 * Response object of types
 */
public class Types {
    /**
     * types where every letter is upper case
     */
    private String[] keys;
    /**
     * types where every letter is lower case
     */
    private String[] values;
    /**
     * first letter is a capital letter
     */
    private String[] friendly_values;
    /**
     * price for each type
     */
    private int[] prices;

    /**
     * @param keys types where every letter is upper case
     * @param values types where every letter is lower case
     * @param friendly_values first letter is a capital letter
     * @param prices for each type
     */
    public Types(String[] keys, String[] values, String[] friendly_values, int[] prices) {
        this.keys = keys;
        this.values = values;
        this.friendly_values = friendly_values;
        this.prices = prices;
    }

    /**
     * @return types where every letter is upper case
     */
    public String[] getKeys() {
        return keys;
    }

    /**
     * @param keys types where every letter is upper case
     */
    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    /**
     * @return types where every letter is lower case
     */
    public String[] getValues() {
        return values;
    }

    /**
     * @param values types where every letter is lower case
     */
    public void setValues(String[] values) {
        this.values = values;
    }

    /**
     * @return types where first letter is a capital letter
     */
    public String[] getFriendly_values() {
        return friendly_values;
    }

    /**
     * @param friendly_values where first letter is a capital letter
     */
    public void setFriendly_values(String[] friendly_values) {
        this.friendly_values = friendly_values;
    }

    /**
     * @return price for each type
     */
    public int[] getPrices() {
        return prices;
    }

    /**
     * @param prices for each type
     */
    public void setPrices(int[] prices) {
        this.prices = prices;
    }
}
