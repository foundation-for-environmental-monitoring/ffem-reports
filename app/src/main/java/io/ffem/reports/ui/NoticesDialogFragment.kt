/*
 * Copyright (C) ffem (Foundation for Environmental Monitoring)
 *
 * This file is part of ffem reports
 *
 * ffem Reports is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * ffem Reports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ffem Reports. If not, see <http://www.gnu.org/licenses/>.
 */
package io.ffem.reports.ui

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import io.ffem.reports.R
import io.ffem.reports.databinding.FragmentNoticesDialogBinding

class NoticesDialogFragment : DialogFragment() {
    private var _binding: FragmentNoticesDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoticesDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        makeEverythingClickable(binding.aboutContainer)

        binding.homeButton.setOnClickListener { dismiss() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun makeEverythingClickable(vg: ViewGroup) {
        for (i in 0 until vg.childCount) {
            if (vg.getChildAt(i) is ViewGroup) {
                makeEverythingClickable(vg.getChildAt(i) as ViewGroup)
            } else if (vg.getChildAt(i) is TextView) {
                val tv = vg.getChildAt(i) as TextView
                if (tv.isClickable) {
                    tv.setLinkTextColor(
                            ContextCompat.getColor(
                                    requireContext(),
                                    R.color.link_text
                            )
                    )
                    tv.movementMethod = LinkMovementMethod.getInstance()
                }
            }
        }
    }

    override fun onResume() {
        if (dialog!!.window != null) {
            val params = dialog!!.window!!.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.MATCH_PARENT
            dialog!!.window!!.attributes = params
        }

        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(): NoticesDialogFragment {
            return NoticesDialogFragment()
        }
    }
}