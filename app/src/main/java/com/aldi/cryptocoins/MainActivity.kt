package com.aldi.cryptocoins

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aldi.cryptocoins.architecture.navigation.api.Navigator
import com.aldi.cryptocoins.features.coinlist.navigation.CoinListDestination
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val navigator = get<Navigator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator.bindToActivity(this, R.id.mainFragmentContainer)

        if (savedInstanceState == null) {
            navigator.navigateTo(CoinListDestination)
        }
    }
}
