public class Passenger implements Comparable<Passenger> {
    private int pID;
    private String pName;
    private int fID;
    private char status; //R= Regular, V = VIP
    
    public Passenger(int pID, String pName, int fID, char status) {
        this.pID = pID;
        this.pName = pName;
        this.fID = fID;
        this.status = status;
    }

    public int getpID() {
        return pID;
    }

    public void setpID(int pID) {
        this.pID = pID;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public int getfID() {
        return fID;
    }

    public void setfID(int fID) {
        this.fID = fID;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public boolean isVip(){
        return status == 'V';
    }

    @Override
    public String toString() {
        return pID + "," + pName + "," + fID + "," + status;
    }

    public Passenger clone(){
        return new Passenger(pID, pName, fID, status);
    }

    @Override
    public int compareTo(Passenger o) {
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

    
}
