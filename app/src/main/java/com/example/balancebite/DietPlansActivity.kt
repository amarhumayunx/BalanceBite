    package com.example.balancebite

    import android.os.Bundle
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.content.ContextCompat
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView

            class DietPlanActivity : AppCompatActivity()
            {


                private lateinit var dietPlanAdapter: DietPlanAdapter
                private lateinit var dietPlanGroups: List<DietPlanGroup>

                override fun onCreate(savedInstanceState: Bundle?)
                {
                    super.onCreate(savedInstanceState)
                    setContentView(R.layout.activity_diet_plans)

                    window.statusBarColor = ContextCompat.getColor(this, R.color.green)

                    val recyclerView = findViewById<RecyclerView>(R.id.rvDietPlans)
                    recyclerView.layoutManager = LinearLayoutManager(this)

                    // Sample data for diet plans by age group
                    dietPlanGroups = listOf(
                        DietPlanGroup(
                            "1 to 3 Years",
                            listOf(
                                DietPlan("Day 1",
                                    "Breakfast: Mashed banana with a small bowl of oatmeal\n" +
                                        "Lunch: Soft khichdi (rice and lentil porridge) with mashed vegetables\n" +
                                        "Snack: Small piece of cheese or a few biscuits\n" +
                                        "Dinner: Pureed chicken with vegetables and soft roti"
                                ),
                                DietPlan("Day 2",
                                    "Breakfast: Soft idli with a mild sambar\n" +
                                        "Lunch: Rice with dal (lentil soup) and finely chopped vegetables\n" +
                                        "Snack: Yogurt with a bit of fruit puree\n" +
                                        "Dinner: Soft paneer (cottage cheese) and vegetable curry with small roti"
                                ),
                                DietPlan("Day 3",
                                    "Breakfast: Boiled egg and soft paratha\n" +
                                        "Lunch: Vegetable khichdi\n" +
                                        "Snack: Soft cheese or yogurt\n" +
                                        "Dinner: Chicken stew with soft roti"
                                ),
                                DietPlan("Day 4",
                                    "Breakfast: Porridge with fruit puree\n" +
                                            "Lunch: Rice with mashed vegetable curry\n" +
                                            "Snack: Soft fruit slices\n" +
                                            "Dinner: Lentil soup with soft roti"
                                ),
                                DietPlan("Day 5",
                                    "Breakfast: Scrambled egg with soft bread\n" +
                                            "Lunch: Chicken biryani (mild spices)\n" +
                                            "Snack: Mashed fruit bowl\n" +
                                            "Dinner: Soft roti with paneer curry\n"
                                )
                            )
                        ),
                        DietPlanGroup(
                            "3 to 6 Years",
                            listOf(
                                DietPlan("Day 1",
                                    "Breakfast: Whole grain cereal with milk\n" +
                                            "Lunch: Rice with vegetable curry\n" +
                                            "Snack: Fresh fruit slices or yogurt\n" +
                                            "Dinner: Soft chicken or tofu with steamed vegetables and rice"
                                ),
                                DietPlan("Day 2",
                                    "Breakfast: Oatmeal with chopped fruit\n" +
                                            "Lunch: Pasta with tomato sauce and vegetables\n" +
                                            "Snack: Cheese sticks or whole-grain crackers\n" +
                                            "Dinner: Lentil soup with bread and a side of cooked vegetables"
                                ),
                                DietPlan("Day 3",
                                    "Breakfast: Suji upma with peas\n" +
                                            "Lunch: Chicken kebabs with rice\n" +
                                            "Snack: Smoothie with yogurt and fruit\n" +
                                            "Dinner: Dal makhani with roti"
                                ),
                                DietPlan("Day 4",
                                    "Breakfast: Oats porridge with chopped nuts\n" +
                                            "Lunch: Keema (minced meat) with rice\n" +
                                            "Snack: Cheese cubes\n" +
                                            "Dinner: Fish curry with rice"
                                ),
                                DietPlan("Day 5",
                                    "Breakfast: Scrambled eggs with whole wheat toast\n" +
                                            "Lunch: Chicken biryani with cucumber salad\n" +
                                            "Snack: Fresh fruit slices\n" +
                                            "Dinner: Vegetable curry with roti"
                                )
                            )
                        ),
                        DietPlanGroup("6 to 12 Years",
                            listOf(
                                DietPlan("Day 1",
                                    "Breakfast: Boiled eggs with roti\n" +
                                            "Lunch: Chicken pulao with mixed vegetables\n" +
                                            "Snack: Apple slices\n" +
                                            "Dinner: Dal fry with roti and spinach"
                                ),
                                DietPlan("Day 2",
                                    "Breakfast: Masala omelette with whole wheat bread\n" +
                                            "Lunch: Vegetable biryani with raita\n" +
                                            "Snack: Mixed fruit bowl\n" +
                                            "Dinner: Chicken curry with rice"
                                ),
                                DietPlan("Day 3",
                                    "Breakfast: Pancakes with honey\n" +
                                            "Lunch: Chicken karahi with naan\n" +
                                            "Snack: Yogurt with honey\n" +
                                            "Dinner: Fish curry with roti"
                                ),
                                DietPlan("Day 4",
                                    "Breakfast: Chilla (gram flour pancake)\n" +
                                            "Lunch: Keema with rice and cucumber salad\n" +
                                            "Snack: Cheese sandwich\n" +
                                            "Dinner: Paneer curry with naan"
                                ),
                                DietPlan("Day 5",
                                    "Breakfast: Upma with peas\n" +
                                            "Lunch: Chicken kebabs with a side of rice\n" +
                                            "Snack: Fruit smoothie\n" +
                                            "Dinner: Aloo gobi (potato and cauliflower curry) with roti"
                                )
                            )
                        ),
                        DietPlanGroup("12 to 18 Years",
                            listOf(
                                DietPlan("Day 1",
                                    "Breakfast: Masala omelette with whole wheat toast\n" +
                                            "Lunch: Chicken biryani with raita\n" +
                                            "Snack: Mixed nuts or fresh fruit\n" +
                                            "Dinner: Lentil soup with rice and sautéed vegetables"
                                ),
                                DietPlan("Day 2",
                                    "Breakfast: Smoothie bowl with fruit and yogurt\n" +
                                            "Lunch: Paneer tikka with rice\n" +
                                            "Snack: Yogurt with honey\n" +
                                            "Dinner: Chicken karahi with roti"
                                ),
                                DietPlan("Day 3",
                                    "Breakfast: Stuffed paratha with curd\n" +
                                            "Lunch: Chicken keema with rice\n" +
                                            "Snack: Mixed fruit salad\n" +
                                            "Dinner: Fish curry with rice"
                                ),
                                DietPlan("Day 4",
                                    "Breakfast: Scrambled eggs with toast\n" +
                                            "Lunch: Vegetable biryani with cucumber salad\n" +
                                            "Snack: Cheese cubes\n" +
                                            "Dinner: Dal makhani with naan\n"
                                ),
                                DietPlan("Day 5",
                                    "Breakfast: Oats with milk and chopped nuts\n" +
                                            "Lunch: Chicken kebabs with naan\n" +
                                            "Snack: Fruit smoothie\n" +
                                            "Dinner: Vegetable stew with rice"
                                )
                            )
                        ),
                        DietPlanGroup("18 to 24 Years",
                            listOf(
                                DietPlan("Day 1",
                                    "Breakfast: Vegetable upma with a side of fruit\n" +
                                            "Lunch: Chicken curry with rice\n" +
                                            "Snack: Yogurt with honey and nuts\n" +
                                            "Dinner: Dal fry with roti and a side of vegetables"
                                ),
                                DietPlan("Day 2",
                                    "Breakfast: Paratha with yogurt\n" +
                                            "Lunch: Chicken biryani with raita\n" +
                                            "Snack: Smoothie with nuts\n" +
                                            "Dinner: Paneer curry with roti"
                                ),
                                DietPlan("Day 3",
                                    "Breakfast: Scrambled eggs with toast\n" +
                                            "Lunch: Keema with rice and a side of salad\n" +
                                            "Snack: Fresh fruit slices\n" +
                                            "Dinner: Fish curry with rice and a side of sautéed greens\n"
                                ),
                                DietPlan("Day 4",
                                    "Breakfast: Oatmeal with honey and nuts\n" +
                                            "Lunch: Mixed vegetable pulao with raita\n" +
                                            "Snack: Fresh fruit\n" +
                                            "Dinner: Chicken stew with naan"
                                ),
                                DietPlan("Day 5",
                                    "Breakfast: Masala omelette with whole wheat bread\n" +
                                            "Lunch: Chicken kebabs with a side of rice\n" +
                                            "Snack: Yogurt with honey\n" +
                                            "Dinner: Dal makhani with roti"
                                )
                            )
                        ),
                        DietPlanGroup("24 to 34 Years",
                            listOf(
                                DietPlan("Day 1",
                                    "Breakfast: Vegetable paratha with yogurt\n" +
                                            "Lunch: Chicken curry with rice\n" +
                                            "Snack: Mixed nuts\n" +
                                            "Dinner: Dal with roti and sautéed spinach\n"
                                ),
                                DietPlan("Day 2",
                                    "Breakfast: Smoothie bowl with granola\n" +
                                            "Lunch: Keema with rice\n" +
                                            "Snack: Fruit salad\n" +
                                            "Dinner: Chicken biryani with cucumber raita"
                                ),
                                DietPlan("Day 3",
                                    "Breakfast: Oats with milk and honey\n" +
                                            "Lunch: Vegetable pulao with raita\n" +
                                            "Snack: Cheese sandwich\n" +
                                            "Dinner: Paneer tikka with naan\n"
                                ),
                                DietPlan("Day 4",
                                    "Breakfast: Scrambled eggs with toast\n" +
                                            "Lunch: Chicken kebabs with rice\n" +
                                            "Snack: Yogurt with honey\n" +
                                            "Dinner: Vegetable stew with rice"
                                ),
                                DietPlan("Day 5",
                                    ""
                                )
                            )
                        ),
                        DietPlanGroup("34 to 44 Years",
                            listOf(
                                DietPlan("Day 1",
                                    "Breakfast: Vegetable upma with a glass of milk\n" +
                                            "Lunch: Chicken curry with rice\n" +
                                            "Snack: Fruit salad\n" +
                                            "Dinner: Dal with roti and mixed vegetables"
                                ),
                                DietPlan("Day 2",
                                    "Breakfast: Masala omelette with whole wheat bread\n" +
                                            "Lunch: Chicken pulao with raita\n" +
                                            "Snack: Yogurt with fruit\n" +
                                            "Dinner: Fish curry with rice"
                                ),
                                DietPlan("Day 3",
                                    "Breakfast: Paratha with curd\n" +
                                            "Lunch: Vegetable biryani with salad\n" +
                                            "Snack: Fresh fruit slices\n" +
                                            "Dinner: Paneer curry with roti"
                                ),
                                DietPlan("Day 4",
                                    ""
                                ),
                                DietPlan("Day 5",
                                    ""
                                )
                            )
                        ),
                        DietPlanGroup("44 to 55 Years",
                            listOf(
                                DietPlan("Day 1",
                                    "Breakfast: Oatmeal with nuts and fruit\n" +
                                            "Lunch: Chicken curry with rice\n" +
                                            "Snack: Fruit salad\n" +
                                            "Dinner: Dal with roti and mixed vegetables"
                                ),
                                DietPlan("Day 2",
                                    "Breakfast: Vegetable upma with a glass of milk\n" +
                                            "Lunch: Vegetable pulao with raita\n" +
                                            "Snack: Yogurt with fruit\n" +
                                            "Dinner: Paneer tikka with roti"
                                ),
                                DietPlan("Day 3",
                                    "Breakfast: Boiled eggs with whole wheat toast\n" +
                                            "Lunch: Chicken kebabs with rice\n" +
                                            "Snack: Fruit smoothie\n" +
                                            "Dinner: Dal makhani with naan"
                                ),
                                DietPlan("Day 4",
                                    ""
                                ),
                                DietPlan("Day 5",
                                    ""
                                )
                            )
                        ),
                        DietPlanGroup("55 to 65 Years",
                            listOf(
                                DietPlan("Day 1",
                                    "Breakfast: Oatmeal with chia seeds, nuts, and fresh fruit\n" +
                                            "Lunch: Masoor dal (red lentil curry) with brown rice and a side of steamed vegetables\n" +
                                            "Snack: Fresh fruit slices (apple, orange)\n" +
                                            "Dinner: Grilled fish with mixed vegetable salad and roti"
                                ),
                                DietPlan("Day 2",
                                    "Breakfast: Vegetable poha (flattened rice) with peas and a glass of buttermilk\n" +
                                            "Lunch: Chicken curry (low oil) with quinoa or brown rice\n" +
                                            "Snack: Yogurt with honey\n" +
                                            "Dinner: Vegetable soup with a small portion of rice and grilled paneer"
                                ),
                                DietPlan("Day 3",
                                    "Breakfast: Whole wheat toast with scrambled eggs and a glass of milk\n" +
                                            "Lunch: Dal khichdi (rice and lentils) with cucumber raita\n" +
                                            "Snack: Mixed nuts or fresh fruit salad\n" +
                                            "Dinner: Chicken stew with whole wheat chapati"
                                ),
                                DietPlan("Day 4",
                                    "Breakfast: Multigrain porridge with nuts and seeds\n" +
                                            "Lunch: Chana pulao (chickpea rice) with a side of yogurt\n" +
                                            "Snack: Smoothie with spinach, apple, and yogurt\n" +
                                            "Dinner: Baingan bharta (roasted eggplant) with a side of chapati and salad"
                                ),
                                DietPlan("Day 5",
                                    "Breakfast: Besan chilla (gram flour pancake) with a side of curd\n" +
                                            "Lunch: Palak dal (spinach lentil soup) with brown rice\n" +
                                            "Snack: Dates and almonds\n" +
                                            "Dinner: Grilled chicken with sautéed spinach and roti"
                                ),
                                DietPlan("Day 6",
                                    "Breakfast: Vegetable upma with a glass of milk\n" +
                                            "Lunch: Mutton curry (light) with quinoa or whole wheat roti\n" +
                                            "Snack: Fresh fruit (pear, guava)\n" +
                                            "Dinner: Dal makhani with brown rice and a side of mixed salad\n"
                                ),
                                DietPlan("Day 7",
                                    "Breakfast: Idli with sambar (lightly spiced lentil soup)\n" +
                                            "Lunch: Fish curry with brown rice and steamed vegetables\n" +
                                            "Snack: Yogurt with a handful of nuts\n" +
                                            "Dinner: Moong dal khichdi with a side of cucumber raita"
                                )
                            )
                        ),
                        DietPlanGroup("65 Years and Older",
                            listOf(
                                DietPlan("Day 1",
                                    "Breakfast: Oats with chopped nuts, seeds, and a few berries\n" +
                                            "Lunch: Lentil soup with quinoa and a side of steamed vegetables\n" +
                                            "Snack: Mashed banana with yogurt\n" +
                                            "Dinner: Chicken stew with whole wheat chapati and steamed greens"
                                ),
                                DietPlan("Day 2",
                                    "Breakfast: Soft boiled eggs with whole wheat toast\n" +
                                            "Lunch: Light vegetable biryani with cucumber raita\n" +
                                            "Snack: Fresh fruit bowl (mango, papaya, apple)\n" +
                                            "Dinner: Palak paneer with brown rice and salad"
                                ),
                                DietPlan("Day 3",
                                    "Breakfast: Suji (semolina) upma with peas and carrots\n" +
                                            "Lunch: Moong dal with roti and a side of mixed salad\n" +
                                            "Snack: Dates with a glass of milk\n" +
                                            "Dinner: Grilled fish with quinoa and steamed vegetables"
                                ),
                                DietPlan("Day 4",
                                    "Breakfast: Chia seed pudding with yogurt and honey\n" +
                                            "Lunch: Spinach and chickpea curry with brown rice\n" +
                                            "Snack: Almonds and raisins\n" +
                                            "Dinner: Light mutton curry with whole wheat chapati and steamed vegetables"
                                ),
                                DietPlan("Day 5",
                                    "Breakfast: Masala oats with chopped veggies\n" +
                                            "Lunch: Chicken and vegetable stew with brown rice\n" +
                                            "Snack: Fruit smoothie\n" +
                                            "Dinner: Dal (lentils) with roti and cucumber salad"
                                ),
                                DietPlan("Day 6",
                                    "Breakfast: Multigrain toast with peanut butter and banana slices\n" +
                                            "Lunch: Mixed vegetable khichdi (rice and lentils) with a side of yogurt\n" +
                                            "Snack: Fresh fruit bowl (guava, apple, pear)\n" +
                                            "Dinner: Baked fish with whole wheat chapati and sautéed spinach"
                                ),
                                DietPlan("Day 7",
                                    "Breakfast: Soft idli with coconut chutney\n" +
                                            "Lunch: Chicken pulao with cucumber and tomato salad\n" +
                                            "Snack: Yogurt with honey and almonds\n" +
                                            "Dinner: Grilled paneer with a side of chapati and vegetable soup"
                                )
                            )
                        )
                    )
                    dietPlanAdapter = DietPlanAdapter(dietPlanGroups)
                    recyclerView.adapter = dietPlanAdapter
                }
            }