package ro.taxi_ineu.taxiineusofer;

public class Task {

    private String name;
    private String desc;
    private Integer id;
    private String phone;
    private String CURRENT_TIME;
    private String minutes;


    public Task(String name, String desc, Integer id, String phone, String CURRENT_TIME, String minutes) {
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.phone = phone;
        this.CURRENT_TIME = CURRENT_TIME;
        this.minutes = minutes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCURRENT_TIME() {
        return CURRENT_TIME;
    }

    public void setCURRENT_TIME(String CURRENT_TIME) {
        this.CURRENT_TIME = CURRENT_TIME;
    }
    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
