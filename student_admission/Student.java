public class Student implements Comparable<Student>{
    private int id;
    private String name;
    private double grade;
    private double placement;
    private double admission;
    boolean accepted;// this attribute to see if the student got the admission mark or not;
    private Major major;

    Student(String name){
        this.name = name;
    }

    public Student(int id, String name, double grade, double placement, Major major) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.placement = placement;
        this.major = major;
    }

    public Student(int id, String name, double grade, double placement) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.placement = placement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public double getPlacement() {
        return placement;
    }

    public void setPlacement(double placement) {
        this.placement = placement;
    }

    public double getAdmission() {
        return admission;
    }

    public void setAdmission(double admission) {
        this.admission = admission;
    }

    public Major getMajor(){
        return this.major;
    }

    public void setMajor(Major major){
        this.major = major;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Grade: " + grade + ", Placement Grade: " + placement + ", Admission Mark: "
                + admission + ", Major: " + major.getName() + "\n";
    }

    @Override
    public int compareTo(Student o) {
        return (int)(o.admission - this.admission);
    }

    double calculateAdmissionMark(double tawjihiWeight, double testWeight){ // this method calculates the student mark with major weight prefrences.
        this.admission = (grade*tawjihiWeight) + placement*testWeight;
        return this.admission;
    }

    

    
}
