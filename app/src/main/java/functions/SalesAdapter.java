package functions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mherlsmanagementsystem.R;

import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.SalesViewHolder> {

    private List<Sales> salesList;
    private OnDeleteClickListener onDeleteClickListener;



    public SalesAdapter(List<Sales> salesList) {
        this.salesList = salesList;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }



    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }



    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.salesitem, parent, false);
        return new SalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesViewHolder holder, int position) {
        Sales info = salesList.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    // ViewHolder class
    public class SalesViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout
        private final TextView productname;
        private final TextView price;
        private final TextView quantity;

        private final TextView date;
        private final Button deleteButton;


        public SalesViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            productname = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            date = itemView.findViewById(R.id.date);
            deleteButton = itemView.findViewById(R.id.deleteButton);


            // Set click listener for delete button
            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(position);
                }
            });


        }

        public void bind(Sales info) {
            // Bind data to views
            productname.setText(info.getProductName());
            price.setText(String.valueOf(info.getTotalprice()));
            quantity.setText(String.valueOf(info.getQuantity()));
            date.setText(info.getDate());
        }

    }
}
