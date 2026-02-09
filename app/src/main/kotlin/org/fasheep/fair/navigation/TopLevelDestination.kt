package org.fasheep.fair.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.fasheep.fair.R
import org.fasheep.fair.feature.homepage.navigation.HomepageRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    @DrawableRes val icon: Int,
    @StringRes val descriptionTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route
) {
    HOMEPAGE(
        icon = R.drawable.assignment,
        descriptionTextId = R.string.app_name,
        route = HomepageRoute::class,
        baseRoute = HomepageRoute::class
    ),
//    VOTE(R.drawable.vote, R.string.app_name),
//    SORTITION(R.drawable.random, R.string.app_name),
//    HISTORY(R.drawable.history, R.string.app_name),
}
