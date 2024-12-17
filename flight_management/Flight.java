public class Flight implements Comparable<Flight>{
    private int fID;
    private String dest;
    private char status; // A: Active, I: Inactive
    
    public Flight(int fID, String dest, char status) {
        this.fID = fID;
        this.dest = dest;
        this.status = status;
    }

    public int getfID() {
        return fID;
    }

    public void setfID(int fID) {
        this.fID = fID;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return fID + "," + dest + "," + status;
    }

    @Override
    public int compareTo(Flight o) {
        return this.getfID() - o.getfID();
    }
    
    
}
