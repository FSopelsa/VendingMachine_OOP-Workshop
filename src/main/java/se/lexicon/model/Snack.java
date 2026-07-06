package se.lexicon.model;

public final class Snack extends Product {

    private final int weightGrams;

    public Snack(int id, String name, int price, int quantity, int weightGrams) {
        super(id, name, price, quantity);
        this.weightGrams = requirePositive(weightGrams, "weightGrams");
    }

    public int getWeightGrams() {
        return weightGrams;
    }

    @Override
    public String getType() {
        return "Snack";
    }

    @Override
    protected String getSpecificDetail() {
        return weightGrams + "g";
    }
}
