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

data class ChatMessage(
    val sender: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
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
    private lateinit var chatMessagesRef: DatabaseReference
    private lateinit var auth: FirebaseAuth


    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var dietPlanRef: DatabaseReference
    private lateinit var userProfileRef: DatabaseReference

    private lateinit var naiveBayesModel: NaiveBayesModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        val trainingData: Map<String, String> = mapOf(
            "What is a balanced diet?" to "A balanced diet includes a variety of foods",
            "What should I eat?" to "You can eat fruits and nuts.",
            "Tell me about breakfast." to "A healthy breakfast includes eggs and toast.",
            "I want a diet plan." to "What is your age group?",
            "Give me a diet for day 1." to "You can have a fruit salad for breakfast.",
            "hey" to "How can I assist you?",
            "Hi" to "Welcome to BalanceBite Chatbot, How can I help you?",
            "Show my profile" to "Fetching your profile...",
            "What are some healthy snacks?" to "You can try yogurt, nuts, or fruit.",
            "Can you suggest a lunch?" to "How about a salad with grilled chicken?",
            "What is a balanced diet?" to "A balanced diet includes a variety of foods: fruits, vegetables, " +
                    "whole grains, proteins, and healthy fats.",
            "How many calories should I consume?" to "It depends on your age, gender, and activity level. " +
                    "A general guideline is around 2000-2500 calories for adults.",
            "What are good sources of protein?" to "Good sources include chicken, fish, beans, nuts, and dairy products.",
            "How can I lose weight?" to "To lose weight, focus on eating healthy foods, controlling portion sizes, and exercising regularly.",
            "What should I avoid in my diet?" to "Try to avoid processed foods, excessive sugars, and high-fat foods.",
            "What are some healthy breakfast options?" to "Oatmeal, smoothies, and eggs are great breakfast choices.",
            "Can you give me dinner ideas?" to "How about grilled salmon with vegetables or a vegetable stir-fry?",
            "What are some benefits of eating fruits?" to "Fruits are rich in vitamins, minerals, and fiber, which can improve your health.",
            "How often should I eat?" to "It’s generally recommended to eat three balanced meals a day, with healthy snacks in between if needed.",
            "What is the best diet for weight loss?" to "A well-balanced diet low in sugars and refined carbs is effective for weight loss. " +
                    "Consult a dietitian for personalized advice.",
            "What should I drink to stay hydrated?" to "Water is the best choice for hydration. Herbal teas and infused water are also great options.",
            "Do you have tips for meal prep?" to "Plan your meals ahead of time, batch cook, and store healthy options in convenient containers.",
            "What are some quick healthy meals?" to "Try salads, wraps, or stir-fries for quick and healthy meal options.",
            "Can you recommend some desserts?" to "Consider fruits, yogurt with honey, or dark chocolate as healthier dessert options.",
            "Is it okay to eat snacks?" to "Yes, healthy snacks can help maintain energy levels. Choose options like fruits, nuts, or yogurt.",
            "How can I increase my fiber intake?" to "Include more fruits, vegetables, whole grains, and legumes in your diet.",
            "What is a portion size?" to "A portion size is the amount of food you choose to eat. It can vary depending on the food type.",
            "Can you give me examples of whole grains?" to "Whole grains include oats, brown rice, quinoa, and whole wheat bread.",
            "What should I include in my salad?" to "Add a variety of vegetables, lean protein, nuts, and a healthy dressing.",
            "How can I reduce sugar intake?" to "Limit sugary drinks, read labels for added sugars, and choose whole foods over processed snacks.",
            "What are healthy fats?" to "Healthy fats include avocados, olive oil, nuts, and fatty fish like salmon.",
            "How can I improve my eating habits?" to "Start by making small changes, such as cooking more at home and choosing whole foods.",
            "What are some vegetarian protein sources?" to "You can get protein from beans, lentils, tofu, quinoa, and nuts.",
            "What is the difference between saturated and unsaturated fats?" to "Saturated fats are usually solid at room temperature and found in animal products. " +
                    "Unsaturated fats are liquid and found in plant oils.",
            "How can I eat healthy on a budget?" to "Plan your meals, buy seasonal fruits and vegetables, and consider bulk purchases.",
            "What is intermittent fasting?" to "Intermittent fasting is an eating pattern that cycles between periods of fasting and eating.",
            "Can you suggest some healthy cooking methods?" to "Try steaming, grilling, baking, or sautéing instead of frying.",
            "What are the health benefits of vegetables?" to "Vegetables are low in calories and rich in vitamins, minerals, and antioxidants.",
            "How do I read food labels?" to "Check the serving size, calories, and nutrients to understand what you are consuming.",
            "Can you help me with portion control?" to "Use smaller plates, measure servings, and listen to your hunger cues.",
            "What is a superfood?" to "Superfoods are nutrient-rich foods considered to be beneficial for health, like blueberries and kale.",
            "What should I eat before a workout?" to "Opt for a meal rich in carbohydrates and moderate in protein, like a banana or yogurt with fruit.",
            "What are the signs of dehydration?" to "Signs include thirst, dry mouth, fatigue, and dark-colored urine.",
            "What is mindful eating?" to "Mindful eating involves paying attention to your food, savoring flavors, and recognizing hunger cues.",
            "What is the role of vitamins in our diet?" to "Vitamins support various bodily functions, including immune health and energy production.",
            "How can I maintain a healthy gut?" to "Include probiotics and fiber-rich foods like yogurt, kefir, and whole grains.",
            "What are the best sources of calcium?" to "Dairy products, leafy greens, almonds, and fortified foods are good calcium sources.",
            "How do I start a new diet?" to "Begin by setting realistic goals and consulting with a nutritionist for a personalized plan.",
            "Can you explain the food pyramid?" to "The food pyramid is a guide to help you understand the recommended food groups " +
                    "and proportions for a balanced diet.",
            "How much protein do I need daily?" to "The recommended daily intake is about 46 grams for women and 56 grams for men, " +
                    "but it can vary based on activity level.",
            "What are the effects of eating too much salt?" to "Excess salt can lead to high blood pressure and increase the risk of heart disease.",
            "Can you give me tips for healthy eating while traveling?" to "Pack healthy snacks, stay hydrated, " +
                    "and choose grilled options when dining out.",
            "How can I reduce my cholesterol levels?" to "Eat more fiber, reduce saturated fats, and include healthy fats like omega-3 fatty acids.",
            "What are some low-calorie snack options?" to "Consider air-popped popcorn, carrot sticks with hummus, or apple slices with almond butter.",
            "What are the benefits of drinking green tea?" to "Green tea is rich in antioxidants and may boost metabolism and improve brain function.",
            "How does meal timing affect metabolism?" to "Eating at regular intervals can help maintain energy levels and stabilize blood sugar.",
            "What should I eat after a workout?" to "Focus on a combination of protein and carbohydrates, like a protein shake with a banana.",
            "How can I support my immune system through diet?" to "Include a variety of colorful fruits and vegetables, lean proteins, " +
                    "and healthy fats in your meals.",
            "Can you tell me about the Mediterranean diet?" to "The Mediterranean diet emphasizes fruits, vegetables, whole grains, fish, " +
                    "and healthy fats, like olive oil.",
            "What is gluten, and who should avoid it?" to "Gluten is a protein found in wheat, barley, and rye. Those with celiac disease or " +
                    "gluten sensitivity should avoid it.",
            "What are some benefits of eating nuts?" to "Nuts are high in healthy fats, protein, fiber, and antioxidants, which can support heart health.",
            "How do probiotics benefit gut health?" to "Probiotics are live bacteria that can improve digestion and support the immune system.",
            "What are the effects of a high-sugar diet?" to "A high-sugar diet can lead to weight gain, increased risk of type 2 diabetes, " +
                    "and dental problems.",
            "What are some healthy alternatives to white rice?" to "Consider quinoa, brown rice, or cauliflower rice as healthier alternatives.",
            "What is meal prepping?" to "Meal prepping involves preparing meals in advance to save time and ensure healthy eating throughout the week.",
            "How can I stay motivated to eat healthy?" to "Set specific goals, find healthy recipes you enjoy, and consider joining a support group.",
            "What are the health benefits of eating berries?" to "Berries are rich in antioxidants and vitamins, which can boost heart health and " +
                    "improve brain function.",
            "Can you explain the importance of hydration?" to "Hydration is essential for maintaining bodily functions, regulating temperature, and " +
                    "supporting digestion.",
            "How can I recognize food intolerances?" to "Food intolerances can cause symptoms like bloating, gas, and stomach pain after eating certain foods.",
            "What is a DASH diet?" to "The DASH diet is designed to help reduce high blood pressure by emphasizing fruits, vegetables, whole grains, " +
                    "and low-fat dairy.",
            "How does fiber aid digestion?" to "Fiber helps keep the digestive system healthy by promoting regular bowel movements and preventing constipation.",
            "What are some ways to incorporate more vegetables into my meals?" to "Add vegetables to smoothies, stir-fries, " +
                    "and salads or try roasting them for a flavorful side dish.",
            "What are some good sources of omega-3 fatty acids?" to "Fatty fish like salmon, walnuts, flaxseeds, and chia seeds are great sources.",
            "How can I improve my sleep with diet?" to "Avoid caffeine and heavy meals before bedtime; instead, try foods rich in magnesium, like spinach and nuts.",
            "What is the importance of meal variety?" to "Variety ensures you get a range of nutrients and helps prevent dietary boredom.",
            "Can you recommend a healthy smoothie recipe?" to "Blend spinach, banana, almond milk, and a tablespoon of nut butter for a nutritious smoothie.",
            "How do I choose healthy snacks?" to "Look for options high in protein or fiber, like Greek yogurt, nuts, or fresh fruits.",
            "What are the benefits of eating whole foods?" to "Whole foods are less processed and packed with nutrients, promoting better health.",
            "How can I enjoy desserts while eating healthy?" to "Try fruit-based desserts, dark chocolate, or yogurt with honey instead of traditional sweets.",
            "What are some easy healthy meals for beginners?" to "Simple meals like scrambled eggs with veggies, quinoa salad, or baked chicken with steamed broccoli are easy to prepare.",
            "How can I boost my metabolism?" to "Incorporate strength training, eat enough protein, and stay hydrated to keep your metabolism active.",
            "What should I include in a balanced breakfast?" to "Include protein, whole grains, and healthy fats, such as eggs, oatmeal, and avocado.",
            "Can you explain the concept of caloric deficit?" to "A caloric deficit occurs when you consume fewer calories than your body burns, leading to weight loss.",
            "What are some healthy alternatives to sugary drinks?" to "Opt for water, herbal teas, or sparkling water with a splash of fruit juice instead of soda.",
            "How can I make healthy eating more enjoyable?" to "Experiment with new recipes, spices, and cooking techniques to make healthy meals exciting.",
            "What are the health benefits of eating beans?" to "Beans are high in fiber and protein, which can help with digestion and keep you feeling full.",
            "How does stress affect my eating habits?" to "Stress can lead to emotional eating or cravings for unhealthy foods; practicing mindfulness can help manage this.",
            "What are some tips for eating out healthily?" to "Choose grilled or baked options, ask for dressings on the side, and avoid fried foods.",
            "How can I track my daily calorie intake?" to "Use apps or a food diary to log your meals and monitor your caloric intake effectively.",
            "What role do antioxidants play in health?" to "Antioxidants help protect your body from oxidative stress and can reduce the risk of chronic diseases.",
            "How can I make healthier choices at the grocery store?" to "Stick to the perimeter of the store where fresh foods are located and avoid processed items in the middle aisles.",
            "What are some signs of a nutrient deficiency?" to "Signs may include fatigue, hair loss, brittle nails, or changes in skin health; consult a healthcare provider for diagnosis.",
            "How can I incorporate more protein into my diet?" to "Add protein-rich foods like chicken, fish, beans, lentils, and dairy to your meals and snacks.",
            "What is the role of hydration in weight loss?" to "Staying hydrated can help control appetite, boost metabolism, and improve overall health.",
            "Can you explain the benefits of mindful eating?" to "Mindful eating encourages awareness of hunger cues and helps prevent overeating by focusing on the eating experience.",
            "What are some heart-healthy foods?" to "Incorporate foods like salmon, avocados, nuts, and whole grains to support heart health.",
            "How does sugar impact my health?" to "Excess sugar consumption can lead to weight gain, increased risk of type 2 diabetes, and other health issues.",
            "What is the difference between whole grain and refined grain?" to "Whole grains contain the entire grain kernel, " +
                    "providing more fiber and nutrients than refined grains, which are processed.",
            "How can I improve my cooking skills?" to "Practice regularly, watch cooking tutorials, and experiment with new recipes to enhance your skills.",
            "What are the benefits of eating seasonal fruits and vegetables?" to "Seasonal produce is often fresher, tastier, and more nutritious, " +
                    "plus it's usually more affordable.",
            "How can I maintain a balanced diet while traveling?" to "Plan ahead by packing healthy snacks and researching restaurants that offer nutritious options.",
            "What is the importance of portion control?" to "Portion control helps manage caloric intake and can aid in maintaining a healthy weight."
        )


        naiveBayesModel = NaiveBayesModel(trainingData)
        firebaseAuth = FirebaseAuth.getInstance()

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        val chatbotInput = findViewById<EditText>(R.id.chatbotInput)
        val chatbotSendButton = findViewById<Button>(R.id.chatbotSendButton)


        firebaseDatabase = FirebaseDatabase.getInstance()
        dietPlanRef = firebaseDatabase.getReference("dietPlans")
        userProfileRef = firebaseDatabase.getReference("Users") // Adjust the path as per your database structure

        auth = FirebaseAuth.getInstance()

        val userId = auth.currentUser?.uid
        if (userId == null) {
            // Handle user not logged in (optional)
            finish() // Close activity or navigate to login
            return
        }

        // Initialize Firebase Database reference for the user
        val firebaseDatabase = FirebaseDatabase.getInstance()
        chatMessagesRef = firebaseDatabase.getReference("chatbotInteractionText").child(userId)

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

    private fun saveChatMessage(sender: String, message: String) {
        val chatMessage = ChatMessage(sender, message)

        chatMessagesRef.push().setValue(chatMessage)
            .addOnSuccessListener {
                // Optionally handle success
                println("Message saved successfully")
            }
            .addOnFailureListener { error ->
                // Optionally handle failure
                println("Error saving message: ${error.message}")
            }
    }


    private fun getChatbotResponse(userInput: String): String {
        val formattedInput = capitalizeFirstLetter(userInput.trim())

        val trainingData = naiveBayesModel.trainingData
        // Check if any key in trainingData contains the formatted input
        for (key in trainingData.keys) {
            if (key.contains(formattedInput, ignoreCase = true)) {
                return trainingData[key] ?: "I'm sorry, I don't understand that."
            }
        }

        return "I'm sorry, I don't understand that."
    }


    // Function to capitalize the first letter of the input
    private fun capitalizeFirstLetter(input: String): String {
        return if (input.isNotEmpty()) {
            input.replaceFirstChar { it.uppercase() }
        } else {
            input
        }
    }

    private fun handleUserInput(input: String) {
        // Convert input to lower case for easier matching
        val caseInput = input.uppercase()

        // Save the user's message
        saveChatMessage("User", input)

        // Example chatbot response logic
        val chatbotResponse = getChatbotResponse(input) // Replace with your response generation logic
        saveChatMessage("Chatbot", chatbotResponse)

        // Check for user profile request
        if (caseInput.contains("profile",ignoreCase = true)) {
            fetchUserProfile()
            return
        }

        // Check for diet-related queries
        if (caseInput.contains("diet",ignoreCase = true) || caseInput.contains("eat",ignoreCase = true)) {
            val response = naiveBayesModel.predict(input)
            chatbotReply(response)
            return
        }

        // Check for help requests
        if (caseInput.contains("help",ignoreCase = true) || caseInput.contains("what can you do",ignoreCase = true)) {
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
            caseInput.contains("suggest me a diet plan",ignoreCase = true) -> {
                chatbotReply("Please tell me your age.")
                userAge = caseInput.toInt()
                userAgeGroup = determineAgeGroup(userAge)
                chatbotReply("You are in the $userAgeGroup age group. Which day's plan would you like? (e.g., Day1)")
                selectedDay = input
                fetchDietPlanForDay(userAgeGroup, selectedDay)
            }

            caseInput.contains("suggest me an diet plan",ignoreCase = true)->{
                chatbotReply("Please tell me your age.")
                userAge = caseInput.toInt()
                userAgeGroup = determineAgeGroup(userAge)
                chatbotReply("You are in the $userAgeGroup age group. Which day's plan would you like? (e.g., Day1)")
                selectedDay = input
                fetchDietPlanForDay(userAgeGroup, selectedDay)
            }

            caseInput.contains("i need a diet plan", ignoreCase = true)->{
                chatbotReply("Please tell me your age.")
                userAge = caseInput.toInt()
                userAgeGroup = determineAgeGroup(userAge)
                chatbotReply("You are in the $userAgeGroup age group. Which day's plan would you like? (e.g., Day1)")
                selectedDay = input
                fetchDietPlanForDay(userAgeGroup, selectedDay)
            }

            caseInput.contains("i need a diet plan for me",ignoreCase = true)->{
                chatbotReply("Please tell me your age.")
                userAge = caseInput.toInt()
                userAgeGroup = determineAgeGroup(userAge)
                chatbotReply("You are in the $userAgeGroup age group. Which day's plan would you like? (e.g., Day1)")
                selectedDay = input
                fetchDietPlanForDay(userAgeGroup, selectedDay)
            }

            caseInput.contains("i need a diet plan for my age",ignoreCase = true)->{
                chatbotReply("Please tell me your age.")
                userAge = caseInput.toInt()
                userAgeGroup = determineAgeGroup(userAge)
                chatbotReply("You are in the $userAgeGroup age group. Which day's plan would you like? (e.g., Day1)")
                selectedDay = input
                fetchDietPlanForDay(userAgeGroup, selectedDay)
            }

            caseInput.contains("age",ignoreCase = true) -> {
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
            caseInput.contains("Day",ignoreCase = true) -> {
                selectedDay = input
                fetchDietPlanForDay(userAgeGroup, selectedDay)
                return
            }
            caseInput.contains("recommend",ignoreCase = true) || caseInput.contains("suggest",ignoreCase = true) -> {
                chatbotReply("I can suggest diet plans based on your age. Please tell me your age.")
                return
            }
            caseInput.contains("calories",ignoreCase = true) -> {
                chatbotReply("Are you asking for calorie information for a specific food? Please specify.")
                return
            }
            caseInput.contains("exercise",ignoreCase = true) -> {
                chatbotReply("I can help with exercise recommendations. What are you looking for?")
                return
            }
            caseInput.contains("food",ignoreCase = true) -> {
                chatbotReply("What type of food are you inquiring about? I can help with diets, fruits, or vegetables.")
                return
            }
            caseInput.contains("hello",ignoreCase = true) || caseInput.contains("hi",ignoreCase = true) -> {
                chatbotReply("Hello! How can I assist you today?")
                return
            }
            caseInput.contains("bye",ignoreCase = true) || caseInput.contains("exit",ignoreCase = true) -> {
                chatbotReply("Goodbye! Have a great day!")
                return
            }
            caseInput.contains("thank you",ignoreCase = true) -> {
                chatbotReply("You're welcome! If you have more questions, feel free to ask.")
                return
            }
            // Default response for unrecognized queries
            else -> {
                chatbotReply("Sorry, I'm the BalanceBite Chatbot. " +
                        "I can only assist with diet-related queries. " +
                        "Please try asking about diet plans, your profile, or exercise.")
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
        print("\n")
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
        messageTextView.text = "BalanceBite Chatbot:\n$message"
        messageTextView.setTextColor(ContextCompat.getColor(this, R.color.gray))

        messageTextView.setPadding(16, 16, 16, 16)

        cardView.addView(messageTextView)

        val messageContainer = findViewById<LinearLayout>(R.id.messageContainer)
        messageContainer.addView(cardView)

        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        scrollView.post { scrollView.smoothScrollTo(0, messageContainer.bottom) }
    }
}