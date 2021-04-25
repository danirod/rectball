package es.danirod.rectball.android

import android.content.Context

class GooglePlayConstants(val context: Context): GameServicesConstants {
    override val leaderboardHighestScore: String
        get() = context.getString(R.string.leaderboard_highest_score)
    override val leaderboardHighestLength: String
        get() = context.getString(R.string.leaderboard_highest_length)
    override val achievementStarter: String
        get() = context.getString(R.string.achievement_starter)
    override val achievementExperienced: String
        get() = context.getString(R.string.achievement_experienced)
    override val achievementMaster: String
        get() = context.getString(R.string.achievement_master)
    override val achievementScoreBreaker: String
        get() = context.getString(R.string.achievement_score_breaker)
    override val achievementCasualPlayer: String
        get() = context.getString(R.string.achievement_casual_player)
    override val achievementPerserverant: String
        get() = context.getString(R.string.achievement_perseverant)
    override val achievementCommuter: String
        get() = context.getString(R.string.achievement_commuter)


}