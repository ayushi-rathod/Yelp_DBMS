CREATE TABLE Users(
  userId VARCHAR2(50) primary key,
  name VARCHAR2(50),
  yelpingSince date,
  votesFunny int,
  votesUseful int,
  votesCool int,
  total_votes number,
  review_count int,
  fans int,
  averageStars float
);

CREATE TABLE UserFriends(userId VARCHAR2(50), friend_id VARCHAR2(50));

CREATE TABLE Reviews(
  reviewId VARCHAR2(50) primary key,
  stars number check (
    stars between 1 and 5
    ),
  publishDate date,
  text VARCHAR2(3000),
  businessId VARCHAR2(50),
  userId VARCHAR2(50),
  votesFunny number,
  votes_cool number,
  votes_useful number,
  total_votes number
);

CREATE TABLE BUSINESS(
  businessId VARCHAR2(50) primary key,
  name VARCHAR2(100),
  address VARCHAR2(200),
  city VARCHAR2(50),
  state VARCHAR2(10),
  review_count int,
  stars float
);

CREATE TABLE BUSINESS_CAT(businessId VARCHAR2(50), category VARCHAR2(50));

CREATE TABLE BUSINESS_SUBCAT(
  businessId VARCHAR2(50),
  subcategory VARCHAR2(50)
);

CREATE TABLE BUSINESS_ATTR (
  businessId VARCHAR2(50),
  attr_name VARCHAR2(50),
  attr_value VARCHAR2(50)
);

ALTER TABLESPACE SYSTEM ADD DATAFILE '/u01/app/oracle/oradata/XE/system1.dbf' SIZE 1200M;

ALTER TABLESPACE SYSTEM ADD DATAFILE '/u01/app/oracle/oradata/XE/syste2.dbf' SIZE 1200M;

-- create INDEX ReviewsIndex on Reviews (reviewId, stars, publishDate, text, userId);
-- 
-- create INDEX BusinessIndex on BUSINESS (businessId, name, city, state, stars);

CREATE INDEX ReviewsIndex on Reviews (reviewId, stars, publishDate, text, userId);
CREATE INDEX BusinessIndex on BUSINESS (businessId, name, city, state, stars);