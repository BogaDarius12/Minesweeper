public class Celula {

    private boolean minat = false;
    private boolean areSteag = false;
    private boolean descoperit = false;
    private int bombeVecine = 0;

    public Celula() {

    }

    public boolean esteMinat() {
        return this.minat;
    }

    public void setMinat(boolean minat) {
        this.minat = minat;
    }

    public boolean areSteag() {
        return this.areSteag;
    }

    public void setAreSteag(boolean areSteag) {
        this.areSteag = areSteag;
    }

    public boolean esteDescoperit() {
        return this.descoperit;
    }

    public void setEsteDescoperit(boolean descoperit) {
        this.descoperit = descoperit;
    }

    public int getBombeVecine() {
        return this.bombeVecine;
    }

    public void setBombeVecine(int bombeVecine) {
        this.bombeVecine = bombeVecine;
    }
}