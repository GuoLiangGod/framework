package com.guoliang.framekt.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/6/28 9:40
 */
abstract class BaseVMActivity<VM : ViewModel,DB : ViewDataBinding>(
    useDataBinding: Boolean = true,
    hideStatusBar: Boolean = true
) : AppCompatActivity() {

    private val _useBinding = useDataBinding
    private val _hideStatusBar = hideStatusBar
    lateinit var mBinding: DB
    lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (initVM!=null) {
            mViewModel = initVM!!
        }

        if (_useBinding) {
            mBinding = DataBindingUtil.setContentView(this, layoutId)
            mBinding.lifecycleOwner = this
        } else {
            setContentView(layoutId)
        }
        if (_hideStatusBar) {
            // 5.0以上系统状态栏透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                //白色SYSTEM_UI_FLAG_LAYOUT_STABLE、深色SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
        startObserve()
        initView(savedInstanceState)
        initData(savedInstanceState)
        ActivityManager.instance?.addActivity(this)
    }
    override fun finish() {
        super.finish()
        ActivityManager.instance?.removeActivity(this)
    }
    abstract val layoutId: Int
    abstract val initVM: VM?
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData(savedInstanceState: Bundle?)
    abstract fun startObserve()

}