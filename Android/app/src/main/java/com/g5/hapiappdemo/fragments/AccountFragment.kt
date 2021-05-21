package com.g5.hapiappdemo.fragments

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.g5.hapiappdemo.PreferenceConstants
import com.g5.hapiappdemo.R
import com.g5.hapiappdemo.api.ApiClient
import com.g5.hapiappdemo.auth.set
import com.g5.hapiappdemo.databinding.FragmentAccountBinding
import com.g5.hapiappdemo.extensions.PreferenceHelper
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


class AccountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding
    var URL = "https://notevn.com/file/vi_internal"
    private var disposable: Disposable? = null
    private var pd: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account, container, false
        )
        return binding.root
    }

    private fun clickpic() {
        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(i, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && null != data) {
            val selectedImage: Uri? = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor =
                requireContext().contentResolver?.query(
                    selectedImage!!,
                    filePathColumn, null, null, null
                )!!
            cursor!!.moveToFirst()
            val columnIndex: Int = cursor!!.getColumnIndex(filePathColumn[0])
            val picturePath: String = cursor!!.getString(columnIndex)
            cursor!!.close()

            this.upload(picturePath)
        }
    }

    private fun upload(picturePath: String) {
        pd!!.setCancelable(false)
        pd!!.setMessage(resources.getString(R.string.image_uploading_txt))
        pd!!.show()

        val bm = BitmapFactory.decodeFile(picturePath)
        val bao = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao)
        val ba: ByteArray = bao.toByteArray()
        val base64 = Base64.encodeToString(ba, Base64.DEFAULT)

        if (ApiClient.getInstance(requireContext()).isReady()) {
            disposable = ApiClient.getInstance(requireActivity()).uploadAvatar(base64)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        val bitmap = BitmapFactory.decodeFile(picturePath)
                        val prefs = PreferenceHelper.securePrefs(requireContext())

                        binding.profileImage.setImageBitmap(bitmap)
                        prefs[PreferenceConstants.avatar] = "https://cdn.notevn.com/${result.file_name}${result.type}"
                        pd!!.hide()
                        pd!!.dismiss()
                    },
                    { _ ->
                        pd!!.hide();
                        pd!!.dismiss();
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.avatar_upload_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
        }
    }

    private fun initStudentData() {
        val uData = ApiClient.getInstance(requireActivity().applicationContext)

        binding.studentName.text = uData.getStudentName()
        binding.studentId.text = uData.getStudentID()
        binding.studentEmail.text = uData.getStudentEmail()
        binding.studentSdt1.text = uData.getStudentP1()
        binding.studentSdt2.text = uData.getStudentP2()
        Picasso.with(requireContext()).load(uData.getAvatar()).into(binding.profileImage)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pd = ProgressDialog(requireContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)

        this.initStudentData()
        binding.profileImage.setOnClickListener {
            clickpic()
        }
    }

    companion object {
        fun newInstance(): AccountFragment {
            return AccountFragment()
        }
    }
}
