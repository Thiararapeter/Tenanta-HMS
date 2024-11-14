package com.thiarara.tenantahms.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun NavController.navigateWithOptions(
    route: String,
    options: NavOptions? = null
) {
    this.navigate(route, options)
} 