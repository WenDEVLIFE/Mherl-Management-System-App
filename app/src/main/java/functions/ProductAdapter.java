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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductBase> productList;
    private OnDeleteClickListener onDeleteClickListener;
    private OnEditClickListener onEditClickListener;



    public ProductAdapter(List<ProductBase> productList) {
        this.productList = productList;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnEditClickListener {
        void onEditClick(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productitem, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductBase info = productList.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder class
    public class ProductViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout
        private final TextView productname;
        private final TextView price;
        private final TextView quantity;
        private final Button deleteButton;
        private final Button editButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            productname = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.buybutton);

            // Set click listener for delete button
            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(position);
                }
            });

            // Set click listener for edit button
            editButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onEditClickListener != null) {
                    onEditClickListener.onEditClick(position);
                }
            });
        }

        public void bind(ProductBase info) {
            // Bind data to views
            productname.setText(info.getProductname());
            price.setText(info.getPrice());
            quantity.setText(String.valueOf(info.getQuantity()));
        }
    }
}
