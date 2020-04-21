package com.qh.wanandroid.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.devlibrary.ext.getThemeColor
import com.example.devlibrary.helper.LiveEventBusHelper
import com.example.devlibrary.mvp.BaseMvpActivity
import com.example.devlibrary.utils.SettingUtil
import com.google.android.material.navigation.NavigationView
import com.qh.wanandroid.R
import com.qh.wanandroid.adapter.ViewPagerAdapter
import com.qh.wanandroid.bean.UserInfoEntity
import com.qh.wanandroid.constant.Const
import com.qh.wanandroid.databinding.ActivityMainBinding
import com.qh.wanandroid.ext.startActivity
import com.qh.wanandroid.ui.collect.CollectActivity
import com.qh.wanandroid.ui.girl.GirlActivity
import com.qh.wanandroid.ui.home.HomeFragment
import com.qh.wanandroid.ui.integral.IntegralActivity
import com.qh.wanandroid.ui.login.LoginActivity
import com.qh.wanandroid.ui.navigation.NavigationFragment
import com.qh.wanandroid.ui.question.QuestionActivity
import com.qh.wanandroid.ui.search.SearchActivity
import com.qh.wanandroid.ui.setting.SettingActivity
import com.qh.wanandroid.ui.share.ShareListActivity
import com.qh.wanandroid.ui.system.SystemListFragment
import com.qh.wanandroid.ui.tab.TabFragment
import com.tencent.mmkv.MMKV
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import java.util.*

