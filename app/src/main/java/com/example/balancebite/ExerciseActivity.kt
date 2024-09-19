package com.example.balancebite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ExerciseActivity : AppCompatActivity() {

    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var exercises: List<Exercise>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        val recyclerView = findViewById<RecyclerView>(R.id.rvExercises)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sample data for exercises
        exercises = listOf(
            Exercise(
                "1 to 3 Years (Toddlers)",
                "Crawling, walking, running\nPlaying with balls (throwing, kicking)\nDancing to music\nClimbing on soft, safe equipment\nPush-and-pull toys",
                "At least 60 minutes of active play spread throughout the day"
            ),
            Exercise(
                "3 to 6 Years (Preschoolers)",
                "Jumping, skipping, hopping\nSimple running races or tag\nRiding a tricycle or beginner bicycle\nBeginner swimming\nDancing or gymnastics\nPlayground activities",
                "1-2 hours of moderate to vigorous physical activity daily"
            ),
            Exercise(
                "6 to 12 Years (School-age Children)",
                "Running, skipping, jumping rope\nSoccer, basketball, cricket\nSwimming or cycling\nGymnastics or martial arts\nHiking or nature walks\nBasic strength exercises",
                "At least 1 hour of moderate to vigorous physical activity daily"
            ),
            Exercise(
                "12 to 18 Years (Adolescents)",
                "Sports like football, basketball\nRunning, cycling, swimming\nStrength training\nYoga or Pilates\nDancing, aerobics\nHIT",
                "At least 60 minutes of moderate to vigorous physical activity daily"
            ),
            Exercise(
                "18 to 24 Years",
                "Running, cycling, swimming\nStrength training\nHIT\nYoga or Pilates\nSports\nHiking or group fitness classes",
                "At least 150 minutes of moderate aerobic activity weekly"
            ),
            Exercise(
                "24 to 34 Years",
                "Weight training 2-3 times a week\nCardio exercises\nHIT\nSports\nYoga, stretching, or Pilates",
                "150 minutes of moderate-intensity aerobic activity weekly"
            ),
            Exercise(
                "34 to 44 Years",
                "Cardio activities like jogging, cycling, brisk walking, or swimming\n" +
                        "Strength training (free weights, machines, or body weight) 2-3 times a week\n" +
                        "Yoga, Pilates, or stretching for flexibility and relaxation\n" +
                        "Group classes like Zumba, kickboxing, or aerobics\n" +
                        "Sports (tennis, badminton, or squash)",
                "150-300 minutes of moderate aerobic activity per week, along with strength training and flexibility exercises."
            ),
            Exercise(
                "44 to 55 Years",
                "Walking, swimming, cycling, or light jogging\n" +
                        "Strength training with weights or resistance bands 2-3 times a week\n" +
                        "Low-impact aerobics or dance classes\n" +
                        "Pilates, yoga, or Tai Chi for flexibility, balance, and stress relief\n" +
                        "Hiking or nature walks for cardiovascular fitness and relaxation",
                "150-300 minutes of moderate activity or 75-150 minutes of vigorous aerobic activity weekly, with strength and flexibility exercises."
            ),
            Exercise(
                "55 Years and Older",
                "Walking, swimming, or cycling for low-impact cardio\n" +
                        "Strength training with light weights or resistance bands (2-3 times a week)\n" +
                        "Water aerobics for joint-friendly movement\n" +
                        "Yoga, Tai Chi, or Pilates for flexibility and balance\n" +
                        "Chair exercises or gentle stretching routines",
                "150 minutes of moderate-intensity aerobic activity weekly, with strength exercises for muscle and bone health, plus balance exercises to prevent falls."
            )
        )

        exerciseAdapter = ExerciseAdapter(exercises)
        recyclerView.adapter = exerciseAdapter
    }
}
