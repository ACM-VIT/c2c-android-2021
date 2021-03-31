package com.acmvit.c2c2021.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.model.Prize
import com.acmvit.c2c2021.ui.adapters.PrizesAdapter
import java.util.*

class PrizesActivity : AppCompatActivity() {
    var prizesRecyclerView: RecyclerView? = null
    var prizes: ArrayList<Prize> = ArrayList<Prize>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prizes)

        prizes.add(
            Prize(
                R.drawable.ic_prize_1,
                getString(R.string.prize_1),
                getString(R.string.prize_money_1)
            )
        )
        prizes.add(
            Prize(
                R.drawable.ic_prize_2,
                getString(R.string.prize_2),
                getString(R.string.prize_money_2)
            )
        )
        prizes.add(
            Prize(
                R.drawable.ic_prize_3,
                getString(R.string.prize_3),
                getString(R.string.prize_money_3)
            )
        )
        prizes.add(
            Prize(
                R.drawable.ic_prize_4,
                getString(R.string.prize_4),
                getString(R.string.prize_money_4)
            )
        )
        prizes.add(
            Prize(
                R.drawable.ic_prize_4,
                getString(R.string.prize_5),
                getString(R.string.prize_money_5)
            )
        )
        prizes.add(
            Prize(
                R.drawable.ic_prize_4,
                getString(R.string.prize_6),
                getString(R.string.prize_money_6)
            )
        )
        prizes.add(
            Prize(
                R.drawable.ic_prize_4,
                getString(R.string.prize_7),
                getString(R.string.prize_money_7)
            )
        )
        prizes.add(
            Prize(
                R.drawable.ic_prize_4,
                getString(R.string.prize_8),
                getString(R.string.prize_money_8)
            )
        )
        prizes.add(
            Prize(
                R.drawable.ic_prize_4,
                getString(R.string.prize_9),
                getString(R.string.prize_money_9)
            )
        )
        prizesRecyclerView = findViewById(R.id.prizes_rv)
        val prizesLinearLayoutManager = LinearLayoutManager(this)
        prizesLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        prizesRecyclerView?.layoutManager = prizesLinearLayoutManager
        prizesRecyclerView?.adapter = PrizesAdapter(prizes)
    }
}