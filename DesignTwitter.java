/**
 * TC: follow, unfollow, postTweet : O(1), getNewsFeed: O(n * k * log 10), n = no of users followed, k = average number of tweets, 10 for most recent tweets
 * SC: O(max(totalTweets, newsFeedTweets))
 * LeetCode: Y(https://leetcode.com/problems/design-twitter/)
 * Approach: Create a tweet class to store tweetId, and the time the tweet was tweeted
 * use a hashset for followers i.e. map: <user, set of people followed by user> for O(1) follow and unfollow users
 * post tweet: create a new tweet and add it in the tweets hashmap for the tweets by that user
 * get newsfeed: maintain a minheap of most recent 10 tweets. if you see a new tweet then remove the oldest tweet at root. advantage of minheap over maxheap is that this way the heap size does not exceed 10
 */
class Twitter {

    class Tweet {
        int tweetId;
        int tweetTime;
        public Tweet(int tweetId, int tweetTime) {
            this.tweetId = tweetId;
            this.tweetTime = tweetTime;
        }
    }
    Map<Integer, HashSet<Integer>> following;
    Map<Integer, List<Tweet>> tweets;
    int time;
    /** Initialize your data structure here. */
    public Twitter() {
        following = new HashMap<>();
        tweets = new HashMap<>();
        time = 0;
    }
    
    /** Compose a new tweet. */
    public void postTweet(int userId, int tweetId) {
        Tweet newTweet = new Tweet(tweetId, time++);
        if(!tweets.containsKey(userId)) {
            tweets.put(userId, new ArrayList<>());
        }
        
        tweets.get(userId).add(newTweet);
    }
    
    /** Retrieve the 10 most recent tweet ids in the user's news feed. Each item in the news feed must be posted by users who the user followed or by the user herself. Tweets must be ordered from most recent to least recent. */
    public List<Integer> getNewsFeed(int userId) {
        follow(userId, userId);
        PriorityQueue<Tweet> minHeap = new PriorityQueue<>((a,b) -> a.tweetTime - b.tweetTime);
        // get the users followed by userId
        Set<Integer> followingIds = following.get(userId);
        // if there are valid user(s) userId is following
        if(followingIds != null) {
            for(Integer followingId : followingIds) {
                // get tweet(s) of each user followed by userId
                List<Tweet> tweetsOfFollowing = tweets.get(followingId);
                // if the user followed by userId has their own tweet then add to priority queue
                if(tweetsOfFollowing != null) {
                    for(Tweet tweet : tweetsOfFollowing) {
                        // add to priority queue
                        minHeap.add(tweet);
                        // maintain newsfeed size
                        if(minHeap.size() > 10) {
                            minHeap.remove();
                        }
                    }
                }
            }
        }
        // populate newsFeed
        List<Integer> newsFeed = new ArrayList<>();
        while(!minHeap.isEmpty()) {
            newsFeed.add( minHeap.remove().tweetId);
        }
        // reverse newsFeed
        Collections.reverse(newsFeed);
        // return newsFeed
        return newsFeed;
    }
    
    /** Follower follows a followee. If the operation is invalid, it should be a no-op. */
    public void follow(int followerId, int followeeId) {
        // create a list of followers for user
        if(!following.containsKey(followerId)){
            following.put(followerId, new HashSet<>());
        }
        // update following
        following.get(followerId).add(followeeId);
    }
    
    /** Follower unfollows a followee. If the operation is invalid, it should be a no-op. */
    public void unfollow(int followerId, int followeeId) {
        // remove a valid follower
        if(following.containsKey(followerId)){
            following.get(followerId).remove(followeeId);
        }
        
    }
}

/**
 * Your Twitter object will be instantiated and called as such:
 * Twitter obj = new Twitter();
 * obj.postTweet(userId,tweetId);
 * List<Integer> param_2 = obj.getNewsFeed(userId);
 * obj.follow(followerId,followeeId);
 * obj.unfollow(followerId,followeeId);
 */
