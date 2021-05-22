package com.omnyom.yumyum.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBindingBottomSheetFragment<T : ViewDataBinding>(
        @LayoutRes private val contentLayoutId: Int
): BottomSheetDialogFragment() {

    private var _binder: ViewDataBinding? = null
    protected val binder: ViewDataBinding
        get() = _binder!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binder = DataBindingUtil.inflate(inflater, contentLayoutId, container, false)
        extraSetupBinding()
        setup()
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        onSubScribe()
    }

    protected fun extraSetupBinding(){
        //자식 요소에서 구체화 한다.
    }
    protected fun setup(){
        //자식 요소에서 구체화 한다.
    }
    protected fun setupViews(){
        //자식 요소에서 구체화 한다.
    }
    protected fun onSubScribe(){
        //자식 요소에서 구체화 한다.
    }

    override fun onDestroyView() {
        release()
        _binder = null
        super.onDestroyView()
    }

    protected abstract fun release()
}