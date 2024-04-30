package com.example.dappsplit;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dappsplit.GroupItem;
import com.example.dappsplit.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<GroupItem> {

    private ArrayList<GroupItem> groupItems;

    private Context context;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;


    public CustomAdapter(Context context, ArrayList<GroupItem> groupItems) {
        super(context, 0, groupItems);
        this.context = context;
        this.groupItems = groupItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item, parent, false);
        }

        TextView tvGrpName = convertView.findViewById(R.id.tvGrpName);
        TextView tvAmountSpiltted = convertView.findViewById(R.id.tvAmountSpiltted);
        TextView tvAmountPayable = convertView.findViewById(R.id.tvAmountPayable);
        Button btnPayAndSettle = convertView.findViewById(R.id.btnPayAndSettle);
        Button btnDelete = convertView.findViewById(R.id.btndelete);

        tvGrpName.setText(item.getGroupName());
        tvAmountSpiltted.setText(item.getAmountSpiltted());
        tvAmountPayable.setText(item.getAmountPayable());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set OnClickListener for btnPayAndSettle if needed
        btnPayAndSettle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the functionality to transfer payable amount to recipient's address
                // For now, let's display a toast message when the button is clicked
                String recipientAddress1 = item.getrecepeint();
                String payableAmount1 = item.getAmountPayable();
                int pa = Integer.parseInt(payableAmount1);
                BigInteger paBigInt = BigInteger.valueOf(pa);
                BigInteger pa2BigInt = paBigInt.multiply(BigInteger.TEN.pow(10));

                String INFURA_URL = "https://sepolia.infura.io/v3/173aaec8ffaf4a41bccd4fedebbf47db";

                // Load your Ethereum account credentials
                String uid = mAuth.getCurrentUser().getUid().toString();
                DatabaseReference dref = mDatabase.child("Users").child(uid).child("Key");
                dref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String address = snapshot.getValue().toString();
                        Web3j web3j = Web3j.build(new HttpService(INFURA_URL));
                        
                        Credentials credentials = Credentials.create(address);

                        EtherSender transactionReceipt = null;
                        try {
                            transactionReceipt = EtherSender.deploy(web3j, credentials, new DefaultGasProvider()).sendAsync().join();
                        } catch (Exception e) {
                            Log.d("Err1",e.toString());
                            throw new RuntimeException(e);
                        }
                        String contractAddress = transactionReceipt.getContractAddress();

                        // Send the transaction and retrieve the transaction receipt
                        EtherSender contract = EtherSender.load(contractAddress, web3j, credentials, new DefaultGasProvider());

                        // Call the contract function to send ether
                        String receiverAddress = recipientAddress1;
                        BigInteger amount = BigInteger.valueOf(Long.parseLong(String.valueOf(pa2BigInt))); // 1 ether in wei

                        DatabaseReference dr = mDatabase.child("Users").child(receiverAddress).child("Address");

                        dr.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String rAddress = snapshot.getValue().toString();
                                String message = "Paying " + pa + " to " + rAddress;
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                // Send the transaction and wait for the receipt
                                TransactionReceipt receipt = contract.sendEther(rAddress, amount).sendAsync().join();

                                // Check if the transaction was successfully executed
                                if (receipt.isStatusOK()) {
                                    // Transaction was successful
                                    String grpKey = item.getGrpKey();
                                    btnDelete.setClickable(true);
                                    tvAmountPayable.setText("0");
                                    DatabaseReference dref1 = mDatabase.child("Users").child(uid).child("groups").child(grpKey);
                                    dref1.child("payable").setValue(0);

                                    Toast.makeText(context, "Transaction successful", Toast.LENGTH_SHORT).show();
                                    btnPayAndSettle.setText("Settled");
                                    System.out.println("Transaction successful. Transaction hash: " + receipt.getTransactionHash());
                                } else {
                                    // Transaction failed
                                    Toast.makeText(context, "Transaction failed", Toast.LENGTH_SHORT).show();
                                    System.out.println("Transaction failed. Status: " + receipt.getStatus());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = tvAmountPayable.getText().toString();
                if(a!="0"){
                    Toast.makeText(context,"Amount not settled!",Toast.LENGTH_SHORT).show();
                    return;
                }

                String grpKey = item.getGrpKey();
                String uid = mAuth.getCurrentUser().getUid().toString();
                DatabaseReference dref = mDatabase.child("Users").child(uid).child("groups").child(grpKey);

                dref.removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Group deleted successfully
                                Log.d(TAG, "Group deleted successfully");
                                int itemPosition = position;
                                // Remove the item from the ArrayList
                                groupItems.remove(itemPosition);
                                // Notify the adapter that the data set has changed
                                notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to delete the group
                                Log.e(TAG, "Error deleting group", e);
                            }
                        });
            }
        });

        return convertView;
    }
}
