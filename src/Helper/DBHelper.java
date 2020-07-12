package Helper;
import Models.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    public String finalQuery;
    public String userFinalQuery;
    public  Connection connection;
    public DBHelper(){

    }
    public void DBConnect() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        } catch (Exception e) {
            System.err.println("Unable to load driver.");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle");
        }catch (Exception e){
            System.err.println("Unable to connect");
            e.printStackTrace();
        }
    }

    public void DBClose(){
        try {
            connection.close();
        } catch (Exception e){
            System.err.println("Unable to close db");
            e.printStackTrace();
        }
    }

    public void insertUsers(List<User> users){
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("INSERT INTO USERS (userId , name , yelpingSince,  votesFunny, votesUseful, votesCool, total_votes, review_count , fans, averageStars ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            for(User user: users){
                statement.setString(1,user.getUserId());
                statement.setString(2,user.getName());
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
                java.util.Date date = sdf1.parse(user.getYelpingSince());
                java.sql.Date sqlYelpSince = new java.sql.Date(date.getTime());
                statement.setDate(3, sqlYelpSince);
                statement.setInt(4,user.getVotesFunny());
                statement.setInt(5,user.getVotesUseful());
                statement.setInt(6,user.getVotesCool());
                statement.setInt(7,user.getTotalVotes());
                statement.setInt(8,user.getReviewCount());
                statement.setInt(9,user.getFans());
                statement.setFloat(10, user.getAverageStars());
                statement.addBatch();
            }
            
            statement.executeBatch();
            statement.close();

            System.out.println("rows inserted ");

        }catch (Exception e){
            System.err.println("Query error");
            e.printStackTrace();
        }
    }
        
    public void insertUserFriends(List<User> users){
        try{
            PreparedStatement statement = connection.prepareStatement("INSERT INTO UserFriends (userId , friend_id ) VALUES (?, ?)");

            for(User user: users){
                for(Friends friend: user.getFriends()){
                    statement.setString(1,user.getUserId());
                    statement.setString(2, friend.getUserId());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
            System.out.println("rows inserted ");

        } catch (Exception e){
            System.err.println("Query error");
            e.printStackTrace();
        }
    }

    public void insertBusiness(List<Business> businesses){
        try{
            PreparedStatement statement = connection.prepareStatement("INSERT INTO  BUSINESS(businessId, name, address, city, state, review_count, stars) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement statement1 = connection.prepareStatement("INSERT INTO BUSINESS_HOURS(businessId, day, from_time, to_time) VALUES(?, ?, ?, ?)");
            PreparedStatement statement2 = connection.prepareStatement("INSERT INTO BUSINESS_CAT(businessId, category) VALUES(?, ?)");
            PreparedStatement statement3 = connection.prepareStatement("INSERT INTO BUSINESS_SUBCAT(businessid, subcategory)  VALUES(?,?)");
            PreparedStatement statement4 = connection.prepareStatement("INSERT INTO BUSINESS_ATTR(businessId, attr_name , attr_value)  VALUES(?,?,?)");

            for(Business business: businesses){
                statement.setString(1,business.getBusinessId());
                statement.setString(2,business.getName());
                statement.setString(3,business.getFullAddress());
                statement.setString(4,business.getCity());
                statement.setString(5,business.getState());
                statement.setInt(6,business.getReview_count());
                statement.setFloat(7, business.getStars());
                statement.addBatch();

                List<BusinessCategories> bcats = business.getCategories();
                for(BusinessCategories bcat: bcats){
                    statement2.setString(1, bcat.getBusinessId());
                    statement2.setString(2, bcat.getCategoryName());
                    statement2.addBatch();
                }

                List<BusinessSubCategories> bsubcats = business.getSubCategories();
                for(BusinessSubCategories bsubcat: bsubcats){
                    statement3.setString(1, bsubcat.getBusinessId());
                    statement3.setString(2, bsubcat.getSubCategoryName());
                    statement3.addBatch();
                }

                List<Attribute> attrs = business.getAttr();
                for(Attribute attr : attrs){
                    statement4.setString(1, attr.getBusinessId());
                    statement4.setString(2, attr.getAttribute_name());
                    statement4.setString(3, attr.getAttribute_value());
                    statement4.addBatch();
                }

            }
            statement.executeBatch();
            statement1.executeBatch();
            statement2.executeBatch();
            statement3.executeBatch();
            statement4.executeBatch();
            System.out.println("rows inserted for business table");

        }catch (SQLException se){
            System.err.println("Query error: SQL Exception");
            se.printStackTrace();
        }catch (Exception e){
            System.err.println("Query error ");
            e.printStackTrace();
        }
    }
    
    public void insertReviews(List<Reviews> reviews){
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO REVIEWS (reviewId, stars, publishDate, text, businessId, userId,  votesFunny, votes_cool, votes_useful, total_votes ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for(Reviews review: reviews) {
                statement.setString(1,review.getReviewId());
                statement.setInt(2,review.getStars());
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
                java.util.Date date = sdf1.parse(review.getDate());
                java.sql.Date publishDate = new java.sql.Date(date.getTime());
                statement.setDate(3, publishDate);
                statement.setString(4, review.getText());
                statement.setString(5, review.getBusinessId());
                statement.setString(6, review.getUserId());
                statement.setInt(7, review.getVotesFunny());
                statement.setInt(8, review.getVotesCool());
                statement.setInt(9, review.getVotesUseful());
                statement.setInt(10, review.getTotalVotes());
                statement.addBatch();
            }
            statement.executeBatch();
            System.out.println("rows inserted for reviews table");

        } catch (Exception e){
            System.err.println("Query not inserted" + e.getMessage());
        }
    }

    public ArrayList<Attribute> getAttributes(ArrayList<String> selectedSubCategories, ArrayList<String> selectedCategories, String condition){
        ArrayList<Attribute> attrs = new ArrayList<Attribute>();
        try {
            String query = "";
            if(condition == "AND"){
                String subq = "(SELECT businessId FROM BUSINESS_CAT WHERE  category = '"+selectedCategories.get(0)+"')";
                if(selectedCategories.size() > 1){
                    for(int i=1; i<selectedCategories.size(); i++){
                        subq += " INTERSECT (SELECT businessId FROM BUSINESS_CAT WHERE  category = '"+selectedCategories.get(i) +"' )";
                    }
                }
                query = "SELECT DISTINCT ba.attr_name FROM BUSINESS_ATTR ba JOIN BUSINESS_SUBCAT bs on bs.BUSINESSID = ba.BUSINESSID JOIN BUSINESS_CAT BC on ba.BUSINESSID = BC.BUSINESSID WHERE ba.businessId IN ( "+subq+" )";

            } else {

                query = "SELECT DISTINCT ba.attr_name FROM BUSINESS_ATTR ba JOIN BUSINESS_SUBCAT bs on bs.BUSINESSID = ba.BUSINESSID JOIN BUSINESS_CAT BC on ba.BUSINESSID = BC.BUSINESSID WHERE bc.category = ";
                query += "'" + selectedCategories.get(0) + "' ";

                if (selectedCategories.size() > 1) {
                    for (int i = 1; i < selectedCategories.size(); i++) {
                        query += condition + " bc.category = '" + selectedCategories.get(i) + "' ";
                    }
                }
            }

            query += " AND bs.subcategory = '" + selectedSubCategories.get(0) + "' ";
            if (selectedSubCategories.size() > 1) {
                for (int i = 1; i < selectedSubCategories.size(); i++) {
                    query += condition + " bs.subcategory = '" + selectedSubCategories.get(i) + "' ";
                }
            }
            System.out.println(query);

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1));
                Attribute attribute = new Attribute();
                attribute.setAttribute_name(rs.getString(1));
                attrs.add(attribute);
            }
        }catch (SQLException se){
            System.err.println("Query error: SQL Exception");
            se.printStackTrace();
        }catch (Exception e){
            System.err.println("Query error ");
            e.printStackTrace();
        }
        return attrs;
    }

    public ArrayList<BusinessSubCategories> getSubCategories(ArrayList<String> selectedCategories, String condition){
        ArrayList<BusinessSubCategories> subs = new ArrayList<BusinessSubCategories>();
        try {
            String query;
            if(condition == "AND"){
                String subq = "(SELECT businessId FROM BUSINESS_CAT WHERE  category = '"+selectedCategories.get(0)+"')";
                if(selectedCategories.size() > 1){
                    for(int i=1; i<selectedCategories.size(); i++){
                        subq += " INTERSECT (SELECT businessId FROM BUSINESS_CAT WHERE  category = '"+selectedCategories.get(i) +"' )";
                    }
                }
                query = "SELECT DISTINCT bs.subcategory FROM BUSINESS_SUBCAT bs JOIN BUSINESS_CAT bc on bs.BUSINESSID = bc.BUSINESSID  WHERE bs.businessId IN ( "+subq+" )";

            } else{

                 query = "SELECT DISTINCT bs.subcategory FROM BUSINESS_SUBCAT bs JOIN BUSINESS_CAT bc on bs.BUSINESSID = bc.BUSINESSID  WHERE bc.CATEGORY = ";
                query += "'"+selectedCategories.get(0) +"' ";
                if(selectedCategories.size() > 1){
                    for(int i=1; i<selectedCategories.size(); i++){
                        query += condition+" bc.category = '"+selectedCategories.get(i) +"' ";
                    }
                }
            }
            System.out.println(query);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1));
                BusinessSubCategories sub = new BusinessSubCategories();
                sub.setSubCategoryName(rs.getString(1));
                subs.add(sub);
            }
        }catch (SQLException se){
            System.err.println("Query error: SQL Exception");
            se.printStackTrace();
        }catch (Exception e){
            System.err.println("Query error ");
            e.printStackTrace();
        }
        return subs;
    }

    public ArrayList<BusinessCategories> getAllCategories(){
        ArrayList<BusinessCategories> cats = new ArrayList<BusinessCategories>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT category FROM BUSINESS_CAT  ORDER BY category");
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1));
                BusinessCategories cat = new BusinessCategories();
                cat.setCategoryName(rs.getString(1));
                cats.add(cat);
            }
        }catch (SQLException se){
            System.err.println("Query error: SQL Exception");
            se.printStackTrace();
        }catch (Exception e){
            System.err.println("Query error ");
            e.printStackTrace();
        }
        return cats;
    }

    public ArrayList<Business> businessFilterQuery(ArrayList<String> selectedCategories, ArrayList<String> selectedSubCategories, 
            ArrayList<String> selectedAttributes, String reviewFrom, String reviewTo, String starCondition, String starValue, String votesCondition, 
            String votesValue, String condition){
        
        ArrayList<Business> businesses = new ArrayList<Business>();
        try {
            String query = "SELECT DISTINCT b.businessId, b.name, b.city, b.state, b.stars FROM BUSINESS b, Reviews r WHERE b.businessid = r.businessid AND b.businessid IN ";
            
            String pairingcond;
            if (condition == "AND") {
                pairingcond = "INTERSECT";
            } else {
                pairingcond = "UNION";
            }
            
            query += " (";
                query += " ( SELECT bc.businessid from Business_cat bc where bc.category = '"+selectedCategories.get(0) +"' ";       
            if(selectedCategories.size() == 1){ 
                 query += " )";
            }
            if(selectedCategories.size() > 1){
                for(int i=1; i < selectedCategories.size(); i++){
                     query +=  pairingcond + " SELECT bc.businessid from Business_cat bc where bc.category = '"+selectedCategories.get(i) +"' ";
                }
                query += " )";
            }

            if (selectedSubCategories.size() > 0) {
                query += pairingcond;
                query += " ( SELECT bs.businessid from Business_subcat bs where bs.subcategory = '"+selectedSubCategories.get(0) +"' ";
                if(selectedSubCategories.size() > 1){
                    for(int i=1; i < selectedSubCategories.size(); i++){
                        query +=  pairingcond + " SELECT bs.businessid from Business_subcat bs where bs.subcategory = '"+selectedSubCategories.get(i) +"'";                    }
                }
                query += " )";
            }

            if (selectedAttributes.size() > 0) {
                query += pairingcond;
                query += " ( SELECT ba.businessid from Business_attr ba where ba.attr_name = '"+selectedAttributes.get(0) +"' ";
                if(selectedSubCategories.size() > 1){
                    for(int i=1; i < selectedSubCategories.size(); i++){
                        query +=  pairingcond + " SELECT ba.businessid from Business_attr ba where ba.attr_name = '"+selectedAttributes.get(i) +"'";                    }
                }
                query += " )";
            }
                
            query += " )";

            if (!reviewFrom.isEmpty()) {
                query += " AND r.PUBLISHDATE > '" + reviewFrom +"'";
            }
            
            if (!reviewTo.isEmpty()) {
                query += " AND r.PUBLISHDATE < '" + reviewTo +"'";
            }

            if(!starValue.isEmpty()){
                query += " AND r.stars " + starCondition + " " + Float.parseFloat(starValue);
            }
            
            if(!votesValue.isEmpty()){
                query += " AND r.total_votes " + votesCondition + " " + Integer.parseInt(votesValue);
            }
            
            System.out.println(query);
            finalQuery = query;

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Business b = new Business();
                b.setBusinessId(rs.getString(1));
                b.setName(rs.getString(2));
                b.setCity(rs.getString(3));
                b.setState(rs.getString(4));
                b.setStars(rs.getFloat(5));
                businesses.add(b);
            }
        }catch (SQLException se){
            System.err.println("Query error: SQL Exception");
            se.printStackTrace();
        }catch (Exception e){
            System.err.println("Query error ");
            e.printStackTrace();
        }
        return businesses;
    }

    public ArrayList<User> userFilterQuery(String yelping_since, String u_review_cond, String u_reviews_val, String u_friends_cond, String u_friends_val, 
                String u_stars_cond, String u_stars_val, String u_votes_cond, String u_votes_val, String condition) {
        
        ArrayList<User> user = new ArrayList<User>();
        try {
            String query = "";
            query = "SELECT DISTINCT u.name, u.yelpingsince, u.averagestars, u.userid FROM users u ";

            if (!yelping_since.isEmpty()) {
                query += " WHERE u.yelpingsince > '" + yelping_since + "'";
            }
            if (!u_reviews_val.isEmpty()) {
                query += " " + condition + " u.review_count " + u_review_cond + " " + u_reviews_val;
            }
            if (!u_stars_val.isEmpty()) {
                query += " " + condition + " u.averagestars" + u_stars_cond + " " + u_stars_val;
            }

            if (!u_votes_val.isEmpty()) {
                query += " " + condition + " u.total_votes " + u_votes_cond + " " + u_votes_val;
            }

            if (!u_friends_val.isEmpty()) {
                query += " AND u.userid in (select f.userid from userfriends f having count(f.friend_id) " + u_friends_cond  + " " + u_friends_val+ " group by f.userid)";
            }

            System.out.println("==query===========query=======" + query);
            userFinalQuery = query;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                User u = new User();
                u.setName(rs.getString(1));
                u.setYelpingSince(rs.getString(2));
                u.setAverageStars(Float.parseFloat(rs.getString(3)));
                u.setUserId(rs.getString(4));
                user.add(u);
            }
        }catch (SQLException se){
            System.err.println("Query error: SQL Exception");
            se.printStackTrace();
        }catch (Exception e){
            System.err.println("Query error ");
            e.printStackTrace();
        }
        return user;
        
    }
    
    public ArrayList<Reviews> getBusinessReviews(String businessId,String reviewFrom, String reviewTo, String starCondition, 
            String starValue, String votesCondition, String votesValue){
        System.out.println("==========businessId==========" + businessId);
        ArrayList<Reviews> reviews = new ArrayList<Reviews>();
        try {
            String query = "SELECT u.name, r.text FROM REVIEWS r JOIN Users u ON u.userId = r.userId WHERE r.BUSINESSID = '"+businessId+"'" ;
                       
            if (!reviewFrom.isEmpty()) {
                query += " AND r.PUBLISHDATE >= '" + reviewFrom +"'";
            }
            
            if (!reviewTo.isEmpty()) {
                query += " AND r.PUBLISHDATE <= '" + reviewTo +"'";
            }

            if(!starValue.isEmpty()){
                query += " AND r.stars " + starCondition + " " + Float.parseFloat(starValue);
            }
            
            if(!votesValue.isEmpty()){
                query += " AND r.total_votes " + votesCondition + " " + Integer.parseInt(votesValue);
            }
            
            finalQuery = query;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Reviews r = new Reviews();
                r.setUserId(rs.getString(1));
                r.setText(rs.getString(2));
                reviews.add(r);
            }

        }catch (SQLException se){
            System.err.println("Query error: SQL Exception");
            se.printStackTrace();
        }catch (Exception e){
            System.err.println("Query error ");
            e.printStackTrace();
        }
        return reviews;
    }
    
    public ArrayList<Reviews> getUserReviews(String userId){
        ArrayList<Reviews> reviews = new ArrayList<Reviews>();
        try {
            String query = "SELECT b.name, r.text FROM  Business b, Reviews r where b.businessId = r.businessId AND r.userId = '"+userId+"'" ;
           
            userFinalQuery = query;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Reviews r = new Reviews();
                r.setBusinessId(rs.getString(1));
                r.setText(rs.getString(2));
                reviews.add(r);
            }
        } catch (SQLException se){
            System.err.println("Query error: SQL Exception");
            se.printStackTrace();
        } catch (Exception e){
            System.err.println("Query error ");
            e.printStackTrace();
        }
        return reviews;
    }
}
