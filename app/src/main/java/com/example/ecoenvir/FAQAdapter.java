package com.example.ecoenvir;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQVH> {

    List<FAQ> faqList;

    public FAQAdapter(List<FAQ> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FAQAdapter.FAQVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.faq,parent,false);
        return new FAQVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQAdapter.FAQVH holder, int position) {
        FAQ faq=faqList.get(position);
        holder.qtext.setText(faq.getQuestion());
        holder.atext.setText(faq.getAnswer());
        boolean isExpandable=faqList.get(position).isExpandable();
        holder.relativeLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }
    public class FAQVH extends RecyclerView.ViewHolder{
        TextView qtext, atext;
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;

        public FAQVH(@NonNull View itemView) {
            super(itemView);
            qtext=itemView.findViewById(R.id.questions);
            atext=itemView.findViewById((R.id.ans1));
            linearLayout=itemView.findViewById(R.id.linear);
            relativeLayout=itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FAQ faq=faqList.get(getAdapterPosition());
                    faq.setExpandable(!faq.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
