public class Major implements Comparable<Major>{
    private String name;
    private double acceptGrade;
    private double tawjihiWeight;
    private double testWeight;
    int acceptNum, rejectNum;

    public Major(String name, double acceptGrade, double tawjihiWeight, double testWeight) {
        this.name = name;
        this.acceptGrade = acceptGrade;
        this.tawjihiWeight = tawjihiWeight;
        this.testWeight = testWeight;
        acceptNum = rejectNum = 0;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getAcceptGrade() {
        return acceptGrade;
    }
    public void setAcceptGrade(double acceptGrade) {
        this.acceptGrade = acceptGrade;
    }
    public double getTawjihiWeight() {
        return tawjihiWeight;
    }
    public void setTawjihiWeight(double tawjihiWeight) {
        this.tawjihiWeight = tawjihiWeight;
    }
    public double getTestWeight() {
        return testWeight;
    }
    public void setTestWeight(double testWeight) {
        this.testWeight = testWeight;
    }
    @Override
    public String toString() {
        return name + ", AG:" + acceptGrade + ", TW:" + tawjihiWeight
                + ", PT:" + testWeight + "\n";
    }

    @Override
    public int compareTo(Major o) {
        return this.name.compareToIgnoreCase(o.name);
    }

    


}
