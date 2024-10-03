package com.KAC.vikranthpublications.view.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.KAC.vikranthpublications.R
import com.KAC.vikranthpublications.databinding.ActivityFinalformBinding
import com.KAC.vikranthpublications.model.CompletedLeadDataClass
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FinalformActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinalformBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalformBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val finaloutcome = resources.getStringArray(R.array.Final_Outcome)
        val spinner = findViewById<Spinner>(R.id.finaloutcomespinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(this, R.layout.spinner_item, finaloutcome)
            spinner.adapter = adapter
        }

        binding.expectedorderdateBtn.setOnClickListener {
            binding.expectedDatePicker.visibility = View.VISIBLE
            val expecteddatePicker = binding.expectedDatePicker
            expecteddatePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                val expectedselectedDate = "$dayOfMonth-${monthOfYear + 1}-$year"
                binding.expectedorderdateTxt.text = expectedselectedDate
                binding.expectedDatePicker.visibility = View.GONE
                binding.expectedorderdateBtn.visibility = View.VISIBLE
            }
        }

        binding.installmentoneBtn.setOnClickListener {
            binding.installment1DatePicker.visibility = View.VISIBLE
            val I1datePicker = binding.installment1DatePicker
            I1datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                val I1selectedDate = "$dayOfMonth-${monthOfYear + 1}-$year"
                binding.installmentoneTxt.text = I1selectedDate
                binding.installment1DatePicker.visibility = View.GONE
                binding.installmentoneBtn.visibility = View.VISIBLE
            }
        }
        binding.installmenttwoBtn.setOnClickListener {
            binding.installment2DatePicker.visibility = View.VISIBLE
            val I2datePicker = binding.installment2DatePicker
            I2datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                val I2selectedDate = "$dayOfMonth-${monthOfYear + 1}-$year"
                binding.installmenttwoTxt.text = I2selectedDate
                binding.installment2DatePicker.visibility = View.GONE
                binding.installmenttwoBtn.visibility = View.VISIBLE
            }
        }
        binding.installmentthreeBtn.setOnClickListener {
            binding.installment3DatePicker.visibility = View.VISIBLE
            val I3datePicker = binding.installment3DatePicker
            I3datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                val I3selectedDate = "$dayOfMonth-${monthOfYear + 1}-$year"
                binding.installmentthreeTxt.text = I3selectedDate
                binding.installment3DatePicker.visibility = View.GONE
                binding.installmentthreeBtn.visibility = View.VISIBLE
            }
        }


        binding.TaskSubmitBtn.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val uid = user?.uid
            database = FirebaseDatabase.getInstance().getReference("Completed Leads").child(uid!!)
            val expected_order_value = binding.expectedordervalueTxt.text.toString().trim()
            val final_outcome = binding.finaloutcomespinner.selectedItem.toString().trim()
            val final_order_value = binding.finalordervalueTxt.text.toString().trim()
            val feedback = binding.finalformfeedbackTxt.text.toString().trim()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            val time = LocalDateTime.now().format(formatter).toString().trim()
            val dateformat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val task_closed_date = LocalDateTime.now().format(dateformat).toString().trim()

            val expecteddatePicker = binding.expectedDatePicker
            val expectedyear = expecteddatePicker.year
            val expectedmonth = expecteddatePicker.month
            val expectedday = expecteddatePicker.dayOfMonth
            val expectedselectedDate =
                "$expectedday-${expectedmonth + 1}-$expectedyear" // +1 because months are 0-indexed
            val expected_order_date = expectedselectedDate.trim()


            val instalment1datePicker = binding.installment1DatePicker
            val instalment1year = instalment1datePicker.year
            val instalment1month = instalment1datePicker.month
            val instalment1day = instalment1datePicker.dayOfMonth
            val instalment1selectedDate =
                "$instalment1day-${instalment1month + 1}-$instalment1year" // +1 because months are 0-indexed
            val installment_1 = instalment1selectedDate.trim()

            val instalment2datePicker = binding.installment2DatePicker
            val instalment2year = instalment2datePicker.year
            val instalment2month = instalment2datePicker.month
            val instalment2day = instalment2datePicker.dayOfMonth
            val instalment2selectedDate =
                "$instalment2day-${instalment2month + 1}-$instalment2year" // +1 because months are 0-indexed
            val installment_2 = instalment2selectedDate.trim()

            val instalment3datePicker = binding.installment3DatePicker
            val instalment3year = instalment3datePicker.year
            val instalment3month = instalment3datePicker.month
            val instalment3day = instalment3datePicker.dayOfMonth
            val instalment3selectedDate =
                "$instalment3day-${instalment3month + 1}-$instalment3year" // +1 because months are 0-indexed
            val installment_3 = instalment3selectedDate.trim()


            if (expected_order_value.isEmpty()) {
                Toast.makeText(this, "Please Enter School Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (final_order_value.isEmpty()) {
                Toast.makeText(this, "Please Enter your employee code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (feedback.isEmpty()) {
                Toast.makeText(this, "Please Enter your employee code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userid = uid.toString().trim()
            val uniqueKey = database.push().key
            val school_name = intent.getStringExtra("SchoolName")!!
            val school_email = intent.getStringExtra("SchoolEmail")
            val school_phone_number = intent.getStringExtra("SchoolPhone")
            val school_address = intent.getStringExtra("SchoolAddress")
            val school_pincode = intent.getStringExtra("SchoolPincode")
            val no_of_students = intent.getStringExtra("NoOfStudents")
            val contact_person_name = intent.getStringExtra("ContactPersonName")
            val contact_person_phone = intent.getStringExtra("ContactPersonPhone")
            val contact_person_email = intent.getStringExtra("ContactPersonEmail")
            val desicion_maker_name = intent.getStringExtra("DesicionName")
            val desicion_maker_phone = intent.getStringExtra("DesicionPhone")
            val desicion_maker_email = intent.getStringExtra("DesicionEmail")
            val desicion_maker_designation = intent.getStringExtra("DesicionDesignation")
            val current_supplier = intent.getStringExtra("CurrentSupplier")
            val current_supplier_discount_percentage =
                intent.getStringExtra("CurrentSupplierDiscount")
            val prospect = intent.getStringExtra("Prospect")




            val finalformtask = CompletedLeadDataClass(uniqueKey,
                userid,
                task_closed_date,
                school_name,
                school_email,
                school_phone_number,
                school_address,
                school_pincode,
                no_of_students,
                contact_person_name,
                contact_person_phone,
                contact_person_email,
                desicion_maker_name,
                desicion_maker_phone,
                desicion_maker_email,
                desicion_maker_designation,
                current_supplier,
                current_supplier_discount_percentage,
                prospect,
                expected_order_value,
                expected_order_date,
                final_outcome,
                final_order_value,
                installment_1,
                installment_2,
                installment_3,
                feedback,
                time)



            database.child(uniqueKey!!).setValue(finalformtask).addOnSuccessListener {

                binding.finalordervalueTxt.text?.clear()
                binding.finalformfeedbackTxt.text?.clear()
                binding.expectedordervalueTxt.text?.clear()

                Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
                deleteCompletedItem()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()

            }


        }

    }


    private fun deleteCompletedItem() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val uniqueKey = intent.getStringExtra("UniqueKey")
        val database2 = FirebaseDatabase.getInstance()
        val reminderTasklistRef: DatabaseReference = database2.getReference("New Leads").child(uid!!)
        val uniqueKeyToDelete = "$uniqueKey"
        reminderTasklistRef.child(uniqueKeyToDelete).removeValue()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}