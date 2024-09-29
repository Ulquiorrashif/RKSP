package PR3;

import io.reactivex.Observable;

public class UserFriend {
    private final int userId;
    private final int friendId;
    public UserFriend(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
    public int getUserId() {
        return userId;
    }
    public int getFriendId() {
        return friendId;
    }
    public static Observable<UserFriend> getFriends(int userId) {

        return Observable.fromArray(
                new UserFriend(userId, (int) (Math.random() * 100)),
                new UserFriend(userId, (int) (Math.random() * 100)),
                new UserFriend(userId, (int) (Math.random() * 100))
        );
    }

}
