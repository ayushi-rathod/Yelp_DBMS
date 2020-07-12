package Models;

public class Reviews {
    private String userId;
    private String reviewId;
    private int stars;
    private String date;
    private String text;
    private String businessId;
    private int votesFunny;
    private int votesUseful;
    private int votesCool;
    private int totalVotes;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

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
    
    public void setTotalVotes (int totalVotes) {
        this.totalVotes = totalVotes;
    }
    
    public int getTotalVotes() {
        return totalVotes;
    }
}
