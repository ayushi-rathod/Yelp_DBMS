package Models;

import java.util.List;
import Models.Friends;


public class User{
    private String yelpingSince;
    private int votesFunny;
    private int votesUseful;
    private int votesCool;
    private int totalVotes;
    private int reviewCount;
    private String name;
    private String user_Id;
    private List<Friends> friends;
    private int fans;
    private float averageStars;
    private String type;

    public int getVotesFunny() {
        return votesFunny;
    }
    
    public void setVotesFunny(int votesFunny) {
        this.votesFunny = votesFunny;
    }
    
    public int getVotesUseful() {
        return votesUseful;
    }
    
    public void setVotesUseful(int votesUseful) {
        this.votesUseful = votesUseful;
    }
    
    public int getVotesCool() {
        return votesCool;
    }
    
    public void setVotesCool(int votesCool) {
        this.votesCool = votesCool;
    }
    
    public String getYelpingSince(){
        return yelpingSince;
    }
    
    public void setYelpingSince(String input){
        this.yelpingSince = input;
    }
    
    public int getReviewCount(){
        return reviewCount;
    }
    
    public void setReviewCount(int input){
        this.reviewCount = input;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String input){
        this.name = input;
    }
    
    public String getUserId(){
        return user_Id;
    }
    
    public void setUserId(String input){
        this.user_Id = input;
    }
    
    public List<Friends> getFriends(){
        return friends;
    }
    
    public void setFriends(List<Friends> input){
        this.friends = input;
    }
    
    public int getFans(){
        return fans;
    }
    
    public void setFans(int input){
        this.fans = input;
    }
    
    public float getAverageStars(){
        return averageStars;
    }
    
    public void setAverageStars(float input){
        this.averageStars = input;
    }
    
    public String getType(){
        return type;
    }
    public void setType(String input){
        this.type = input;
    }
   
    public void setTotalVotes (int totalVotes) {
        this.totalVotes = totalVotes;
    }
    
    public int getTotalVotes() {
        return totalVotes;
    }

}