class MainActivity :
    BaseMvpActivity<MainContract.View, MainContract.Presenter, ActivityMainBinding>(),
    MainContract.View {

    private val mmkv by lazy { MMKV.defaultMMKV() }
    private val isLogin by lazy {
        mmkv.decodeBool(com.example.common.constant.Const.IS_LOGIN, false)
    }
    private val fragments = ArrayList<Fragment>()
    private lateinit var navHeaderView: View
    private lateinit var tvUserId: TextView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserGrade: TextView
    private lateinit var tvUserRank: TextView

    override fun attachLayoutRes(): Int = R.layout.activity_main

    override fun initData() {
        receiveNotice()
        initFragments()
    }

    private fun initFragments() {
        //首页
        fragments.add(HomeFragment())
        //体系
        fragments.add(SystemListFragment())
        //公众号
        val account = TabFragment()
        val accountBundle = Bundle()
        accountBundle.putInt(Const.TYPE, Const.ACCOUNT_TYPE)
        account.arguments = accountBundle
        fragments.add(account)
        //导航
        fragments.add(NavigationFragment())
        //项目
        val project = TabFragment()
        val proBundle = Bundle()
        proBundle.putInt(Const.TYPE, Const.PROJECT_TYPE)
        project.arguments = proBundle
        fragments.add(project)
    }

    override fun initView() {
        super.initView()
        initDrawerLayout()
        initNavView()
        setThemeColor()
        initViewPager()
        initBottom()
    }

    private fun initDrawerLayout() {
        mBinding.drawerLayout.run {
            val toggle = ActionBarDrawerToggle(
                this@MainActivity,
                this,
                findViewById(R.id.toolbar)
                , R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            addDrawerListener(toggle)
            toggle.syncState()
        }
    }

    private fun initViewPager() {
        mBinding.viewPager.isUserInputEnabled = false
        mBinding.viewPager.adapter = ViewPagerAdapter(this, fragments)
        mBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mBinding.btmNavigation.selectedItemId = when (position) {
                    0 -> R.id.menu_home
                    1 -> R.id.menu_system
                    2 -> R.id.menu_official_account
                    3 -> R.id.menu_navigation
                    4 -> R.id.menu_project
                    else -> 0
                }
                setTitle(
                    when (position) {
                        0 -> R.string.tab_1
                        1 -> R.string.tab_2
                        2 -> R.string.tab_3
                        3 -> R.string.tab_4
                        4 -> R.string.tab_5
                        else -> 0
                    }
                )
            }
        })
    }

    override fun loadData() {
        mPresenter?.getUserInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> {
                Intent(this, SearchActivity::class.java).run {
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * init NavigationView
     */
    private fun initNavView() {
        mBinding.navView.run {
            getHeaderView(0).run {
                navHeaderView = this
                tvUserId = findViewById(R.id.tv_user_id)
                tvUserName = findViewById(R.id.tv_username)
                tvUserGrade = findViewById(R.id.tv_user_grade)
                tvUserRank = findViewById(R.id.tv_user_rank)
            }
            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
            menu.findItem(R.id.nav_logout).isVisible = isLogin
        }
        tvUserName.onClick { if (isLogin.not()) startActivity<LoginActivity>() }
    }

    /**
     * NavigationView 监听
     */
    private val onDrawerNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_score -> {
                    startActivity(IntegralActivity::class.java, true)
                }
                R.id.nav_collect -> {
                    startActivity(CollectActivity::class.java, true)
                }
                R.id.nav_girl -> {
                    startActivity(GirlActivity::class.java)
                }
                R.id.nav_question -> {
                    startActivity(QuestionActivity::class.java)
                }
                R.id.nav_setting -> {
                    startActivity(SettingActivity::class.java)
                }
                R.id.nav_about_us -> {

                }
                R.id.nav_logout -> {
                    logout()
                }
                R.id.nav_night_mode -> {
                    switchNightMode()
                }
                R.id.nav_todo -> {
//                    startActivity(TodoActivity::class.java, true)
                }
                R.id.nav_square -> {
                    startActivity(ShareListActivity::class.java)
                }
            }
            // drawer_layout.closeDrawer(GravityCompat.START)
            true
        }

    private fun logout() {
        mPresenter?.logout()
    }

    /**
     * 切换日夜间模式
     */
    private fun switchNightMode() {
        if (SettingUtil.getIsNightMode()) {
            SettingUtil.setIsNightMode(false)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            SettingUtil.setIsNightMode(true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
        recreate()
    }

    private fun initBottom() {
        mBinding.btmNavigation.run {
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_home -> mBinding.viewPager.currentItem = 0
                    R.id.menu_system -> mBinding.viewPager.currentItem = 1
                    R.id.menu_official_account -> mBinding.viewPager.currentItem = 2
                    R.id.menu_navigation -> mBinding.viewPager.currentItem = 3
                    R.id.menu_project -> mBinding.viewPager.currentItem = 4
                }
                // 这里注意返回true,否则点击失效
                true
            }
        }
    }

    override fun createPresenter(): MainContract.Presenter = MainPresenter()

    override fun showLogoutSuccess(success: Boolean) {
        mmkv.encode(com.example.common.constant.Const.IS_LOGIN, false)
        mmkv.removeValueForKey(com.example.common.constant.Const.USER_GSON)
        LiveEventBusHelper.post(com.example.common.constant.Const.LOGOUT_SUCCESS, true)
        finish()
    }

    override fun showUserInfo(bean: UserInfoEntity) {
        tvUserId.text = bean.userId.toString()
        tvUserName.text = bean.username.toString()
        tvUserGrade.text = (bean.coinCount / 100 + 1).toString()
        tvUserRank.text = bean.rank.toString()
    }

    private fun receiveNotice() {
        LiveEventBusHelper.observe(com.example.common.constant.Const.THEME_COLOR,
            Int::class.java, this, androidx.lifecycle.Observer<Int> {
                setThemeColor()
            })
        LiveEventBusHelper.observe(com.example.common.constant.Const.LOGIN_SUCCESS,
            Boolean::class.java, this, androidx.lifecycle.Observer<Boolean> {
                mPresenter?.getUserInfo()
            })
    }

    private fun setThemeColor() {
        navHeaderView.backgroundColor = getThemeColor(this)
    }
}