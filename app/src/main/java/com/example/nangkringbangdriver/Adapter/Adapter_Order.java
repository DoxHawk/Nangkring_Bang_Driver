package com.example.nangkringbangdriver.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbangdriver.Model.Model_Orders_Driver;
import com.example.nangkringbangdriver.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Adapter_Order extends FirestoreRecyclerAdapter<Model_Orders_Driver, Adapter_Order.OrderHolder> {

    private FirebaseUser mUser;
    private OnItemClickListener listener;

    public Adapter_Order(@NonNull FirestoreRecyclerOptions<Model_Orders_Driver> options, FirebaseUser mUser) {
        super(options);
        this.mUser = mUser;
    }

    private String formatRupiah(int number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull Model_Orders_Driver model) {
        SimpleDateFormat sfd = new SimpleDateFormat("DD MM yyyy");
        holder.o_id.setText(getSnapshots().getSnapshot(position).getId());
        holder.o_metode.setText(model.getPesanan_metode());
        holder.o_tgl.setText(sfd.format(new Date(String.valueOf(model.getPesanan_tgl_order().toDate()))));
        if (model.getPesanan_status().equals("Selesai")){
            holder.o_sts.setImageResource(R.drawable.ic_selesai);
        }else if (model.getPesanan_status().equals("Proses")){
            holder.o_sts.setImageResource(R.drawable.ic_proses);
        }else{
            holder.o_sts.setImageResource(R.drawable.ic_batal);
        }
        holder.o_total.setText(formatRupiah(model.getPesanan_sub()));
        holder.o_alamat.setText(model.getPesanan_alamat());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != RecyclerView.NO_POSITION && listener != null){
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            }
        });
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders, parent, false);
        return new OrderHolder(view);
    }

    class OrderHolder extends RecyclerView.ViewHolder{
        TextView o_id, o_tgl, o_metode, o_alamat, o_total;
        ImageView o_sts;

        public OrderHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            o_id = itemView.findViewById(R.id.orderName);
            o_tgl = itemView.findViewById(R.id.orderDate);
            o_metode = itemView.findViewById(R.id.orderMetd);
            o_total = itemView.findViewById(R.id.orderTotal);
            o_sts = itemView.findViewById(R.id.orderImg);
            o_alamat = itemView.findViewById(R.id.orderLok);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
