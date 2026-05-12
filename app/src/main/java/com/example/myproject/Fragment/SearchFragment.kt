
    //SEARCH PAGE(ICON)

    package com.example.myproject.Fragment

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.appcompat.app.AppCompatActivity
    import androidx.fragment.app.Fragment
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.myproject.R
    import com.example.myproject.Service2Data
    import com.example.myproject.adapter.MenuAdapter

    class SearchFragment : Fragment() {

        private lateinit var recyclerView: RecyclerView
        private lateinit var menuAdapter: MenuAdapter
        private val menuList = ArrayList<Service2Data>()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {

            val view = inflater.inflate(R.layout.activity_fragment_item, container, false)

            recyclerView = view.findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            addData()

            menuAdapter = MenuAdapter(menuList) { item ->

                val bookingFragment = BookingFragment().apply {
                    arguments = Bundle().apply {
                        putString("source", "POOJA_ITEM")
                        putString("itemName", item.title)
                        putString("itemDesc", item.description)
                        putString("itemPrice", item.price)
                        putInt("itemImage", item.imageRes)
                    }
                }

                // 🔥 VERY IMPORTANT LINE
                (requireActivity() as AppCompatActivity)
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container1, bookingFragment)
                    .addToBackStack(null)
                    .commit()
            }

            recyclerView.adapter = menuAdapter
            return view
        }

        private fun addData() {
            menuList.clear()

            // 🔥 Kapoor / Dhoop
            menuList.add(Service2Data("Kapoor", R.drawable.kapoorimg, "Small Pack", "₹100"))
            menuList.add(Service2Data("Kapoor", R.drawable.kapoorimg, "Medium Pack", "₹200"))
            menuList.add(Service2Data("Kapoor", R.drawable.kapoorimg, "Large Pack", "₹300"))

            // 🔥 Thali
            menuList.add(Service2Data("Thali", R.drawable.steelthali, "Steel Thali", "₹400"))
            menuList.add(Service2Data("Thali", R.drawable.brassthali, "Brass Thali", "₹500"))
            menuList.add(Service2Data("Thali", R.drawable.decorative, "Decorative Thali", "₹650"))

            // 🔥 Incense Sticks
            menuList.add(Service2Data("Incense Sticks", R.drawable.incensestick, "Rose Fragrance", "₹40"))
            menuList.add(Service2Data("Incense Sticks", R.drawable.incensestick, "Sandalwood", "₹50"))
            menuList.add(Service2Data("Incense Sticks", R.drawable.incensestick, "Mogra", "₹45"))

            // 🔥 Pooja Samagri Kits
            menuList.add(Service2Data("Pooja Samagri Kit", R.drawable.kit, "Basic Kit", "₹700"))
            menuList.add(Service2Data("Pooja Samagri Kit", R.drawable.prekit, "Premium Kit", "₹1200"))

            // 🔥 Flowers & Offerings
            menuList.add(Service2Data("Flowers", R.drawable.rose, "Rose Flowers", "₹100"))
            menuList.add(Service2Data("Flowers", R.drawable.marigold, "Marigold Mala", "₹150"))

            // 🔥 Misc Items
            menuList.add(Service2Data("Camphor Lamp", R.drawable.lamp, "Brass Lamp", "₹900"))
            menuList.add(Service2Data("Kalash", R.drawable.kalash, "Copper Kalash", "₹750"))

            // Optional: sort by name
            menuList.sortBy { it.title }
        }

    }

