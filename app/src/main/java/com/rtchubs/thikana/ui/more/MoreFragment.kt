package com.rtchubs.thikana.ui.more

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rtchubs.thikana.BR
import com.rtchubs.thikana.BuildConfig
import com.rtchubs.thikana.R
import com.rtchubs.thikana.databinding.MoreFragmentBinding
import com.rtchubs.thikana.ui.LoginActivity
import com.rtchubs.thikana.ui.LogoutHandlerCallback
import com.rtchubs.thikana.ui.NavDrawerHandlerCallback
import com.rtchubs.thikana.ui.common.BaseFragment
import com.rtchubs.thikana.util.showSuccessToast

class MoreFragment : BaseFragment<MoreFragmentBinding, MoreViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_more

    override val viewModel: MoreViewModel by viewModels { viewModelFactory }

    private var listener: LogoutHandlerCallback? = null

    private var drawerListener: NavDrawerHandlerCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LogoutHandlerCallback) {
            listener = context
        } else {
            throw RuntimeException("$context must implement LoginHandlerCallback")
        }

        if (context is NavDrawerHandlerCallback) {
            drawerListener = context
        } else {
            throw RuntimeException("$context must implement LoginHandlerCallback")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        drawerListener = null
    }

    override fun onResume() {
        super.onResume()
        checkIsLoggedIn()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIsLoggedIn()

        viewDataBinding.version.text = "Version ${BuildConfig.VERSION_NAME}"

        viewDataBinding.btnSignInSignOut.setOnClickListener {
            if (preferencesHelper.isLoggedIn) {
                showSuccessToast(requireContext(), "Signed out successfully!")
                preferencesHelper.isLoggedIn = false
                checkIsLoggedIn()
            } else {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                //finish()
            }
//            SplashFragment.fromLogout = true

//            listener?.onLoggedOut()
        }

        viewDataBinding.appLogo.setOnClickListener {
            drawerListener?.toggleNavDrawer()
        }

        viewDataBinding.cartMenu.setOnClickListener {
            navController.navigate(MoreFragmentDirections.actionMoreFragmentToCartNavGraph())
        }

        viewModel.cartItemCount.observe(viewLifecycleOwner, Observer {
            it?.let { value ->
                if (value < 1) {
                    viewDataBinding.badge.visibility = View.INVISIBLE
                    return@Observer
                } else {
                    viewDataBinding.badge.visibility = View.VISIBLE
                    viewDataBinding.badge.text = value.toString()
                }
            }
        })

        viewDataBinding.menuProfiles.setOnClickListener {
            navigateTo(MoreFragmentDirections.actionMoreFragmentToProfilesFragment())
        }

        viewDataBinding.menuTransactions.setOnClickListener {
            navigateTo(MoreFragmentDirections.actionMoreFragmentToTransactionsFragment())
        }

        viewDataBinding.menuSettings.setOnClickListener {
            navigateTo(MoreFragmentDirections.actionMoreFragmentToSettingsFragment())
        }
    }

    private fun checkIsLoggedIn() {
        if (preferencesHelper.isLoggedIn) {
            viewDataBinding.btnSignInSignOut.text = "Sign Out"
        } else {
            viewDataBinding.btnSignInSignOut.text = "Sign In"
        }
    }

}