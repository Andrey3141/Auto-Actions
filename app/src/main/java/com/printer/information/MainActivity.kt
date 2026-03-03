package com.printer.information

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.SmsManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager

    // UI Elements
    private lateinit var etPhoneNumber: TextInputEditText
    private lateinit var cardLocation: CardView
    private lateinit var cardMessage: CardView
    private lateinit var cardCall: CardView
    private lateinit var tvValidation: TextView
    private lateinit var fab: ImageButton
    private lateinit var btnRefresh: ImageButton
    private lateinit var tvActionCount: TextView
    private lateinit var tvGreeting: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView

    private val DEFAULT_MESSAGE = "Привет! Это автоматическое сообщение из приложения."
    private var actionCount = 3

    // JNI методы
    private external fun formatPhoneNumberNative(input: String): String
    private external fun sendLocationNative(phone: String, lat: Double, lon: Double): Boolean
    private external fun sendSmsNative(phone: String, message: String): Boolean
    private external fun makeCallNative(phone: String): Boolean

    companion object {
        init {
            System.loadLibrary("information")
        }
    }

    private val requestMultiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            when (currentAction) {
                "location" -> getCurrentLocation()
                "message" -> sendSmsMessage()
                "call" -> makePhoneCall()
            }
        } else {
            Toast.makeText(this, "Необходимы разрешения", Toast.LENGTH_SHORT).show()
        }
    }

    private var currentAction: String = ""
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupPhoneNumberFormatting()
        setupAnimations()

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        cardLocation.setOnClickListener {
            animateButton(it)
            if (validatePhoneNumber()) {
                currentAction = "location"
                checkLocationPermissions()
            }
        }

        cardMessage.setOnClickListener {
            animateButton(it)
            if (validatePhoneNumber()) {
                currentAction = "message"
                checkSmsPermissions()
            }
        }

        cardCall.setOnClickListener {
            animateButton(it)
            if (validatePhoneNumber()) {
                currentAction = "call"
                checkCallPermissions()
            }
        }

        fab.setOnClickListener {
            animateFab()
            Toast.makeText(this, "Быстрое действие", Toast.LENGTH_SHORT).show()
        }

        btnRefresh.setOnClickListener {
            animateRefresh()
            updateActionCount()
        }
    }

    private fun initViews() {
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        cardLocation = findViewById(R.id.cardLocation)
        cardMessage = findViewById(R.id.cardMessage)
        cardCall = findViewById(R.id.cardCall)
        tvValidation = findViewById(R.id.tvValidation)
        fab = findViewById(R.id.fab)
        btnRefresh = findViewById(R.id.btnRefresh)
        tvActionCount = findViewById(R.id.tvActionCount)
        tvGreeting = findViewById(R.id.tvGreeting)
        tvTitle = findViewById(R.id.tvTitle)
        tvSubtitle = findViewById(R.id.tvSubtitle)

        tvActionCount.text = actionCount.toString()
        startInitialAnimations()
    }

    private fun startInitialAnimations() {
        val slideIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)

        Handler(Looper.getMainLooper()).postDelayed({
            tvGreeting.visibility = View.VISIBLE
            tvGreeting.startAnimation(fadeIn)
        }, 100)

        Handler(Looper.getMainLooper()).postDelayed({
            tvTitle.visibility = View.VISIBLE
            tvTitle.startAnimation(fadeIn)
        }, 200)

        Handler(Looper.getMainLooper()).postDelayed({
            tvSubtitle.visibility = View.VISIBLE
            tvSubtitle.startAnimation(fadeIn)
        }, 300)

        Handler(Looper.getMainLooper()).postDelayed({
            cardLocation.visibility = View.VISIBLE
            cardLocation.startAnimation(slideIn)
        }, 500)

        Handler(Looper.getMainLooper()).postDelayed({
            cardMessage.visibility = View.VISIBLE
            cardMessage.startAnimation(slideIn)
        }, 600)

        Handler(Looper.getMainLooper()).postDelayed({
            cardCall.visibility = View.VISIBLE
            cardCall.startAnimation(slideIn)
        }, 700)

        Handler(Looper.getMainLooper()).postDelayed({
            fab.visibility = View.VISIBLE
            fab.startAnimation(fadeIn)
        }, 800)
    }

    private fun setupAnimations() {
        val pulseAnim = ObjectAnimator.ofPropertyValuesHolder(
            fab,
            PropertyValuesHolder.ofFloat("scaleX", 1f, 1.1f, 1f),
            PropertyValuesHolder.ofFloat("scaleY", 1f, 1.1f, 1f)
        )
        pulseAnim.duration = 1000
        pulseAnim.interpolator = AccelerateDecelerateInterpolator()
        pulseAnim.repeatCount = ValueAnimator.INFINITE
        pulseAnim.startDelay = 2000
        pulseAnim.start()
    }

    private fun animateButton(view: View) {
        view.animate().scaleX(1f).scaleY(1f).setDuration(0).start() // сброс
        view.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
            view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
        }.start()
    }

    private fun animateFab() {
        fab.animate().scaleX(1f).scaleY(1f).setDuration(0).start() // сброс
        fab.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction {
            fab.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
        }.start()
    }

    private fun animateRefresh() {
        btnRefresh.animate().rotation(0f).setDuration(0).start() // сброс
        btnRefresh.animate().rotation(360f).setDuration(500).start()
    }

    private fun updateActionCount() {
        actionCount = (3..10).random()
        tvActionCount.text = actionCount.toString()
        tvActionCount.animate().scaleX(1.5f).scaleY(1.5f).setDuration(200).withEndAction {
            tvActionCount.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
        }.start()
    }

    private fun setupPhoneNumberFormatting() {
        etPhoneNumber.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val input = s?.toString() ?: ""

                // Если пользователь сам ввел +, не форматируем
                if (input.startsWith("+")) {
                    isFormatting = false
                    return
                }

                // Убираем все кроме цифр
                val digits = input.replace(Regex("[^0-9]"), "")

                if (digits.isNotEmpty()) {
                    val formatted = formatPhoneNumberNative(digits)
                    if (formatted != input && !formatted.startsWith("+7")) {
                        s?.replace(0, s.length, formatted)
                    } else if (formatted.startsWith("+7") && digits.startsWith("375")) {
                        // Если определили как РФ, но это РБ - пропускаем
                        isFormatting = false
                        return
                    }
                }

                validateNumber()
                isFormatting = false
            }
        })
    }

    private fun validateNumber() {
        val number = etPhoneNumber.text.toString()
        tvValidation.visibility = View.VISIBLE

        when {
            number.startsWith("+375") -> {
                tvValidation.text = "✓ Белорусский номер"
                tvValidation.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            }
            number.startsWith("80") -> {
                tvValidation.text = "✓ Белорусский номер"
                tvValidation.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            }
            number.startsWith("375") -> {
                tvValidation.text = "✓ Белорусский номер (добавьте +)"
                tvValidation.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark))
            }
            else -> {
                tvValidation.text = "✗ Введите белорусский номер (+375)"
                tvValidation.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            }
        }
    }

    private fun validatePhoneNumber(): Boolean {
        val number = etPhoneNumber.text.toString()

        val isValid = number.startsWith("+375") || number.startsWith("80")

        return if (isValid) {
            true
        } else {
            Toast.makeText(this, "Введите белорусский номер (+375)", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun getCleanPhoneNumber(): String {
        return etPhoneNumber.text.toString().replace(Regex("[^0-9+]"), "")
    }

    // ============= ГЕОЛОКАЦИЯ =============

    private fun checkLocationPermissions() {
        val permissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (permissions.isNotEmpty()) {
            requestMultiplePermissionsLauncher.launch(permissions.toTypedArray())
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        Toast.makeText(this, "Получение геолокации...", Toast.LENGTH_SHORT).show()

        try {
            var location: Location? = null
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
            }

            if (location != null) {
                currentLocation = location
                val phone = getCleanPhoneNumber()
                sendLocationNative(phone, location.latitude, location.longitude)
                sendLocationToTelegramIntent(phone, location)
            } else {
                Toast.makeText(this, "Геолокация недоступна", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка геолокации", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendLocationToTelegramIntent(phone: String, location: Location) {
        val locationLink = "https://maps.google.com/?q=${location.latitude},${location.longitude}"
        val locationText = "📍 Моя текущая геолокация:\n$locationLink"

        try {
            val telegramIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://t.me/$phone?text=${Uri.encode(locationText)}"))
            telegramIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(telegramIntent)
            Toast.makeText(this, "Геолокация отправляется в Telegram", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Установите Telegram", Toast.LENGTH_SHORT).show()
        }
    }

    // ============= SMS =============

    private fun checkSmsPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestMultiplePermissionsLauncher.launch(arrayOf(Manifest.permission.SEND_SMS))
        } else {
            sendSmsMessage()
        }
    }

    private fun sendSmsMessage() {
        try {
            val phone = getCleanPhoneNumber()
            sendSmsNative(phone, DEFAULT_MESSAGE)

            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phone, null, DEFAULT_MESSAGE, null, null)
            Toast.makeText(this, "SMS отправлено на номер $phone", Toast.LENGTH_SHORT).show()
            actionCount++
            tvActionCount.text = actionCount.toString()
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка отправки SMS: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // ============= ЗВОНОК =============

    private fun checkCallPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestMultiplePermissionsLauncher.launch(arrayOf(Manifest.permission.CALL_PHONE))
        } else {
            makePhoneCall()
        }
    }

    private fun makePhoneCall() {
        try {
            val phone = getCleanPhoneNumber()

            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phone")

            val packageManager = packageManager
            val resolveInfoList = packageManager.queryIntentActivities(callIntent, 0)

            val phonePackages = listOf(
                "com.android.dialer",
                "com.google.android.dialer",
                "com.android.incallui",
                "com.android.phone",
                "com.huawei.incall",
                "com.hihonor.incall",
                "com.sec.android.app.dialer",
                "com.xiaomi.incall",
                "com.oppo.incall",
                "com.vivo.incall",
                "com.android.server.telecom"
            )

            var found = false
            for (resolveInfo in resolveInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                if (phonePackages.any { packageName.contains(it, ignoreCase = true) }) {
                    callIntent.setPackage(packageName)
                    found = true
                    break
                }
            }

            if (found) {
                startActivity(callIntent)
                Toast.makeText(this, "Звонок на $phone", Toast.LENGTH_SHORT).show()
            } else {
                callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(callIntent)
            }

        } catch (e: SecurityException) {
            Toast.makeText(this, "Нет разрешения на звонки", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}