package se.lexicon.model;

public final class Beverage extends Product {

    private final int volumeMl;

    public Beverage(int id, String name, int price, int quantity, int volumeMl) {
        super(id, name, price, quantity);
        this.volumeMl = requirePositive(volumeMl, "volumeMl");
    }

    public int getVolumeMl() {
        return volumeMl;
    }

    @Override
    public String getType() {
        return "Beverage";
    }

    @Override
    protected String getSpecificDetail() {
        return volumeMl + "ml";
    }
}
