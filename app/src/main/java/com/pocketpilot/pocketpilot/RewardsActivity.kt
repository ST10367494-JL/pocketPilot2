package com.pocketpilot.pocketpilot

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RewardsActivity : AppCompatActivity() {

    private var points = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards)

        val pointsText = findViewById<TextView>(R.id.pointsText)
        val addPointsButton = findViewById<Button>(R.id.addPointsButton)

        val sharedPref = getSharedPreferences("Rewards", Context.MODE_PRIVATE)
        points = sharedPref.getInt("points", 0)

        pointsText.text = "Points: $points"

        addPointsButton.setOnClickListener {
            points += 10

            sharedPref.edit().putInt("points", points).apply()

            pointsText.text = "Points: $points"
        }
    }
}
