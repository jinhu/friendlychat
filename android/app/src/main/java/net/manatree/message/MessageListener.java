package net.manatree.message;

import net.manatree.chat.FriendlyMessage;

/**
 * Created by jin on 5/28/16.
 */
public interface MessageListener {
    void add(FriendlyMessage aFriendlyMessage);

    void populated();
}
