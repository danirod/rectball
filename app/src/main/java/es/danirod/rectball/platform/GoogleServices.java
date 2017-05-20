package es.danirod.rectball.platform;

/**
 * @author danirod
 * @since 0.4.0
 */
public interface GoogleServices {

    void signIn();

    void signOut();

    boolean isSignedIn();

    void uploadScore(int score, int time);

    void showLeaderboards();

    void showAchievements();
}
