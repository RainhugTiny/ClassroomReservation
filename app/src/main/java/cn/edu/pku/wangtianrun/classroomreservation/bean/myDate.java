package cn.edu.pku.wangtianrun.classroomreservation.bean;

public class myDate {
    private String date;
    private int room_2101;
    private int room_2102;
    private int room_2103;
    private int room_2201;
    private int room_2202;
    private int room_2203;
    private int room_2301;
    private int room_2302;
    private int room_2303;
    private int room_3101;
    private int room_3102;
    private int room_3103;
    private int room_3201;
    private int room_3202;
    private int room_3203;
    private int room_3301;
    private int room_3302;
    private int room_3303;
    //获取房间信息
    public String getDate(){
        return date;
    }
    public int getRoom_2101(){
        return room_2101;
    }
    public int getRoom_2102(){
        return room_2102;
    }
    public int getRoom_2103(){
        return room_2103;
    }
    public int getRoom_2201(){
        return room_2201;
    }
    public int getRoom_2202(){
        return room_2202;
    }
    public int getRoom_2203(){
        return room_2203;
    }
    public int getRoom_2301(){
        return room_2301;
    }
    public int getRoom_2302(){
        return room_2302;
    }
    public int getRoom_2303(){
        return room_2303;
    }
    public int getRoom_3101(){
        return room_3101;
    }
    public int getRoom_3102(){
        return room_3102;
    }
    public int getRoom_3103(){
        return room_3103;
    }
    public int getRoom_3201(){
        return room_3201;
    }
    public int getRoom_3202(){
        return room_3202;
    }
    public int getRoom_3203(){
        return room_3203;
    }
    public int getRoom_3301(){
        return room_3301;
    }
    public int getRoom_3302(){
        return room_3302;
    }
    public int getRoom_3303(){
        return room_3303;
    }
    //设置房间信息
    public void setDate(String str){
        date=str;
    }
    public void setRoom_2101(int i){
        room_2101=i;
    }
    public void setRoom_2102(int i){
        room_2102=i;
    }
    public void setRoom_2103(int i){
        room_2103=i;
    }
    public void setRoom_2201(int i){
        room_2201=i;
    }
    public void setRoom_2202(int i){
        room_2202=i;
    }
    public void setRoom_2203(int i){
        room_2203=i;
    }
    public void setRoom_2301(int i){
        room_2301=i;
    }
    public void setRoom_2302(int i){
        room_2302=i;
    }
    public void setRoom_2303(int i){
        room_2303=i;
    }
    public void setRoom_3101(int i){
        room_3101=i;
    }
    public void setRoom_3102(int i){
        room_3102=i;
    }
    public void setRoom_3103(int i){
        room_3103=i;
    }
    public void setRoom_3201(int i){
        room_3201=i;
    }
    public void setRoom_3202(int i){
        room_3202=i;
    }
    public void setRoom_3203(int i){
        room_3203=i;
    }
    public void setRoom_3301(int i){
        room_3301=i;
    }
    public void setRoom_3302(int i){
        room_3302=i;
    }
    public void setRoom_3303(int i){
        room_3303=i;
    }
    public int[] stateList(){
        int[] stateList={room_2101,room_2102,room_2103,room_2201,room_2202,room_2203,room_2301,room_2302,room_2303,room_3101,room_3102,
        room_3103,room_3201,room_3202,room_3203,room_3301,room_3302,room_3303};
        return stateList;
    }
}
