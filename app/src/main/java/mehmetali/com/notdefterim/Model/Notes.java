package mehmetali.com.notdefterim.Model;

public class Notes {

    private int id;
    private String notContext;
    private long notAddDate;
    private long notDate;
    private int isDone;


    public Notes(int id, String notContext, long notAddDate, long notDate, int isDone) {
        this.id = id;
        this.notContext = notContext;
        this.notAddDate = notAddDate;
        this.notDate = notDate;
        this.isDone = isDone;
    }

    public Notes(int id, String notContext, long notDate, int isDone) {
        this.id = id;
        this.notContext = notContext;
        this.notDate = notDate;
        this.isDone = isDone;
    }

    public long getNotAddDate() {
        return notAddDate;
    }

    public void setNotAddDate(long notAddDate) {
        this.notAddDate = notAddDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotContext() {
        return notContext;
    }

    public void setNotContext(String notContext) {
        this.notContext = notContext;
    }

    public long getNotDate() {
        return notDate;
    }

    public void setNotDate(long notDate) {
        this.notDate = notDate;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }
}
