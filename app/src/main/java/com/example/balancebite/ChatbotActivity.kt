package com.example.balancebite

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.database.*
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth


data class DietPlanDay(
    val Breakfast: String = "",
    val Lunch: String = "",
    val Dinner: String = "",
    val Snack: String = "",
    val Snack2: String = ""
)

data class NaiveBayesModel(
    val trainingData: Map<String, String>
) {
    private val classProbabilities: MutableMap<String, Double> = mutableMapOf()
    private val wordProbabilities: MutableMap<String, MutableMap<String, Double>> = mutableMapOf()

    init {
        train()
    }

    private fun train() {
        val totalMessages = trainingData.size.toDouble()

        trainingData.forEach { (input, response) ->
            val words = input.split(" ")
            classProbabilities[response] = classProbabilities.getOrDefault(response, 0.0) + 1.0

            words.forEach { word ->
                if (word !in wordProbabilities) {
                    wordProbabilities[word] = mutableMapOf()
                }
                wordProbabilities[word]!![response] = wordProbabilities[word]!!.getOrDefault(response, 0.0) + 1.0
            }
        }

        classProbabilities.forEach { (key, value) ->
            classProbabilities[key] = value / totalMessages
        }

        wordProbabilities.forEach { (word, responses) ->
            responses.forEach { (response, count) ->
                wordProbabilities[word]!![response] = count / classProbabilities[response]!!
            }
        }
    }

    fun predict(input: String): String {
        val words = input.split(" ")
        val responseScores = mutableMapOf<String, Double>()

        classProbabilities.forEach { (response, classProb) ->
            var score = classProb

            words.forEach { word ->
                val wordProb = wordProbabilities[word]?.get(response) ?: 0.0
                score *= (wordProb + 1e-10)
            }

            responseScores[response] = score
        }

        val maxScore = responseScores.values.maxOrNull() ?: 1.0
        responseScores.forEach { (response, score) ->
            responseScores[response] = score / maxScore
        }

        return responseScores.maxByOrNull { it.value }?.key ?: "I don't understand."
    }
}

class ChatbotActivity : AppCompatActivity() {

    private lateinit var userAgeGroup: String
    private lateinit var selectedDay: String
    private var userAge: Int = 0
    private lateinit var userProfile: UserProfile
    private lateinit var firebaseAuth: FirebaseAuth


    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var dietPlanRef: DatabaseReference
    private lateinit var userProfileRef: DatabaseReference

    private lateinit var naiveBayesModel: NaiveBayesModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

         val trainingData: Map<String, String> = mapOf(
            "What should I eat?" to "You can eat fruits and nuts.",
            "Tell me about breakfast." to "A healthy breakfast includes eggs and toast.",
            "I want a diet plan." to "What is your age group?",
            "Give me a diet for day 1." to "You can have a fruit salad for breakfast.",
            "hey" to "How can I assist you?",
            "Hi" to "Welcome to BalanceBite Chatbot, How can I help you?",
            "Show my profile" to "Fetching your profile...",
            "What are some healthy snacks?" to "You can try yogurt, nuts, or fruit.",
            "Can you suggest a lunch?" to "How about a salad with grilled chicken?",
            "What is a balanced diet?" to "A balanced diet includes a variety of foods: fruits, vegetables, whole grains, proteins, and healthy fats.",
            "How many calories should I consume?" to "It depends on your age, gender, and activity level. A general guideline is around 2000-2500 calories for adults.",
            "What are good sources of protein?" to "Good sources include chicken, fish, beans, nuts, and dairy products.",
            "How can I lose weight?" to "To lose weight, focus on eating healthy foods, controlling portion sizes, and exercising regularly.",
            "What should I avoid in my diet?" to "Try to avoid processed foods, excessive sugars, and high-fat foods.",
            "What are some healthy breakfast options?" to "Oatmeal, smoothies, and eggs are great breakfast choices.",
            "Can you give me dinner ideas?" to "How about grilled salmon with vegetables or a vegetable stir-fry?",
            "What are some benefits of eating fruits?" to "Fruits are rich in vitamins, minerals, and fiber, which can improve your health.",
            "How often should I eat?" to "It’s generally recommended to eat three balanced meals a day, with healthy snacks in between if needed.",
            "What is the best diet for weight loss?" to "A well-balanced diet low in sugars and refined carbs is effective for weight loss. Consult a dietitian for personalized advice.",
            "What should I drink to stay hydrated?" to "Water is the best choice for hydration. Herbal teas and infused water are also great options.",
            "Do you have tips for meal prep?" to "Plan your meals ahead of time, batch cook, and store healthy options in convenient containers.",
            "What are some quick healthy meals?" to "Try salads, wraps, or stir-fries for quick and healthy meal options.",
            "Can you recommend some desserts?" to "Consider fruits, yogurt with honey, or dark chocolate as healthier dessert options.",
            "Is it okay to eat snacks?" to "Yes, healthy snacks can help maintain energy levels. Choose options like fruits, nuts, or yogurt.",
            "How can I increase my fiber intake?" to "Include more fruits, vegetables, whole grains, and legumes in your diet.",
            "What is a portion size?" to "A portion size is the amount of food you choose to eat. It can vary depending on the food type."
            // Add more as needed
        )


