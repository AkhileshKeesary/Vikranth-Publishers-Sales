package com.KAC.vikranthpublications.view.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.KAC.vikranthpublications.R
import com.KAC.vikranthpublications.databinding.ActivityMainBinding
import com.KAC.vikranthpublications.model.NewLeadDataClass
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var follow_up_timestamp : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth


        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener {
            showAddNewLead()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,R.id.completedleadsFragment,R.id.outdatedFragment,R.id.catalogueFragment,R.id.idcardFragment), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    showLogoutConfirmationDialog()
                    true
                }
                else -> {
                    navController.popBackStack()
                    navController.navigate(item.itemId)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.catalogueFragment,R.id.attendanceFragment -> binding.appBarMain.fab.hide()  // Hide the FAB
                else -> binding.appBarMain.fab.show()  // Show the FAB
            }
        }

        val headerView = navView.getHeaderView(0)
        val imageView = headerView.findViewById<ImageView>(R.id.imageView)
        val fullname = headerView.findViewById<TextView>(R.id.fullnameTextView)
        val emailid = headerView.findViewById<TextView>(R.id.emailidTextView)
        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user!!.uid
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("User Details").child(uid)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val fn = dataSnapshot.child("full_name").getValue(String::class.java)
                val id = dataSnapshot.child("employee_code").getValue(String::class.java)
                val Dob = dataSnapshot.child("date_of_birth").getValue(String::class.java)
                val gender = dataSnapshot.child("gender").getValue(String::class.java)
                val email = dataSnapshot.child("email_id").getValue(String::class.java)

                val parts = fn.toString().trim().split(" ")
                val firstName = parts.firstOrNull() ?: ""
                val lastName = parts.drop(1).joinToString(" ")
                val bitmap = createProfileBitmap(firstName, lastName, 200)
                imageView.setImageBitmap(bitmap)
                fullname.text = fn.toString()
                emailid.text = email.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    @SuppressLint("MissingInflatedId")
    private fun showAddNewLead() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.new_lead_layout, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        val schoolnameTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.schoolnameTxt)
        val schoolphoneTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.schoolphoneTxt)
        val schoolemailTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.schoolemailTxt)
        val schooldoornoTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.schoolDoornoTxt)
        val schoolstreetTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.schoolStreetnoTxt)
        val schooltownTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.schoolTwonorCityTxt)
        val schoolpincodeTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.schoolpincodeTxt)
        val schoolno_studentsTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.schoolNOSTxt)
        val contactnameTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.contactnameTxt)
        val contactphoneTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.contactphoneTxt)
        val contactemailTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.contatemailTxt)
        val desicionmakernameTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.desicionmakenameTxt)
        val desicionmakerphoneTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.desicionmakephoneTxt)
        val desicionmakeremailTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.desicionmakeemailidTxt)
        val desicionmakerdesignationTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.desicionmakedesignationTxt)
        val currentsupplierTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.currentsuplierTxt)
        val discountTextInputEditText: TextInputEditText = bottomSheetView.findViewById(R.id.curentsupplierdiscountTxt)
        val priority: Spinner = bottomSheetView.findViewById(R.id.prioritySpinner)
        val followupDateButton: Button = bottomSheetView.findViewById(R.id.followupdateBtn)
        val followupDateTextView: TextView = bottomSheetView.findViewById(R.id.followuTextView)

        val newLeadButton: Button = bottomSheetView.findViewById(R.id.TaskSubmitBtn)
        val folloupDatePicker: DatePicker = bottomSheetView.findViewById(R.id.folloupDatePicker)

        followupDateButton.setOnClickListener {
            folloupDatePicker.visibility = View.VISIBLE
            val datePicker = folloupDatePicker
            datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth-${monthOfYear + 1}-$year"
                followupDateTextView.text = selectedDate
                folloupDatePicker.visibility = View.GONE
                followupDateButton.visibility = View.VISIBLE
            }
        }

        val prospectarray = resources.getStringArray(R.array.Prospect)
        val spinner = priority
        if (spinner != null) {
            val adapter = ArrayAdapter(this, R.layout.spinner_item, prospectarray)
            spinner.adapter = adapter
        }
        val prospect = priority.selectedItem.toString().trim()
        val status = "Incomplete"
        val datePicker = folloupDatePicker
        val year = datePicker.year
        val month = datePicker.month
        val day = datePicker.dayOfMonth
        val selectedDate = "$day-${month + 1}-$year" // +1 because months are 0-indexed
        val follow_up_date = selectedDate.trim()

        val currentTimeMillis = System.currentTimeMillis()
        val ten = "100000"
        if(month.toString().length == 1){
            val newmonth = month+1
            val monthstring = "0$newmonth"
            val dateFormat = SimpleDateFormat("$year$monthstring$day$ten")
            follow_up_timestamp = dateFormat.format(Date(currentTimeMillis))
        }
        else {
            val dateFormat = SimpleDateFormat("$year${month + 1}$day$ten")
            follow_up_timestamp = dateFormat.format(Date(currentTimeMillis))
        }

        
        database = FirebaseDatabase.getInstance().getReference("New Leads")
        val uniqueKey = database.push().key.toString()
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val dateformat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val task_generated_date = LocalDateTime.now().format(dateformat).toString().trim()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val timestamp = LocalDateTime.now().format(formatter).toString().trim()


        newLeadButton.setOnClickListener {
            val schoolname = schoolnameTextInputEditText.text.toString()
            val schoolphone = schoolphoneTextInputEditText.text.toString()
            val schoolemail = schoolemailTextInputEditText.text.toString()
            val schooldoorno = schooldoornoTextInputEditText.text.toString()
            val schoolstreet = schoolstreetTextInputEditText.text.toString()
            val schooltown = schooltownTextInputEditText.text.toString()
            val schoolpincode = schoolpincodeTextInputEditText.text.toString()
            val schoolno_students = schoolno_studentsTextInputEditText.text.toString()
            val contactname = contactnameTextInputEditText.text.toString()
            val contactphone = contactphoneTextInputEditText.text.toString()
            val contactemail = contactemailTextInputEditText.text.toString()
            val desicionmakername = desicionmakernameTextInputEditText.text.toString()
            val desicionmakerphone = desicionmakerphoneTextInputEditText.text.toString()
            val desicionmakeremail = desicionmakeremailTextInputEditText.text.toString()
            val desicionmakerdesignation = desicionmakerdesignationTextInputEditText.text.toString()
            val currentsupplier = currentsupplierTextInputEditText.text.toString()
            val discount = discountTextInputEditText.text.toString()
            val schooladdress = "$schooldoorno,$schoolstreet,$schooltown,$schoolpincode"


            if (schoolname.isEmpty()) {
                Toast.makeText(this, "Please Enter School Name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (schoolphone.isEmpty()) {
                Toast.makeText(this, "Please Enter School Phone Number.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (schoolemail.isEmpty()) {
                Toast.makeText(this, "Please Enter School Email.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (schooldoorno.isEmpty()) {
                Toast.makeText(this, "Please Enter School Door No., Street No.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (schoolstreet.isEmpty()) {
                Toast.makeText(this, "Please Enter School Colony/Area.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (schooltown.isEmpty()) {
                Toast.makeText(this, "Please Enter School Town/City.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (schoolpincode.isEmpty()) {
                Toast.makeText(this, "Please Enter School Pincode.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (schoolno_students.isEmpty()) {
                Toast.makeText(this, "Please Enter No. of Students.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (contactname.isEmpty()) {
                Toast.makeText(this, "Please Enter Contact Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (contactphone.isEmpty()) {
                Toast.makeText(this, "Please Enter Contact Phone No.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (contactemail.isEmpty()) {
                Toast.makeText(this, "Please Enter Contact Email.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (desicionmakername.isEmpty()) {
                Toast.makeText(this, "Please Enter Decision Maker Name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (desicionmakerphone.isEmpty()) {
                Toast.makeText(this, "Please Enter Decision Maker Phone No.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (desicionmakeremail.isEmpty()) {
                Toast.makeText(this, "Please Enter Decision Maker Email.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (desicionmakerdesignation.isEmpty()) {
                Toast.makeText(this, "Please Enter Decision Maker Designation.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (currentsupplier.isEmpty()) {
                Toast.makeText(this, "Please Enter Current Supplier.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (discount.isEmpty()) {
                Toast.makeText(this, "Please Enter Current Supplier Discount.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(selectedDate.isEmpty()){
                Toast.makeText(this,"Select Follow up date.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            TaskGenerated(
                uniqueKey, uid.toString(), task_generated_date, schoolname,
                schoolemail, schoolphone, schooladdress, schoolpincode,
                schoolno_students, contactname, contactphone, contactemail,
                desicionmakername, desicionmakerphone, desicionmakeremail,
                desicionmakerdesignation, currentsupplier, discount,
                prospect, status, follow_up_date, follow_up_timestamp, timestamp
            ) { isSuccess ->
                if (isSuccess) {
                    bottomSheetDialog.dismiss()
                }
            }

        }



        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.isHideable = false // Prevent hiding by touch
                behavior.isDraggable = false // Allow dragging if needed
                it.background = ContextCompat.getDrawable(this, R.drawable.bottom_sheet_background)

                bottomSheet.post {
                    val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                    val desiredHeight = screenHeight - 150.dpToPx(this)
                    val layoutParams = it.layoutParams
                    layoutParams.height = desiredHeight
                    it.layoutParams = layoutParams
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED // Ensure the bottom sheet is expanded
                }
            }
        }
        bottomSheetDialog.show()
    }

    private fun TaskGenerated(uniqueKey:String,uid:String,task_generated_date:String,
                              schoolname:String,schoolemail:String, schoolphone:String,
                              schooladdress:String,schoolpincode:String,schoolno_students:String,
                              contactname:String,contactphone:String,contactemail:String,
                              desicionmakername:String, desicionmakerphone:String,
                              desicionmakeremail:String, desicionmakerdesignation:String,
                              currentsupplier:String,discount:String,prospect:String,
                              status:String,follow_up_date:String,follow_up_timestamp:String,
                              timestamp:String,onComplete: (Boolean) -> Unit) {


        val salesemployeestask = NewLeadDataClass(  uniqueKey,uid,task_generated_date,schoolname,
            schoolemail,schoolphone,schooladdress,schoolpincode,
            schoolno_students, contactname,contactphone,contactemail,
            desicionmakername, desicionmakerphone,desicionmakeremail,
            desicionmakerdesignation,currentsupplier,discount,
            prospect,status,follow_up_date,follow_up_timestamp,timestamp)


        database.child(uid).child(uniqueKey).setValue(salesemployeestask).addOnSuccessListener {
            Toast.makeText(this, "Lead Generated.", Toast.LENGTH_SHORT).show()
            onComplete(true)
        }.addOnFailureListener {
            Toast.makeText(this, "Lead Generation Failed", Toast.LENGTH_SHORT).show()

        }
    }


    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_message -> {
                showLogoutConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Logout")
        builder.setMessage("Are you sure you want to log out?")
        builder.setPositiveButton("Yes") { _, _ ->
            performLogout()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun performLogout() {
        FirebaseAuth.getInstance().signOut()

        val loginIntent = Intent(this, LoginActivity::class.java)
        loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(loginIntent)
        finish()
    }



    private fun createProfileBitmap(firstName: String, lastName: String, size: Int): Bitmap {
        val initials = (firstName.firstOrNull()?.toString() ?: "") +
                (lastName.firstOrNull()?.toString() ?: "")
        val color = Color.parseColor("#E3F2FD")
        val paint = Paint().apply {
            val color = Color.BLACK
            textSize = size * 0.5f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(color)
        canvas.drawText(
            initials,
            size / 2f,
            size / 2f - (paint.descent() + paint.ascent()) / 2,
            paint
        )
        return bitmap
    }



}