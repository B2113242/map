package com.example.mymap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymap.databinding.ActivityMainBinding
import com.example.mymap.models.Place
import com.example.mymap.models.UserMap


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var userMaps: MutableList<UserMap>
    lateinit var mapAdapter: MapsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userMaps = generateSimpleData().toMutableList()
        binding.rvMaps.layoutManager = LinearLayoutManager(this)
        mapAdapter = MapsAdapter(this, userMaps, object : MapsAdapter.OnClickListener {
            override fun onItemClick(position: Int) {
                Log.i(TAG, "onItemClick $position")
                val intent = Intent(this@MainActivity, DisplayMapActivity::class.java)
                intent.putExtra(Utils.EXTRA_USER_MAP, userMaps[position])
                startActivity(intent)
            }
        }
        )
        binding.rvMaps.adapter = mapAdapter

        binding.floatingActionButton.setOnClickListener {
            val mapFormView = LayoutInflater.from(this).inflate(R.layout.dialog_create_map, null)
            androidx.appcompat.app.AlertDialog.Builder(this).setTitle("Map Title")
                .setView(mapFormView).setNegativeButton("Cancel", null)
                .setPositiveButton("OK") { _, _ ->
                    val _title =
                        mapFormView.findViewById<EditText>(R.id.et_title_map).text.toString()
                    if (_title.trim().isEmpty()) {
                        Toast.makeText(this, "Fill out title", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    val intent = Intent(this@MainActivity, CreateMapActivity::class.java)
                    intent.putExtra(Utils.EXTRA_MAP_TITLE, _title)
                    getResult.launch(intent)
                }.show()
        }
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val userMap = it.data?.getSerializableExtra(Utils.EXTRA_USER_MAP) as UserMap
                userMaps.add(userMap)
                mapAdapter.notifyItemInserted(userMaps.size - 1)
                Log.i(TAG, userMap.title)
            }
        }

    private fun generateSimpleData(): List<UserMap> {
        /* return listOf(
            UserMap("Đại học Cần Thơ",listOf(
                    Place("Trường CNTT&TT", "thuộc ĐH Cần Thơ", 10.0308541, 105.768986),
                    Place("Trường Nông NGhiệp", "thuộc Đh Cần Thơ",10.0302655,105.7679642),
                    Place("Hội trường rùa", "Nơi tổ chức các hoạt động...",10.0293402,105.7690273)
                )
            ),
            UserMap("Ẩm thực",listOf(
                    Place("The 80s icafe", "Đường Mạc Thiên Tích", 10.0286827,105.7732964),
                    Place("Trà sữa Tigon", "Đường Mạc Thiên Tích",10.0278105,105.7718373),
                    Place("Cafe Thủy Mộc", "Đường 3/2",10.0273775,105.7704913)
                )
            )
        ) */

        val simpleData = mutableListOf<UserMap>()

        val university = mutableListOf<Place>()
        university.add(Place("Trường CNTT&TT", "thuộc ĐH Cần Thơ", 10.0308541, 105.768986))
        university.add(Place("Trường Nông NGhiệp", "thuộc Đh Cần Thơ", 10.0302655, 105.7679642))
        university.add(Place("Hội trường rùa", "Nơi tổ chức các hoạt động...", 10.0293402, 105.7690273))
        simpleData.add(UserMap("Dai Hoc Can Tho", university))

        val drink = mutableListOf<Place>()
        drink.add(Place("The 80s icafe", "Đường Mạc Thiên Tích", 10.0286827, 105.7732964))
        drink.add(Place("Trà sữa Tigon", "Đường Mạc Thiên Tích", 10.0278105, 105.7718373))
        drink.add(Place("Cafe Thủy Mộc", "Đường 3/2", 10.0273775, 105.7704913))
        simpleData.add(UserMap("Coffee", drink))

        val foody = mutableListOf<Place>()
        foody.add(Place("Cơm gà Lê Trang","Đường Nguyễn Văn Linh",10.025874,105.758767))
        foody.add(Place("Mì cay Seoul Cần Thơ","Đường 3/2",10.022933,105.765786))
        simpleData.add((UserMap("Foody",foody)))

        return simpleData
    }

}