package org.fasheep.fair.navigation

import androidx.annotation.DrawableRes
import org.fasheep.fair.R

enum class TopLevelDestination(
    @DrawableRes val icon: Int,
    val description: String
) {
    ASSIGNMENT(R.drawable.assignment, "assignment"),
    VOTE(R.drawable.vote, "vote"),
    SORTITION(R.drawable.random, "sortition"),
    HISTORY(R.drawable.history, "history"),
}
