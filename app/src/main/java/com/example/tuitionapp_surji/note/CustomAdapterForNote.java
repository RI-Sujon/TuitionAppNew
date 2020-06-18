package com.example.tuitionapp_surji.note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tuitionapp_surji.R;

import java.util.ArrayList;

public class CustomAdapterForNote extends BaseAdapter {

    private Context context ;
    private ArrayList<NoteInfo> addNoteInfoArrayList ;

    public CustomAdapterForNote(Context context, ArrayList<NoteInfo> addNoteInfoArrayList) {
        this.context = context;
        this.addNoteInfoArrayList = addNoteInfoArrayList;
    }

    @Override
    public int getCount() {
        return addNoteInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomAdapterForNote.ViewHolder holder ;
        if(convertView==null){
            holder = new CustomAdapterForNote.ViewHolder() ;
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_adapter_note_list_view, null);

            holder.note_title = convertView.findViewById(R.id.note_title) ;
            holder.note = convertView.findViewById(R.id.notePostTextView) ;
            holder.note_attachment = convertView.findViewById(R.id.noteAttachmentTextView) ;
            holder.noteDate = convertView.findViewById(R.id.noteDate) ;

            convertView.setTag(holder);
        }else{
            holder = (CustomAdapterForNote.ViewHolder)convertView.getTag() ;
        }

        String post = addNoteInfoArrayList.get(position).getNote_post();
        String attachmentName = addNoteInfoArrayList.get(position).getNotePdfName() ;
        if(attachmentName==null){
            holder.note.setText(post);
            holder.note.setVisibility(View.VISIBLE);
        }
        else {
            holder.note_attachment.setText(attachmentName);
            holder.note_attachment.setVisibility(View.VISIBLE);
        }

        holder.note_title.setText(addNoteInfoArrayList.get(position).getNote_title());
        System.out.println("Dateeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee : "+addNoteInfoArrayList.get(position).getNoteDate());
        holder.noteDate.setText(addNoteInfoArrayList.get(position).getNoteDate() + ",  " +addNoteInfoArrayList.get(position).getNoteTime());

        return convertView ;
    }

    class ViewHolder{
        TextView note_title ;
        TextView note;
        TextView note_attachment;
        TextView noteDate ;
    }
}
