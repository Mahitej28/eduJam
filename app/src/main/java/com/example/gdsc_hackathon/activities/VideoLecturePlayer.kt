package com.example.gdsc_hackathon.activities

import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.gdsc_hackathon.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.youtube.player.*
import android.view.View
import androidx.appcompat.widget.Toolbar


class VideoLecturePlayer :YouTubeBaseActivity() {

    lateinit var parentVew: RelativeLayout

    private lateinit var lectureLink: String
    private lateinit var downloadPdfButton: MaterialButton
    private lateinit var lectureShare: ImageView
    private lateinit var lectureDownload: ImageView
    private lateinit var lectureTitle: TextView
    private lateinit var lectureTeacher: TextView
    private lateinit var lectureDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_lecture_video_player)

        val ytPlayer = findViewById<YouTubePlayerView>(R.id.ytPlayer)
        parentVew = findViewById(R.id.VideoLecturePlayerLayout)
        downloadPdfButton = findViewById(R.id.downloadLectureNotesButton)
        lectureShare = findViewById(R.id.lectureShare)
        lectureDownload = findViewById(R.id.lectureDownload)

        val bundle: Bundle? = intent.extras

        lectureTitle = findViewById(R.id.lectureTitle)
        lectureTeacher = findViewById(R.id.lectureTeacher)
        lectureDate = findViewById(R.id.lectureDate)

        var lectureID:Any = ""
        if(bundle!=null) {
            lectureID = bundle.get("lectureID")!!
            val lectureTitleText = bundle.get("lectureTitle")
            val lectureTeacherText = bundle.get("lectureTeacher")
            val lectureDateText = bundle.get("lectureDate")

            lectureTitle.text = lectureTitleText.toString()
            lectureTeacher.text = lectureTeacherText.toString()
            lectureDate.text = lectureDateText.toString()
        }

        // setting toolbar
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.title = lectureTitle.text.toString()

        toolbar.setNavigationOnClickListener {
            finish()
        }

        lectureLink = "https://www.youtube.com/watch?v=$lectureID"

        ytPlayer.initialize(
            getString(R.string.developer_key),
            object : YouTubePlayer.OnInitializedListener {

                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {
                    youTubePlayer.cueVideo(lectureID.toString())
                    youTubePlayer.play()
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                    Snackbar.make(parentVew, "Video player Failed", Snackbar.LENGTH_SHORT)
                        .show()
                }
            })


        downloadPdfButton.setOnClickListener {
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val uri =
                Uri.parse("https://drive.google.com/u/0/uc?id=1lQiSIweLdRywz204nAE5p3ztzJq_11pS&export=download")
            val request = DownloadManager.Request(uri)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            val reference: Long = downloadManager.enqueue(request)
        }

        lectureShare.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            share.putExtra(Intent.EXTRA_TEXT, lectureLink)
            this.startActivity(Intent.createChooser(share, "Share Quote!"))
        }

        // todo lmao obv we cant download yt vids lel use librarby or let it be gdrive links for now hehe
        lectureDownload.setOnClickListener {
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val uri =
                Uri.parse(lectureLink)
            val request = DownloadManager.Request(uri)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            val reference: Long = downloadManager.enqueue(request)
        }
    }

    }