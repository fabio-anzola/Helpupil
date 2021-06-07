package at.helpupil.application.utils.responses;

public class Types {
    private String[] keys;
    private String[] values;
    private String[] friendly_values;
    private int[] prices;

    public Types(String[] keys, String[] values, String[] friendly_values, int[] prices) {
        this.keys = keys;
        this.values = values;
        this.friendly_values = friendly_values;
        this.prices = prices;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String[] getFriendly_values() {
        return friendly_values;
    }

    public void setFriendly_values(String[] friendly_values) {
        this.friendly_values = friendly_values;
    }

    public int[] getPrices() {
        return prices;
    }

    public void setPrices(int[] prices) {
        this.prices = prices;
    }
}
