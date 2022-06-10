package com.example.applibros20.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applibros20.R;
import com.example.applibros20.ui.entidades.Libro;

import java.util.List;

//Esta clase es un puente entre nuestros datos y las listas obtenidas
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {
    List<Libro> bookList;
    public BookAdapter(List<Libro> bookList){
        this.bookList=bookList;
    }


    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        holder.list_num_book.setText(String.valueOf(bookList.get(position).getId()));
        holder.list_title.setText(bookList.get(position).getTitle().toString());
        holder.list_author.setText(bookList.get(position).getAuthor().toString());
        holder.list_description.setText(bookList.get(position).getDescription().toString());
        holder.list_status.setText(bookList.get(position).getStatus().toString());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder{
        TextView list_num_book, list_title, list_author, list_description, list_status;
        public BookHolder(@NonNull View itemView) {
            super(itemView);
            list_num_book = (TextView) itemView.findViewById(R.id.list_num_book);
            list_title = (TextView) itemView.findViewById(R.id.list_title);
            list_author = (TextView) itemView.findViewById(R.id.list_author);
            list_description = (TextView) itemView.findViewById(R.id.list_description);
            list_status = (TextView) itemView.findViewById(R.id.list_status);
        }
    }
}
