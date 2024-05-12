import java.time.LocalDate;

public class Dates {
    private LocalDate martyrDate;
    LinkedList martyrs;

    public Dates(LocalDate martyrDate) {
        this.martyrDate = martyrDate;
        martyrs = new LinkedList();
    }

    public LocalDate getMartyrDate() {
        return martyrDate;
    }

    public void setMartyrDate(LocalDate martyrDate) {
        this.martyrDate = martyrDate;
    }

    public boolean exists(String name) {
        return martyrs.exist(name);
    }

    public int number_of_martyrs() {
        return martyrs.size();
    }

    public int martyrs_avg_age() {
        int ages = 0;
        for (int i = 0; i < martyrs.size(); i++)
            ages += ((Martyr) martyrs.get(i)).getAge();
        return ages / martyrs.size();
    }

    public int youngest_martyr() {
        return ((Martyr) martyrs.getFirst()).getAge();
    }

    public int oldest_martyr() {
        return ((Martyr) martyrs.getLast()).getAge();
    }

    @Override
    public String toString() {
        return martyrDate.toString();
    }

}
