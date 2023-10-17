package com.example.mexpense.activity.main.transaction

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mexpense.R
import com.example.mexpense.base.BaseActivity
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.ActivityAddTransactionBinding
import com.example.mexpense.entity.Transaction
import com.example.mexpense.exts.gone
import com.example.mexpense.exts.setOnDelayClickListener
import com.example.mexpense.exts.visible
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddTransactionActivity : BaseActivity() {
    private lateinit var binding: ActivityAddTransactionBinding
    private lateinit var databaseHelper: SqlService
    var selectCate = ""

    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = SqlService.getInstance(this)
        binding.tvClose.setOnClickListener { finish() }

        binding.btnSave.setOnDelayClickListener {
            val iname = selectCate
            val icategory = selectCate
            val iamount = binding.edtAmount.text.toString().trim()
            val inote = binding.edtNote.text.toString().trim()
            val idate = binding.tvDate.text.toString().trim()
            val ireturnDate = binding.tvReturnDate.text.toString().trim()
            val idestination = binding.edtDestination.text.toString().trim()
            val itransportation = binding.edtTransportation.text.toString().trim()
            val istatus = true
            val iwallet = 1
            val iuserId = SharePreUtil.GetShareInt(this, Constants.KEY_USER_ID);
            val transaction = Transaction()

            if (iamount.isNotEmpty() && idate.isNotEmpty()) {
                transaction.apply {
                    name = iname
                    category = icategory
                    amount = iamount.toLong()
                    date = idate
                    returnDate = ireturnDate
                    destination = idestination
                    transportation = itransportation
                    status = istatus
                    wallet = iwallet
                    userId = iuserId
                    note = inote
                }
                databaseHelper.addTrans(transaction)
                finish()
            }
        }

        val categories = resources.getStringArray(R.array.trans_category)
        selectCate = categories[0]

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, categories)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                selectCate = categories[position]
                if (selectCate == categories[8]) binding.detailZone.visible() else binding.detailZone.gone()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                selectCate = categories[0]
            }
        }


        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            binding.tvDate.text = sdf.format(cal.time)

        }
        val returnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            binding.tvReturnDate.text = sdf.format(cal.time)

        }

        binding.btnSelectDate.setOnDelayClickListener {

                DatePickerDialog(this, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.btnSelectReturnDate.setOnDelayClickListener {

                DatePickerDialog(this, returnDateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.btnTakeBill.setOnDelayClickListener {
            val MY_PERMISSIONS_REQUEST_CAMERA = 0
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA
                )
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    dispatchTakePictureIntent()
                } else {
                    Toast.makeText(
                        this,
                        "Camera access must be allowed to capture image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                try {
                    dispatchTakePictureIntent()
                } catch (e: Exception) {
                    Toast.makeText(
                       this,
                        "Camera access must be allowed to capture image",
                        Toast.LENGTH_SHORT
                    ).show()
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf<String>(Manifest.permission.CAMERA),
                        MY_PERMISSIONS_REQUEST_CAMERA
                    )
                }
            }
        }

    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go\]

                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    File("")
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                  //  startForResult.launch(takePictureIntent, REQUEST_IMAGE_CAPTURE)

                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        galleryAddPic()
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            binding.tvBill.text = f.name
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }


}