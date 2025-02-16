package community.io.com.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import community.io.com.R
import community.io.com.data.model.DataModel
import community.io.com.databinding.ActivityHomeBinding
import community.io.com.singleton.App
import community.io.com.ui.adapter.DrawerMenuAdapter
import community.io.com.ui.adapter.HomeMenuAdapter
import community.io.com.utils.Utils
import community.io.com.viewmodel.ForgotPasswordViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {


    lateinit var loginViewModel: ForgotPasswordViewModel


    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_home)
        loginViewModel = ForgotPasswordViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        bindViewModel()
    }


    fun bindViewModel() {

        lifecycleScope.launch {
            loginViewModel.userName.value =  App.userPreferences.userName.first()
            loginViewModel.userEmail.value = App.userPreferences.userEmail.first()

        }


        binding.llLogout.setOnClickListener {

            lifecycleScope.launch {
                App.userPreferences.saveToken("")
                App.userPreferences.saveUserEmail("")
                App.userPreferences.saveUserId("")
                App.userPreferences.saveCommunityId("")
                App.userPreferences.saveUsername("")
                App.userPreferences.saveUserMobile("")
                App.userPreferences.saveUserAddress1("")

                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        //        ivToolbarBack.background = resources.getDrawable(R.drawable.ic_baseline_dehaze_24)

        binding.ivToolbarBack.setOnClickListener {

            if ( binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

         var dataList = mutableListOf<DataModel>()

        dataList.add(DataModel("Blogs", R.drawable.baseline_wifi_24))
        dataList.add(DataModel("Hanini Business classifieds", R.drawable.sharp_business_center_24))
        dataList.add(DataModel("View / Create Events", R.drawable.baseline_event_24))
        dataList.add(DataModel("Hanini directory", R.drawable.baseline_group_24))
        dataList.add(DataModel("Information", R.drawable.announcement))
        dataList.add(DataModel("Admin Contact", R.drawable.sharp_account_circle_24))

    var adapter =    HomeMenuAdapter(this@HomeActivity,dataList)

        binding.rcvCommunityMenu.layoutManager = GridLayoutManager(this@HomeActivity,2)
        binding.rcvCommunityMenu.adapter  = adapter
        binding.rcvCommunityMenu.setHasFixedSize(true)
        adapter.notifyDataSetChanged()

        adapter.onItemClick = {

            when (it) {
                0 -> {

                    val intent = Intent(this@HomeActivity, BlogsListActivity::class.java)
                    startActivity(intent)

                }
                1 -> {
                    val intent = Intent(this@HomeActivity, AdvtListActivity::class.java)
                    startActivity(intent)



                }
                2 -> {

                    val intent = Intent(this@HomeActivity, EventActivity::class.java)
                    startActivity(intent)



                }
                3 -> {
                    val intent = Intent(this@HomeActivity, HaninaDirectoryActivity::class.java)
                    startActivity(intent)
                }
                4 -> {
                    val intent = Intent(this@HomeActivity, NotificationActivity::class.java)
                    startActivity(intent)
                }
                5 -> {
                    val intent = Intent(this@HomeActivity, AdminContactActivity::class.java)
                    startActivity(intent)
                }



//                    0 -> myCommunityViewModel.onEventReceived(MyCommunityViewModel.Event.UserLogout)
//                    1 -> {
//
//                        gotoNewActivity(createIntent<HomeActivity, JoinGroupLinkActivity>())
////                        Log.e("myvaluesGoon", "myvaluesGoon")
//                    }
            }
        }


        val drawerItems = resources.obtainTypedArray(R.array.drawer_menu_item)

        var drawerAdapter = DrawerMenuAdapter(this@HomeActivity, drawerItems)
        binding.rcvDrawer.layoutManager = LinearLayoutManager(this@HomeActivity)

        binding.rcvDrawer.adapter = drawerAdapter
        drawerAdapter?.onItemClick = {
//            Log.e("Myvalueselected", "===>$it")
            binding.drawerLayout.tag = it
            binding.drawerLayout.closeDrawer(GravityCompat.START)

        }

        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerClosed(drawerView: View) {
                Log.e("myvaluesGoon", "myvaluesGoon")
                when (binding.drawerLayout.tag) {
                    0 -> {

                        val intent = Intent(this@HomeActivity, EventActivity::class.java)
                        startActivity(intent)

                    }
                    1 -> {

                        val intent = Intent(this@HomeActivity, HaninaDirectoryActivity::class.java)
                        startActivity(intent)
                    }
                    2 -> {
                        val intent = Intent(this@HomeActivity, NotificationActivity::class.java)
                        startActivity(intent)

                    }
                    3 -> {

                        val intent = Intent(this@HomeActivity, AdvtListActivity::class.java)
                        startActivity(intent)

                    }
                    4 -> {
                        val intent = Intent(this@HomeActivity, BlogsListActivity::class.java)
                        startActivity(intent)
                    }
                    5 -> {
                        val intent = Intent(this@HomeActivity, AdminContactActivity::class.java)
                        startActivity(intent)
                    }
                    6 -> {
                        val intent = Intent(this@HomeActivity, EditProfileActivity::class.java)
                        startActivity(intent)
                    }
                    7 -> {

                        val intent = Intent(this@HomeActivity, ChangePasswordActivity::class.java)
                        startActivity(intent)
                    }

                    8 -> {
                        val intent = Intent(this@HomeActivity, AboutUsActivity::class.java)
                        startActivity(intent)
                    }



//                    0 -> myCommunityViewModel.onEventReceived(MyCommunityViewModel.Event.UserLogout)
//                    1 -> {
//
//                        gotoNewActivity(createIntent<HomeActivity, JoinGroupLinkActivity>())
////                        Log.e("myvaluesGoon", "myvaluesGoon")
//                    }
                }
//                drawerAdapter?.notifyDataSetChanged()
            }

            override fun onDrawerOpened(drawerView: View) {

            }
        })


    }
}