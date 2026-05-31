public class Celula {

    private boolean minat = false;
    private boolean areSteag = false;
    private boolean descoperit = false;
    private int bombeVecine = 0;

    public Celula() {

    }

    public boolean isMinat() {
        return this.minat;
    }

    public void setMinat(boolean minat) {
        this.minat = minat;
    }

    public boolean isAreSteag() {
        return this.areSteag;
    }

    public void setAreSteag(boolean areSteag) {
        this.areSteag = areSteag;
    }

    public boolean isDescoperit() {
        return this.descoperit;
    }

    public void setDescoperit(boolean descoperit) {
        this.descoperit = descoperit;
    }

    public int getBombeVecine() {
        return this.bombeVecine;
    }

    public void setBombeVecine(int bombeVecine) {
        this.bombeVecine = bombeVecine;
    }
}