package com.artilearn.happygolfgo.ui.home.lecture

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.LectureComment
import com.artilearn.happygolfgo.databinding.FragmentLectureCommentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.internal.ViewUtils.hideKeyboard
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel


class CommentListDialog(context: Context, lectNo : String) : BottomSheetDialogFragment() {
    private val viewModel: CommentListViewModel by viewModel()
    override fun getTheme() = R.style.ExamBottomSheetDialog
    private lateinit var binding: FragmentLectureCommentBinding
    private var lectNos: String = lectNo
    private lateinit var  adapter : CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_lecture_comment,
            container,
            false
        )


        return binding.root
    }
    companion object {
        const val TAG = "BottomSheetFragment"
    }
    /** onCreateDialog
     *  목적 : 나만의 커스텀 다이얼로그를 만들기 위함.
     *  How? : dialog.setOnShowListener{}의 인자값인 'DialogInterface'를 크기 조절하고 이벤트를 달 수 있음(닫힘, 취소 등)
     * */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            // 다이얼로그 크기 설정 (인자값 : DialogInterface)
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    /** setupRatio()
     * bottomSheet
     *      - 전달받은 DialogInterface를 통해 View를 참조하도록 한다.
     *      - 이때 id 값에는 내가 만든 View가 들어가는게 아님을 주의하자.
     * layoutParams
     *      - View를 감싸고 있는 Parents에게 어떻게 레이아웃 할 것인지 설정하는데 사용함.
     * behavior
     *      - 생성한 View를 통해 BottomSheetBehavior를 생성한다.
     *      - 역할 : BottomSheetBehavior로 상태값을 '확장형'으로 설정해준다.
     * */
    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from<View>(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 95 / 100
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.getComment(lectNos)

        with(viewModel) {
            commentData.observe(this@CommentListDialog, Observer {
                Log.d("myLog", "comment " + it!!.size)
                setComment(it)
            })
            reg_result.observe(this@CommentListDialog, Observer {
                Log.d("myLog", "comment  reg" + it!!.serverStatus)
                if(it!!.serverStatus=="2"){
                    binding.etComment.setText("")
                    val imm =   binding.etComment.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(  binding.etComment.windowToken, 0)

                    viewModel.getComment(lectNos)
                }

            })

        }

        binding.etComment.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val map: HashMap<String, String> = HashMap()
                    map["lectNo"] = lectNos
                    map["cmmtText"] = binding.etComment.text.toString()
                    map["upprCmmtNo"] = ""

                    viewModel.regComment(map)

                  //  commentList.add(new LectureComment)

                    return true
                }
                return false
            }
        })

    }


    private lateinit var commentList : List<LectureComment>


    private fun setComment(lectureComment : List<LectureComment>?) {
        if (lectureComment != null) {
            commentList = lectureComment
        }
        adapter = commentList?.let { CommentAdapter(it) }!!
        val layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.rvComment.layoutManager = layoutManager


        with(binding) {
            binding.rvComment.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            // swipeToDelete(recyclerview)
            binding.rvComment.adapter = adapter
        }
    }



//    override fun onStart() {
//        super.onStart()
//        val bottomSheet =
//            (dialog as BottomSheetDialog?)!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//        if (bottomSheet != null) {
//            // set the bottom sheet state to Expanded to expand to the entire window
//            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
//            // disable the STATE_HALF_EXPANDED state
//            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
//            // set the bottom sheet height to match_parent
//            val layoutParams = bottomSheet.layoutParams
//            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
//            bottomSheet.layoutParams = layoutParams
//        }
//
//        // Make the dialog cover the status bar
//        dialog!!.window!!.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
//    }


}