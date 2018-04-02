/*** 
 * @Author 侯凱翔
 * 學號: 104403519
 * 系級: 資管3A
 * HW7: 通訊錄
***/

public class Person {

    private int Memberid;
    private String Name;
    private String Phone;
    private String Email;
    private String Sex;

    public Person(int memberid, String name, String phone, String email, String sex) {
        setMemberID(memberid);
        setName(name);
        setPhone(phone);
        setMail(email);
        setSex(sex);

    }

    public void setMemberID(int memberid) {
        Memberid = memberid;
    }

    public int getMemberID() {
        return Memberid;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPhone() {
        return Phone;
    }

    public void setMail(String email) {
        Email = email;
    }

    public String getMail() {
        return Email;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getSex() {
        return Sex;
    }

} // end class Person
