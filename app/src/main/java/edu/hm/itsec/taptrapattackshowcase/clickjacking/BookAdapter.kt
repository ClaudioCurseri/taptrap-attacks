package edu.hm.itsec.taptrapattackshowcase.clickjacking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.hm.itsec.taptrapattackshowcase.R

/**
 * Adapter to show a list of books in a RecyclerView.
 *
 * @param books List of books to show.
 * @param onViewDetailsClick Callback, which is called when the "view details" button is clicked.
 */
class BookAdapter(
    private val books: List<Book>,
    private val onViewDetailsClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView? = itemView.findViewById(R.id.text_book_title)
        val author: TextView? = itemView.findViewById(R.id.text_book_author)
        val button: Button? = itemView.findViewById(R.id.btn_add_to_reading_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.title?.text = book.title
        holder.author?.text = book.author

        holder.button?.setOnClickListener {
            onViewDetailsClick(book)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }
}