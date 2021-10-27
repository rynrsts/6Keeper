package com.example.sixkeeper

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs

class WebViewFragment : Fragment() {
    private val args: WebViewFragmentArgs by navArgs()

    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var wvInAppWebView: WebView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appCompatActivity = activity as AppCompatActivity
        val ivInAppWebViewBack: ImageView = appCompatActivity.findViewById(R.id.ivInAppWebViewBack)
        wvInAppWebView = appCompatActivity.findViewById(R.id.wvInAppWebView)

        ivInAppWebViewBack.apply {
            postDelayed(
                    {
                        ivInAppWebViewBack.apply {
                            setBackgroundResource(R.color.blue)
                            setImageResource(R.drawable.ic_arrow_back_white)
                        }
                    }, 400
            )
        }

        ivInAppWebViewBack.setOnClickListener {
            appCompatActivity.onBackPressed()
        }

        wvInAppWebView.settings.loadsImagesAutomatically = true
        wvInAppWebView.settings.javaScriptEnabled = true
        wvInAppWebView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        wvInAppWebView.loadUrl(args.websiteURL)

        wvInAppWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {                                                    // Override back press
                wvInAppWebView.clearHistory()
                wvInAppWebView.clearFormData()
                wvInAppWebView.clearCache(true)

                val controller = Navigation.findNavController(view!!)
                controller.popBackStack(R.id.webViewFragment, true)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}