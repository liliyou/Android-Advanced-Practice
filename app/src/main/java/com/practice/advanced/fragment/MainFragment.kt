package com.practice.advanced.fragment


/**
 * Created by xuyating on 2017/9/30.
 */

class MainFragment : BaseFragment() {
    private lateinit var unbinder: butterknife.Unbinder

    override fun onCreateView(
            inflater: android.view.LayoutInflater?, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View? {
        var layout = inflater!!.inflate(com.practice.advanced.R.layout.fragment_main, container, false)
        unbinder = butterknife.ButterKnife.bind(this, layout as android.view.View)
        return layout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

    @butterknife.OnClick(com.practice.advanced.R.id.btn_demo_schedulers)
    fun demoConcurrencyWithSchedulers() {
        android.util.Log.e("click", "demoConcurrencyWithSchedulers")
    }

}