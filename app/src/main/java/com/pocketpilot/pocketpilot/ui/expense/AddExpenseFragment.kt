package com.pocketpilot.pocketpilot.ui.expense

import android.app.Activity.RESULT_OK

/**
 * Add Expense Fragment for PocketPilot budget tracking app
 * This fragment handles:
 * - Input validation for expense amounts
 * - Category selection from dropdown
 * - Date picking via calendar
 * - Camera integration for receipt photos
 * - Saving expenses to database (to be integrated with Member 1's Room DB)
 *
 * References:
 * @see <a href="https://developer.android.com/training/camera/photobasics">Camera Documentation</a>
 * @see <a href="https://developer.android.com/guide/topics/ui/controls/pickers">Date Picker Documentation</a>
 */

class AddExpenseFragment : Fragment() {
    private lateinit var etAmount: TextInputEditText
    private lateinit var etCategory: AutoCompleteTextView
    private lateinit var etDescription: TextInputEditText
    private lateinit var btnTakePhoto: MaterialButton
    private lateinit var ivReceipt: ImageView
    private lateinit var btnSaveExpense: Button

    private var currentPhotoPath: String? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (result.resultCode == RESULT_OK) {
            val photFile = File(currentPhotoPath!!)
            val photoUri = FileProvider.getUriForFile(
                requireContext(),
                "com.pocketpilot.fileprovider",
                photoFile
            )
            ivReceipt.setImageURI(photoUri)
            ivReceipt.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "Receipt saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupCategoryDropdown()
        setupDatePicker()
        setupCameraButton()
        setupSaveButton()
    }

    private fun initViews(view: View) {
        etAmount = view.findViewById(R.id.etAmount)
        etCategory = view.findViewById(R.id.etCategory)
        etDate = view.findViewById(R.id.etDate)
        etDescription = view.findViewById(R.id.etDescription)
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto)
        ivReceiptPreview = view.findViewById(R.id.ivReceiptPreview)
        btnSaveExpense = view.findViewById(R.id.btnSaveExpense)
    }

    private fun setupCategoryDropdown() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, ExpenseCategories.categories)
        etCategory.setAdapter(adapter)
        etCategory.setText(ExpenseCategories.categories[0], false)
    }

    private fun setupDatePicker() {
        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            android.app.DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)
                    etDate.setText(dateFormat.format(selectedDate.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        etDate.setText(dateFormat.format(Date()))
    }

    private fun setupCameraButton() {
        btnTakePhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        try {
            val photoFile = createImageFile()
            currentPhotoPath = photoFile.absolutePath
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                photoFile
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            takePictureLauncher.launch(intent)
        } catch (e: IOException) {
            Toast.makeText(context, "Error creating photo file", Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = requireContext().getExternalFilesDir(null)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun setupSaveButton() {
        btnSaveExpense.setOnClickListener {
            if (validateInputs()) {
                val expense = createExpenseFromInputs()
                saveExpense(expense)
            }
        }
    }

    private fun validateInputs(): Boolean {
        val amountText = etAmount.text.toString()
        if (amountText.isEmpty()) {
            etAmount.error = "Please enter an amount"
            return false
        }
        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            etAmount.error = "Please enter a valid amount greater than 0"
            return false
        }

        if (etCategory.text.toString().isEmpty()) {
            etCategory.error = "Please select a category"
            return false
        }

        if (etDate.text.toString().isEmpty()) {
            etDate.error = "Please select a date"
            return false
        }

        return true
    }

    private fun createExpenseFromInputs(): Expense {
        return Expense(
            amount = etAmount.text.toString().toDouble(),
            category = etCategory.text.toString(),
            description = etDescription.text.toString(),
            date = dateFormat.parse(etDate.text.toString()) ?: Date(),
            receiptPath = currentPhotoPath
        )
    }

    private fun saveExpense(expense: Expense) {
        // TODO: Integrate with Member 1's Room Database when available
        Toast.makeText(context, "Expense saved: R${expense.amount} for ${expense.category}", Toast.LENGTH_LONG).show()
        clearForm()
    }

    private fun clearForm() {
        etAmount.text?.clear()
        etDate.setText(dateFormat.format(Date()))
        etDescription.text?.clear()
        ivReceiptPreview.visibility = View.GONE
        currentPhotoPath = null
    }
}