        naiveBayesModel = NaiveBayesModel(trainingData)
        firebaseAuth = FirebaseAuth.getInstance()

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        val chatbotInput = findViewById<EditText>(R.id.chatbotInput)
        val chatbotSendButton = findViewById<Button>(R.id.chatbotSendButton)

        firebaseDatabase = FirebaseDatabase.getInstance()
        dietPlanRef = firebaseDatabase.getReference("dietPlans")
        userProfileRef = firebaseDatabase.getReference("Users") // Adjust the path as per your database structure

        chatbotSendButton.setOnClickListener {
            val userMessage = chatbotInput.text.toString()
            if (userMessage.isNotEmpty())
            {
                displayUserMessage(userMessage)
                handleUserInput(userMessage)
                chatbotInput.text.clear()
            }
        }
    }

    private fun handleUserInput(input: String) {
        // Convert input to lower case for easier matching
        val caseInput = input

        // Check for user profile request
        if (caseInput.contains("profile")) {
            fetchUserProfile()
            return
        }

        // Check for diet-related queries
        if (caseInput.contains("diet") || caseInput.contains("eat")) {
            val response = naiveBayesModel.predict(input)
            chatbotReply(response)
            return
        }

        // Check for help requests
        if (caseInput.contains("help") || caseInput.contains("what can you do")) {
            chatbotReply("I can assist you with diet plans, profile inquiries, and more. Just ask me!")
            return
        }

        // Try to get a response from the training data
        getResponseFromTrainingData(input)?.let { responseFromTrainingData ->
            chatbotReply(responseFromTrainingData)
            return
        }

        // Handle user age input and corresponding actions
        when {
            caseInput.contains("suggest me a diet plan") -> {
                chatbotReply("Please tell me your age.")
                userAge = caseInput.toInt()
                userAgeGroup = determineAgeGroup(userAge)
                chatbotReply("You are in the $userAgeGroup age group. Which day's plan would you like? (e.g., Day1)")
                selectedDay = input
                fetchDietPlanForDay(userAgeGroup, selectedDay)
            }
            caseInput.contains("age") -> {
                chatbotReply("Please enter your age:")
            }
            caseInput.toIntOrNull() != null -> {
                userAge = caseInput.toInt()
                userAgeGroup = determineAgeGroup(userAge)
                chatbotReply("You are in the $userAgeGroup age group. Which day's plan would you like? (e.g., Day1)")
            }

            caseInput.contains("day") && caseInput.contains("plan") -> {
                chatbotReply("Please specify which day you would like the diet plan for (e.g., Day1).")
            }
            caseInput.contains("Day") -> {
                selectedDay = input
                fetchDietPlanForDay(userAgeGroup, selectedDay)
                return
            }
            caseInput.contains("recommend") || caseInput.contains("suggest") -> {
                chatbotReply("I can suggest diet plans based on your age. Please tell me your age.")
                return
            }
            caseInput.contains("calories") -> {
                chatbotReply("Are you asking for calorie information for a specific food? Please specify.")
                return
            }
            caseInput.contains("exercise") -> {
                chatbotReply("I can help with exercise recommendations. What are you looking for?")
                return
            }
            caseInput.contains("food") -> {
                chatbotReply("What type of food are you inquiring about? I can help with diets, fruits, or vegetables.")
                return
            }
            caseInput.contains("hello") || caseInput.contains("hi") -> {
                chatbotReply("Hello! How can I assist you today?")
                return
            }
            caseInput.contains("bye") || caseInput.contains("exit") -> {
                chatbotReply("Goodbye! Have a great day!")
                return
            }
            // Default response for unrecognized queries
            else -> {
                chatbotReply("Sorry, I'm the BalanceBite Chatbot. I can only assist with diet-related queries. Please try asking about diet plans, your profile, or exercise.")
            }
        }
    }


    private fun getResponseFromTrainingData(input: String): String? {
        // Normalize the input string
        val normalizedInput = input.trim().lowercase()

        // Print to check the input being searched
        println("Looking for response for: '$normalizedInput'")

        // Check for a match in the training data
        val trainingData = naiveBayesModel.trainingData
        for ((key, response) in trainingData) {
            if (key.lowercase() == normalizedInput) {
                return response // Return the response if found
            }
        }

        // Print to indicate no match found
        println("No match found for: '$normalizedInput'")
        return null // Return null if no match found
    }


    private fun determineAgeGroup(age: Int): String {
        return when (age) {
            in 1..3 -> "1to3Years"
            in 3..6 -> "3to6Years"
            in 6..12 -> "6to12Years"
            in 12..18 -> "12to18Years"
            in 18..24 -> "18to24Years"
            in 24..34 -> "24to34Years"
            in 34..44 -> "34to44Years"
            in 44..54 -> "44to54Years"
            else -> "55YearsandAbove"
        }
    }

    private fun fetchUserProfile() {
        // Assuming the user ID is available
        val userId = firebaseAuth.currentUser?.uid // Replace with actual user ID retrieval logic
        if (userId != null) {
            userProfileRef.child(userId).child("profile").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userProfile = snapshot.getValue(UserProfile::class.java) ?: UserProfile()
                    if (userProfile.name.isNotEmpty()) {
                        displayUserProfile(userProfile)
                    } else {
                        chatbotReply("No profile found.")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    chatbotReply("Error fetching profile: ${error.message}")
                }
            })
        }
        else
        {
            chatbotReply("You are not logged in. Please log in to see your profile.")
        }
    }

    private fun displayUserProfile(profile: UserProfile) {
        val profileMessage = """
            Name: ${profile.name}
            Age: ${profile.age}
            Height: ${profile.height}
            Weight: ${profile.weight}
            Health Info: ${profile.healthInfo}
        """.trimIndent()
        chatbotReply(profileMessage)
    }

    @SuppressLint("SetTextI18n")
    private fun displayUserMessage(message: String) {
        val cardView = CardView(this)
        cardView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        cardView.cardElevation = 4f
        cardView.radius = 12f
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white)) // Set a different color for user messages

        val messageTextView = TextView(this)
        messageTextView.text = "You: $message"
        messageTextView.setTextColor(ContextCompat.getColor(this, R.color.black))

        messageTextView.setPadding(16, 16, 16, 16)

        cardView.addView(messageTextView)

        val messageContainer = findViewById<LinearLayout>(R.id.messageContainer)
        messageContainer.addView(cardView)

        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        scrollView.post { scrollView.smoothScrollTo(0, messageContainer.bottom) }
    }

    private fun fetchDietPlanForDay(ageGroup: String, day: String) {
        val dayRef = dietPlanRef.child(ageGroup).child(day)

        dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dietPlanDay = snapshot.getValue(DietPlanDay::class.java)
                dietPlanDay?.let { showDietPlan(it) } ?: run {
                    chatbotReply("No diet plan found for $day in the $ageGroup age group.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                chatbotReply("Error fetching diet plan: ${error.message}")
            }
        })
    }

    private fun showDietPlan(dietPlanDay: DietPlanDay) {
        val planMessage = """
            Breakfast: ${dietPlanDay.Breakfast}
            Lunch: ${dietPlanDay.Lunch}
            Dinner: ${dietPlanDay.Dinner}
            Snack 1: ${dietPlanDay.Snack}
            Snack 2: ${dietPlanDay.Snack2}
        """.trimIndent()

        chatbotReply(planMessage)
    }

    @SuppressLint("SetTextI18n")
    private fun chatbotReply(message: String) {
        val cardView = CardView(this)
        cardView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        cardView.cardElevation = 4f
        cardView.radius = 12f
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))

        val messageTextView = TextView(this)
        messageTextView.text = "BalanceBite Chatbot: $message"
        messageTextView.setTextColor(ContextCompat.getColor(this, R.color.gray))

        messageTextView.setPadding(16, 16, 16, 16)

        cardView.addView(messageTextView)

        val messageContainer = findViewById<LinearLayout>(R.id.messageContainer)
        messageContainer.addView(cardView)

        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        scrollView.post { scrollView.smoothScrollTo(0, messageContainer.bottom) }
    }
}