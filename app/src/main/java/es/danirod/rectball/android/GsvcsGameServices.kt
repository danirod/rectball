package es.danirod.rectball.android

import de.golfgl.gdxgamesvcs.IGameServiceClient

class GsvcsGameServices(private val client: IGameServiceClient,
                        private val constants: GameServicesConstants) : GameServices {

    override fun signIn() {
        client.logIn()
    }

    override fun signOut() {
        client.logOff()
    }

    override val isSignedIn: Boolean
        get() = client.isSessionActive

    override fun uploadScore(score: Int, time: Int) {
        if (!client.isSessionActive) {
            return
        }

        client.submitToLeaderboard(constants.leaderboardHighestScore, score.toLong(), client.gameServiceId)
        client.submitToLeaderboard(constants.leaderboardHighestLength, time.toLong(), client.gameServiceId)

        if (score > 250) {
            client.unlockAchievement(constants.achievementStarter)
        }
        if (score > 2500) {
            client.unlockAchievement(constants.achievementExperienced)
        }
        if (score > 7500) {
            client.unlockAchievement(constants.achievementMaster)
        }
        if (score > 10000) {
            client.unlockAchievement(constants.achievementScoreBreaker)
        }

        if (score > 500) {
            client.incrementAchievement(constants.achievementCasualPlayer, 1, 1f)
            client.incrementAchievement(constants.achievementPerserverant, 1, 1f)
            client.incrementAchievement(constants.achievementCommuter, 1, 1f)
        }
    }

    override fun showLeaderboards() {
        client.showLeaderboards(constants.leaderboardHighestScore)
    }

    override fun showAchievements() {
        client.showAchievements()
    }

}