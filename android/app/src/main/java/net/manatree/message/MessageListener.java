package net.manatree.message;

/**
 * Created by jin on 5/28/16.
 */
public interface MessageListener {
    void add(FriendlyMessage aFriendlyMessage);

    void populated();
}
