package com.example.i210566

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.Manifest
import android.annotation.SuppressLint
import android.database.ContentObserver
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.FileProvider
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var sendPhotoButton: ImageView
    private lateinit var sendPicandVideoButton: ImageView
    private lateinit var sendFileButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var recordButton: ImageView

    private var mediaRecorder: MediaRecorder? = null
    private var audioFileName: String? = null
    private var receiverUid: String? = null
    private var senderUid: String? = null
    private var senderRoom: String? = null
    private var receiverRoom: String? = null
    private var isRecording = false // To track recording state
    private var photoFile: File? = null // To hold the image file



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        senderUid = FirebaseAuth.getInstance().currentUser?.uid
        receiverUid = intent.getStringExtra("uid")
        val receiverName = intent.getStringExtra("name")

        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid

        supportActionBar?.title = receiverName ?: "Chat"

        chatRecyclerView = findViewById(R.id.charRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)
        sendPhotoButton = findViewById(R.id.sendPhotoButton)
        sendPicandVideoButton = findViewById(R.id.sendPicandVideoButton)
        sendFileButton = findViewById(R.id.sendFileButton)
        recordButton = findViewById(R.id.sendVoiceNote)
        messageList = ArrayList()

        messageAdapter = MessageAdapter(this, messageList,
            onMessageEdit = { message -> editMessage(message) },
            onMessageDelete = { message -> deleteMessage(message) }
        )




        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        senderRoom?.let { room ->
            db.collection("conversations").document(room)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Toast.makeText(this, "Error loading messages.", Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                    }

                    val messages = snapshots?.documents?.map { document ->
                        document.toObject(Message::class.java)?.apply { messageId = document.id }!!
                    }

                    messageList.clear()
                    messages?.let { msgs -> messageList.addAll(msgs) }
                    messageAdapter.notifyDataSetChanged()
                }
        }


        sendButton.setOnClickListener {
            val messageText = messageBox.text.toString()
            if (messageText.isNotEmpty()) {
                val message = Message(
                    message = messageText,
                    senderId = senderUid,
                    receiverId = receiverUid,
                    timestamp = System.currentTimeMillis()
                )
                sendMessage(message)
            }
        }

        sendPhotoButton.setOnClickListener {
            // Check if the camera permission is granted
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                // Request for camera permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION_CAMERA)
            }
        }

        sendPicandVideoButton.setOnClickListener {
            selectMedia("*/*", REQUEST_CODE_MEDIA)
        }

        sendFileButton.setOnClickListener {
            selectMedia("*/*", REQUEST_CODE_FILE)
        }

        recordButton.setOnClickListener {
            Toast.makeText(this, "CLICKED IT", Toast.LENGTH_SHORT).show()

            if (isRecording) {
                stopRecording()
            } else {
                    startRecording()

            }
        }


        val contentResolver = applicationContext.contentResolver
        val screenshotsUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        contentResolver.registerContentObserver(screenshotsUri, true, screenshotObserver)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val contentResolver = applicationContext.contentResolver
            val screenshotsUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            contentResolver.registerContentObserver(screenshotsUri, true, screenshotObserver)
        } else {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_READ_STORAGE)
        }
    }

    private val screenshotObserver by lazy {
        object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                super.onChange(selfChange, uri)
                checkForScreenshots()
            }
        }
    }
    companion object {
        private const val REQUEST_CODE_PHOTO = 1001
        private const val REQUEST_CODE_VIDEO = 1002
        private const val REQUEST_CODE_MEDIA = 1004
        private const val REQUEST_CODE_FILE = 1005
        private const val REQUEST_PERMISSION_CODE = 2001
        private const val REQUEST_PERMISSION_CAMERA = 2002
        private const val REQUEST_PERMISSION_READ_STORAGE = 3001
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                photoFile = createImageFile()
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.i210566.fileprovider", // Replace with your application's authority
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_CODE_PHOTO)
                }
            }
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            photoFile = this
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSION_CAMERA -> {
                // ... Your existing camera permission handling code ...
            }
            REQUEST_PERMISSION_READ_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, register the content observer
                    val contentResolver = applicationContext.contentResolver
                    val screenshotsUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    contentResolver.registerContentObserver(screenshotsUri, true, screenshotObserver)
                } else {
                    // Permission denied, show a toast or disable screenshot detection functionality
                    Toast.makeText(this, "Permission denied to detect screenshots.", Toast.LENGTH_SHORT).show()
                }
            }
            // ... Handle other permissions if there are any ...
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PHOTO, REQUEST_CODE_MEDIA, REQUEST_CODE_FILE -> { // Handle file selection
                    data?.data?.also { uri ->
                        contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        val type = contentResolver.getType(uri) ?: "file"
                        uploadFileAndSendMessage(uri, type)
                    }
                }
            }
        }
    }



    private fun uploadFileAndSendMessage(uri: Uri, type: String) {
        val fileExtension = when {
            type.contains("image") -> ".jpg"
            type.contains("video") -> ".mp4"
            type.contains("audio") -> ".m4a"
            type.contains("pdf") -> ".pdf"
            type.contains("word") -> ".docx"
            type.contains("excel") -> ".xlsx"
            else -> ".file"
        }

        val fileName = UUID.randomUUID().toString() + fileExtension
        val storageReference = FirebaseStorage.getInstance().getReference("uploads/$fileName")

        storageReference.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                    val messageType = when {
                        type.contains("image") -> "image"
                        type.contains("video") -> "video"
                        type.contains("audio") -> "audio"
                        else -> "file"
                    }
                    val message = Message(
                        messageId = "",
                        message = "Sent a $messageType",
                        senderId = FirebaseAuth.getInstance().currentUser?.uid,
                        receiverId = receiverUid,
                        timestamp = System.currentTimeMillis(),
                        type = messageType,
                        mediaUrl = downloadUri.toString()
                    )
                    sendMessage(message)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload $type.", Toast.LENGTH_SHORT).show()
            }
    }



    // Make sure the `sendMessage` function looks like this:
    private fun sendMessage(message: Message) {
        val messageId = db.collection("conversations").document(senderRoom!!)
            .collection("messages").document().id // Generate the document ID first
        val messageWithId = message.copy(messageId = messageId) // Copy the message with the ID

        db.collection("conversations").document(senderRoom!!)
            .collection("messages").document(messageId).set(messageWithId)
            .addOnSuccessListener {
                messageBox.setText("")
            }
        // Do the same for receiverRoom if necessary...
    }


    private fun deleteMessage(message: Message) {
        AlertDialog.Builder(this)
            .setTitle("Delete Message")
            .setMessage("Are you sure you want to delete this message?")
            .setPositiveButton("Delete") { _, _ ->
                if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
                    // Find the index of the message to be deleted first
                    val indexToDelete = messageList.indexOfFirst { it.messageId == message.messageId }

                    // Proceed with deletion only if the message is in the list
                    if (indexToDelete != -1) {
                        db.collection("conversations").document(senderRoom!!)
                            .collection("messages").document(message.messageId)
                            .delete()
                            .addOnSuccessListener {
                                // Now remove the message using the index found earlier
                                messageList.removeAt(indexToDelete)
                                messageAdapter.notifyItemRemoved(indexToDelete)
                                Toast.makeText(this, "Message deleted", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to delete message: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(this, "Message could not be found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "You can only delete your messages", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun editMessage(oldMessage: Message) {
        val editText = EditText(this).apply { setText(oldMessage.message) }
        AlertDialog.Builder(this)
            .setTitle("Edit Message")
            .setView(editText)
            .setPositiveButton("Update") { dialog, which ->
                val newText = editText.text.toString()
                if (newText.isNotEmpty()) {
                    val messageRef = db.collection("conversations").document(senderRoom!!)
                        .collection("messages").document(oldMessage.messageId)
                    messageRef
                        .update("message", newText)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Message updated", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to update message", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }



    private fun selectMedia(fileType: String, requestCode: Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            val type = if (fileType == "file") "*/*" else fileType // Use a local variable instead of the parameter
            setType(type)

            if (fileType == "file") {
                val mimeTypes = arrayOf(
                    "application/pdf",
                    "application/msword",
                    "application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                )
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
        }
        startActivityForResult(intent, requestCode)
    }




    private fun startRecording() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                audioFileName = "${externalCacheDir?.absolutePath}/audiorecord_${System.currentTimeMillis()}.m4a"
                setOutputFile(audioFileName)
                try {
                    prepare()
                    start()
                    isRecording = true
                    recordButton.setImageResource(R.drawable.red_mic) // Change icon to red mic
                    Toast.makeText(this@ChatActivity, "Recording started", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    Log.e("ChatActivity", "prepare() failed", e)
                    Toast.makeText(this@ChatActivity, "Recording failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_PERMISSION_CODE)
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        isRecording = false
        recordButton.setImageResource(R.drawable.mic) // Change icon back to mic
        Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()
        audioFileName?.let { fileName ->
            val uri = Uri.fromFile(File(fileName))
            uploadFileAndSendMessage(uri, "audio")
        }
    }



    private fun checkPermission(): Boolean {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_PERMISSION_CODE
            )
            return false
        }

    @SuppressLint("Range")
    private fun checkForScreenshots() {
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        val selection = "${MediaStore.Images.Media.DISPLAY_NAME} LIKE '%screenshot%'"
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val fileName = it.getString(it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                // Additional check to avoid multiple toasts for the same screenshot if needed.
                if (fileName.contains("screenshot", ignoreCase = true)) {
                    runOnUiThread {
                        Toast.makeText(this, "Screenshot taken: $fileName", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

        override fun onDestroy() {
            super.onDestroy()
            // Unregister the content observer
            val contentResolver = applicationContext.contentResolver
            contentResolver.unregisterContentObserver(screenshotObserver)
        }


    }
