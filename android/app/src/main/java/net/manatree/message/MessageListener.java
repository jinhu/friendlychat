package net.manatree.message;

/**
 * Created by jin on 5/28/16.
 */
public interface MessageListener {
    void add(String aMessage);

    void populated();

    void signIn();

    void signOut();

    String getPhotoUrl();
}
