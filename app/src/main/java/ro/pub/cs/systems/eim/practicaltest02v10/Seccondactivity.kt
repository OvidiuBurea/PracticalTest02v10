package ro.pub.cs.systems.eim.practicaltest02v10

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging

class Seccondactivity : AppCompatActivity() {
    private lateinit var topicNameTextView: TextView
    private lateinit var subscribeButton: Button
    private lateinit var unsubscribeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seccond)

        topicNameTextView = findViewById(R.id.topic_name)
        subscribeButton = findViewById(R.id.subscribe_button)
        unsubscribeButton = findViewById(R.id.unsubscribe_button)

        val topic = "news"

        subscribeButton.setOnClickListener {
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener { task ->
                    var msg = "Subscribed to $topic"
                    if (!task.isSuccessful) {
                        msg = "Subscription failed"
                    }
                    Log.d("FCM", msg)
                }
        }

        unsubscribeButton.setOnClickListener {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener { task ->
                    var msg = "Unsubscribed from $topic"
                    if (!task.isSuccessful) {
                        msg = "Unsubscription failed"
                    }
                    Log.d("FCM", msg)
                }
        }
    }
}