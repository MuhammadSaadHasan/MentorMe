package com.example.i210566

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(
    private val context: Context,
    private var messageList: ArrayList<Message>,
    private val onMessageEdit: (Message) -> Unit,
    private val onMessageDelete: (Message) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = if (viewType == ITEM_RECEIVE) {
            LayoutInflater.from(context).inflate(R.layout.recieve, parent, false)
        } else {
            LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
        }
        return if (viewType == ITEM_RECEIVE) ReceiveViewHolder(view) else SentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        // Assuming SentViewHolder is for messages sent by the user and can be edited
        if (holder is SentViewHolder) {
            holder.sentMessage.text = currentMessage.message
            holder.itemView.setOnLongClickListener {
                // Present options to delete or edit
                AlertDialog.Builder(context).apply {
                    setTitle("Edit or Delete Message")
                    setPositiveButton("Edit") { _, _ ->
                        onMessageEdit(currentMessage)
                    }
                    setNegativeButton("Delete") { _, _ ->
                        onMessageDelete(currentMessage)
                    }
                    setNeutralButton("Cancel", null)
                }.show()
                true
            }
        } else if (holder is ReceiveViewHolder) {
            holder.receiveMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid == currentMessage.senderId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun getItemCount() = messageList.size

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage: TextView = itemView.findViewById(R.id.txt_sent_message)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.txt_receive_message)
    }
}
