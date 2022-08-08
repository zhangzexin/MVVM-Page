package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentFirstBinding
import zzx.mvvm.basepage.base.IStatusPage
import zzx.mvvm.basepage.fragment.BaseBVMFragment

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : BaseBVMFragment<MainViewModel,FragmentFirstBinding>() {

//    private var _binding: FragmentFirstBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!

//    override fun onCreateView(
//            inflater: LayoutInflater, container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//
//        _binding = FragmentFirstBinding.inflate(inflater, container, false)
//        return binding.root
//
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.buttonFirst?.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override var mStatusView: IStatusPage? = null

    override fun initView() {
    }

    override fun loadData() {
    }

    override var layoutId: Int = R.layout.fragment_first
}